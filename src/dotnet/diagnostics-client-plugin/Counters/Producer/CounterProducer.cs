using System;
using System.Collections.Generic;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Common;
using DiagnosticsClientPlugin.EventPipes;
using JetBrains.Lifetimes;
using Microsoft.Diagnostics.Tracing;
// ReSharper disable ReplaceSliceWithRangeIndexer

namespace DiagnosticsClientPlugin.Counters.Producer;

internal sealed class CounterProducer
{
    private readonly int _pid;
    private readonly EventPipeSessionManager _sessionManager;
    private readonly CounterProducerConfiguration _configuration;
    private readonly ChannelWriter<ValueCounter> _writer;

    internal CounterProducer(
        int pid,
        CounterProducerConfiguration configuration,
        ChannelWriter<ValueCounter> writer,
        Lifetime lifetime)
    {
        _pid = pid;
        _sessionManager = new EventPipeSessionManager(pid);
        _configuration = configuration;
        _writer = writer;

        lifetime.OnTermination(() => _writer.Complete());
    }

    internal Task Produce()
    {
        var session = _sessionManager.StartSession(_configuration.EventPipeProviders, false);
        Lifetime.AsyncLocal.Value.AddDispose(session);

        var source = new EventPipeEventSource(session.EventStream);
        Lifetime.AsyncLocal.Value.AddDispose(source);

        Lifetime.AsyncLocal.Value.Bracket(
            () => source.Dynamic.All += HandleEvent,
            () => source.Dynamic.All -= HandleEvent
        );

        var cancellationToken = Lifetime.AsyncLocal.Value.ToCancellationToken();
        cancellationToken.Register(() => EventPipeSessionManager.StopSession(session));

        return Task.Run(() => source.Process(), Lifetime.AsyncLocal.Value);
    }

    private void HandleEvent(TraceEvent evt)
    {
        if (evt.ProcessID != _pid)
        {
            return;
        }

        if (evt.ProviderName is Providers.SystemDiagnosticsMetricsProvider)
        {
            switch (evt.EventName)
            {
                case "CounterRateValuePublished":
                    HandleCounterRateEvent(evt);
                    break;
                case "GaugeValuePublished":
                    HandleGaugeEvent(evt);
                    break;
                case "HistogramValuePublished":
                    HandleHistogramEvent(evt);
                    break;
            }
        }
        else if (evt.EventName is "EventCounters")
        {
            HandleCounterEvent(evt);
        }
    }

    private void HandleCounterRateEvent(TraceEvent evt)
    {
        var sessionId = (string)evt.PayloadValue(0);
        if (sessionId != _configuration.SessionId)
        {
            return;
        }

        if (!double.TryParse((string)evt.PayloadValue(6), out var rate))
        {
            return;
        }

        var meterName = (string)evt.PayloadValue(1);
        var instrumentName = (string)evt.PayloadValue(3);
        var unit = (string)evt.PayloadValue(4);
        var tags = (string)evt.PayloadValue(5);
        var counter = MapToRateCounter(evt.TimeStamp, instrumentName, unit, meterName, rate, tags);
        _writer.TryWrite(counter);
    }

    private void HandleGaugeEvent(TraceEvent evt)
    {
        var sessionId = (string)evt.PayloadValue(0);
        if (sessionId != _configuration.SessionId)
        {
            return;
        }

        if (!double.TryParse((string)evt.PayloadValue(6), out var lastValue))
        {
            return;
        }

        var meterName = (string)evt.PayloadValue(1);
        var instrumentName = (string)evt.PayloadValue(3);
        var unit = (string)evt.PayloadValue(4);
        var tags = (string)evt.PayloadValue(5);
        var counter = MapToMetricCounter(evt.TimeStamp, instrumentName, unit, meterName, lastValue, tags);
        _writer.TryWrite(counter);
    }

    private void HandleHistogramEvent(TraceEvent evt)
    {
        var sessionId = (string)evt.PayloadValue(0);
        if (sessionId != _configuration.SessionId)
        {
            return;
        }

        var meterName = (string)evt.PayloadValue(1);
        var instrumentName = (string)evt.PayloadValue(3);
        var unit = (string)evt.PayloadValue(4);
        var tags = (string)evt.PayloadValue(5);
        var quantiles = (string)evt.PayloadValue(6);
        var quantileValues = ParseQuantiles(quantiles.AsSpan());

        var counter = MapToMetricCounter(evt.TimeStamp, instrumentName, unit, meterName, quantileValues.Value50,
            CombineTagsAndQuantiles(tags, "Percentile=50"));
        _writer.TryWrite(counter);

        counter = MapToMetricCounter(evt.TimeStamp, instrumentName, unit, meterName, quantileValues.Value95,
            CombineTagsAndQuantiles(tags, "Percentile=95"));
        _writer.TryWrite(counter);

        counter = MapToMetricCounter(evt.TimeStamp, instrumentName, unit, meterName, quantileValues.Value99,
            CombineTagsAndQuantiles(tags, "Percentile=99"));
        _writer.TryWrite(counter);

        string CombineTagsAndQuantiles(string tagString, string quantileString) =>
            string.IsNullOrEmpty(tagString) ? quantileString : $"{tagString},{quantileString}";
    }

    private Quantiles ParseQuantiles(ReadOnlySpan<char> quantiles)
    {
        var firstDelimiterIndex = quantiles.IndexOf(';');
        var value50 = ParsePair(quantiles.Slice(0, firstDelimiterIndex).Trim());

        quantiles = quantiles.Slice(firstDelimiterIndex + 1);
        var secondDelimiterIndex = quantiles.IndexOf(';');
        var value95 = ParsePair(quantiles.Slice(0, secondDelimiterIndex).Trim());

        quantiles = quantiles.Slice(secondDelimiterIndex + 1);
        var value99 = ParsePair(quantiles);

        return new Quantiles(value50, value95, value99);
    }

    private double ParsePair(ReadOnlySpan<char> pair)
    {
        var pairDelimiter = pair.IndexOf('=');
        var valueSlice = pair.Slice(pairDelimiter + 1).Trim();
        return double.TryParse(valueSlice.ToString(), out var value) ? value : 0;
    }

    private readonly record struct Quantiles(double Value50, double Value95, double Value99);

    private void HandleCounterEvent(TraceEvent evt)
    {
        var payloadVal = (IDictionary<string, object>)evt.PayloadValue(0);
        var payloadFields = (IDictionary<string, object>)payloadVal["Payload"];
        var name = payloadFields["Name"].ToString();
        if (!_configuration.IsCounterEnabled(evt.ProviderName, name))
        {
            return;
        }

        var counter = MapCounterEvent(evt.ProviderName, name, evt.TimeStamp, payloadFields);
        _writer.TryWrite(counter);
    }

    private ValueCounter MapCounterEvent(string providerName, string name, DateTime timeStamp,
        IDictionary<string, object> payloadFields)
    {
        var displayName = payloadFields["DisplayName"].ToString();
        displayName = string.IsNullOrEmpty(displayName) ? name : displayName;
        var displayUnits = payloadFields["DisplayUnits"].ToString();

        if (payloadFields["CounterType"].ToString() == "Sum")
        {
            var value = (double)payloadFields["Increment"];
            return MapToRateCounter(timeStamp, displayName, displayUnits, providerName, value, null);
        }
        else
        {
            var value = (double)payloadFields["Mean"];
            return MapToMetricCounter(timeStamp, displayName, displayUnits, providerName, value, null);
        }
    }

    private ValueCounter MapToRateCounter(
        DateTime timeStamp,
        string name,
        string units,
        string providerName,
        double value,
        string? tags
    )
    {
        var displayUnits = string.IsNullOrEmpty(units) ? "Count" : units;
        return new ValueCounter(
            timeStamp,
            name,
            $"{name} ({displayUnits} / {_configuration.RefreshInterval} sec)",
            providerName,
            value,
            CounterType.Rate,
            tags
        );
    }

    private static ValueCounter MapToMetricCounter(
        DateTime timeStamp,
        string name,
        string units,
        string providerName,
        double value,
        string? tags
    ) => new(
        timeStamp,
        name,
        string.IsNullOrEmpty(units) ? name : $"{name} ({units})",
        providerName,
        value,
        CounterType.Metric,
        tags
    );
}