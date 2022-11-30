using System;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Lifetimes;

namespace DiagnosticsClientPlugin.Traces;

internal sealed class TraceProtocolExporter
{
    private readonly TraceMonitoringSession _session;
    private readonly ChannelReader<ValueTrace> _reader;

    internal TraceProtocolExporter(TraceMonitoringSession session, ChannelReader<ValueTrace> reader)
    {
        _session = session;
        _reader = reader;
    }

    internal async Task ConsumeAsync()
    {
        try
        {
            while (await _reader.WaitToReadAsync(Lifetime.AsyncLocal.Value))
            {
                if (_reader.TryRead(out var trace))
                {
                    _session.TraceReceived.Fire(
                        new Trace(
                            trace.EventName,
                            trace.Provider,
                            trace.TimeStamp,
                            trace.Content
                        )
                    );
                }
            }
        }
        catch (OperationCanceledException)
        {
            //do nothing
        }
    }
}