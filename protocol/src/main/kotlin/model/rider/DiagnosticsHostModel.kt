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
    }

    private val ProcessList = aggregatedef("ProcessList") {
        list("items", ProcessInfo)
        property("selected", int.nullable)
        source("refresh", void)
    }

    private val CountersCollectionSession = structdef {
        field("pid", int)
        field("filePath", string)
    }

    private val CountersMonitoringSession = classdef("CountersMonitoringSession") {
        field("pid", int)
        property("active", bool).async
        map("counters", string, Counter).async

        call("monitor", int.nullable, void)
        source("stop", void).async

        sink("started", void).async
        sink("stopped", void).async
    }

    private val Counter = structdef {
        field("name", string)
        field("value", double)
    }

    init {
        setting(CSharp50Generator.Namespace, "DiagnosticsClientPlugin.Generated")
        setting(Kotlin11Generator.Namespace, "com.github.rafaelldi.diagnosticsclientplugin.generated")

        field("processList", ProcessList)
        map("countersCollectionSessions", int, CountersCollectionSession).async
        map("countersMonitoringSessions", int, CountersMonitoringSession).async

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
            "collectCounters",
            structdef("CollectCountersCommand") {
                field("pid", int)
                field("filePath", string)
                field("format", enum("CountersFileFormat") {
                    +"Csv"
                    +"Json"
                })
                field("refreshInterval", int)
                field("providers", string)
                field("duration", int.nullable)
            },
            void
        )
        source("stopCountersCollection", structdef("StopCountersCollectionCommand") {
            field("pid", int)
        }).async

        call(
            "monitorCounters",
            structdef("MonitorCountersCommand") {
                field("pid", int)
                field("refreshInterval", int)
                field("providers", string)
                field("duration", int.nullable)
            },
            void
        )
        source("stopCountersMonitoring", structdef("StopCountersMonitoringCommand") {
            field("pid", int)
        }).async
        source("removeCountersMonitoringSession", structdef("RemoveCountersMonitoringSessionCommand") {
            field("pid", int)
        }).async
    }
}