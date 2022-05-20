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

        TraceProvider provider;
        var remaining = providersString;
        var delimiterIndex = remaining.IndexOf(',');
        while (delimiterIndex != -1)
        {
            provider = ParseProvider(remaining.Slice(0, delimiterIndex).Trim());
            providers.Add(provider);
            remaining = remaining.Slice(delimiterIndex + 1);
            delimiterIndex = remaining.IndexOf(',');
        }

        provider = ParseProvider(remaining.Trim());
        providers.Add(provider);

        return providers;
    }

    private TraceProvider ParseProvider(in ReadOnlySpan<char> providerString)
    {
        var delimiterIndex = providerString.IndexOf(':');
        if (delimiterIndex == -1)
        {
            return new TraceProvider(providerString.ToString(), EventLevel.Verbose, -1);
        }

        var name = providerString.Slice(0, delimiterIndex).ToString();

        var remaining = providerString.Slice(delimiterIndex + 1);

        delimiterIndex = remaining.IndexOf(':');
        var flags = ParseProviderFlags(remaining.Slice(0, delimiterIndex));

        remaining = remaining.Slice(delimiterIndex + 1);

        EventLevel level;
        delimiterIndex = remaining.IndexOf(':');
        if (delimiterIndex == -1)
        {
            level = ParseProviderLevel(remaining.Slice(0));

            return new TraceProvider(name, level, flags);
        }

        level = ParseProviderLevel(remaining.Slice(0, delimiterIndex));

        remaining = remaining.Slice(delimiterIndex + 1);

        var args = ParseProviderArgs(remaining);

        return new TraceProvider(name, level, flags, args);
    }

    private long ParseProviderFlags(in ReadOnlySpan<char> flagsPart)
    {
        if (flagsPart.IsEmpty)
        {
            return -1;
        }

        return Convert.ToInt64(flagsPart.ToString(), 16);
    }

    private EventLevel ParseProviderLevel(in ReadOnlySpan<char> levelPart)
    {
        if (levelPart.IsEmpty)
        {
            return EventLevel.Verbose;
        }

        var levelString = levelPart.ToString();
        if (int.TryParse(levelString, out var levelValue))
        {
            return levelValue > (int)EventLevel.Verbose ? EventLevel.Verbose : (EventLevel)levelValue;
        }

        return levelString.ToLower() switch
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

    private Dictionary<string, string>? ParseProviderArgs(in ReadOnlySpan<char> argsString)
    {
        if (argsString.IsEmpty)
        {
            return null;
        }

        var args = new Dictionary<string, string>();

        var insideArgQuote = false;
        var argStartIndex = 0;
        (string Key, string Value)? arg;

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

            if (argsString[i] == '\"')
            {
                insideArgQuote = true;
                continue;
            }

            if (argsString[i] != ';')
            {
                continue;
            }

            arg = ParseArg(argsString.Slice(argStartIndex, i - argStartIndex));
            if (arg.HasValue)
            {
                args.Add(arg.Value.Key, arg.Value.Value);
            }

            argStartIndex = i + 1;
        }

        arg = ParseArg(argsString.Slice(argStartIndex));
        if (arg.HasValue)
        {
            args.Add(arg.Value.Key, arg.Value.Value);
        }

        return args;
    }

    private (string Key, string Value)? ParseArg(in ReadOnlySpan<char> argString)
    {
        var delimiterIndex = argString.IndexOf('=');
        if (delimiterIndex == -1)
        {
            return null;
        }

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