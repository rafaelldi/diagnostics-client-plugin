using System;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters.Exporters;
using DiagnosticsClientPlugin.Counters.Producer;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Collections.Viewable;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.RdBackend.Common.Features;

namespace DiagnosticsClientPlugin.Counters;

[SolutionComponent]
internal sealed class CounterCollectionHandler
{
    public CounterCollectionHandler(ISolution solution, Lifetime lifetime)
    {
        var hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        hostModel.PersistentCounterSessions.View(lifetime, (lt, pid, session) => Handle(lt, pid, session));
    }

    private static void Handle(Lifetime lt, int pid, PersistentCounterSession session)
    {
        var channel = Channel.CreateBounded<ValueCounter>(new BoundedChannelOptions(100)
        {
            SingleReader = true,
            SingleWriter = true,
            FullMode = BoundedChannelFullMode.DropOldest
        });
        
        var exporter = CreateExporter(session, channel);
        var producer = CreateProducer(pid, session, channel, lt);

        lt.StartAttachedAsync(TaskScheduler.Default, async () => await exporter.ConsumeAsync());
        lt.StartAttachedAsync(TaskScheduler.Default, async () => await producer.Produce());
    }

    private static FileCounterExporter CreateExporter(
        PersistentCounterSession session,
        Channel<ValueCounter> channel) =>
        FileCounterExporter.Create(session.FilePath, session.Format, channel.Reader);

    private static CounterProducer CreateProducer(
        int pid,
        PersistentCounterSession session,
        Channel<ValueCounter> channel,
        Lifetime lt)
    {
        var configuration = new CounterProducerConfiguration(
            Guid.NewGuid().ToString(),
            session.Providers,
            session.Metrics,
            session.RefreshInterval,
            session.MaxTimeSeries,
            session.MaxHistograms
        );
        return new CounterProducer(pid, configuration, channel.Writer, lt);
    }
}