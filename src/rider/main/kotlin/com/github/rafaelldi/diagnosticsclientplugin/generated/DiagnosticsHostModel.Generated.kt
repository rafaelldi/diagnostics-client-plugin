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
    private val _counterCollectionSessions: RdMap<Int, CounterCollectionSession>,
    private val _counterMonitoringSessions: RdMap<Int, CounterMonitoringSession>,
    private val _gcEventCollectionSessions: RdMap<Int, GcEventCollectionSession>,
    private val _gcEventMonitoringSessions: RdMap<Int, GcEventMonitoringSession>,
    private val _triggerGc: RdSignal<Int>,
    private val _traceCollectionSessions: RdList<Int>,
    private val _collectDump: RdCall<CollectDumpCommand, DumpCollectionResult>,
    private val _collectTraces: RdCall<CollectTracesCommand, Unit>,
    private val _collectStackTrace: RdCall<CollectStackTraceCommand, String>
) : RdExtBase() {
    //companion
    
    companion object : ISerializersOwner {
        
        override fun registerSerializersCore(serializers: ISerializers)  {
            serializers.register(ProcessInfo)
            serializers.register(ProcessList)
            serializers.register(CounterCollectionSession)
            serializers.register(CounterMonitoringSession)
            serializers.register(Counter)
            serializers.register(GcEventCollectionSession)
            serializers.register(GcEventMonitoringSession)
            serializers.register(GcEvent)
            serializers.register(CollectDumpCommand)
            serializers.register(DumpCollectionResult)
            serializers.register(CollectTracesCommand)
            serializers.register(CollectStackTraceCommand)
            serializers.register(CounterFileFormat.marshaller)
            serializers.register(DumpType.marshaller)
            serializers.register(TracingProfile.marshaller)
        }
        
        
        
        
        
        const val serializationHash = -7734284393143760592L
        
    }
    override val serializersOwner: ISerializersOwner get() = DiagnosticsHostModel
    override val serializationHash: Long get() = DiagnosticsHostModel.serializationHash
    
    //fields
    val counterCollectionSessions: IMutableViewableMap<Int, CounterCollectionSession> get() = _counterCollectionSessions
    val counterMonitoringSessions: IMutableViewableMap<Int, CounterMonitoringSession> get() = _counterMonitoringSessions
    val gcEventCollectionSessions: IMutableViewableMap<Int, GcEventCollectionSession> get() = _gcEventCollectionSessions
    val gcEventMonitoringSessions: IMutableViewableMap<Int, GcEventMonitoringSession> get() = _gcEventMonitoringSessions
    val triggerGc: ISignal<Int> get() = _triggerGc
    val traceCollectionSessions: IMutableViewableList<Int> get() = _traceCollectionSessions
    val collectDump: IRdCall<CollectDumpCommand, DumpCollectionResult> get() = _collectDump
    val collectTraces: IRdCall<CollectTracesCommand, Unit> get() = _collectTraces
    val collectStackTrace: IRdCall<CollectStackTraceCommand, String> get() = _collectStackTrace
    //methods
    //initializer
    init {
        _traceCollectionSessions.optimizeNested = true
    }
    
    init {
        _traceCollectionSessions.async = true
    }
    
    init {
        bindableChildren.add("processList" to processList)
        bindableChildren.add("counterCollectionSessions" to _counterCollectionSessions)
        bindableChildren.add("counterMonitoringSessions" to _counterMonitoringSessions)
        bindableChildren.add("gcEventCollectionSessions" to _gcEventCollectionSessions)
        bindableChildren.add("gcEventMonitoringSessions" to _gcEventMonitoringSessions)
        bindableChildren.add("triggerGc" to _triggerGc)
        bindableChildren.add("traceCollectionSessions" to _traceCollectionSessions)
        bindableChildren.add("collectDump" to _collectDump)
        bindableChildren.add("collectTraces" to _collectTraces)
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
        RdList<Int>(FrameworkMarshallers.Int),
        RdCall<CollectDumpCommand, DumpCollectionResult>(CollectDumpCommand, DumpCollectionResult),
        RdCall<CollectTracesCommand, Unit>(CollectTracesCommand, FrameworkMarshallers.Void),
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
            print("collectDump = "); _collectDump.print(printer); println()
            print("collectTraces = "); _collectTraces.print(printer); println()
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
            _collectDump.deepClonePolymorphic(),
            _collectTraces.deepClonePolymorphic(),
            _collectStackTrace.deepClonePolymorphic()
        )
    }
    //contexts
}
val com.jetbrains.rd.ide.model.Solution.diagnosticsHostModel get() = getOrCreateExtension("diagnosticsHostModel", ::DiagnosticsHostModel)



/**
 * #### Generated from [DiagnosticsHostModel.kt:103]
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
 * #### Generated from [DiagnosticsHostModel.kt:137]
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
 * #### Generated from [DiagnosticsHostModel.kt:120]
 */
data class CollectTracesCommand (
    val pid: Int,
    val filePath: String,
    val profile: TracingProfile,
    val providers: String,
    val duration: Int?
) : IPrintable {
    //companion
    
    companion object : IMarshaller<CollectTracesCommand> {
        override val _type: KClass<CollectTracesCommand> = CollectTracesCommand::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): CollectTracesCommand  {
            val pid = buffer.readInt()
            val filePath = buffer.readString()
            val profile = buffer.readEnum<TracingProfile>()
            val providers = buffer.readString()
            val duration = buffer.readNullable { buffer.readInt() }
            return CollectTracesCommand(pid, filePath, profile, providers, duration)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: CollectTracesCommand)  {
            buffer.writeInt(value.pid)
            buffer.writeString(value.filePath)
            buffer.writeEnum(value.profile)
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
        
        other as CollectTracesCommand
        
        if (pid != other.pid) return false
        if (filePath != other.filePath) return false
        if (profile != other.profile) return false
        if (providers != other.providers) return false
        if (duration != other.duration) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + pid.hashCode()
        __r = __r*31 + filePath.hashCode()
        __r = __r*31 + profile.hashCode()
        __r = __r*31 + providers.hashCode()
        __r = __r*31 + if (duration != null) duration.hashCode() else 0
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("CollectTracesCommand (")
        printer.indent {
            print("pid = "); pid.print(printer); println()
            print("filePath = "); filePath.print(printer); println()
            print("profile = "); profile.print(printer); println()
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
 * #### Generated from [DiagnosticsHostModel.kt:26]
 */
class CounterCollectionSession (
    val filePath: String,
    val format: CounterFileFormat,
    val refreshInterval: Int,
    val providers: String,
    val metrics: String?,
    val maxTimeSeries: Int,
    val maxHistograms: Int,
    val duration: Int?
) : RdBindableBase() {
    //companion
    
    companion object : IMarshaller<CounterCollectionSession> {
        override val _type: KClass<CounterCollectionSession> = CounterCollectionSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): CounterCollectionSession  {
            val _id = RdId.read(buffer)
            val filePath = buffer.readString()
            val format = buffer.readEnum<CounterFileFormat>()
            val refreshInterval = buffer.readInt()
            val providers = buffer.readString()
            val metrics = buffer.readNullable { buffer.readString() }
            val maxTimeSeries = buffer.readInt()
            val maxHistograms = buffer.readInt()
            val duration = buffer.readNullable { buffer.readInt() }
            return CounterCollectionSession(filePath, format, refreshInterval, providers, metrics, maxTimeSeries, maxHistograms, duration).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: CounterCollectionSession)  {
            value.rdid.write(buffer)
            buffer.writeString(value.filePath)
            buffer.writeEnum(value.format)
            buffer.writeInt(value.refreshInterval)
            buffer.writeString(value.providers)
            buffer.writeNullable(value.metrics) { buffer.writeString(it) }
            buffer.writeInt(value.maxTimeSeries)
            buffer.writeInt(value.maxHistograms)
            buffer.writeNullable(value.duration) { buffer.writeInt(it) }
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
            print("filePath = "); filePath.print(printer); println()
            print("format = "); format.print(printer); println()
            print("refreshInterval = "); refreshInterval.print(printer); println()
            print("providers = "); providers.print(printer); println()
            print("metrics = "); metrics.print(printer); println()
            print("maxTimeSeries = "); maxTimeSeries.print(printer); println()
            print("maxHistograms = "); maxHistograms.print(printer); println()
            print("duration = "); duration.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): CounterCollectionSession   {
        return CounterCollectionSession(
            filePath,
            format,
            refreshInterval,
            providers,
            metrics,
            maxTimeSeries,
            maxHistograms,
            duration
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:28]
 */
enum class CounterFileFormat {
    Csv, 
    Json;
    
    companion object {
        val marshaller = FrameworkMarshallers.enum<CounterFileFormat>()
        
    }
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:40]
 */
class CounterMonitoringSession private constructor(
    private val _active: RdOptionalProperty<Boolean>,
    private val _duration: RdProperty<Int?>,
    private val _counters: RdMap<String, Counter>,
    val refreshInterval: Int,
    val providers: String,
    val metrics: String?,
    val maxTimeSeries: Int,
    val maxHistograms: Int
) : RdBindableBase() {
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
            return CounterMonitoringSession(_active, _duration, _counters, refreshInterval, providers, metrics, maxTimeSeries, maxHistograms).withId(_id)
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
    val active: IOptProperty<Boolean> get() = _active
    val duration: IProperty<Int?> get() = _duration
    val counters: IMutableViewableMap<String, Counter> get() = _counters
    //methods
    //initializer
    init {
        _active.optimizeNested = true
        _duration.optimizeNested = true
        _counters.optimizeNested = true
    }
    
    init {
        _counters.async = true
    }
    
    init {
        bindableChildren.add("active" to _active)
        bindableChildren.add("duration" to _duration)
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
        RdOptionalProperty<Boolean>(FrameworkMarshallers.Bool),
        RdProperty<Int?>(null, __IntNullableSerializer),
        RdMap<String, Counter>(FrameworkMarshallers.String, Counter),
        refreshInterval,
        providers,
        metrics,
        maxTimeSeries,
        maxHistograms
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("CounterMonitoringSession (")
        printer.indent {
            print("active = "); _active.print(printer); println()
            print("duration = "); _duration.print(printer); println()
            print("counters = "); _counters.print(printer); println()
            print("refreshInterval = "); refreshInterval.print(printer); println()
            print("providers = "); providers.print(printer); println()
            print("metrics = "); metrics.print(printer); println()
            print("maxTimeSeries = "); maxTimeSeries.print(printer); println()
            print("maxHistograms = "); maxHistograms.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): CounterMonitoringSession   {
        return CounterMonitoringSession(
            _active.deepClonePolymorphic(),
            _duration.deepClonePolymorphic(),
            _counters.deepClonePolymorphic(),
            refreshInterval,
            providers,
            metrics,
            maxTimeSeries,
            maxHistograms
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:114]
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
 * #### Generated from [DiagnosticsHostModel.kt:105]
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
 * #### Generated from [DiagnosticsHostModel.kt:69]
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
 * #### Generated from [DiagnosticsHostModel.kt:58]
 */
class GcEventCollectionSession (
    val filePath: String,
    val duration: Int?
) : RdBindableBase() {
    //companion
    
    companion object : IMarshaller<GcEventCollectionSession> {
        override val _type: KClass<GcEventCollectionSession> = GcEventCollectionSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): GcEventCollectionSession  {
            val _id = RdId.read(buffer)
            val filePath = buffer.readString()
            val duration = buffer.readNullable { buffer.readInt() }
            return GcEventCollectionSession(filePath, duration).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: GcEventCollectionSession)  {
            value.rdid.write(buffer)
            buffer.writeString(value.filePath)
            buffer.writeNullable(value.duration) { buffer.writeInt(it) }
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
            print("filePath = "); filePath.print(printer); println()
            print("duration = "); duration.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): GcEventCollectionSession   {
        return GcEventCollectionSession(
            filePath,
            duration
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:63]
 */
class GcEventMonitoringSession private constructor(
    private val _active: RdOptionalProperty<Boolean>,
    private val _duration: RdProperty<Int?>,
    private val _gcHappened: RdSignal<GcEvent>
) : RdBindableBase() {
    //companion
    
    companion object : IMarshaller<GcEventMonitoringSession> {
        override val _type: KClass<GcEventMonitoringSession> = GcEventMonitoringSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): GcEventMonitoringSession  {
            val _id = RdId.read(buffer)
            val _active = RdOptionalProperty.read(ctx, buffer, FrameworkMarshallers.Bool)
            val _duration = RdProperty.read(ctx, buffer, __IntNullableSerializer)
            val _gcHappened = RdSignal.read(ctx, buffer, GcEvent)
            return GcEventMonitoringSession(_active, _duration, _gcHappened).withId(_id)
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
    val active: IOptProperty<Boolean> get() = _active
    val duration: IProperty<Int?> get() = _duration
    val gcHappened: IAsyncSignal<GcEvent> get() = _gcHappened
    //methods
    //initializer
    init {
        _active.optimizeNested = true
        _duration.optimizeNested = true
    }
    
    init {
        _gcHappened.async = true
    }
    
    init {
        bindableChildren.add("active" to _active)
        bindableChildren.add("duration" to _duration)
        bindableChildren.add("gcHappened" to _gcHappened)
    }
    
    //secondary constructor
    constructor(
    ) : this(
        RdOptionalProperty<Boolean>(FrameworkMarshallers.Bool),
        RdProperty<Int?>(null, __IntNullableSerializer),
        RdSignal<GcEvent>(GcEvent)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("GcEventMonitoringSession (")
        printer.indent {
            print("active = "); _active.print(printer); println()
            print("duration = "); _duration.print(printer); println()
            print("gcHappened = "); _gcHappened.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): GcEventMonitoringSession   {
        return GcEventMonitoringSession(
            _active.deepClonePolymorphic(),
            _duration.deepClonePolymorphic(),
            _gcHappened.deepClonePolymorphic()
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:11]
 */
data class ProcessInfo (
    val processId: Int,
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
            val processId = buffer.readInt()
            val processName = buffer.readString()
            val filename = buffer.readNullable { buffer.readString() }
            val startTime = buffer.readNullable { buffer.readString() }
            val commandLine = buffer.readNullable { buffer.readString() }
            val operatingSystem = buffer.readNullable { buffer.readString() }
            val processArchitecture = buffer.readNullable { buffer.readString() }
            return ProcessInfo(processId, processName, filename, startTime, commandLine, operatingSystem, processArchitecture)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ProcessInfo)  {
            buffer.writeInt(value.processId)
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
        
        if (processId != other.processId) return false
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
        __r = __r*31 + processId.hashCode()
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
            print("processId = "); processId.print(printer); println()
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
 * #### Generated from [DiagnosticsHostModel.kt:21]
 */
class ProcessList private constructor(
    private val _items: RdList<ProcessInfo>,
    private val _refresh: RdSignal<Unit>
) : RdBindableBase() {
    //companion
    
    companion object : IMarshaller<ProcessList> {
        override val _type: KClass<ProcessList> = ProcessList::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ProcessList  {
            val _id = RdId.read(buffer)
            val _items = RdList.read(ctx, buffer, ProcessInfo)
            val _refresh = RdSignal.read(ctx, buffer, FrameworkMarshallers.Void)
            return ProcessList(_items, _refresh).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ProcessList)  {
            value.rdid.write(buffer)
            RdList.write(ctx, buffer, value._items)
            RdSignal.write(ctx, buffer, value._refresh)
        }
        
        
    }
    //fields
    val items: IMutableViewableList<ProcessInfo> get() = _items
    val refresh: ISignal<Unit> get() = _refresh
    //methods
    //initializer
    init {
        _items.optimizeNested = true
    }
    
    init {
        bindableChildren.add("items" to _items)
        bindableChildren.add("refresh" to _refresh)
    }
    
    //secondary constructor
    constructor(
    ) : this(
        RdList<ProcessInfo>(ProcessInfo),
        RdSignal<Unit>(FrameworkMarshallers.Void)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ProcessList (")
        printer.indent {
            print("items = "); _items.print(printer); println()
            print("refresh = "); _refresh.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): ProcessList   {
        return ProcessList(
            _items.deepClonePolymorphic(),
            _refresh.deepClonePolymorphic()
        )
    }
    //contexts
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:123]
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
