using System;
using System.Collections.Generic;
using System.Diagnostics.Tracing;
using System.Linq;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Util;
using Microsoft.Diagnostics.NETCore.Client;
using static DiagnosticsClientPlugin.Counters.EventPipes.EventPipeProviderFactory;
using static Microsoft.Diagnostics.Tracing.Parsers.ClrTraceEventParser;

namespace DiagnosticsClientPlugin.Traces;

internal sealed class TraceProviderCollection
{
    internal EventPipeProvider[] EventPipeProviders { get; }

    internal TraceProviderCollection(string providersString, TracingProfile profile)
    {
        var providers = providersString.IsNotEmpty()
            ? ParseProviders(providersString.AsSpan())
            : new List<TraceProvider>();

        var profileProviders = ParseProfile(profile);

        MergeProviders(providers, profileProviders);

        EventPipeProviders = CreateTraceProviders(providers);
    }

    private List<TraceProvider> ParseProviders(in ReadOnlySpan<char> providersString)
    {
        var providers = new List<TraceProvider>();

        var providerStartIndex = 0;
        for (var i = 0; i < providersString.Length; i++)
        {
            if (providersString[i] != ',')
            {
                continue;
            }

            var provider = ParseProvider(providersString.Slice(providerStartIndex, i - providerStartIndex));
            providers.Add(provider);

            providerStartIndex = i + 1;
        }

        var lastProvider = ParseProvider(providersString.Slice(providerStartIndex));
        providers.Add(lastProvider);

        return providers;
    }

    private TraceProvider ParseProvider(in ReadOnlySpan<char> providerString)
    {
        var name = string.Empty;
        var level = EventLevel.Verbose;
        long flags = -1;
        Dictionary<string, string>? args = null;

        var step = 0;
        var currentStart = 0;

        for (var i = 0; i < providerString.Length; i++)
        {
            if (providerString[i] != ':' && step < 3)
            {
                continue;
            }

            switch (step)
            {
                case 0:
                    name = providerString.Slice(0, i).ToString();
                    currentStart = i + 1;
                    step++;
                    break;
                case 1:
                    flags = ParseProviderFlags(providerString, currentStart, i);
                    currentStart = i + 1;
                    step++;
                    break;
                case 2:
                    level = ParseProviderLevel(providerString, currentStart, i);
                    currentStart = i + 1;
                    step++;
                    break;
            }

            if (step == 3)
            {
                args = ParseProviderArgs(providerString.Slice(currentStart));
                break;
            }
        }

        return new TraceProvider(name, level, flags, args);
    }

    private static long ParseProviderFlags(in ReadOnlySpan<char> providerString, int startIndex, int currentIndex) =>
        startIndex != currentIndex
            ? Convert.ToInt64(providerString.Slice(startIndex, currentIndex - startIndex).ToString(), 16)
            : -1;

    private static EventLevel ParseProviderLevel(in ReadOnlySpan<char> providerString, int startIndex, int currentIndex)
    {
        if (startIndex == currentIndex)
        {
            return EventLevel.Verbose;
        }

        var level = providerString.Slice(startIndex, currentIndex - startIndex).ToString();
        if (int.TryParse(level, out var levelValue))
        {
            return levelValue > (int)EventLevel.Verbose ? EventLevel.Verbose : (EventLevel)levelValue;
        }

        return level.ToLower() switch
        {
            "logalways" => EventLevel.LogAlways,
            "critical" => EventLevel.Critical,
            "error" => EventLevel.Error,
            "warning" => EventLevel.Warning,
            "informational" => EventLevel.Informational,
            "verbose" => EventLevel.Verbose,
            _ => throw new ArgumentOutOfRangeException()
        };
    }

    private static Dictionary<string, string> ParseProviderArgs(in ReadOnlySpan<char> argsString)
    {
        var args = new Dictionary<string, string>();

        var insideArgQuote = false;
        var argStartIndex = 0;

        for (var i = 0; i < argsString.Length; i++)
        {
            if (insideArgQuote)
            {
                if (argsString[i] == '\"')
                {
                    insideArgQuote = false;
                }

                continue;
            }

            if (argsString[i] != ';')
            {
                continue;
            }

            var arg = ParseArg(argsString.Slice(argStartIndex, i - argStartIndex));
            args.Add(arg.Key, arg.Value);
            argStartIndex = i + 1;
        }

        var lastArg = ParseArg(argsString.Slice(argStartIndex));
        args.Add(lastArg.Key, lastArg.Value);

        return args;
    }

    private static (string Key, string Value) ParseArg(in ReadOnlySpan<char> argString)
    {
        var delimiterIndex = argString.IndexOf('=');
        return (
            argString.Slice(0, delimiterIndex).ToString(),
            argString.Slice(delimiterIndex + 1).ToString()
        );
    }

    private TraceProvider[] ParseProfile(TracingProfile profile) =>
        profile switch
        {
            TracingProfile.None => Array.Empty<TraceProvider>(),
            TracingProfile.CpuSampling => new[]
            {
                new TraceProvider(SampleProfilerProvider, EventLevel.Informational, 0xF00000000000),
                new TraceProvider(DotNetRuntimeProvider, EventLevel.Informational, (long)Keywords.Default)
            },
            TracingProfile.GcVerbose => new[]
            {
                new TraceProvider(
                    DotNetRuntimeProvider,
                    EventLevel.Verbose,
                    (long)Keywords.GC | (long)Keywords.GCHandle | (long)Keywords.Exception
                )
            },
            TracingProfile.GcCollect => new[]
            {
                new TraceProvider(DotNetRuntimeProvider, EventLevel.Informational, (long)Keywords.GC)
            },
            _ => throw new ArgumentOutOfRangeException(nameof(profile), profile, null)
        };

    private void MergeProviders(List<TraceProvider> providers, IReadOnlyCollection<TraceProvider> profileProviders)
    {
        var providerNames = providers.Select(it => it.Name).ToList();

        providers.AddRange(profileProviders.Where(profileProvider => !providerNames.Contains(profileProvider.Name)));
    }
}