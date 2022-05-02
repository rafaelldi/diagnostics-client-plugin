using System;
using System.IO;
using System.Threading;
using System.Threading.Channels;
using System.Threading.Tasks;
using DiagnosticsClientPlugin.Counters.Common;

namespace DiagnosticsClientPlugin.Counters.Consuming;

internal abstract class AbstractFileCountersConsumer
{
    private readonly ChannelReader<Counter> _reader;
    private readonly FileInfo _file;

    protected AbstractFileCountersConsumer(string filePath, ChannelReader<Counter> reader)
    {
        _file = CreateFile(filePath);
        _reader = reader;
    }

    private static FileInfo CreateFile(string filePath)
    {
        var file = new FileInfo(filePath);
        if (file.Exists)
        {
            file.Delete();
        }

        using var stream = file.Create();
        stream.Close();
        return file;
    }

    internal async Task ConsumeAsync(CancellationToken ct)
    {
        if (ct.IsCancellationRequested)
        {
            return;
        }

        using var streamWriter = _file.AppendText();

        await InitializeFileAsync(streamWriter);

        try
        {
            while (await _reader.WaitToReadAsync(ct))
            {
                if (_reader.TryRead(out var counter))
                {
                    await HandleCounterAsync(streamWriter, counter);
                }
            }
        }
        catch (OperationCanceledException)
        {
            //do nothing
        }

        await FinalizeFileAsync(streamWriter);
    }

    protected abstract Task InitializeFileAsync(StreamWriter sw);

    protected abstract Task HandleCounterAsync(StreamWriter sw, Counter counter);

    protected abstract Task FinalizeFileAsync(StreamWriter sw);
}