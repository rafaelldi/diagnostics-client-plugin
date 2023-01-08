using System;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Lifetimes;

namespace DiagnosticsClientPlugin.Gc;

internal sealed class GcEventProtocolExporter
{
    private readonly LiveGcEventSession _session;
    private readonly ChannelReader<ValueGcEvent> _reader;

    internal GcEventProtocolExporter(LiveGcEventSession session, ChannelReader<ValueGcEvent> reader)
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
                        gcEvent.SizeGen1,
                        gcEvent.SizeGen2,
                        gcEvent.SizeLoh,
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