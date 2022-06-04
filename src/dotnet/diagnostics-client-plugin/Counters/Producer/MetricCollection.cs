using System;
using System.Text;

namespace DiagnosticsClientPlugin.Counters.Producer;

internal sealed class MetricCollection
{
    internal string Metrics { get; }

    internal MetricCollection(string metricCollectionString)
    {
        var meters = CounterCollectionParser.Parse(metricCollectionString.AsSpan());

        var sb = new StringBuilder();
        foreach (var meter in meters)
        {
            if (meter.Value is not null)
            {
                foreach (var metric in meter.Value)
                {
                    sb.Append($"{meter.Key}\\{metric},");
                }
            }
            else
            {
                sb.Append($"{meter.Key},");
            }
        }

        if (sb.Length > 0)
        {
            sb.Remove(sb.Length - 1, 1);
        }

        Metrics = sb.ToString();
    }
}