package model.rider

import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rd.generator.nova.PredefinedType.*
import com.jetbrains.rd.generator.nova.csharp.CSharp50Generator
import com.jetbrains.rd.generator.nova.kotlin.Kotlin11Generator
import com.jetbrains.rider.model.nova.ide.SolutionModel

@Suppress("unused")
object DiagnosticsHostModel : Ext(SolutionModel.Solution) {
    private val ProcessInfo = structdef {
        field("processId", int)
        field("processName", string)
        field("filename", string.nullable)
        field("startTime", string.nullable)
        field("commandLine", string.nullable)
        field("operatingSystem", string.nullable)
        field("processArchitecture", string.nullable)
    }

    private val ProcessList = aggregatedef("ProcessList") {
        list("items", ProcessInfo)
        source("refresh", void)
    }

    private val GcEventsMonitoringSession = classdef("GcEventsMonitoringSession") {
        field("pid", int)
        property("active", bool).async
        source("gcHappened", GcEvent).async
        call("monitor", int.nullable, void)
        source("close", void).async
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

    private val CounterCollectionSession = classdef("CounterCollectionSession") {
        field("filePath", string)
        field("format", enum("CounterFileFormat") {
            +"Csv"
            +"Json"
        })
        field("refreshInterval", int)
        field("providers", string)
        field("metrics", string.nullable)
        field("maxTimeSeries", int)
        field("maxHistograms", int)
        field("duration", int.nullable)
    }

    private val CounterMonitoringSession = classdef("CountersMonitoringSession") {
        property("active", bool)
        property("duration", int.nullable)
        map("counters", string, Counter).async

        field("refreshInterval", int)
        field("providers", string)
        field("metrics", string.nullable)
        field("maxTimeSeries", int)
        field("maxHistograms", int)
    }

    init {
        setting(CSharp50Generator.Namespace, "DiagnosticsClientPlugin.Generated")
        setting(Kotlin11Generator.Namespace, "com.github.rafaelldi.diagnosticsclientplugin.generated")

        field("processList", ProcessList)

        list("gcEventsCollectionSessions", int).async
        map("gcEventsMonitoringSessions", int, GcEventsMonitoringSession).async
        list("traceCollectionSessions", int).async

        map("counterCollectionSessions", int, CounterCollectionSession)
        map("counterMonitoringSessions", int, CounterMonitoringSession)

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
            "collectGcEvents",
            structdef("CollectGcEventsCommand") {
                field("pid", int)
                field("filePath", string)
                field("duration", int.nullable)
            },
            void
        )

        call(
            "monitorGcEvents",
            structdef("MonitorGcEventsCommand") {
                field("pid", int)
                field("duration", int.nullable)
            },
            void
        )

        call("triggerGc", structdef("TriggerGcCommand") { field("pid", int) }, void)

        call(
            "collectTraces",
            structdef("CollectTracesCommand") {
                field("pid", int)
                field("filePath", string)
                field("profile", enum("TracingProfile") {
                    +"None"
                    +"CpuSampling"
                    +"GcVerbose"
                    +"GcCollect"
                })
                field("providers", string)
                field("duration", int.nullable)
            },
            void
        )

        call(
            "collectStackTrace",
            structdef("CollectStackTraceCommand") {
                field("pid", int)
            },
            string
        )
    }
}