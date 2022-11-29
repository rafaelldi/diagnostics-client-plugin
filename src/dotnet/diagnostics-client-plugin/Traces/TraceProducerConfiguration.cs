using System.Collections.Generic;
using DiagnosticsClientPlugin.EventPipes;
using DiagnosticsClientPlugin.Generated;
using Microsoft.Diagnostics.NETCore.Client;

namespace DiagnosticsClientPlugin.Traces;

internal sealed class TraceProducerConfiguration
{
    internal IReadOnlyCollection<EventPipeProvider> EventPipeProviders { get; }
    internal bool IsHttpEnabled { get; }
    internal bool IsAspNetEnabled { get; }
    internal bool IsEfEnabled { get; }
    internal bool IsExceptionsEnabled { get; }
    internal bool IsThreadsEnabled { get; }
    internal bool IsContentionsEnabled { get; }
    internal bool IsTasksEnabled { get; }
    internal bool IsLoaderEnabled { get; }

    internal TraceProducerConfiguration(List<PredefinedProvider> providers)
    {
        var traceProviders = PredefinedProviderConverter.Convert(providers);
        EventPipeProviders = EventPipeProviderFactory.CreateTraceProviders(traceProviders);
        IsHttpEnabled = providers.Contains(PredefinedProvider.Http);
        IsAspNetEnabled = providers.Contains(PredefinedProvider.AspNet);
        IsEfEnabled = providers.Contains(PredefinedProvider.EF);
        IsExceptionsEnabled = providers.Contains(PredefinedProvider.Exceptions);
        IsThreadsEnabled = providers.Contains(PredefinedProvider.Threads);
        IsContentionsEnabled = providers.Contains(PredefinedProvider.Contentions);
        IsTasksEnabled = providers.Contains(PredefinedProvider.Tasks);
        IsLoaderEnabled = providers.Contains(PredefinedProvider.Loader);
    }
}