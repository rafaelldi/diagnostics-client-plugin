package model.rider

import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rd.generator.nova.PredefinedType.*
import com.jetbrains.rd.generator.nova.csharp.CSharp50Generator
import com.jetbrains.rd.generator.nova.kotlin.Kotlin11Generator

object DiagnosticsHostRoot : Root() {
    init {
        setting(Kotlin11Generator.Namespace, "com.github.rafaelldi.diagnosticsclientplugin.model")
        setting(CSharp50Generator.Namespace, "DiagnosticsAgent.Model")
    }
}

@Suppress("unused")
object DiagnosticsHostModel : Ext(DiagnosticsHostRoot) {
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
        map("items", int, ProcessInfo).async
    }

    private val Session = baseclass {
        property("active", bool)
        property("duration", int.nullable)
    }

    private val CounterSession = classdef extends Session {
        source("counterReceived", Counter).async
        field("refreshInterval", int)
        field("providers", string)
        field("metrics", string.nullable)
        field("maxTimeSeries", int)
        field("maxHistograms", int)
    }

    private val GcEventSession = classdef extends Session {
        source("gcHappened", GcEvent).async
    }

    private val TraceSession = classdef extends Session {
        source("traceReceived", Trace).async
        field("predefinedProviders", immutableList(PredefinedProvider))
    }

    private val ChartSession = classdef extends Session {
        source("eventReceived", ChartEvent).async
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

    private val ChartEvent = structdef {
        field("timeStamp", long)
        field("value", double)
        field("type", ChartEventType)
        field("label", string.nullable)
    }

    private val ChartEventType = enum("ChartEventType") {
        +"Cpu"
        +"WorkingSet"
        +"GcHeapSize"
        +"Gc"
        +"Exception"
        +"ExceptionCount"
        +"ThreadCount"
    }

    init {
        field("processList", ProcessList)

        map("counterSessions", int, CounterSession)
        map("gcEventSessions", int, GcEventSession)
        map("traceSessions", int, TraceSession)
        map("chartSessions", int, ChartSession)

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
            }
        )

        call(
            "collectStackTrace",
            structdef("CollectStackTraceCommand") {
                field("pid", int)
            },
            string
        )

        call(
            "collectCounters",
            structdef("CollectCounterCommand") {
                field("pid", int)
                field("duration", int.nullable)
                field("exportFilePath", string)
                field("format", enum("CounterFileFormat") {
                    +"Csv"
                    +"Json"
                })
                field("refreshInterval", int)
                field("providers", string)
                field("metrics", string.nullable)
                field("maxTimeSeries", int)
                field("maxHistograms", int)
            }, structdef("CounterCollectionResult") {
                field("filePath", string)
            }
        )

        call(
            "collectGcEvents",
            structdef("CollectGcEventCommand") {
                field("pid", int)
                field("duration", int.nullable)
                field("exportFilePath", string)
            }, structdef("GcEventCollectionResult") {
                field("filePath", string)
            }
        )

        call(
            "collectTraces",
            structdef("CollectTraceCommand") {
                field("pid", int)
                field("duration", int.nullable)
                field("exportFilePath", string)
                field("profile", enum("TracingProfile") {
                    +"None"
                    +"CpuSampling"
                    +"GcVerbose"
                    +"GcCollect"
                })
                field("providers", string)
                field("predefinedProviders", immutableList(PredefinedProvider))
            }, structdef("TraceCollectionResult") {
                field("filePath", string)
            }
        )
    }
}