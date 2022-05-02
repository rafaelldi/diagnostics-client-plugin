using System.IO;
using System.Text;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters.Common;

namespace DiagnosticsClientPlugin.Counters.Consuming;

internal sealed class ExportToCsvCountersConsumer : AbstractFileCountersConsumer
{
    private readonly StringBuilder _stringBuilder = new(9);

    public ExportToCsvCountersConsumer(string filePath, ChannelReader<Counter> reader)
        : base(filePath, reader)
    {
    }

    protected override async Task InitializeFileAsync(StreamWriter sw)
    {
        await sw.WriteLineAsync("Timestamp,Provider,Counter,Value,Type");
    }

    protected override async Task HandleCounterAsync(StreamWriter sw, Counter counter)
    {
        if (_stringBuilder.Length > 0)
        {
            _stringBuilder.Clear();
        }

        _stringBuilder
            .Append(counter.TimeStamp)
            .Append(",")
            .Append(counter.ProviderName)
            .Append(",")
            .Append(counter.Name)
            .Append(",")
            .Append(counter.Value)
            .Append(",")
            .Append(counter.Type.ToValue());

        await sw.WriteLineAsync(_stringBuilder.ToString());
    }

    protected override Task FinalizeFileAsync(StreamWriter sw)
    {
        return Task.CompletedTask;
    }
}