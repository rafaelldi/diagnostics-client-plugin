using JetBrains.Application.BuildScript.Application.Zones;

namespace DiagnosticsClientPlugin;

[ZoneMarker]
public class ZoneMarker : IRequire<IDiagnosticsClientPluginZone>
{
}