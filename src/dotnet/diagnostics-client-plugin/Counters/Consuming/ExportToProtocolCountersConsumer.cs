using System;
using System.Threading;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Generated;
using Counter = DiagnosticsClientPlugin.Counters.Common.Counter;

namespace DiagnosticsClientPlugin.Counters.Consuming;

internal sealed class ExportToProtocolCountersConsumer
{
    private readonly CountersMonitoringSession _session;
    private readonly ChannelReader<Counter> _reader;

    internal ExportToProtocolCountersConsumer(
        CountersMonitoringSession session,
        ChannelReader<Counter> reader )
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
                    HandleCounter(counter);
                }
            }
        }
        catch (OperationCanceledException)
        {
            //do nothing
        }
    }

    private void HandleCounter(Counter counter)
    {
        _session.Counters[counter.Name] = new Generated.Counter(counter.Name, counter.Value);
    }
}