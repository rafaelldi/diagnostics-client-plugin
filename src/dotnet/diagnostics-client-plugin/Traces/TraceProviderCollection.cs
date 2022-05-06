using DiagnosticsClientPlugin.Counters.EventPipes;
using DiagnosticsClientPlugin.Generated;
using Microsoft.Diagnostics.NETCore.Client;

namespace DiagnosticsClientPlugin.Traces;

internal sealed class TraceProviderCollection
{
    internal EventPipeProvider[] EventPipeProviders { get; }

    internal TraceProviderCollection(TracingProfile profile)
    {
        EventPipeProviders = EventPipeProviderFactory.CreateProfileProvider(profile);
    }
}