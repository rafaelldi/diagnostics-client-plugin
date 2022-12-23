package model.rider

import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rd.generator.nova.PredefinedType.*
import com.jetbrains.rd.generator.nova.csharp.CSharp50Generator
import com.jetbrains.rd.generator.nova.kotlin.Kotlin11Generator
import com.jetbrains.rider.model.nova.ide.SolutionModel

@Suppress("unused")
object DiagnosticsHostModel : Ext(SolutionModel.Solution) {
    private val ProcessInfo = structdef {
        field("processName", string)
        field("filename", string.nullable)
        field("startTime", string.nullable)
        field("commandLine", string.nullable)
        field("operatingSystem", string.nullable)
        field("processArchitecture", string.nullable)
    }

    private val ProcessList = aggregatedef("ProcessList") {
        property("active", bool)
        map("items", int, ProcessInfo).async
    }

    private val CollectionSession = baseclass {
        field("duration", int.nullable)
        field("filePath", string)
    }

    private val CounterCollectionSession = classdef extends CollectionSession {
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

    private val GcEventCollectionSession = classdef extends CollectionSession {
    }

    private val TraceCollectionSession = classdef extends CollectionSession {
        field("profile", enum("TracingProfile") {
            +"None"
            +"CpuSampling"
            +"GcVerbose"
            +"GcCollect"
        })
        field("providers", string)
        field("predefinedProviders", immutableList(PredefinedProvider))
    }

    private val MonitoringSession = baseclass {
        property("active", bool)
        property("duration", int.nullable)
    }

    private val CounterMonitoringSession = classdef extends MonitoringSession {
        map("counters", string, Counter).async
        field("refreshInterval", int)
        field("providers", string)
        field("metrics", string.nullable)
        field("maxTimeSeries", int)
        field("maxHistograms", int)
    }

    private val GcEventMonitoringSession = classdef extends MonitoringSession {
        source("gcHappened", GcEvent).async
    }

    private val TraceMonitoringSession = classdef extends MonitoringSession {
        source("traceReceived", Trace).async
        field("predefinedProviders", immutableList(PredefinedProvider))
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

    init {
        setting(CSharp50Generator.Namespace, "DiagnosticsClientPlugin.Generated")
        setting(Kotlin11Generator.Namespace, "com.github.rafaelldi.diagnosticsclientplugin.generated")

        field("processList", ProcessList)

        map("counterCollectionSessions", int, CounterCollectionSession)
        map("counterMonitoringSessions", int, CounterMonitoringSession)

        map("gcEventCollectionSessions", int, GcEventCollectionSession)
        map("gcEventMonitoringSessions", int, GcEventMonitoringSession)
        source("triggerGc", int)

        map("traceCollectionSessions", int, TraceCollectionSession)
        map("traceMonitoringSessions", int, TraceMonitoringSession)

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