using System;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Lifetimes;

namespace DiagnosticsClientPlugin.Chart;

internal sealed class ChartProtocolExporter
{
    private const string CpuCounterName = "CPU Usage";
    private const string GcHeapSizeCounterName = "GC Heap Size";
    private const string WorkingSetCounterName = "Working Set";

    private readonly LiveChartSession _session;
    private readonly ChannelReader<ValueCounter> _reader;

    internal ChartProtocolExporter(LiveChartSession session, ChannelReader<ValueCounter> reader)
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
                if (!_reader.TryRead(out var counter)) continue;

                var chartValue = Map(counter);
                if (chartValue is not null)
                {
                    _session.ValueReceived.Fire(chartValue);
                }
            }
        }
        catch (OperationCanceledException)
        {
            //do nothing
        }
    }

    private static ChartValue? Map(ValueCounter counter)
    {
        var offset = new DateTimeOffset(counter.TimeStamp);
        var timestamp = offset.ToUnixTimeSeconds();

        return counter.Name switch
        {
            CpuCounterName => new ChartValue(timestamp, counter.Value, ChartValueType.Cpu),
            GcHeapSizeCounterName => new ChartValue(timestamp, counter.Value, ChartValueType.GcHeapSize),
            WorkingSetCounterName => new ChartValue(timestamp, counter.Value, ChartValueType.WorkingSet),
            _ => null
        };
    }
}