using System.Diagnostics.Tracing;
using System.Linq;
using DiagnosticsClientPlugin.Generated;
using DiagnosticsClientPlugin.Traces;
using NUnit.Framework;
using Should;
using static Microsoft.Diagnostics.Tracing.Parsers.ClrTraceEventParser;
using static DiagnosticsClientPlugin.Common.Providers;

namespace DiagnosticsClientPlugin.Basic.Tests.Traces;

public class TraceProfileParserTests
{
    [Test]
    public void Parse_none_profile()
    {
        var providers = TraceProfileConverter.Convert(TracingProfile.None);

        providers.ShouldBeEmpty();
    }

    [Test]
    public void Parse_cpu_sampling_profile()
    {
        var providers = TraceProfileConverter.Convert(TracingProfile.CpuSampling);

        providers.Length.ShouldEqual(2);
        var sampleProvider = providers.Single(it => it.Name == SampleProfilerProvider);
        sampleProvider.Level.ShouldEqual(EventLevel.Informational);
        sampleProvider.Flags.ShouldEqual(0xF00000000000);
        var dotNetRuntimeProvider = providers.Single(it => it.Name == DotNetRuntimeProvider);
        dotNetRuntimeProvider.Level.ShouldEqual(EventLevel.Informational);
        dotNetRuntimeProvider.Flags.ShouldEqual((long)Keywords.Default);
    }

    [Test]
    public void Parse_gc_verbose_profile()
    {
        var providers = TraceProfileConverter.Convert(TracingProfile.GcVerbose);
        
        providers.Length.ShouldEqual(1);
        var provider = providers.Single();
        provider.Name.ShouldEqual(DotNetRuntimeProvider);
        provider.Level.ShouldEqual(EventLevel.Verbose);
        provider.Flags.ShouldEqual((long)Keywords.GC | (long)Keywords.GCHandle | (long)Keywords.Exception);
    }
    
    [Test]
    public void Parse_gc_collect_profile()
    {
        var providers = TraceProfileConverter.Convert(TracingProfile.GcCollect);
        
        providers.Length.ShouldEqual(1);
        var provider = providers.Single();
        provider.Name.ShouldEqual(DotNetRuntimeProvider);
        provider.Level.ShouldEqual(EventLevel.Informational);
        provider.Flags.ShouldEqual((long)Keywords.GC);
    }
}