using System;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Lifetimes;

namespace DiagnosticsClientPlugin.Counters.Exporters;

internal sealed class CounterProtocolExporter
{
    private readonly CountersMonitoringSession _session;
    private readonly ChannelReader<ValueCounter> _reader;

    internal CounterProtocolExporter(CountersMonitoringSession session, ChannelReader<ValueCounter> reader)
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
                if (_reader.TryRead(out var counter))
                {
                    var key = string.IsNullOrEmpty(counter.Tags) ? counter.Name : $"{counter.Name}-{counter.Tags}";
                    _session.Counters[key] = new Counter(counter.DisplayName, counter.Tags, counter.Value);
                }
            }
        }
        catch (OperationCanceledException)
        {
            //do nothing
        }
    }
}