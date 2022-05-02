using System.Globalization;
using System.IO;
using System.Text;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters.Common;

namespace DiagnosticsClientPlugin.Counters.Consuming;

internal sealed class ExportToJsonCountersConsumer : AbstractFileCountersConsumer
{
    private readonly StringBuilder _stringBuilder = new(11);

    internal ExportToJsonCountersConsumer(string filePath, ChannelReader<Counter> reader)
        : base(filePath, reader)
    {
    }

    protected override async Task InitializeFileAsync(StreamWriter sw)
    {
        await sw.WriteAsync(@"{""Events"":[");
    }

    protected override async Task HandleCounterAsync(StreamWriter sw, Counter counter)
    {
        if (_stringBuilder.Length > 0)
        {
            _stringBuilder.Clear();
            _stringBuilder.Append(",");
        }

        _stringBuilder
            .Append(@"{""timestamp"":""")
            .Append(counter.TimeStamp.ToString(CultureInfo.CurrentCulture))
            .Append(@""",""provider"":""")
            .Append(counter.ProviderName)
            .Append(@""",""counter"":""")
            .Append(counter.Name)
            .Append(@""",""value"":""")
            .Append(counter.Value.ToString(CultureInfo.InvariantCulture))
            .Append(@""",""type"":""")
            .Append(counter.Type.ToValue())
            .Append(@"""}");

        await sw.WriteAsync(_stringBuilder.ToString());
    }

    protected override async Task FinalizeFileAsync(StreamWriter sw)
    {
        await sw.WriteAsync("]}");
    }
}