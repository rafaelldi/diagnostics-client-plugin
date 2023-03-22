package model.rider

import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rd.generator.nova.PredefinedType.*
import com.jetbrains.rd.generator.nova.csharp.CSharp50Generator
import com.jetbrains.rd.generator.nova.kotlin.Kotlin11Generator
import com.jetbrains.rider.model.nova.ide.SolutionModel

@Suppress("unused")
object DiagnosticsHostModel : Ext(SolutionModel.Solution) {
    private val ProcessEnvironmentVariable = structdef {
        field("key", string)
        field("value", string)
    }

    private val ProcessInfo = structdef {
        field("processName", string)
        field("filename", string.nullable)
        field("startTime", string.nullable)
        field("commandLine", string.nullable)
        field("operatingSystem", string.nullable)
        field("processArchitecture", string.nullable)
        field("environmentVariables", array(ProcessEnvironmentVariable))
    }

    private val ProcessList = aggregatedef("ProcessList") {
        property("active", bool)
        map("items", int, ProcessInfo).async
    }

    private val PersistentSession = baseclass {
        field("duration", int.nullable)
        field("filePath", string)
    }

    private val PersistentCounterSession = classdef extends PersistentSession {
        field("format", enum("CounterFileFormat") {
            +"Csv"
            +"Json"
        })
        field("refreshInterval", int)
        field("providers", string)
        field("metrics", string.nullable)
        field("maxTimeSeries", int)
        field("maxHistograms", int)
    }

    private val PersistentGcEventSession = classdef extends PersistentSession {
    }

    private val PersistentTraceSession = classdef extends PersistentSession {
        field("profile", enum("TracingProfile") {
            +"None"
            +"CpuSampling"
            +"GcVerbose"
            +"GcCollect"
        })
        field("providers", string)
        field("predefinedProviders", immutableList(PredefinedProvider))
    }

    private val LiveSession = baseclass {
        property("active", bool)
        property("duration", int.nullable)
    }

    private val LiveCounterSession = classdef extends LiveSession {
        map("counters", string, Counter).async
        field("refreshInterval", int)
        field("providers", string)
        field("metrics", string.nullable)
        field("maxTimeSeries", int)
        field("maxHistograms", int)
    }

    private val LiveGcEventSession = classdef extends LiveSession {
        source("gcHappened", GcEvent).async
    }

    private val LiveTraceSession = classdef extends LiveSession {
        source("traceReceived", Trace).async
        field("predefinedProviders", immutableList(PredefinedProvider))
    }

    private val LiveChartSession = classdef extends LiveSession {
        source("valueReceived", ChartValue).async
    }

    private val Counter = structdef {
        field("name", string)
        field("tags", string.nullable)
        field("value", double)
    }

    private val GcEvent = structdef {
        field("number", int)
        field("generation", string)
        field("reason", string)
        field("pauseDuration", double)
        field("peak", double)
        field("after", double)
        field("ratio", double)
        field("promoted", double)
        field("allocated", double)
        field("allocationRate", double)
        field("sizeGen0", double)
        field("sizeGen1", double)
        field("sizeGen2", double)
        field("sizeLoh", double)
        field("pinnedObjects", int)
    }

    private val PredefinedProvider = enum("PredefinedProvider") {
        +"Http"
        +"AspNet"
        +"EF"
        +"Exceptions"
        +"Threads"
        +"Contentions"
        +"Tasks"
        +"Loader"
    }

    private val Trace = structdef {
        field("eventName", string)
        field("provider", PredefinedProvider)
        field("timeStamp", dateTime)
        field("content", string)
    }

    private val ChartValue = structdef {
        field("timeStamp", long)
        field("value", double)
        field("type", ChartValueType)
    }

    private val ChartValueType = enum("ChartValueType") {
        +"Cpu"
        +"WorkingSet"
        +"GcHeapSize"
    }

    init {
        setting(CSharp50Generator.Namespace, "DiagnosticsClientPlugin.Generated")
        setting(Kotlin11Generator.Namespace, "com.github.rafaelldi.diagnosticsclientplugin.generated")

        field("processList", ProcessList)

        map("persistentCounterSessions", int, PersistentCounterSession)
        map("liveCounterSessions", int, LiveCounterSession)

        map("persistentGcEventSessions", int, PersistentGcEventSession)
        map("liveGcEventSessions", int, LiveGcEventSession)
        source("triggerGc", int)

        map("persistentTraceSessions", int, PersistentTraceSession)
        map("liveTraceSessions", int, LiveTraceSession)

        map("liveChartSessions", int, LiveChartSession)

        call("collectDump",
            structdef("CollectDumpCommand") {
                field("pid", int)
                field("type", enum("DumpType") {
                    +"Full"
                    +"Heap"
                    +"Triage"
                    +"Mini"
                })
                field("outFolder", string)
                field("filename", string)
                field("diag", bool)
            }, structdef("DumpCollectionResult") {
                field("filePath", string)
            })

        call(
            "collectStackTrace",
            structdef("CollectStackTraceCommand") {
                field("pid", int)
            },
            string
        )
    }
}