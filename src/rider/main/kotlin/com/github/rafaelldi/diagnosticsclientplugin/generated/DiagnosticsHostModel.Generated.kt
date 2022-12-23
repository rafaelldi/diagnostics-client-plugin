@file:Suppress("EXPERIMENTAL_API_USAGE","EXPERIMENTAL_UNSIGNED_LITERALS","PackageDirectoryMismatch","UnusedImport","unused","LocalVariableName","CanBeVal","PropertyName","EnumEntryName","ClassName","ObjectPropertyName","UnnecessaryVariable","SpellCheckingInspection")
package com.github.rafaelldi.diagnosticsclientplugin.generated

import com.jetbrains.rd.framework.*
import com.jetbrains.rd.framework.base.*
import com.jetbrains.rd.framework.impl.*

import com.jetbrains.rd.util.lifetime.*
import com.jetbrains.rd.util.reactive.*
import com.jetbrains.rd.util.string.*
import com.jetbrains.rd.util.*
import kotlin.time.Duration
import kotlin.reflect.KClass
import kotlin.jvm.JvmStatic



/**
 * #### Generated from [DiagnosticsHostModel.kt:10]
 */
class DiagnosticsHostModel private constructor(
    val processList: ProcessList,
    private val _counterCollectionSessions: RdMap<Int, CounterCollectionSession>,
    private val _counterMonitoringSessions: RdMap<Int, CounterMonitoringSession>,
    private val _gcEventCollectionSessions: RdMap<Int, GcEventCollectionSession>,
    private val _gcEventMonitoringSessions: RdMap<Int, GcEventMonitoringSession>,
    private val _triggerGc: RdSignal<Int>,
    private val _traceCollectionSessions: RdMap<Int, TraceCollectionSession>,
    private val _traceMonitoringSessions: RdMap<Int, TraceMonitoringSession>,
    private val _collectDump: RdCall<CollectDumpCommand, DumpCollectionResult>,
    private val _collectStackTrace: RdCall<CollectStackTraceCommand, String>
) : RdExtBase() {
    //companion
    
    companion object : ISerializersOwner {
        
        override fun registerSerializersCore(serializers: ISerializers)  {
            serializers.register(ProcessInfo)
            serializers.register(ProcessList)
            serializers.register(CounterCollectionSession)
            serializers.register(GcEventCollectionSession)
            serializers.register(TraceCollectionSession)
            serializers.register(CounterMonitoringSession)
            serializers.register(GcEventMonitoringSession)
            serializers.register(TraceMonitoringSession)
            serializers.register(Counter)
            serializers.register(GcEvent)
            serializers.register(PredefinedProvider.marshaller)
            serializers.register(Trace)
            serializers.register(CollectDumpCommand)
            serializers.register(DumpCollectionResult)
            serializers.register(CollectStackTraceCommand)
            serializers.register(CounterFileFormat.marshaller)
            serializers.register(TracingProfile.marshaller)
            serializers.register(DumpType.marshaller)
            serializers.register(CollectionSession_Unknown)
            serializers.register(MonitoringSession_Unknown)
        }
        
        
        
        
        
        const val serializationHash = -9122463597509515190L
        
    }
    override val serializersOwner: ISerializersOwner get() = DiagnosticsHostModel
    override val serializationHash: Long get() = DiagnosticsHostModel.serializationHash
    
    //fields
    val counterCollectionSessions: IMutableViewableMap<Int, CounterCollectionSession> get() = _counterCollectionSessions
    val counterMonitoringSessions: IMutableViewableMap<Int, CounterMonitoringSession> get() = _counterMonitoringSessions
    val gcEventCollectionSessions: IMutableViewableMap<Int, GcEventCollectionSession> get() = _gcEventCollectionSessions
    val gcEventMonitoringSessions: IMutableViewableMap<Int, GcEventMonitoringSession> get() = _gcEventMonitoringSessions
    val triggerGc: ISignal<Int> get() = _triggerGc
    val traceCollectionSessions: IMutableViewableMap<Int, TraceCollectionSession> get() = _traceCollectionSessions
    val traceMonitoringSessions: IMutableViewableMap<Int, TraceMonitoringSession> get() = _traceMonitoringSessions
    val collectDump: IRdCall<CollectDumpCommand, DumpCollectionResult> get() = _collectDump
    val collectStackTrace: IRdCall<CollectStackTraceCommand, String> get() = _collectStackTrace
    //methods
    //initializer
    init {
        bindableChildren.add("processList" to processList)
        bindableChildren.add("counterCollectionSessions" to _counterCollectionSessions)
        bindableChildren.add("counterMonitoringSessions" to _counterMonitoringSessions)
        bindableChildren.add("gcEventCollectionSessions" to _gcEventCollectionSessions)
        bindableChildren.add("gcEventMonitoringSessions" to _gcEventMonitoringSessions)
        bindableChildren.add("triggerGc" to _triggerGc)
        bindableChildren.add("traceCollectionSessions" to _traceCollectionSessions)
        bindableChildren.add("traceMonitoringSessions" to _traceMonitoringSessions)
        bindableChildren.add("collectDump" to _collectDump)
        bindableChildren.add("collectStackTrace" to _collectStackTrace)
    }
    
    //secondary constructor
    internal constructor(
    ) : this(
        ProcessList(),
        RdMap<Int, CounterCollectionSession>(FrameworkMarshallers.Int, CounterCollectionSession),
        RdMap<Int, CounterMonitoringSession>(FrameworkMarshallers.Int, CounterMonitoringSession),
        RdMap<Int, GcEventCollectionSession>(FrameworkMarshallers.Int, GcEventCollectionSession),
        RdMap<Int, GcEventMonitoringSession>(FrameworkMarshallers.Int, GcEventMonitoringSession),
        RdSignal<Int>(FrameworkMarshallers.Int),
        RdMap<Int, TraceCollectionSession>(FrameworkMarshallers.Int, TraceCollectionSession),
        RdMap<Int, TraceMonitoringSession>(FrameworkMarshallers.Int, TraceMonitoringSession),
        RdCall<CollectDumpCommand, DumpCollectionResult>(CollectDumpCommand, DumpCollectionResult),
        RdCall<CollectStackTraceCommand, String>(CollectStackTraceCommand, FrameworkMarshallers.String)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("DiagnosticsHostModel (")
        printer.indent {
            print("processList = "); processList.print(printer); println()
            print("counterCollectionSessions = "); _counterCollectionSessions.print(printer); println()
            print("counterMonitoringSessions = "); _counterMonitoringSessions.print(printer); println()
            print("gcEventCollectionSessions = "); _gcEventCollectionSessions.print(printer); println()
            print("gcEventMonitoringSessions = "); _gcEventMonitoringSessions.print(printer); println()
            print("triggerGc = "); _triggerGc.print(printer); println()
            print("traceCollectionSessions = "); _traceCollectionSessions.print(printer); println()
            print("traceMonitoringSessions = "); _traceMonitoringSessions.print(printer); println()
            print("collectDump = "); _collectDump.print(printer); println()
            print("collectStackTrace = "); _collectStackTrace.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): DiagnosticsHostModel   {
        return DiagnosticsHostModel(
            processList.deepClonePolymorphic(),
            _counterCollectionSessions.deepClonePolymorphic(),
            _counterMonitoringSessions.deepClonePolymorphic(),
            _gcEventCollectionSessions.deepClonePolymorphic(),
            _gcEventMonitoringSessions.deepClonePolymorphic(),
            _triggerGc.deepClonePolymorphic(),
            _traceCollectionSessions.deepClonePolymorphic(),
            _traceMonitoringSessions.deepClonePolymorphic(),
            _collectDump.deepClonePolymorphic(),
            _collectStackTrace.deepClonePolymorphic()
        )
    }
    //contexts
}
val com.jetbrains.rd.ide.model.Solution.diagnosticsHostModel get() = getOrCreateExtension("diagnosticsHostModel", ::DiagnosticsHostModel)



/**
 * #### Generated from [DiagnosticsHostModel.kt:138]
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
 * #### Generated from [DiagnosticsHostModel.kt:155]
 */
data class CollectStackTraceCommand (
    val pid: Int
) : IPrintable {
    //companion
    
    companion object : IMarshaller<CollectStackTraceCommand> {
        override val _type: KClass<CollectStackTraceCommand> = CollectStackTraceCommand::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): CollectStackTraceCommand  {
            val pid = buffer.readInt()
            return CollectStackTraceCommand(pid)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: CollectStackTraceCommand)  {
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
        
        other as CollectStackTraceCommand
        
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
        printer.println("CollectStackTraceCommand (")
        printer.indent {
            print("pid = "); pid.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:25]
 */
abstract class CollectionSession (
    val duration: Int?,
    val filePath: String
) : RdBindableBase() {
    //companion
    
    companion object : IAbstractDeclaration<CollectionSession> {
        override fun readUnknownInstance(ctx: SerializationCtx, buffer: AbstractBuffer, unknownId: RdId, size: Int): CollectionSession  {
            val objectStartPosition = buffer.position
            val _id = RdId.read(buffer)
            val duration = buffer.readNullable { buffer.readInt() }
            val filePath = buffer.readString()
            val unknownBytes = ByteArray(objectStartPosition + size - buffer.position)
            buffer.readByteArrayRaw(unknownBytes)
            return CollectionSession_Unknown(duration, filePath, unknownId, unknownBytes).withId(_id)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    //hash code trait
    //pretty print
    //deepClone
    //contexts
}


class CollectionSession_Unknown (
    duration: Int?,
    filePath: String,
    override val unknownId: RdId,
    val unknownBytes: ByteArray
) : CollectionSession (
    duration,
    filePath
), IUnknownInstance {
    //companion
    
    companion object : IMarshaller<CollectionSession_Unknown> {
        override val _type: KClass<CollectionSession_Unknown> = CollectionSession_Unknown::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): CollectionSession_Unknown  {
            throw NotImplementedError("Unknown instances should not be read via serializer")
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: CollectionSession_Unknown)  {
            value.rdid.write(buffer)
            buffer.writeNullable(value.duration) { buffer.writeInt(it) }
            buffer.writeString(value.filePath)
            buffer.writeByteArrayRaw(value.unknownBytes)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("CollectionSession_Unknown (")
        printer.indent {
            print("duration = "); duration.print(printer); println()
            print("filePath = "); filePath.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): CollectionSession_Unknown   {
        return CollectionSession_Unknown(
            duration,
            filePath,
            unknownId,
            unknownBytes
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:79]
 */
data class Counter (
    val name: String,
    val tags: String?,
    val value: Double
) : IPrintable {
    //companion
    
    companion object : IMarshaller<Counter> {
        override val _type: KClass<Counter> = Counter::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): Counter  {
            val name = buffer.readString()
            val tags = buffer.readNullable { buffer.readString() }
            val value = buffer.readDouble()
            return Counter(name, tags, value)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: Counter)  {
            buffer.writeString(value.name)
            buffer.writeNullable(value.tags) { buffer.writeString(it) }
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
        if (tags != other.tags) return false
        if (value != other.value) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + name.hashCode()
        __r = __r*31 + if (tags != null) tags.hashCode() else 0
        __r = __r*31 + value.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("Counter (")
        printer.indent {
            print("name = "); name.print(printer); println()
            print("tags = "); tags.print(printer); println()
            print("value = "); value.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:30]
 */
class CounterCollectionSession (
    val format: CounterFileFormat,
    val refreshInterval: Int,
    val providers: String,
    val metrics: String?,
    val maxTimeSeries: Int,
    val maxHistograms: Int,
    duration: Int?,
    filePath: String
) : CollectionSession (
    duration,
    filePath
) {
    //companion
    
    companion object : IMarshaller<CounterCollectionSession> {
        override val _type: KClass<CounterCollectionSession> = CounterCollectionSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): CounterCollectionSession  {
            val _id = RdId.read(buffer)
            val duration = buffer.readNullable { buffer.readInt() }
            val filePath = buffer.readString()
            val format = buffer.readEnum<CounterFileFormat>()
            val refreshInterval = buffer.readInt()
            val providers = buffer.readString()
            val metrics = buffer.readNullable { buffer.readString() }
            val maxTimeSeries = buffer.readInt()
            val maxHistograms = buffer.readInt()
            return CounterCollectionSession(format, refreshInterval, providers, metrics, maxTimeSeries, maxHistograms, duration, filePath).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: CounterCollectionSession)  {
            value.rdid.write(buffer)
            buffer.writeNullable(value.duration) { buffer.writeInt(it) }
            buffer.writeString(value.filePath)
            buffer.writeEnum(value.format)
            buffer.writeInt(value.refreshInterval)
            buffer.writeString(value.providers)
            buffer.writeNullable(value.metrics) { buffer.writeString(it) }
            buffer.writeInt(value.maxTimeSeries)
            buffer.writeInt(value.maxHistograms)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("CounterCollectionSession (")
        printer.indent {
            print("format = "); format.print(printer); println()
            print("refreshInterval = "); refreshInterval.print(printer); println()
            print("providers = "); providers.print(printer); println()
            print("metrics = "); metrics.print(printer); println()
            print("maxTimeSeries = "); maxTimeSeries.print(printer); println()
            print("maxHistograms = "); maxHistograms.print(printer); println()
            print("duration = "); duration.print(printer); println()
            print("filePath = "); filePath.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): CounterCollectionSession   {
        return CounterCollectionSession(
            format,
            refreshInterval,
            providers,
            metrics,
            maxTimeSeries,
            maxHistograms,
            duration,
            filePath
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:31]
 */
enum class CounterFileFormat {
    Csv, 
    Json;
    
    companion object {
        val marshaller = FrameworkMarshallers.enum<CounterFileFormat>()
        
    }
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:61]
 */
class CounterMonitoringSession private constructor(
    private val _counters: RdMap<String, Counter>,
    val refreshInterval: Int,
    val providers: String,
    val metrics: String?,
    val maxTimeSeries: Int,
    val maxHistograms: Int,
    _active: RdOptionalProperty<Boolean>,
    _duration: RdProperty<Int?>
) : MonitoringSession (
    _active,
    _duration
) {
    //companion
    
    companion object : IMarshaller<CounterMonitoringSession> {
        override val _type: KClass<CounterMonitoringSession> = CounterMonitoringSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): CounterMonitoringSession  {
            val _id = RdId.read(buffer)
            val _active = RdOptionalProperty.read(ctx, buffer, FrameworkMarshallers.Bool)
            val _duration = RdProperty.read(ctx, buffer, __IntNullableSerializer)
            val _counters = RdMap.read(ctx, buffer, FrameworkMarshallers.String, Counter)
            val refreshInterval = buffer.readInt()
            val providers = buffer.readString()
            val metrics = buffer.readNullable { buffer.readString() }
            val maxTimeSeries = buffer.readInt()
            val maxHistograms = buffer.readInt()
            return CounterMonitoringSession(_counters, refreshInterval, providers, metrics, maxTimeSeries, maxHistograms, _active, _duration).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: CounterMonitoringSession)  {
            value.rdid.write(buffer)
            RdOptionalProperty.write(ctx, buffer, value._active)
            RdProperty.write(ctx, buffer, value._duration)
            RdMap.write(ctx, buffer, value._counters)
            buffer.writeInt(value.refreshInterval)
            buffer.writeString(value.providers)
            buffer.writeNullable(value.metrics) { buffer.writeString(it) }
            buffer.writeInt(value.maxTimeSeries)
            buffer.writeInt(value.maxHistograms)
        }
        
        private val __IntNullableSerializer = FrameworkMarshallers.Int.nullable()
        
    }
    //fields
    val counters: IMutableViewableMap<String, Counter> get() = _counters
    //methods
    //initializer
    init {
        _counters.optimizeNested = true
    }
    
    init {
        _counters.async = true
    }
    
    init {
        bindableChildren.add("counters" to _counters)
    }
    
    //secondary constructor
    constructor(
        refreshInterval: Int,
        providers: String,
        metrics: String?,
        maxTimeSeries: Int,
        maxHistograms: Int
    ) : this(
        RdMap<String, Counter>(FrameworkMarshallers.String, Counter),
        refreshInterval,
        providers,
        metrics,
        maxTimeSeries,
        maxHistograms,
        RdOptionalProperty<Boolean>(FrameworkMarshallers.Bool),
        RdProperty<Int?>(null, __IntNullableSerializer)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("CounterMonitoringSession (")
        printer.indent {
            print("counters = "); _counters.print(printer); println()
            print("refreshInterval = "); refreshInterval.print(printer); println()
            print("providers = "); providers.print(printer); println()
            print("metrics = "); metrics.print(printer); println()
            print("maxTimeSeries = "); maxTimeSeries.print(printer); println()
            print("maxHistograms = "); maxHistograms.print(printer); println()
            print("active = "); _active.print(printer); println()
            print("duration = "); _duration.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): CounterMonitoringSession   {
        return CounterMonitoringSession(
            _counters.deepClonePolymorphic(),
            refreshInterval,
            providers,
            metrics,
            maxTimeSeries,
            maxHistograms,
            _active.deepClonePolymorphic(),
            _duration.deepClonePolymorphic()
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:149]
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
 * #### Generated from [DiagnosticsHostModel.kt:140]
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
 * #### Generated from [DiagnosticsHostModel.kt:85]
 */
data class GcEvent (
    val number: Int,
    val generation: String,
    val reason: String,
    val pauseDuration: Double,
    val peak: Double,
    val after: Double,
    val ratio: Double,
    val promoted: Double,
    val allocated: Double,
    val allocationRate: Double,
    val sizeGen0: Double,
    val sizeGen1: Double,
    val sizeGen2: Double,
    val sizeLoh: Double,
    val pinnedObjects: Int
) : IPrintable {
    //companion
    
    companion object : IMarshaller<GcEvent> {
        override val _type: KClass<GcEvent> = GcEvent::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): GcEvent  {
            val number = buffer.readInt()
            val generation = buffer.readString()
            val reason = buffer.readString()
            val pauseDuration = buffer.readDouble()
            val peak = buffer.readDouble()
            val after = buffer.readDouble()
            val ratio = buffer.readDouble()
            val promoted = buffer.readDouble()
            val allocated = buffer.readDouble()
            val allocationRate = buffer.readDouble()
            val sizeGen0 = buffer.readDouble()
            val sizeGen1 = buffer.readDouble()
            val sizeGen2 = buffer.readDouble()
            val sizeLoh = buffer.readDouble()
            val pinnedObjects = buffer.readInt()
            return GcEvent(number, generation, reason, pauseDuration, peak, after, ratio, promoted, allocated, allocationRate, sizeGen0, sizeGen1, sizeGen2, sizeLoh, pinnedObjects)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: GcEvent)  {
            buffer.writeInt(value.number)
            buffer.writeString(value.generation)
            buffer.writeString(value.reason)
            buffer.writeDouble(value.pauseDuration)
            buffer.writeDouble(value.peak)
            buffer.writeDouble(value.after)
            buffer.writeDouble(value.ratio)
            buffer.writeDouble(value.promoted)
            buffer.writeDouble(value.allocated)
            buffer.writeDouble(value.allocationRate)
            buffer.writeDouble(value.sizeGen0)
            buffer.writeDouble(value.sizeGen1)
            buffer.writeDouble(value.sizeGen2)
            buffer.writeDouble(value.sizeLoh)
            buffer.writeInt(value.pinnedObjects)
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
        
        other as GcEvent
        
        if (number != other.number) return false
        if (generation != other.generation) return false
        if (reason != other.reason) return false
        if (pauseDuration != other.pauseDuration) return false
        if (peak != other.peak) return false
        if (after != other.after) return false
        if (ratio != other.ratio) return false
        if (promoted != other.promoted) return false
        if (allocated != other.allocated) return false
        if (allocationRate != other.allocationRate) return false
        if (sizeGen0 != other.sizeGen0) return false
        if (sizeGen1 != other.sizeGen1) return false
        if (sizeGen2 != other.sizeGen2) return false
        if (sizeLoh != other.sizeLoh) return false
        if (pinnedObjects != other.pinnedObjects) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + number.hashCode()
        __r = __r*31 + generation.hashCode()
        __r = __r*31 + reason.hashCode()
        __r = __r*31 + pauseDuration.hashCode()
        __r = __r*31 + peak.hashCode()
        __r = __r*31 + after.hashCode()
        __r = __r*31 + ratio.hashCode()
        __r = __r*31 + promoted.hashCode()
        __r = __r*31 + allocated.hashCode()
        __r = __r*31 + allocationRate.hashCode()
        __r = __r*31 + sizeGen0.hashCode()
        __r = __r*31 + sizeGen1.hashCode()
        __r = __r*31 + sizeGen2.hashCode()
        __r = __r*31 + sizeLoh.hashCode()
        __r = __r*31 + pinnedObjects.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("GcEvent (")
        printer.indent {
            print("number = "); number.print(printer); println()
            print("generation = "); generation.print(printer); println()
            print("reason = "); reason.print(printer); println()
            print("pauseDuration = "); pauseDuration.print(printer); println()
            print("peak = "); peak.print(printer); println()
            print("after = "); after.print(printer); println()
            print("ratio = "); ratio.print(printer); println()
            print("promoted = "); promoted.print(printer); println()
            print("allocated = "); allocated.print(printer); println()
            print("allocationRate = "); allocationRate.print(printer); println()
            print("sizeGen0 = "); sizeGen0.print(printer); println()
            print("sizeGen1 = "); sizeGen1.print(printer); println()
            print("sizeGen2 = "); sizeGen2.print(printer); println()
            print("sizeLoh = "); sizeLoh.print(printer); println()
            print("pinnedObjects = "); pinnedObjects.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:42]
 */
class GcEventCollectionSession (
    duration: Int?,
    filePath: String
) : CollectionSession (
    duration,
    filePath
) {
    //companion
    
    companion object : IMarshaller<GcEventCollectionSession> {
        override val _type: KClass<GcEventCollectionSession> = GcEventCollectionSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): GcEventCollectionSession  {
            val _id = RdId.read(buffer)
            val duration = buffer.readNullable { buffer.readInt() }
            val filePath = buffer.readString()
            return GcEventCollectionSession(duration, filePath).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: GcEventCollectionSession)  {
            value.rdid.write(buffer)
            buffer.writeNullable(value.duration) { buffer.writeInt(it) }
            buffer.writeString(value.filePath)
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("GcEventCollectionSession (")
        printer.indent {
            print("duration = "); duration.print(printer); println()
            print("filePath = "); filePath.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): GcEventCollectionSession   {
        return GcEventCollectionSession(
            duration,
            filePath
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:70]
 */
class GcEventMonitoringSession private constructor(
    private val _gcHappened: RdSignal<GcEvent>,
    _active: RdOptionalProperty<Boolean>,
    _duration: RdProperty<Int?>
) : MonitoringSession (
    _active,
    _duration
) {
    //companion
    
    companion object : IMarshaller<GcEventMonitoringSession> {
        override val _type: KClass<GcEventMonitoringSession> = GcEventMonitoringSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): GcEventMonitoringSession  {
            val _id = RdId.read(buffer)
            val _active = RdOptionalProperty.read(ctx, buffer, FrameworkMarshallers.Bool)
            val _duration = RdProperty.read(ctx, buffer, __IntNullableSerializer)
            val _gcHappened = RdSignal.read(ctx, buffer, GcEvent)
            return GcEventMonitoringSession(_gcHappened, _active, _duration).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: GcEventMonitoringSession)  {
            value.rdid.write(buffer)
            RdOptionalProperty.write(ctx, buffer, value._active)
            RdProperty.write(ctx, buffer, value._duration)
            RdSignal.write(ctx, buffer, value._gcHappened)
        }
        
        private val __IntNullableSerializer = FrameworkMarshallers.Int.nullable()
        
    }
    //fields
    val gcHappened: IAsyncSignal<GcEvent> get() = _gcHappened
    //methods
    //initializer
    init {
        _gcHappened.async = true
    }
    
    init {
        bindableChildren.add("gcHappened" to _gcHappened)
    }
    
    //secondary constructor
    constructor(
    ) : this(
        RdSignal<GcEvent>(GcEvent),
        RdOptionalProperty<Boolean>(FrameworkMarshallers.Bool),
        RdProperty<Int?>(null, __IntNullableSerializer)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("GcEventMonitoringSession (")
        printer.indent {
            print("gcHappened = "); _gcHappened.print(printer); println()
            print("active = "); _active.print(printer); println()
            print("duration = "); _duration.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): GcEventMonitoringSession   {
        return GcEventMonitoringSession(
            _gcHappened.deepClonePolymorphic(),
            _active.deepClonePolymorphic(),
            _duration.deepClonePolymorphic()
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:56]
 */
abstract class MonitoringSession (
    protected val _active: RdOptionalProperty<Boolean>,
    protected val _duration: RdProperty<Int?>
) : RdBindableBase() {
    //companion
    
    companion object : IAbstractDeclaration<MonitoringSession> {
        override fun readUnknownInstance(ctx: SerializationCtx, buffer: AbstractBuffer, unknownId: RdId, size: Int): MonitoringSession  {
            val objectStartPosition = buffer.position
            val _id = RdId.read(buffer)
            val _active = RdOptionalProperty.read(ctx, buffer, FrameworkMarshallers.Bool)
            val _duration = RdProperty.read(ctx, buffer, __IntNullableSerializer)
            val unknownBytes = ByteArray(objectStartPosition + size - buffer.position)
            buffer.readByteArrayRaw(unknownBytes)
            return MonitoringSession_Unknown(_active, _duration, unknownId, unknownBytes).withId(_id)
        }
        
        private val __IntNullableSerializer = FrameworkMarshallers.Int.nullable()
        
    }
    //fields
    val active: IOptProperty<Boolean> get() = _active
    val duration: IProperty<Int?> get() = _duration
    //methods
    //initializer
    init {
        _active.optimizeNested = true
        _duration.optimizeNested = true
    }
    
    init {
        bindableChildren.add("active" to _active)
        bindableChildren.add("duration" to _duration)
    }
    
    //secondary constructor
    //equals trait
    //hash code trait
    //pretty print
    //deepClone
    //contexts
}


class MonitoringSession_Unknown (
    _active: RdOptionalProperty<Boolean>,
    _duration: RdProperty<Int?>,
    override val unknownId: RdId,
    val unknownBytes: ByteArray
) : MonitoringSession (
    _active,
    _duration
), IUnknownInstance {
    //companion
    
    companion object : IMarshaller<MonitoringSession_Unknown> {
        override val _type: KClass<MonitoringSession_Unknown> = MonitoringSession_Unknown::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): MonitoringSession_Unknown  {
            throw NotImplementedError("Unknown instances should not be read via serializer")
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: MonitoringSession_Unknown)  {
            value.rdid.write(buffer)
            RdOptionalProperty.write(ctx, buffer, value._active)
            RdProperty.write(ctx, buffer, value._duration)
            buffer.writeByteArrayRaw(value.unknownBytes)
        }
        
        private val __IntNullableSerializer = FrameworkMarshallers.Int.nullable()
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    constructor(
        unknownId: RdId,
        unknownBytes: ByteArray
    ) : this(
        RdOptionalProperty<Boolean>(FrameworkMarshallers.Bool),
        RdProperty<Int?>(null, __IntNullableSerializer),
        unknownId,
        unknownBytes
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("MonitoringSession_Unknown (")
        printer.indent {
            print("active = "); _active.print(printer); println()
            print("duration = "); _duration.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): MonitoringSession_Unknown   {
        return MonitoringSession_Unknown(
            _active.deepClonePolymorphic(),
            _duration.deepClonePolymorphic(),
            unknownId,
            unknownBytes
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:103]
 */
enum class PredefinedProvider {
    Http, 
    AspNet, 
    EF, 
    Exceptions, 
    Threads, 
    Contentions, 
    Tasks, 
    Loader;
    
    companion object {
        val marshaller = FrameworkMarshallers.enum<PredefinedProvider>()
        
    }
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:11]
 */
data class ProcessInfo (
    val processName: String,
    val filename: String?,
    val startTime: String?,
    val commandLine: String?,
    val operatingSystem: String?,
    val processArchitecture: String?
) : IPrintable {
    //companion
    
    companion object : IMarshaller<ProcessInfo> {
        override val _type: KClass<ProcessInfo> = ProcessInfo::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ProcessInfo  {
            val processName = buffer.readString()
            val filename = buffer.readNullable { buffer.readString() }
            val startTime = buffer.readNullable { buffer.readString() }
            val commandLine = buffer.readNullable { buffer.readString() }
            val operatingSystem = buffer.readNullable { buffer.readString() }
            val processArchitecture = buffer.readNullable { buffer.readString() }
            return ProcessInfo(processName, filename, startTime, commandLine, operatingSystem, processArchitecture)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ProcessInfo)  {
            buffer.writeString(value.processName)
            buffer.writeNullable(value.filename) { buffer.writeString(it) }
            buffer.writeNullable(value.startTime) { buffer.writeString(it) }
            buffer.writeNullable(value.commandLine) { buffer.writeString(it) }
            buffer.writeNullable(value.operatingSystem) { buffer.writeString(it) }
            buffer.writeNullable(value.processArchitecture) { buffer.writeString(it) }
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
        
        if (processName != other.processName) return false
        if (filename != other.filename) return false
        if (startTime != other.startTime) return false
        if (commandLine != other.commandLine) return false
        if (operatingSystem != other.operatingSystem) return false
        if (processArchitecture != other.processArchitecture) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + processName.hashCode()
        __r = __r*31 + if (filename != null) filename.hashCode() else 0
        __r = __r*31 + if (startTime != null) startTime.hashCode() else 0
        __r = __r*31 + if (commandLine != null) commandLine.hashCode() else 0
        __r = __r*31 + if (operatingSystem != null) operatingSystem.hashCode() else 0
        __r = __r*31 + if (processArchitecture != null) processArchitecture.hashCode() else 0
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ProcessInfo (")
        printer.indent {
            print("processName = "); processName.print(printer); println()
            print("filename = "); filename.print(printer); println()
            print("startTime = "); startTime.print(printer); println()
            print("commandLine = "); commandLine.print(printer); println()
            print("operatingSystem = "); operatingSystem.print(printer); println()
            print("processArchitecture = "); processArchitecture.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:20]
 */
class ProcessList private constructor(
    private val _active: RdOptionalProperty<Boolean>,
    private val _items: RdMap<Int, ProcessInfo>
) : RdBindableBase() {
    //companion
    
    companion object : IMarshaller<ProcessList> {
        override val _type: KClass<ProcessList> = ProcessList::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ProcessList  {
            val _id = RdId.read(buffer)
            val _active = RdOptionalProperty.read(ctx, buffer, FrameworkMarshallers.Bool)
            val _items = RdMap.read(ctx, buffer, FrameworkMarshallers.Int, ProcessInfo)
            return ProcessList(_active, _items).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ProcessList)  {
            value.rdid.write(buffer)
            RdOptionalProperty.write(ctx, buffer, value._active)
            RdMap.write(ctx, buffer, value._items)
        }
        
        
    }
    //fields
    val active: IOptProperty<Boolean> get() = _active
    val items: IMutableViewableMap<Int, ProcessInfo> get() = _items
    //methods
    //initializer
    init {
        _active.optimizeNested = true
        _items.optimizeNested = true
    }
    
    init {
        _items.async = true
    }
    
    init {
        bindableChildren.add("active" to _active)
        bindableChildren.add("items" to _items)
    }
    
    //secondary constructor
    constructor(
    ) : this(
        RdOptionalProperty<Boolean>(FrameworkMarshallers.Bool),
        RdMap<Int, ProcessInfo>(FrameworkMarshallers.Int, ProcessInfo)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ProcessList (")
        printer.indent {
            print("active = "); _active.print(printer); println()
            print("items = "); _items.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): ProcessList   {
        return ProcessList(
            _active.deepClonePolymorphic(),
            _items.deepClonePolymorphic()
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:114]
 */
data class Trace (
    val eventName: String,
    val provider: PredefinedProvider,
    val timeStamp: Date,
    val content: String
) : IPrintable {
    //companion
    
    companion object : IMarshaller<Trace> {
        override val _type: KClass<Trace> = Trace::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): Trace  {
            val eventName = buffer.readString()
            val provider = buffer.readEnum<PredefinedProvider>()
            val timeStamp = buffer.readDateTime()
            val content = buffer.readString()
            return Trace(eventName, provider, timeStamp, content)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: Trace)  {
            buffer.writeString(value.eventName)
            buffer.writeEnum(value.provider)
            buffer.writeDateTime(value.timeStamp)
            buffer.writeString(value.content)
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
        
        other as Trace
        
        if (eventName != other.eventName) return false
        if (provider != other.provider) return false
        if (timeStamp != other.timeStamp) return false
        if (content != other.content) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + eventName.hashCode()
        __r = __r*31 + provider.hashCode()
        __r = __r*31 + timeStamp.hashCode()
        __r = __r*31 + content.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("Trace (")
        printer.indent {
            print("eventName = "); eventName.print(printer); println()
            print("provider = "); provider.print(printer); println()
            print("timeStamp = "); timeStamp.print(printer); println()
            print("content = "); content.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:45]
 */
class TraceCollectionSession (
    val profile: TracingProfile,
    val providers: String,
    val predefinedProviders: List<PredefinedProvider>,
    duration: Int?,
    filePath: String
) : CollectionSession (
    duration,
    filePath
) {
    //companion
    
    companion object : IMarshaller<TraceCollectionSession> {
        override val _type: KClass<TraceCollectionSession> = TraceCollectionSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): TraceCollectionSession  {
            val _id = RdId.read(buffer)
            val duration = buffer.readNullable { buffer.readInt() }
            val filePath = buffer.readString()
            val profile = buffer.readEnum<TracingProfile>()
            val providers = buffer.readString()
            val predefinedProviders = buffer.readList { buffer.readEnum<PredefinedProvider>() }
            return TraceCollectionSession(profile, providers, predefinedProviders, duration, filePath).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: TraceCollectionSession)  {
            value.rdid.write(buffer)
            buffer.writeNullable(value.duration) { buffer.writeInt(it) }
            buffer.writeString(value.filePath)
            buffer.writeEnum(value.profile)
            buffer.writeString(value.providers)
            buffer.writeList(value.predefinedProviders) { v -> buffer.writeEnum(v) }
        }
        
        
    }
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("TraceCollectionSession (")
        printer.indent {
            print("profile = "); profile.print(printer); println()
            print("providers = "); providers.print(printer); println()
            print("predefinedProviders = "); predefinedProviders.print(printer); println()
            print("duration = "); duration.print(printer); println()
            print("filePath = "); filePath.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): TraceCollectionSession   {
        return TraceCollectionSession(
            profile,
            providers,
            predefinedProviders,
            duration,
            filePath
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:74]
 */
class TraceMonitoringSession private constructor(
    private val _traceReceived: RdSignal<Trace>,
    val predefinedProviders: List<PredefinedProvider>,
    _active: RdOptionalProperty<Boolean>,
    _duration: RdProperty<Int?>
) : MonitoringSession (
    _active,
    _duration
) {
    //companion
    
    companion object : IMarshaller<TraceMonitoringSession> {
        override val _type: KClass<TraceMonitoringSession> = TraceMonitoringSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): TraceMonitoringSession  {
            val _id = RdId.read(buffer)
            val _active = RdOptionalProperty.read(ctx, buffer, FrameworkMarshallers.Bool)
            val _duration = RdProperty.read(ctx, buffer, __IntNullableSerializer)
            val _traceReceived = RdSignal.read(ctx, buffer, Trace)
            val predefinedProviders = buffer.readList { buffer.readEnum<PredefinedProvider>() }
            return TraceMonitoringSession(_traceReceived, predefinedProviders, _active, _duration).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: TraceMonitoringSession)  {
            value.rdid.write(buffer)
            RdOptionalProperty.write(ctx, buffer, value._active)
            RdProperty.write(ctx, buffer, value._duration)
            RdSignal.write(ctx, buffer, value._traceReceived)
            buffer.writeList(value.predefinedProviders) { v -> buffer.writeEnum(v) }
        }
        
        private val __IntNullableSerializer = FrameworkMarshallers.Int.nullable()
        
    }
    //fields
    val traceReceived: IAsyncSignal<Trace> get() = _traceReceived
    //methods
    //initializer
    init {
        _traceReceived.async = true
    }
    
    init {
        bindableChildren.add("traceReceived" to _traceReceived)
    }
    
    //secondary constructor
    constructor(
        predefinedProviders: List<PredefinedProvider>
    ) : this(
        RdSignal<Trace>(Trace),
        predefinedProviders,
        RdOptionalProperty<Boolean>(FrameworkMarshallers.Bool),
        RdProperty<Int?>(null, __IntNullableSerializer)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("TraceMonitoringSession (")
        printer.indent {
            print("traceReceived = "); _traceReceived.print(printer); println()
            print("predefinedProviders = "); predefinedProviders.print(printer); println()
            print("active = "); _active.print(printer); println()
            print("duration = "); _duration.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): TraceMonitoringSession   {
        return TraceMonitoringSession(
            _traceReceived.deepClonePolymorphic(),
            predefinedProviders,
            _active.deepClonePolymorphic(),
            _duration.deepClonePolymorphic()
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:46]
 */
enum class TracingProfile {
    None, 
    CpuSampling, 
    GcVerbose, 
    GcCollect;
    
    companion object {
        val marshaller = FrameworkMarshallers.enum<TracingProfile>()
        
    }
}
