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
                    _session.GcHappened.Fire(new GcEvent(
                        gcEvent.Number,
                        gcEvent.Generation,
                        gcEvent.Reason,
                        gcEvent.PauseDuration,
                        gcEvent.Peak,
                        gcEvent.After,
                        gcEvent.Ratio,
                        gcEvent.Promoted,
                        gcEvent.Allocated,
                        gcEvent.AllocationRate,
                        gcEvent.SizeGen0,
                        gcEvent.FragmentationGen0,
                        gcEvent.SurvivalGen0,
                        gcEvent.SizeGen1,
                        gcEvent.FragmentationGen1,
                        gcEvent.SurvivalGen1,
                        gcEvent.SizeGen2,
                        gcEvent.FragmentationGen2,
                        gcEvent.SurvivalGen2,
                        gcEvent.SizeLoh,
                        gcEvent.FragmentationLoh,
                        gcEvent.SurvivalLoh,
                        gcEvent.PinnedObjects
                    ));
                }
            }
        }
        catch (OperationCanceledException)
        {
            //do nothing
        }
    }
}