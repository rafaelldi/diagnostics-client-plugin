﻿using System;
using System.Collections.Generic;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters.Common;
using DiagnosticsClientPlugin.EventPipes;
using JetBrains.Lifetimes;
using Microsoft.Diagnostics.Tracing;

namespace DiagnosticsClientPlugin.Counters.Producer;

internal sealed class CountersProducer
{
    private readonly EventPipeSessionManager _sessionManager;
    private readonly CountersProducerConfiguration _configuration;
    private readonly ChannelWriter<ValueCounter> _writer;
    private readonly Lifetime _lt;

    internal CountersProducer(
        int pid,
        CountersProducerConfiguration configuration,
        ChannelWriter<ValueCounter> writer,
        Lifetime lt)
    {
        _sessionManager = new EventPipeSessionManager(pid);
        _configuration = configuration;
        _writer = writer;
        _lt = lt;

        lt.OnTermination(() => _writer.Complete());
    }

    internal Task Produce(Lifetime lt)
    {
        var lifetime = _lt.Intersect(lt);
        var tcs = new TaskCompletionSource<bool>(TaskCreationOptions.RunContinuationsAsynchronously);

        var session = _sessionManager.StartSession(_configuration.EventPipeProviders, false);
        lifetime.AddDispose(session);

        var source = new EventPipeEventSource(session.EventStream);
        lifetime.AddDispose(source);

        lifetime.Bracket(
            () => source.Dynamic.All += HandleEvent,
            () => source.Dynamic.All -= HandleEvent
        );

        lifetime.OnTermination(() => EventPipeSessionManager.StopSession(session));

        Process(source, tcs, lifetime);

        return tcs.Task;
    }

    private static void Process(EventPipeEventSource source, TaskCompletionSource<bool> tcs, in Lifetime lt)
    {
        Task.Run(() =>
        {
            try
            {
                source.Process();
                tcs.SetResult(true);
            }
            catch (Exception e)
            {
                tcs.SetException(e);
            }
        }, lt);
    }

    private void HandleEvent(TraceEvent evt)
    {
        if (evt.EventName != "EventCounters")
        {
            return;
        }

        var payloadVal = (IDictionary<string, object>)evt.PayloadValue(0);
        var payloadFields = (IDictionary<string, object>)payloadVal["Payload"];
        var name = payloadFields["Name"].ToString();
        if (!_configuration.IsCounterEnabled(evt.ProviderName, name))
        {
            return;
        }

        var counter = Map(evt.ProviderName, name, evt.TimeStamp, payloadFields);
        var success = _writer.TryWrite(counter);
        if (!success)
        {
            //todo: log it
        }
    }

    private ValueCounter Map(string providerName, string name, DateTime timeStamp, IDictionary<string, object> payloadFields)
    {
        var displayName = payloadFields["DisplayName"].ToString();
        displayName = string.IsNullOrEmpty(displayName) ? name : displayName;
        var displayUnits = payloadFields["DisplayUnits"].ToString();
        var counterType = payloadFields["CounterType"].ToString() == "Sum" ? CounterType.Rate : CounterType.Metric;

        if (counterType is CounterType.Rate)
        {
            var value = (double)payloadFields["Increment"];
            displayUnits = string.IsNullOrEmpty(displayUnits) ? "Count" : displayUnits;
            var counterName = $"{displayName} ({displayUnits} / {_configuration.RefreshInterval} sec)";
            return new ValueCounter(
                timeStamp,
                counterName,
                providerName,
                value,
                counterType);
        }
        else
        {
            var value = (double)payloadFields["Mean"];
            var counterName = string.IsNullOrEmpty(displayUnits) ? displayName : $"{displayName} ({displayUnits})";
            return new ValueCounter(
                timeStamp,
                counterName,
                providerName,
                value,
                counterType);
        }
    }
}