using JetBrains.Application.BuildScript.Application.Zones;
using JetBrains.ProjectModel;

namespace DiagnosticsClientPlugin;

[ZoneDefinition(ZoneFlags.AutoEnable)]
public interface IDiagnosticsClientPluginZone : IProjectModelZone
{
}