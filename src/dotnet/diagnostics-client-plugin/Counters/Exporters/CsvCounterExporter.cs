using System.Globalization;
using System.Text;
using System.Threading.Channels;
using DiagnosticsClientPlugin.Counters.Common;

namespace DiagnosticsClientPlugin.Counters.Exporters;

internal sealed class CsvCounterExporter : FileCounterExporter
{
    private readonly StringBuilder _stringBuilder = new();

    public CsvCounterExporter(string filePath, ChannelReader<ValueCounter> reader)
        : base(filePath, reader)
    {
    }

    protected override string GetFileHeader() => "Timestamp,Provider,Counter,Value,Type";

    protected override string GetCounterString(in ValueCounter counter)
    {
        if (_stringBuilder.Length > 0)
        {
            _stringBuilder.Clear();
        }

        _stringBuilder
            .Append(counter.TimeStamp.ToString(CultureInfo.CurrentCulture))
            .Append(",")
            .Append(counter.ProviderName)
            .Append(",")
            .Append(counter.Name)
            .Append(",")
            .Append(counter.Value.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(counter.Type.ToValue());

        return _stringBuilder.ToString();
    }

    protected override string? GetFileFooter() => null;
}