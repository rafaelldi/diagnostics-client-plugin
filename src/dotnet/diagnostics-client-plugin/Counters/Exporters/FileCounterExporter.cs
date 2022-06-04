using System;
using System.IO;
using System.Threading;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters.Common;
using DiagnosticsClientPlugin.Generated;

namespace DiagnosticsClientPlugin.Counters.Exporters;

internal abstract class FileCounterExporter
{
    private readonly ChannelReader<ValueCounter> _reader;
    private readonly string _filePath;

    protected FileCounterExporter(string filePath, ChannelReader<ValueCounter> reader)
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

        var header = GetFileHeader();
        if (header != null)
        {
            await streamWriter.WriteLineAsync(header);
        }

        try
        {
            while (await _reader.WaitToReadAsync(ct))
            {
                if (_reader.TryRead(out var counter))
                {
                    var counterString = GetCounterString(in counter);
                    await streamWriter.WriteLineAsync(counterString);
                }
            }
        }
        catch (OperationCanceledException)
        {
            //do nothing
        }

        var footer = GetFileFooter();
        if (footer != null)
        {
            await streamWriter.WriteLineAsync(footer);
        }
    }

    protected abstract string? GetFileHeader();

    protected abstract string GetCounterString(in ValueCounter counter);

    protected abstract string? GetFileFooter();
    
    internal static FileCounterExporter Create(
        string filePath,
        CounterFileFormat format,
        ChannelReader<ValueCounter> reader) =>
        format switch
        {
            CounterFileFormat.Csv => new CsvCounterExporter(filePath, reader),
            CounterFileFormat.Json => new JsonCounterExporter(filePath, reader),
            _ => throw new ArgumentOutOfRangeException(nameof(format), format, null)
        };
}