using System;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Common;
using DiagnosticsClientPlugin.Counters.Common;
using DiagnosticsClientPlugin.Counters.Exporters;
using DiagnosticsClientPlugin.Counters.Producer;
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

        var exporter = CreateExporter(command, channel);
        var producer = CreateProducer(command, channel, sessionLifetime);

        var exporterTask = exporter.ConsumeAsync(sessionLifetime);
        var producerTask = producer.Produce(sessionLifetime);

        var completedTask = await Task.WhenAny(exporterTask, producerTask);
        await completedTask;

        return Unit.Instance;
    }

    private FileCounterExporter CreateExporter(
        CollectCountersCommand command,
        Channel<ValueCounter> channel) =>
        FileCounterExporter.Create(command.FilePath, command.Format, channel.Reader);

    private CounterProducer CreateProducer(
        CollectCountersCommand command,
        Channel<ValueCounter> channel,
        Lifetime lt)
    {
        var configuration = new CounterProducerConfiguration(
            Guid.NewGuid().ToString(),
            command.Providers,
            command.Metrics,
            command.RefreshInterval,
            command.MaxTimeSeries,
            command.MaxHistograms
        );
        return new CounterProducer(command.Pid, configuration, channel.Writer, lt);
    }
}