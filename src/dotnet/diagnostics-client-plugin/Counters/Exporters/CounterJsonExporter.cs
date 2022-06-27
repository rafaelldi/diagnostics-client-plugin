using System.Globalization;
using System.Text;
using System.Threading.Channels;
using DiagnosticsClientPlugin.Counters.Common;

namespace DiagnosticsClientPlugin.Counters.Exporters;

internal sealed class CounterJsonExporter : FileCounterExporter
{
    private readonly StringBuilder _stringBuilder = new();

    internal CounterJsonExporter(string filePath, ChannelReader<ValueCounter> reader)
        : base(filePath, reader)
    {
    }

    protected override string GetFileHeader() => @"{""Events"":[";

    protected override string GetCounterString(in ValueCounter counter)
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
            .Append(counter.DisplayName)
            .Append(@""",""value"":""")
            .Append(counter.Value.ToString(CultureInfo.InvariantCulture))
            .Append(@""",""type"":""")
            .Append(counter.Type.ToValue())
            .Append(@""",""tags"":""")
            .Append(counter.Tags)
            .Append(@"""}");

        return _stringBuilder.ToString();
    }

    protected override string GetFileFooter() => "]}";
}