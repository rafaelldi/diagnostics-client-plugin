using System;
using System.IO;
using Microsoft.Diagnostics.NETCore.Client;

namespace DiagnosticsClientPlugin.EventPipes;

internal sealed class EventPipeSessionManager
{
    private readonly DiagnosticsClient _client;

    internal EventPipeSessionManager(int pid)
    {
        _client = new DiagnosticsClient(pid);
    }

    internal EventPipeSession StartSession(EventPipeProvider[] providers, bool requestRundown)
    {
        return _client.StartEventPipeSession(providers, requestRundown);
    }

    internal static void StopSession(EventPipeSession session)
    {
        try
        {
            session.Stop();
        }
        catch (EndOfStreamException)
        {
        }
        catch (TimeoutException)
        {
        }
        catch (PlatformNotSupportedException)
        {
        }
        catch (ServerNotAvailableException)
        {
        }
    }
}