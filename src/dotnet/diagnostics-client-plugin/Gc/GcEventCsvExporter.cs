using System;
using System.Globalization;
using System.IO;
using System.Text;
using System.Threading;
using System.Threading.Channels;
using System.Threading.Tasks;

namespace DiagnosticsClientPlugin.Gc;

internal sealed class GcEventCsvExporter
{
    private readonly string _filePath;
    private readonly ChannelReader<ValueGcEvent> _reader;
    private readonly StringBuilder _stringBuilder = new();

    internal GcEventCsvExporter(string filePath, ChannelReader<ValueGcEvent> reader)
    {
        _filePath = filePath;
        _reader = reader;
    }

    internal async Task ConsumeAsync(CancellationToken ct)
    {
        if (ct.IsCancellationRequested)
        {
            return;
        }

        using var streamWriter = File.CreateText(_filePath);

        await streamWriter.WriteLineAsync(GetHeader());

        try
        {
            while (await _reader.WaitToReadAsync(ct))
            {
                if (_reader.TryRead(out var gcEvent))
                {
                    await streamWriter.WriteLineAsync(GetGcEventLine(in gcEvent));
                }
            }
        }
        catch (OperationCanceledException)
        {
            //do nothing
        }
    }

    private string GetHeader() =>
        "Number,Generation,Reason,Pause Duration,Peak,After,Ratio,Promoted,Allocated,Allocation Rate," +
        "Size Gen 0,Fragmentation Gen 0,Survival Gen 0,Budget Gen 0," +
        "Size Gen 1,Fragmentation Gen 1,Survival Gen 1,Budget Gen 1," +
        "Size Gen 2,Fragmentation Gen 2,Survival Gen 2,Budget Gen 2," +
        "Size LOH,Fragmentation LOH,Survival LOH,Budget LOH,Pinned Objects";

    private string GetGcEventLine(in ValueGcEvent gcEvent)
    {
        _stringBuilder.Clear();

        _stringBuilder
            .Append(gcEvent.Number)
            .Append(",")
            .Append(gcEvent.Generation)
            .Append(",")
            .Append(gcEvent.Reason)
            .Append(",")
            .Append(gcEvent.PauseDuration.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.Peak.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.After.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.Ratio.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.Promoted.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.Allocated.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.AllocationRate.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.SizeGen0.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.FragmentationGen0.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.SurvivalGen0.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.BudgetGen0.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.SizeGen1.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.FragmentationGen1.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.SurvivalGen1.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.BudgetGen1.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.SizeGen2.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.FragmentationGen2.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.SurvivalGen2.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.BudgetGen2.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.SizeLoh.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.FragmentationLoh.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.SurvivalLoh.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.BudgetLoh.ToString(CultureInfo.InvariantCulture))
            .Append(",")
            .Append(gcEvent.PinnedObjects);

        return _stringBuilder.ToString();
    }
}