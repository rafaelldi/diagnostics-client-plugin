using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Common;
using DiagnosticsClientPlugin.Counters.Common;
using DiagnosticsClientPlugin.Counters.Consuming;
using DiagnosticsClientPlugin.Counters.Producing;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Tasks;
using JetBrains.RdBackend.Common.Features;
using JetBrains.Util;

namespace DiagnosticsClientPlugin.Counters.Collection;

[SolutionComponent]
internal sealed class CounterCollectionHandler
{
    private readonly DiagnosticsHostModel _hostModel;

    public CounterCollectionHandler(ISolution solution)
    {
        _hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        _hostModel.CollectCounters.Set(async (lt, command) => await Collect(command, lt));
    }

    private async Task<Unit> Collect(CollectCountersCommand command, Lifetime lifetime)
    {
        var sessionLifetime = lifetime.IntersectWithTimer(command.Duration);

        _hostModel.CounterCollectionSessions.Add(sessionLifetime, command.Pid);

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