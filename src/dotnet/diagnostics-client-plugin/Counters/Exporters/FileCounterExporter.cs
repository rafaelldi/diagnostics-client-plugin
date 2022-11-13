using System;
using System.IO;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Generated;
using JetBrains.Lifetimes;

namespace DiagnosticsClientPlugin.Counters.Exporters;

internal abstract class FileCounterExporter
{
    private readonly string _filePath;
    private readonly ChannelReader<ValueCounter> _reader;

    protected FileCounterExporter(string filePath, ChannelReader<ValueCounter> reader)
    {
        _filePath = filePath;
        _reader = reader;
    }

    internal async Task ConsumeAsync()
    {
        if (Lifetime.AsyncLocal.Value.IsNotAlive)
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
            while (await _reader.WaitToReadAsync(Lifetime.AsyncLocal.Value))
            {
                if (_reader.TryRead(out var counter))
                {
                    await streamWriter.WriteLineAsync(GetCounterString(in counter));
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
            CounterFileFormat.Csv => new CounterCsvExporter(filePath, reader),
            CounterFileFormat.Json => new CounterJsonExporter(filePath, reader),
            _ => throw new ArgumentOutOfRangeException(nameof(format), format, null)
        };
}