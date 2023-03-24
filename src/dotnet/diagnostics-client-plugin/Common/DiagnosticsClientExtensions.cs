using System;
using System.Reflection;
using System.Threading.Tasks;
using Microsoft.Diagnostics.NETCore.Client;

namespace DiagnosticsClientPlugin.Common;

internal static class DiagnosticsClientExtensions
{
    private static readonly Type DiagnosticsClientType = typeof(DiagnosticsClient);

    private static readonly MethodInfo? GetProcessInfoMethod =
        DiagnosticsClientType.GetMethod("GetProcessInfo", BindingFlags.NonPublic | BindingFlags.Instance);

    private static readonly Type ProcessInfoType =
        DiagnosticsClientType.Assembly.GetType("Microsoft.Diagnostics.NETCore.Client.ProcessInfo");

    private static readonly PropertyInfo? ProcessInfoCommandLineProperty =
        ProcessInfoType.GetProperty("CommandLine");

    private static readonly PropertyInfo? ProcessInfoOperatingSystemProperty =
        ProcessInfoType.GetProperty("OperatingSystem");

    private static readonly PropertyInfo? ProcessInfoProcessArchitectureProperty =
        ProcessInfoType.GetProperty("ProcessArchitecture");

    private static readonly Type ReversedDiagnosticsServerType =
        DiagnosticsClientType.Assembly.GetType("Microsoft.Diagnostics.NETCore.Client.ReversedDiagnosticsServer");

    private static readonly MethodInfo? DiagnosticsServerStartMethod =
        ReversedDiagnosticsServerType.GetMethod("Start", Array.Empty<Type>());

    private static readonly MethodInfo? DiagnosticsServerAcceptMethod =
        ReversedDiagnosticsServerType.GetMethod("Accept");

    private static readonly MethodInfo? DiagnosticsServerDisposeAsyncMethod =
        ReversedDiagnosticsServerType.GetMethod("DisposeAsync");

    private static readonly Type IpcEndpointInfoType =
        DiagnosticsClientType.Assembly.GetType("Microsoft.Diagnostics.NETCore.Client.IpcEndpointInfo");

    private static readonly PropertyInfo? IpcEndpointInfoEndpointProperty =
        IpcEndpointInfoType.GetProperty("Endpoint");

    private static readonly PropertyInfo? IpcEndpointInfoProcessIdProperty =
        IpcEndpointInfoType.GetProperty("ProcessId");

    private static readonly MethodInfo? ResumeRuntimeMethod =
        DiagnosticsClientType.GetMethod("ResumeRuntime", BindingFlags.NonPublic | BindingFlags.Instance);

    internal static (string? CommandLine, string? OperatingSystem, string? ProcessArchitecture) GetProcessInfo(
        this DiagnosticsClient client)
    {
        var processInfo = GetProcessInfoMethod?.Invoke(client, Array.Empty<object>());
        if (processInfo is null)
        {
            return (null, null, null);
        }

        var commandLine = (string?)ProcessInfoCommandLineProperty?.GetValue(processInfo);
        var operatingSystem = (string?)ProcessInfoOperatingSystemProperty?.GetValue(processInfo);
        var processArchitecture = (string?)ProcessInfoProcessArchitectureProperty?.GetValue(processInfo);

        return (commandLine, operatingSystem, processArchitecture);
    }

    internal static object NewReversedDiagnosticsServer(string address) =>
        Activator.CreateInstance(ReversedDiagnosticsServerType, address);

    internal static void StartDiagnosticsServer(object server)
    {
        DiagnosticsServerStartMethod?.Invoke(server, Array.Empty<object>());
    }

    internal static ValueTask DisposeDiagnosticsServerAsync(object server)
    {
        if (DiagnosticsServerDisposeAsyncMethod is null)
        {
            return new ValueTask();
        }

        return (ValueTask)DiagnosticsServerDisposeAsyncMethod.Invoke(server, Array.Empty<object>());
    }

    public static DiagnosticsClient WaitForProcessToConnect(object server, int pid, TimeSpan timeout)
    {
        var endpointInfo = DiagnosticsServerAcceptMethod?.Invoke(server, new object[] { timeout });

        while ((int?)IpcEndpointInfoProcessIdProperty?.GetValue(endpointInfo) != pid)
        {
            endpointInfo = DiagnosticsServerAcceptMethod?.Invoke(server, new object[] { timeout });
        }

        var endpoint = IpcEndpointInfoEndpointProperty?.GetValue(endpointInfo);

        return (DiagnosticsClient)Activator.CreateInstance
        (
            DiagnosticsClientType,
            BindingFlags.NonPublic | BindingFlags.Instance,
            null,
            new[] { endpoint },
            null
        );
    }

    // ReSharper disable once UnusedMember.Global
    internal static void ResumeRuntime(DiagnosticsClient client)
    {
        ResumeRuntimeMethod?.Invoke(client, Array.Empty<object>());
    }
}