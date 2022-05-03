using System;
using System.Collections.Concurrent;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters.Common;
using DiagnosticsClientPlugin.Counters.Consuming;
using DiagnosticsClientPlugin.Counters.Producing;
using DiagnosticsClientPlugin.Generated;
using JetBrains;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Tasks;
using JetBrains.RdBackend.Common.Features;

namespace DiagnosticsClientPlugin.Counters.Collection;

[SolutionComponent]
internal sealed class CollectCountersHandler
{
    private readonly ConcurrentDictionary<int, LifetimeDefinition> _definitions = new();
    private readonly DiagnosticsHostModel _hostModel;
    private readonly Lifetime _lifetime;

    public CollectCountersHandler(Lifetime lifetime, ISolution solution)
    {
        _lifetime = lifetime;
        _hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();

        _hostModel.CollectCounters.Set(async (lt, command) => await Collect(command, lt));
        _hostModel.StopCountersCollection.Advise(lifetime, it => Stop(it));
    }

    private async Task<Unit> Collect(CollectCountersCommand command, Lifetime lifetime)
    {
        if (_definitions.ContainsKey(command.Pid))
        {
            return Unit.Instance;
        }

        var sessionLifetimeDefinition = _lifetime.CreateNested();
        if (!_definitions.TryAdd(command.Pid, sessionLifetimeDefinition))
        {
            return Unit.Instance;
        }

        var sessionLifetime = CreateSessionLifetime(sessionLifetimeDefinition, lifetime, command.Duration);
        sessionLifetime.OnTermination(() => _definitions.TryRemove(command.Pid, out _));

        var session = new CountersCollectionSession(command.Pid, command.FilePath);
        _hostModel.CountersCollectionSessions.Add(sessionLifetime, command.Pid, session);

        var channel = Channel.CreateBounded<ValueCounter>(new BoundedChannelOptions(100)
        {
            SingleReader = true,
            SingleWriter = true,
            FullMode = BoundedChannelFullMode.DropOldest
        });

        var consumer = CreateConsumer(command, channel);
        var producer = CreateProducer(command, channel, sessionLifetime);

        var consumerTask = consumer.ConsumeAsync(sessionLifetime);
        var producerTask = producer.Produce(sessionLifetime);

        var completedTask = await Task.WhenAny(consumerTask, producerTask);
        await completedTask;

        return Unit.Instance;
    }

    private Lifetime CreateSessionLifetime(
        LifetimeDefinition manualLifetimeDefinition,
        in Lifetime operationLifetime,
        int? duration)
    {
        var sessionLifetime = manualLifetimeDefinition.Lifetime.Intersect(operationLifetime);
        return duration.HasValue
            ? sessionLifetime.CreateTerminatedAfter(TimeSpan.FromSeconds(duration.Value))
            : sessionLifetime;
    }

    private void Stop(StopCountersCollectionCommand command)
    {
        if (_definitions.TryGetValue(command.Pid, out var definition))
        {
            definition.Terminate();
        }
    }

    private AbstractFileCountersConsumer CreateConsumer(
        CollectCountersCommand command,
        Channel<ValueCounter> channel) =>
        CountersConsumerFactory.Create(command.FilePath, command.Format, channel.Reader);

    private CountersProducer CreateProducer(
        CollectCountersCommand command,
        Channel<ValueCounter> channel,
        in Lifetime lt)
    {
        var providers = new CounterProviderCollection(command.Providers);
        var configuration = new CountersProducerConfiguration(command.RefreshInterval, providers);
        return new CountersProducer(command.Pid, configuration, channel.Writer, lt);
    }
}