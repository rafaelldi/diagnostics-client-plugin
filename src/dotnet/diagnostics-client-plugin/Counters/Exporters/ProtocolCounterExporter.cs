using System;
using System.Threading;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters.Common;
using DiagnosticsClientPlugin.Generated;

namespace DiagnosticsClientPlugin.Counters.Exporters;

internal sealed class ProtocolCounterExporter
{
    private readonly CountersMonitoringSession _session;
    private readonly ChannelReader<ValueCounter> _reader;

    internal ProtocolCounterExporter(
        CountersMonitoringSession session,
        ChannelReader<ValueCounter> reader )
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
                if (_reader.TryRead(out var counter))
                {
                    HandleCounter(in counter);
                }
            }
        }
        catch (OperationCanceledException)
        {
            //do nothing
        }
    }

    private void HandleCounter(in ValueCounter counter)
    {
        _session.Counters[counter.Name] = new Counter(counter.Name, counter.Value);
    }
}