using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Common;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Core;
using JetBrains.Lifetimes;
using JetBrains.ProjectModel;
using JetBrains.Rd.Tasks;
using JetBrains.RdBackend.Common.Features;
using JetBrains.Util;

namespace DiagnosticsClientPlugin.Gc;

[SolutionComponent]
internal sealed class GcEventsCollectionHandler
{
    private readonly DiagnosticsHostModel _hostModel;

    public GcEventsCollectionHandler(ISolution solution)
    {
        _hostModel = solution.GetProtocolSolution().GetDiagnosticsHostModel();
        _hostModel.CollectGcEvents.Set(async (lt, command) => await CollectAsync(command, lt));
    }

    private async Task<Unit> CollectAsync(CollectGcEventsCommand command, Lifetime lifetime)
    {
        var sessionLifetime = lifetime.IntersectWithTimer(command.Duration);

        _hostModel.GcEventsCollectionSessions.Add(sessionLifetime, command.Pid);

        var channel = Channel.CreateBounded<ValueGcEvent>(new BoundedChannelOptions(100)
        {
            SingleReader = true,
            SingleWriter = true,
            FullMode = BoundedChannelFullMode.DropOldest
        });

        var exporter = new GcEventCsvExporter(command.FilePath, channel.Reader);
        var producer = new GcEventProducer(command.Pid, channel.Writer, sessionLifetime);

        var exporterTask = exporter.ConsumeAsync(sessionLifetime);
        var producerTask = producer.Produce(sessionLifetime);

        var completedTask = await Task.WhenAny(exporterTask, producerTask);
        await completedTask;

        return Unit.Instance;
    }
}