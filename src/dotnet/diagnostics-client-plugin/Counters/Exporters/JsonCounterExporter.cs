using System.Globalization;
using System.Text;
using System.Threading.Channels;
using DiagnosticsClientPlugin.Counters.Common;

namespace DiagnosticsClientPlugin.Counters.Exporters;

internal sealed class JsonCounterExporter : AbstractFileCounterExporter
{
    private readonly StringBuilder _stringBuilder = new(11);

    internal JsonCounterExporter(string filePath, ChannelReader<ValueCounter> reader)
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
            .Append(counter.Name)
            .Append(@""",""value"":""")
            .Append(counter.Value.ToString(CultureInfo.InvariantCulture))
            .Append(@""",""type"":""")
            .Append(counter.Type.ToValue())
            .Append(@"""}");

        return _stringBuilder.ToString();
    }

    protected override string GetFileFooter() => "]}";
}