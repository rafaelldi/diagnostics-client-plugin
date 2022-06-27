using System;
using System.Threading;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Generated;

namespace DiagnosticsClientPlugin.Gc;

internal sealed class GcEventProtocolExporter
{
    private readonly GcMonitoringSession _session;
    private readonly ChannelReader<ValueGcEvent> _reader;

    internal GcEventProtocolExporter(GcMonitoringSession session, ChannelReader<ValueGcEvent> reader)
    {
        _session = session;
        _reader = reader;
    }

    internal async Task ConsumeAsync(CancellationToken ct)
    {
        try
        {
            while (await _reader.WaitToReadAsync(ct))
            {
                if (_reader.TryRead(out var gcEvent))
                {
                    _session.GcHappened.Fire(new GcEvent(gcEvent.Number, gcEvent.Generation));
                }
            }
        }
        catch (OperationCanceledException)
        {
            //do nothing
        }
    }
}