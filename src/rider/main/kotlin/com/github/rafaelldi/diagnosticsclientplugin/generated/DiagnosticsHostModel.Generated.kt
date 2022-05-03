@file:Suppress("EXPERIMENTAL_API_USAGE","EXPERIMENTAL_UNSIGNED_LITERALS","PackageDirectoryMismatch","UnusedImport","unused","LocalVariableName","CanBeVal","PropertyName","EnumEntryName","ClassName","ObjectPropertyName","UnnecessaryVariable","SpellCheckingInspection")
package com.github.rafaelldi.diagnosticsclientplugin.generated

import com.jetbrains.rd.framework.*
import com.jetbrains.rd.framework.base.*
import com.jetbrains.rd.framework.impl.*

import com.jetbrains.rd.util.lifetime.*
import com.jetbrains.rd.util.reactive.*
import com.jetbrains.rd.util.string.*
import com.jetbrains.rd.util.*
import kotlin.reflect.KClass
import kotlin.jvm.JvmStatic



/**
 * #### Generated from [DiagnosticsHostModel.kt:10]
 */
class DiagnosticsHostModel private constructor(
    val processList: ProcessList,
    private val _countersCollectionSessions: RdMap<Int, CountersCollectionSession>,
    private val _countersMonitoringSessions: RdMap<Int, CountersMonitoringSession>,
    private val _collectDump: RdCall<CollectDumpCommand, DumpCollectionResult>,
    private val _collectCounters: RdCall<CollectCountersCommand, Unit>,
    private val _stopCountersCollection: RdSignal<StopCountersCollectionCommand>,
    private val _monitorCounters: RdCall<MonitorCountersCommand, Unit>,
    private val _stopCountersMonitoring: RdSignal<StopCountersMonitoringCommand>,
    private val _removeCountersMonitoringSession: RdSignal<RemoveCountersMonitoringSessionCommand>
) : RdExtBase() {
    //companion
    
    companion object : ISerializersOwner {
        
        override fun registerSerializersCore(serializers: ISerializers)  {
            serializers.register(ProcessInfo)
            serializers.register(ProcessList)
            serializers.register(CountersCollectionSession)
            serializers.register(CountersMonitoringSession)
            serializers.register(Counter)
            serializers.register(CollectDumpCommand)
            serializers.register(DumpCollectionResult)
            serializers.register(CollectCountersCommand)
            serializers.register(StopCountersCollectionCommand)
            serializers.register(MonitorCountersCommand)
            serializers.register(StopCountersMonitoringCommand)
            serializers.register(RemoveCountersMonitoringSessionCommand)
            serializers.register(DumpType.marshaller)
            serializers.register(CountersFileFormat.marshaller)
        }
        
        
        
        
        
        const val serializationHash = 1120346281893133712L
        
    }
    override val serializersOwner: ISerializersOwner get() = DiagnosticsHostModel
    override val serializationHash: Long get() = DiagnosticsHostModel.serializationHash
    
    //fields
    val countersCollectionSessions: IMutableViewableMap<Int, CountersCollectionSession> get() = _countersCollectionSessions
    val countersMonitoringSessions: IMutableViewableMap<Int, CountersMonitoringSession> get() = _countersMonitoringSessions
    val collectDump: IRdCall<CollectDumpCommand, DumpCollectionResult> get() = _collectDump
    val collectCounters: IRdCall<CollectCountersCommand, Unit> get() = _collectCounters
    val stopCountersCollection: IAsyncSignal<StopCountersCollectionCommand> get() = _stopCountersCollection
    val monitorCounters: IRdCall<MonitorCountersCommand, Unit> get() = _monitorCounters
    val stopCountersMonitoring: IAsyncSignal<StopCountersMonitoringCommand> get() = _stopCountersMonitoring
    val removeCountersMonitoringSession: IAsyncSignal<RemoveCountersMonitoringSessionCommand> get() = _removeCountersMonitoringSession
    //methods
    //initializer
    init {
        _countersCollectionSessions.optimizeNested = true
    }
    
    init {
        _countersCollectionSessions.async = true
        _countersMonitoringSessions.async = true
        _stopCountersCollection.async = true
        _stopCountersMonitoring.async = true
        _removeCountersMonitoringSession.async = true
    }
    
    init {
        bindableChildren.add("processList" to processList)
        bindableChildren.add("countersCollectionSessions" to _countersCollectionSessions)
        bindableChildren.add("countersMonitoringSessions" to _countersMonitoringSessions)
        bindableChildren.add("collectDump" to _collectDump)
        bindableChildren.add("collectCounters" to _collectCounters)
        bindableChildren.add("stopCountersCollection" to _stopCountersCollection)
        bindableChildren.add("monitorCounters" to _monitorCounters)
        bindableChildren.add("stopCountersMonitoring" to _stopCountersMonitoring)
        bindableChildren.add("removeCountersMonitoringSession" to _removeCountersMonitoringSession)
    }
    
    //secondary constructor
    internal constructor(
    ) : this(
        ProcessList(),
        RdMap<Int, CountersCollectionSession>(FrameworkMarshallers.Int, CountersCollectionSession),
        RdMap<Int, CountersMonitoringSession>(FrameworkMarshallers.Int, CountersMonitoringSession),
        RdCall<CollectDumpCommand, DumpCollectionResult>(CollectDumpCommand, DumpCollectionResult),
        RdCall<CollectCountersCommand, Unit>(CollectCountersCommand, FrameworkMarshallers.Void),
        RdSignal<StopCountersCollectionCommand>(StopCountersCollectionCommand),
        RdCall<MonitorCountersCommand, Unit>(MonitorCountersCommand, FrameworkMarshallers.Void),
        RdSignal<StopCountersMonitoringCommand>(StopCountersMonitoringCommand),
        RdSignal<RemoveCountersMonitoringSessionCommand>(RemoveCountersMonitoringSessionCommand)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("DiagnosticsHostModel (")
        printer.indent {
            print("processList = "); processList.print(printer); println()
            print("countersCollectionSessions = "); _countersCollectionSessions.print(printer); println()
            print("countersMonitoringSessions = "); _countersMonitoringSessions.print(printer); println()
            print("collectDump = "); _collectDump.print(printer); println()
            print("collectCounters = "); _collectCounters.print(printer); println()
            print("stopCountersCollection = "); _stopCountersCollection.print(printer); println()
            print("monitorCounters = "); _monitorCounters.print(printer); println()
            print("stopCountersMonitoring = "); _stopCountersMonitoring.print(printer); println()
            print("removeCountersMonitoringSession = "); _removeCountersMonitoringSession.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): DiagnosticsHostModel   {
        return DiagnosticsHostModel(
            processList.deepClonePolymorphic(),
            _countersCollectionSessions.deepClonePolymorphic(),
            _countersMonitoringSessions.deepClonePolymorphic(),
            _collectDump.deepClonePolymorphic(),
            _collectCounters.deepClonePolymorphic(),
            _stopCountersCollection.deepClonePolymorphic(),
            _monitorCounters.deepClonePolymorphic(),
            _stopCountersMonitoring.deepClonePolymorphic(),
            _removeCountersMonitoringSession.deepClonePolymorphic()
        )
    }
    //contexts
}
val com.jetbrains.rd.ide.model.Solution.diagnosticsHostModel get() = getOrCreateExtension("diagnosticsHostModel", ::DiagnosticsHostModel)



/**
 * #### Generated from [DiagnosticsHostModel.kt:69]
 */
data class CollectCountersCommand (
    val pid: Int,
    val filePath: String,
    val format: CountersFileFormat,
    val refreshInterval: Int,
    val providers: String,
    val duration: Int?
) : IPrintable {
    //companion
    
    companion object : IMarshaller<CollectCountersCommand> {
        override val _type: KClass<CollectCountersCommand> = CollectCountersCommand::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): CollectCountersCommand  {
            val pid = buffer.readInt()
            val filePath = buffer.readString()
            val format = buffer.readEnum<CountersFileFormat>()
            val refreshInterval = buffer.readInt()
            val providers = buffer.readString()
            val duration = buffer.readNullable { buffer.readInt() }
            return CollectCountersCommand(pid, filePath, format, refreshInterval, providers, duration)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: CollectCountersCommand)  {
            buffer.writeInt(value.pid)
            buffer.writeString(value.filePath)
            buffer.writeEnum(value.format)
            buffer.writeInt(value.refreshInterval)
            buffer.writeString(value.providers)
            buffer.writeNullable(value.duration) { buffer.writeInt(it) }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as CollectCountersCommand
        
        if (pid != other.pid) return false
        if (filePath != other.filePath) return false
        if (format != other.format) return false
        if (refreshInterval != other.refreshInterval) return false
        if (providers != other.providers) return false
        if (duration != other.duration) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + pid.hashCode()
        __r = __r*31 + filePath.hashCode()
        __r = __r*31 + format.hashCode()
        __r = __r*31 + refreshInterval.hashCode()
        __r = __r*31 + providers.hashCode()
        __r = __r*31 + if (duration != null) duration.hashCode() else 0
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("CollectCountersCommand (")
        printer.indent {
            print("pid = "); pid.print(printer); println()
            print("filePath = "); filePath.print(printer); println()
            print("format = "); format.print(printer); println()
            print("refreshInterval = "); refreshInterval.print(printer); println()
            print("providers = "); providers.print(printer); println()
            print("duration = "); duration.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:52]
 */
data class CollectDumpCommand (
    val pid: Int,
    val type: DumpType,
    val outFolder: String,
    val filename: String,
    val diag: Boolean
) : IPrintable {
    //companion
    
    companion object : IMarshaller<CollectDumpCommand> {
        override val _type: KClass<CollectDumpCommand> = CollectDumpCommand::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): CollectDumpCommand  {
            val pid = buffer.readInt()
            val type = buffer.readEnum<DumpType>()
            val outFolder = buffer.readString()
            val filename = buffer.readString()
            val diag = buffer.readBool()
            return CollectDumpCommand(pid, type, outFolder, filename, diag)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: CollectDumpCommand)  {
            buffer.writeInt(value.pid)
            buffer.writeEnum(value.type)
            buffer.writeString(value.outFolder)
            buffer.writeString(value.filename)
            buffer.writeBool(value.diag)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as CollectDumpCommand
        
        if (pid != other.pid) return false
        if (type != other.type) return false
        if (outFolder != other.outFolder) return false
        if (filename != other.filename) return false
        if (diag != other.diag) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + pid.hashCode()
        __r = __r*31 + type.hashCode()
        __r = __r*31 + outFolder.hashCode()
        __r = __r*31 + filename.hashCode()
        __r = __r*31 + diag.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("CollectDumpCommand (")
        printer.indent {
            print("pid = "); pid.print(printer); println()
            print("type = "); type.print(printer); println()
            print("outFolder = "); outFolder.print(printer); println()
            print("filename = "); filename.print(printer); println()
            print("diag = "); diag.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:38]
 */
data class Counter (
    val name: String,
    val value: Double
) : IPrintable {
    //companion
    
    companion object : IMarshaller<Counter> {
        override val _type: KClass<Counter> = Counter::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): Counter  {
            val name = buffer.readString()
            val value = buffer.readDouble()
            return Counter(name, value)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: Counter)  {
            buffer.writeString(value.name)
            buffer.writeDouble(value.value)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as Counter
        
        if (name != other.name) return false
        if (value != other.value) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + name.hashCode()
        __r = __r*31 + value.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("Counter (")
        printer.indent {
            print("name = "); name.print(printer); println()
            print("value = "); value.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:24]
 */
data class CountersCollectionSession (
    val pid: Int,
    val filePath: String
) : IPrintable {
    //companion
    
    companion object : IMarshaller<CountersCollectionSession> {
        override val _type: KClass<CountersCollectionSession> = CountersCollectionSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): CountersCollectionSession  {
            val pid = buffer.readInt()
            val filePath = buffer.readString()
            return CountersCollectionSession(pid, filePath)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: CountersCollectionSession)  {
            buffer.writeInt(value.pid)
            buffer.writeString(value.filePath)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as CountersCollectionSession
        
        if (pid != other.pid) return false
        if (filePath != other.filePath) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + pid.hashCode()
        __r = __r*31 + filePath.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("CountersCollectionSession (")
        printer.indent {
            print("pid = "); pid.print(printer); println()
            print("filePath = "); filePath.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:72]
 */
enum class CountersFileFormat {
    Csv, 
    Json;
    
    companion object {
        val marshaller = FrameworkMarshallers.enum<CountersFileFormat>()
        
    }
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:29]
 */
class CountersMonitoringSession private constructor(
    val pid: Int,
    private val _active: RdOptionalProperty<Boolean>,
    private val _counters: RdMap<String, Counter>,
    private val _monitor: RdCall<Int?, Unit>,
    private val _stop: RdSignal<Unit>
) : RdBindableBase() {
    //companion
    
    companion object : IMarshaller<CountersMonitoringSession> {
        override val _type: KClass<CountersMonitoringSession> = CountersMonitoringSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): CountersMonitoringSession  {
            val _id = RdId.read(buffer)
            val pid = buffer.readInt()
            val _active = RdOptionalProperty.read(ctx, buffer, FrameworkMarshallers.Bool)
            val _counters = RdMap.read(ctx, buffer, FrameworkMarshallers.String, Counter)
            val _monitor = RdCall.read(ctx, buffer, __IntNullableSerializer, FrameworkMarshallers.Void)
            val _stop = RdSignal.read(ctx, buffer, FrameworkMarshallers.Void)
            return CountersMonitoringSession(pid, _active, _counters, _monitor, _stop).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: CountersMonitoringSession)  {
            value.rdid.write(buffer)
            buffer.writeInt(value.pid)
            RdOptionalProperty.write(ctx, buffer, value._active)
            RdMap.write(ctx, buffer, value._counters)
            RdCall.write(ctx, buffer, value._monitor)
            RdSignal.write(ctx, buffer, value._stop)
        }
        
        private val __IntNullableSerializer = FrameworkMarshallers.Int.nullable()
        
    }
    //fields
    val active: IOptProperty<Boolean> get() = _active
    val counters: IMutableViewableMap<String, Counter> get() = _counters
    val monitor: IRdCall<Int?, Unit> get() = _monitor
    val stop: IAsyncSignal<Unit> get() = _stop
    //methods
    //initializer
    init {
        _active.optimizeNested = true
        _counters.optimizeNested = true
    }
    
    init {
        _active.async = true
        _counters.async = true
        _stop.async = true
    }
    
    init {
        bindableChildren.add("active" to _active)
        bindableChildren.add("counters" to _counters)
        bindableChildren.add("monitor" to _monitor)
        bindableChildren.add("stop" to _stop)
    }
    
    //secondary constructor
    constructor(
        pid: Int
    ) : this(
        pid,
        RdOptionalProperty<Boolean>(FrameworkMarshallers.Bool),
        RdMap<String, Counter>(FrameworkMarshallers.String, Counter),
        RdCall<Int?, Unit>(__IntNullableSerializer, FrameworkMarshallers.Void),
        RdSignal<Unit>(FrameworkMarshallers.Void)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("CountersMonitoringSession (")
        printer.indent {
            print("pid = "); pid.print(printer); println()
            print("active = "); _active.print(printer); println()
            print("counters = "); _counters.print(printer); println()
            print("monitor = "); _monitor.print(printer); println()
            print("stop = "); _stop.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): CountersMonitoringSession   {
        return CountersMonitoringSession(
            pid,
            _active.deepClonePolymorphic(),
            _counters.deepClonePolymorphic(),
            _monitor.deepClonePolymorphic(),
            _stop.deepClonePolymorphic()
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:63]
 */
data class DumpCollectionResult (
    val filePath: String
) : IPrintable {
    //companion
    
    companion object : IMarshaller<DumpCollectionResult> {
        override val _type: KClass<DumpCollectionResult> = DumpCollectionResult::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): DumpCollectionResult  {
            val filePath = buffer.readString()
            return DumpCollectionResult(filePath)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: DumpCollectionResult)  {
            buffer.writeString(value.filePath)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as DumpCollectionResult
        
        if (filePath != other.filePath) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + filePath.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("DumpCollectionResult (")
        printer.indent {
            print("filePath = "); filePath.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:54]
 */
enum class DumpType {
    Full, 
    Heap, 
    Triage, 
    Mini;
    
    companion object {
        val marshaller = FrameworkMarshallers.enum<DumpType>()
        
    }
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:88]
 */
data class MonitorCountersCommand (
    val pid: Int,
    val refreshInterval: Int,
    val providers: String,
    val duration: Int?
) : IPrintable {
    //companion
    
    companion object : IMarshaller<MonitorCountersCommand> {
        override val _type: KClass<MonitorCountersCommand> = MonitorCountersCommand::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): MonitorCountersCommand  {
            val pid = buffer.readInt()
            val refreshInterval = buffer.readInt()
            val providers = buffer.readString()
            val duration = buffer.readNullable { buffer.readInt() }
            return MonitorCountersCommand(pid, refreshInterval, providers, duration)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: MonitorCountersCommand)  {
            buffer.writeInt(value.pid)
            buffer.writeInt(value.refreshInterval)
            buffer.writeString(value.providers)
            buffer.writeNullable(value.duration) { buffer.writeInt(it) }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as MonitorCountersCommand
        
        if (pid != other.pid) return false
        if (refreshInterval != other.refreshInterval) return false
        if (providers != other.providers) return false
        if (duration != other.duration) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + pid.hashCode()
        __r = __r*31 + refreshInterval.hashCode()
        __r = __r*31 + providers.hashCode()
        __r = __r*31 + if (duration != null) duration.hashCode() else 0
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("MonitorCountersCommand (")
        printer.indent {
            print("pid = "); pid.print(printer); println()
            print("refreshInterval = "); refreshInterval.print(printer); println()
            print("providers = "); providers.print(printer); println()
            print("duration = "); duration.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:11]
 */
data class ProcessInfo (
    val processId: Int,
    val processName: String,
    val filename: String?,
    val startTime: String?
) : IPrintable {
    //companion
    
    companion object : IMarshaller<ProcessInfo> {
        override val _type: KClass<ProcessInfo> = ProcessInfo::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ProcessInfo  {
            val processId = buffer.readInt()
            val processName = buffer.readString()
            val filename = buffer.readNullable { buffer.readString() }
            val startTime = buffer.readNullable { buffer.readString() }
            return ProcessInfo(processId, processName, filename, startTime)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ProcessInfo)  {
            buffer.writeInt(value.processId)
            buffer.writeString(value.processName)
            buffer.writeNullable(value.filename) { buffer.writeString(it) }
            buffer.writeNullable(value.startTime) { buffer.writeString(it) }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as ProcessInfo
        
        if (processId != other.processId) return false
        if (processName != other.processName) return false
        if (filename != other.filename) return false
        if (startTime != other.startTime) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + processId.hashCode()
        __r = __r*31 + processName.hashCode()
        __r = __r*31 + if (filename != null) filename.hashCode() else 0
        __r = __r*31 + if (startTime != null) startTime.hashCode() else 0
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ProcessInfo (")
        printer.indent {
            print("processId = "); processId.print(printer); println()
            print("processName = "); processName.print(printer); println()
            print("filename = "); filename.print(printer); println()
            print("startTime = "); startTime.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:18]
 */
class ProcessList private constructor(
    private val _items: RdList<ProcessInfo>,
    private val _selected: RdProperty<Int?>,
    private val _refresh: RdSignal<Unit>
) : RdBindableBase() {
    //companion
    
    companion object : IMarshaller<ProcessList> {
        override val _type: KClass<ProcessList> = ProcessList::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ProcessList  {
            val _id = RdId.read(buffer)
            val _items = RdList.read(ctx, buffer, ProcessInfo)
            val _selected = RdProperty.read(ctx, buffer, __IntNullableSerializer)
            val _refresh = RdSignal.read(ctx, buffer, FrameworkMarshallers.Void)
            return ProcessList(_items, _selected, _refresh).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ProcessList)  {
            value.rdid.write(buffer)
            RdList.write(ctx, buffer, value._items)
            RdProperty.write(ctx, buffer, value._selected)
            RdSignal.write(ctx, buffer, value._refresh)
        }
        
        private val __IntNullableSerializer = FrameworkMarshallers.Int.nullable()
        
    }
    //fields
    val items: IMutableViewableList<ProcessInfo> get() = _items
    val selected: IProperty<Int?> get() = _selected
    val refresh: ISignal<Unit> get() = _refresh
    //methods
    //initializer
    init {
        _items.optimizeNested = true
        _selected.optimizeNested = true
    }
    
    init {
        bindableChildren.add("items" to _items)
        bindableChildren.add("selected" to _selected)
        bindableChildren.add("refresh" to _refresh)
    }
    
    //secondary constructor
    constructor(
    ) : this(
        RdList<ProcessInfo>(ProcessInfo),
        RdProperty<Int?>(null, __IntNullableSerializer),
        RdSignal<Unit>(FrameworkMarshallers.Void)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ProcessList (")
        printer.indent {
            print("items = "); _items.print(printer); println()
            print("selected = "); _selected.print(printer); println()
            print("refresh = "); _refresh.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): ProcessList   {
        return ProcessList(
            _items.deepClonePolymorphic(),
            _selected.deepClonePolymorphic(),
            _refresh.deepClonePolymorphic()
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:99]
 */
data class RemoveCountersMonitoringSessionCommand (
    val pid: Int
) : IPrintable {
    //companion
    
    companion object : IMarshaller<RemoveCountersMonitoringSessionCommand> {
        override val _type: KClass<RemoveCountersMonitoringSessionCommand> = RemoveCountersMonitoringSessionCommand::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): RemoveCountersMonitoringSessionCommand  {
            val pid = buffer.readInt()
            return RemoveCountersMonitoringSessionCommand(pid)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: RemoveCountersMonitoringSessionCommand)  {
            buffer.writeInt(value.pid)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as RemoveCountersMonitoringSessionCommand
        
        if (pid != other.pid) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + pid.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("RemoveCountersMonitoringSessionCommand (")
        printer.indent {
            print("pid = "); pid.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:82]
 */
data class StopCountersCollectionCommand (
    val pid: Int
) : IPrintable {
    //companion
    
    companion object : IMarshaller<StopCountersCollectionCommand> {
        override val _type: KClass<StopCountersCollectionCommand> = StopCountersCollectionCommand::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): StopCountersCollectionCommand  {
            val pid = buffer.readInt()
            return StopCountersCollectionCommand(pid)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: StopCountersCollectionCommand)  {
            buffer.writeInt(value.pid)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as StopCountersCollectionCommand
        
        if (pid != other.pid) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + pid.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("StopCountersCollectionCommand (")
        printer.indent {
            print("pid = "); pid.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:96]
 */
data class StopCountersMonitoringCommand (
    val pid: Int
) : IPrintable {
    //companion
    
    companion object : IMarshaller<StopCountersMonitoringCommand> {
        override val _type: KClass<StopCountersMonitoringCommand> = StopCountersMonitoringCommand::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): StopCountersMonitoringCommand  {
            val pid = buffer.readInt()
            return StopCountersMonitoringCommand(pid)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: StopCountersMonitoringCommand)  {
            buffer.writeInt(value.pid)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    override fun equals(other: Any?): Boolean  {
        if (this === other) return true
        if (other == null || other::class != this::class) return false
        
        other as StopCountersMonitoringCommand
        
        if (pid != other.pid) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + pid.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("StopCountersMonitoringCommand (")
        printer.indent {
            print("pid = "); pid.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}
