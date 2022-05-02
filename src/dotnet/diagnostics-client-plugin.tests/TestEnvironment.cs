using System.Threading;
using DiagnosticsClientPlugin;
using JetBrains.Application.BuildScript.Application.Zones;
using JetBrains.ReSharper.Feature.Services;
using JetBrains.ReSharper.Psi.CSharp;
using JetBrains.ReSharper.TestFramework;
using JetBrains.TestFramework;
using JetBrains.TestFramework.Application.Zones;
using NUnit.Framework;

[assembly: Apartment(ApartmentState.STA)]

namespace DiagnosticsClientPlugin.Tests
{

    [ZoneDefinition]
    public class DiagnosticsClientPluginTestEnvironmentZone : ITestsEnvZone, IRequire<PsiFeatureTestZone>, IRequire<IDiagnosticsClientPluginZone> { }

    [ZoneMarker]
    public class ZoneMarker : IRequire<ICodeEditingZone>, IRequire<ILanguageCSharpZone>, IRequire<DiagnosticsClientPluginTestEnvironmentZone> { }
    
    [SetUpFixture]
    public class DiagnosticsClientPluginTestsAssembly : ExtensionTestEnvironmentAssembly<DiagnosticsClientPluginTestEnvironmentZone> { }
}