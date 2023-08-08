@file:Suppress("EXPERIMENTAL_API_USAGE","EXPERIMENTAL_UNSIGNED_LITERALS","PackageDirectoryMismatch","UnusedImport","unused","LocalVariableName","CanBeVal","PropertyName","EnumEntryName","ClassName","ObjectPropertyName","UnnecessaryVariable","SpellCheckingInspection")
package com.github.rafaelldi.diagnosticsclientplugin.model

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
 * #### Generated from [DiagnosticsHostModel.kt:16]
 */
class DiagnosticsHostModel private constructor(
    val processList: ProcessList,
    private val _counterExportSessions: RdMap<Int, CounterExportSession>,
    private val _counterProtocolSessions: RdMap<Int, CounterProtocolSession>,
    private val _gcEventExportSessions: RdMap<Int, GcEventExportSession>,
    private val _gcEventProtocolSessions: RdMap<Int, GcEventProtocolSession>,
    private val _traceExportSessions: RdMap<Int, TraceExportSession>,
    private val _traceProtocolSessions: RdMap<Int, TraceProtocolSession>,
    private val _chartProtocolSessions: RdMap<Int, ChartProtocolSession>,
    private val _collectDump: RdCall<CollectDumpCommand, DumpCollectionResult>,
    private val _collectStackTrace: RdCall<CollectStackTraceCommand, String>
) : RdExtBase() {
    //companion
    
    companion object : ISerializersOwner {
        
        override fun registerSerializersCore(serializers: ISerializers)  {
            serializers.register(ProcessEnvironmentVariable)
            serializers.register(ProcessInfo)
            serializers.register(ProcessList)
            serializers.register(CounterExportSession)
            serializers.register(GcEventExportSession)
            serializers.register(TraceExportSession)
            serializers.register(CounterProtocolSession)
            serializers.register(GcEventProtocolSession)
            serializers.register(TraceProtocolSession)
            serializers.register(ChartProtocolSession)
            serializers.register(Counter)
            serializers.register(GcEvent)
            serializers.register(PredefinedProvider.marshaller)
            serializers.register(Trace)
            serializers.register(ChartEvent)
            serializers.register(ChartEventType.marshaller)
            serializers.register(CollectDumpCommand)
            serializers.register(DumpCollectionResult)
            serializers.register(CollectStackTraceCommand)
            serializers.register(CounterFileFormat.marshaller)
            serializers.register(TracingProfile.marshaller)
            serializers.register(DumpType.marshaller)
            serializers.register(ExportSession_Unknown)
            serializers.register(ProtocolSession_Unknown)
        }
        
        
        @JvmStatic
        @JvmName("internalCreateModel")
        @Deprecated("Use create instead", ReplaceWith("create(lifetime, protocol)"))
        internal fun createModel(lifetime: Lifetime, protocol: IProtocol): DiagnosticsHostModel  {
            @Suppress("DEPRECATION")
            return create(lifetime, protocol)
        }
        
        @JvmStatic
        @Deprecated("Use protocol.diagnosticsHostModel or revise the extension scope instead", ReplaceWith("protocol.diagnosticsHostModel"))
        fun create(lifetime: Lifetime, protocol: IProtocol): DiagnosticsHostModel  {
            DiagnosticsHostRoot.register(protocol.serializers)
            
            return DiagnosticsHostModel()
        }
        
        
        const val serializationHash = -4546765254926434371L
        
    }
    override val serializersOwner: ISerializersOwner get() = DiagnosticsHostModel
    override val serializationHash: Long get() = DiagnosticsHostModel.serializationHash
    
    //fields
    val counterExportSessions: IMutableViewableMap<Int, CounterExportSession> get() = _counterExportSessions
    val counterProtocolSessions: IMutableViewableMap<Int, CounterProtocolSession> get() = _counterProtocolSessions
    val gcEventExportSessions: IMutableViewableMap<Int, GcEventExportSession> get() = _gcEventExportSessions
    val gcEventProtocolSessions: IMutableViewableMap<Int, GcEventProtocolSession> get() = _gcEventProtocolSessions
    val traceExportSessions: IMutableViewableMap<Int, TraceExportSession> get() = _traceExportSessions
    val traceProtocolSessions: IMutableViewableMap<Int, TraceProtocolSession> get() = _traceProtocolSessions
    val chartProtocolSessions: IMutableViewableMap<Int, ChartProtocolSession> get() = _chartProtocolSessions
    val collectDump: IRdCall<CollectDumpCommand, DumpCollectionResult> get() = _collectDump
    val collectStackTrace: IRdCall<CollectStackTraceCommand, String> get() = _collectStackTrace
    //methods
    //initializer
    init {
        bindableChildren.add("processList" to processList)
        bindableChildren.add("counterExportSessions" to _counterExportSessions)
        bindableChildren.add("counterProtocolSessions" to _counterProtocolSessions)
        bindableChildren.add("gcEventExportSessions" to _gcEventExportSessions)
        bindableChildren.add("gcEventProtocolSessions" to _gcEventProtocolSessions)
        bindableChildren.add("traceExportSessions" to _traceExportSessions)
        bindableChildren.add("traceProtocolSessions" to _traceProtocolSessions)
        bindableChildren.add("chartProtocolSessions" to _chartProtocolSessions)
        bindableChildren.add("collectDump" to _collectDump)
        bindableChildren.add("collectStackTrace" to _collectStackTrace)
    }
    
    //secondary constructor
    private constructor(
    ) : this(
        ProcessList(),
        RdMap<Int, CounterExportSession>(FrameworkMarshallers.Int, CounterExportSession),
        RdMap<Int, CounterProtocolSession>(FrameworkMarshallers.Int, CounterProtocolSession),
        RdMap<Int, GcEventExportSession>(FrameworkMarshallers.Int, GcEventExportSession),
        RdMap<Int, GcEventProtocolSession>(FrameworkMarshallers.Int, GcEventProtocolSession),
        RdMap<Int, TraceExportSession>(FrameworkMarshallers.Int, TraceExportSession),
        RdMap<Int, TraceProtocolSession>(FrameworkMarshallers.Int, TraceProtocolSession),
        RdMap<Int, ChartProtocolSession>(FrameworkMarshallers.Int, ChartProtocolSession),
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
            print("counterExportSessions = "); _counterExportSessions.print(printer); println()
            print("counterProtocolSessions = "); _counterProtocolSessions.print(printer); println()
            print("gcEventExportSessions = "); _gcEventExportSessions.print(printer); println()
            print("gcEventProtocolSessions = "); _gcEventProtocolSessions.print(printer); println()
            print("traceExportSessions = "); _traceExportSessions.print(printer); println()
            print("traceProtocolSessions = "); _traceProtocolSessions.print(printer); println()
            print("chartProtocolSessions = "); _chartProtocolSessions.print(printer); println()
            print("collectDump = "); _collectDump.print(printer); println()
            print("collectStackTrace = "); _collectStackTrace.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): DiagnosticsHostModel   {
        return DiagnosticsHostModel(
            processList.deepClonePolymorphic(),
            _counterExportSessions.deepClonePolymorphic(),
            _counterProtocolSessions.deepClonePolymorphic(),
            _gcEventExportSessions.deepClonePolymorphic(),
            _gcEventProtocolSessions.deepClonePolymorphic(),
            _traceExportSessions.deepClonePolymorphic(),
            _traceProtocolSessions.deepClonePolymorphic(),
            _chartProtocolSessions.deepClonePolymorphic(),
            _collectDump.deepClonePolymorphic(),
            _collectStackTrace.deepClonePolymorphic()
        )
    }
    //contexts
    //threading
    override val extThreading: ExtThreadingKind get() = ExtThreadingKind.Default
}
val IProtocol.diagnosticsHostModel get() = getOrCreateExtension(DiagnosticsHostModel::class) { @Suppress("DEPRECATION") DiagnosticsHostModel.create(lifetime, this) }



/**
 * #### Generated from [DiagnosticsHostModel.kt:136]
 */
data class ChartEvent (
    val timeStamp: Long,
    val value: Double,
    val type: ChartEventType,
    val label: String?
) : IPrintable {
    //companion
    
    companion object : IMarshaller<ChartEvent> {
        override val _type: KClass<ChartEvent> = ChartEvent::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ChartEvent  {
            val timeStamp = buffer.readLong()
            val value = buffer.readDouble()
            val type = buffer.readEnum<ChartEventType>()
            val label = buffer.readNullable { buffer.readString() }
            return ChartEvent(timeStamp, value, type, label)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ChartEvent)  {
            buffer.writeLong(value.timeStamp)
            buffer.writeDouble(value.value)
            buffer.writeEnum(value.type)
            buffer.writeNullable(value.label) { buffer.writeString(it) }
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
        
        other as ChartEvent
        
        if (timeStamp != other.timeStamp) return false
        if (value != other.value) return false
        if (type != other.type) return false
        if (label != other.label) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + timeStamp.hashCode()
        __r = __r*31 + value.hashCode()
        __r = __r*31 + type.hashCode()
        __r = __r*31 + if (label != null) label.hashCode() else 0
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ChartEvent (")
        printer.indent {
            print("timeStamp = "); timeStamp.print(printer); println()
            print("value = "); value.print(printer); println()
            print("type = "); type.print(printer); println()
            print("label = "); label.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:143]
 */
enum class ChartEventType {
    Cpu, 
    WorkingSet, 
    GcHeapSize, 
    Gc, 
    Exception;
    
    companion object {
        val marshaller = FrameworkMarshallers.enum<ChartEventType>()
        
    }
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:90]
 */
class ChartProtocolSession private constructor(
    private val _eventReceived: RdSignal<ChartEvent>,
    _active: RdOptionalProperty<Boolean>,
    _duration: RdProperty<Int?>
) : ProtocolSession (
    _active,
    _duration
) {
    //companion
    
    companion object : IMarshaller<ChartProtocolSession> {
        override val _type: KClass<ChartProtocolSession> = ChartProtocolSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ChartProtocolSession  {
            val _id = RdId.read(buffer)
            val _active = RdOptionalProperty.read(ctx, buffer, FrameworkMarshallers.Bool)
            val _duration = RdProperty.read(ctx, buffer, __IntNullableSerializer)
            val _eventReceived = RdSignal.read(ctx, buffer, ChartEvent)
            return ChartProtocolSession(_eventReceived, _active, _duration).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ChartProtocolSession)  {
            value.rdid.write(buffer)
            RdOptionalProperty.write(ctx, buffer, value._active)
            RdProperty.write(ctx, buffer, value._duration)
            RdSignal.write(ctx, buffer, value._eventReceived)
        }
        
        private val __IntNullableSerializer = FrameworkMarshallers.Int.nullable()
        
    }
    //fields
    val eventReceived: IAsyncSignal<ChartEvent> get() = _eventReceived
    //methods
    //initializer
    init {
        _eventReceived.async = true
    }
    
    init {
        bindableChildren.add("eventReceived" to _eventReceived)
    }
    
    //secondary constructor
    constructor(
    ) : this(
        RdSignal<ChartEvent>(ChartEvent),
        RdOptionalProperty<Boolean>(FrameworkMarshallers.Bool),
        RdProperty<Int?>(null, __IntNullableSerializer)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ChartProtocolSession (")
        printer.indent {
            print("eventReceived = "); _eventReceived.print(printer); println()
            print("active = "); _active.print(printer); println()
            print("duration = "); _duration.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): ChartProtocolSession   {
        return ChartProtocolSession(
            _eventReceived.deepClonePolymorphic(),
            _active.deepClonePolymorphic(),
            _duration.deepClonePolymorphic()
        )
    }
    //contexts
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:166]
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
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:183]
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
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:94]
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
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:41]
 */
class CounterExportSession (
    val format: CounterFileFormat,
    val refreshInterval: Int,
    val providers: String,
    val metrics: String?,
    val maxTimeSeries: Int,
    val maxHistograms: Int,
    duration: Int?,
    exportFilePath: String
) : ExportSession (
    duration,
    exportFilePath
) {
    //companion
    
    companion object : IMarshaller<CounterExportSession> {
        override val _type: KClass<CounterExportSession> = CounterExportSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): CounterExportSession  {
            val _id = RdId.read(buffer)
            val duration = buffer.readNullable { buffer.readInt() }
            val exportFilePath = buffer.readString()
            val format = buffer.readEnum<CounterFileFormat>()
            val refreshInterval = buffer.readInt()
            val providers = buffer.readString()
            val metrics = buffer.readNullable { buffer.readString() }
            val maxTimeSeries = buffer.readInt()
            val maxHistograms = buffer.readInt()
            return CounterExportSession(format, refreshInterval, providers, metrics, maxTimeSeries, maxHistograms, duration, exportFilePath).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: CounterExportSession)  {
            value.rdid.write(buffer)
            buffer.writeNullable(value.duration) { buffer.writeInt(it) }
            buffer.writeString(value.exportFilePath)
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
        printer.println("CounterExportSession (")
        printer.indent {
            print("format = "); format.print(printer); println()
            print("refreshInterval = "); refreshInterval.print(printer); println()
            print("providers = "); providers.print(printer); println()
            print("metrics = "); metrics.print(printer); println()
            print("maxTimeSeries = "); maxTimeSeries.print(printer); println()
            print("maxHistograms = "); maxHistograms.print(printer); println()
            print("duration = "); duration.print(printer); println()
            print("exportFilePath = "); exportFilePath.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): CounterExportSession   {
        return CounterExportSession(
            format,
            refreshInterval,
            providers,
            metrics,
            maxTimeSeries,
            maxHistograms,
            duration,
            exportFilePath
        )
    }
    //contexts
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:42]
 */
enum class CounterFileFormat {
    Csv, 
    Json;
    
    companion object {
        val marshaller = FrameworkMarshallers.enum<CounterFileFormat>()
        
    }
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:72]
 */
class CounterProtocolSession private constructor(
    private val _counterReceived: RdSignal<Counter>,
    val refreshInterval: Int,
    val providers: String,
    val metrics: String?,
    val maxTimeSeries: Int,
    val maxHistograms: Int,
    _active: RdOptionalProperty<Boolean>,
    _duration: RdProperty<Int?>
) : ProtocolSession (
    _active,
    _duration
) {
    //companion
    
    companion object : IMarshaller<CounterProtocolSession> {
        override val _type: KClass<CounterProtocolSession> = CounterProtocolSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): CounterProtocolSession  {
            val _id = RdId.read(buffer)
            val _active = RdOptionalProperty.read(ctx, buffer, FrameworkMarshallers.Bool)
            val _duration = RdProperty.read(ctx, buffer, __IntNullableSerializer)
            val _counterReceived = RdSignal.read(ctx, buffer, Counter)
            val refreshInterval = buffer.readInt()
            val providers = buffer.readString()
            val metrics = buffer.readNullable { buffer.readString() }
            val maxTimeSeries = buffer.readInt()
            val maxHistograms = buffer.readInt()
            return CounterProtocolSession(_counterReceived, refreshInterval, providers, metrics, maxTimeSeries, maxHistograms, _active, _duration).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: CounterProtocolSession)  {
            value.rdid.write(buffer)
            RdOptionalProperty.write(ctx, buffer, value._active)
            RdProperty.write(ctx, buffer, value._duration)
            RdSignal.write(ctx, buffer, value._counterReceived)
            buffer.writeInt(value.refreshInterval)
            buffer.writeString(value.providers)
            buffer.writeNullable(value.metrics) { buffer.writeString(it) }
            buffer.writeInt(value.maxTimeSeries)
            buffer.writeInt(value.maxHistograms)
        }
        
        private val __IntNullableSerializer = FrameworkMarshallers.Int.nullable()
        
    }
    //fields
    val counterReceived: IAsyncSignal<Counter> get() = _counterReceived
    //methods
    //initializer
    init {
        _counterReceived.async = true
    }
    
    init {
        bindableChildren.add("counterReceived" to _counterReceived)
    }
    
    //secondary constructor
    constructor(
        refreshInterval: Int,
        providers: String,
        metrics: String?,
        maxTimeSeries: Int,
        maxHistograms: Int
    ) : this(
        RdSignal<Counter>(Counter),
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
        printer.println("CounterProtocolSession (")
        printer.indent {
            print("counterReceived = "); _counterReceived.print(printer); println()
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
    override fun deepClone(): CounterProtocolSession   {
        return CounterProtocolSession(
            _counterReceived.deepClonePolymorphic(),
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
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:177]
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
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:168]
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
 * #### Generated from [DiagnosticsHostModel.kt:36]
 */
abstract class ExportSession (
    val duration: Int?,
    val exportFilePath: String
) : RdBindableBase() {
    //companion
    
    companion object : IAbstractDeclaration<ExportSession> {
        override fun readUnknownInstance(ctx: SerializationCtx, buffer: AbstractBuffer, unknownId: RdId, size: Int): ExportSession  {
            val objectStartPosition = buffer.position
            val _id = RdId.read(buffer)
            val duration = buffer.readNullable { buffer.readInt() }
            val exportFilePath = buffer.readString()
            val unknownBytes = ByteArray(objectStartPosition + size - buffer.position)
            buffer.readByteArrayRaw(unknownBytes)
            return ExportSession_Unknown(duration, exportFilePath, unknownId, unknownBytes).withId(_id)
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
    //threading
}


class ExportSession_Unknown (
    duration: Int?,
    exportFilePath: String,
    override val unknownId: RdId,
    val unknownBytes: ByteArray
) : ExportSession (
    duration,
    exportFilePath
), IUnknownInstance {
    //companion
    
    companion object : IMarshaller<ExportSession_Unknown> {
        override val _type: KClass<ExportSession_Unknown> = ExportSession_Unknown::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ExportSession_Unknown  {
            throw NotImplementedError("Unknown instances should not be read via serializer")
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ExportSession_Unknown)  {
            value.rdid.write(buffer)
            buffer.writeNullable(value.duration) { buffer.writeInt(it) }
            buffer.writeString(value.exportFilePath)
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
        printer.println("ExportSession_Unknown (")
        printer.indent {
            print("duration = "); duration.print(printer); println()
            print("exportFilePath = "); exportFilePath.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): ExportSession_Unknown   {
        return ExportSession_Unknown(
            duration,
            exportFilePath,
            unknownId,
            unknownBytes
        )
    }
    //contexts
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:100]
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
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:53]
 */
class GcEventExportSession (
    duration: Int?,
    exportFilePath: String
) : ExportSession (
    duration,
    exportFilePath
) {
    //companion
    
    companion object : IMarshaller<GcEventExportSession> {
        override val _type: KClass<GcEventExportSession> = GcEventExportSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): GcEventExportSession  {
            val _id = RdId.read(buffer)
            val duration = buffer.readNullable { buffer.readInt() }
            val exportFilePath = buffer.readString()
            return GcEventExportSession(duration, exportFilePath).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: GcEventExportSession)  {
            value.rdid.write(buffer)
            buffer.writeNullable(value.duration) { buffer.writeInt(it) }
            buffer.writeString(value.exportFilePath)
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
        printer.println("GcEventExportSession (")
        printer.indent {
            print("duration = "); duration.print(printer); println()
            print("exportFilePath = "); exportFilePath.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): GcEventExportSession   {
        return GcEventExportSession(
            duration,
            exportFilePath
        )
    }
    //contexts
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:81]
 */
class GcEventProtocolSession private constructor(
    private val _gcHappened: RdSignal<GcEvent>,
    _active: RdOptionalProperty<Boolean>,
    _duration: RdProperty<Int?>
) : ProtocolSession (
    _active,
    _duration
) {
    //companion
    
    companion object : IMarshaller<GcEventProtocolSession> {
        override val _type: KClass<GcEventProtocolSession> = GcEventProtocolSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): GcEventProtocolSession  {
            val _id = RdId.read(buffer)
            val _active = RdOptionalProperty.read(ctx, buffer, FrameworkMarshallers.Bool)
            val _duration = RdProperty.read(ctx, buffer, __IntNullableSerializer)
            val _gcHappened = RdSignal.read(ctx, buffer, GcEvent)
            return GcEventProtocolSession(_gcHappened, _active, _duration).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: GcEventProtocolSession)  {
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
        printer.println("GcEventProtocolSession (")
        printer.indent {
            print("gcHappened = "); _gcHappened.print(printer); println()
            print("active = "); _active.print(printer); println()
            print("duration = "); _duration.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): GcEventProtocolSession   {
        return GcEventProtocolSession(
            _gcHappened.deepClonePolymorphic(),
            _active.deepClonePolymorphic(),
            _duration.deepClonePolymorphic()
        )
    }
    //contexts
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:118]
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
 * #### Generated from [DiagnosticsHostModel.kt:17]
 */
data class ProcessEnvironmentVariable (
    val key: String,
    val value: String
) : IPrintable {
    //companion
    
    companion object : IMarshaller<ProcessEnvironmentVariable> {
        override val _type: KClass<ProcessEnvironmentVariable> = ProcessEnvironmentVariable::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ProcessEnvironmentVariable  {
            val key = buffer.readString()
            val value = buffer.readString()
            return ProcessEnvironmentVariable(key, value)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ProcessEnvironmentVariable)  {
            buffer.writeString(value.key)
            buffer.writeString(value.value)
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
        
        other as ProcessEnvironmentVariable
        
        if (key != other.key) return false
        if (value != other.value) return false
        
        return true
    }
    //hash code trait
    override fun hashCode(): Int  {
        var __r = 0
        __r = __r*31 + key.hashCode()
        __r = __r*31 + value.hashCode()
        return __r
    }
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ProcessEnvironmentVariable (")
        printer.indent {
            print("key = "); key.print(printer); println()
            print("value = "); value.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:22]
 */
data class ProcessInfo (
    val processName: String,
    val filename: String?,
    val startTime: String?,
    val commandLine: String?,
    val operatingSystem: String?,
    val processArchitecture: String?,
    val environmentVariables: Array<ProcessEnvironmentVariable>
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
            val environmentVariables = buffer.readArray {ProcessEnvironmentVariable.read(ctx, buffer)}
            return ProcessInfo(processName, filename, startTime, commandLine, operatingSystem, processArchitecture, environmentVariables)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ProcessInfo)  {
            buffer.writeString(value.processName)
            buffer.writeNullable(value.filename) { buffer.writeString(it) }
            buffer.writeNullable(value.startTime) { buffer.writeString(it) }
            buffer.writeNullable(value.commandLine) { buffer.writeString(it) }
            buffer.writeNullable(value.operatingSystem) { buffer.writeString(it) }
            buffer.writeNullable(value.processArchitecture) { buffer.writeString(it) }
            buffer.writeArray(value.environmentVariables) { ProcessEnvironmentVariable.write(ctx, buffer, it) }
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
        if (!(environmentVariables contentDeepEquals other.environmentVariables)) return false
        
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
        __r = __r*31 + environmentVariables.contentDeepHashCode()
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
            print("environmentVariables = "); environmentVariables.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    //contexts
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:32]
 */
class ProcessList private constructor(
    private val _items: RdMap<Int, ProcessInfo>
) : RdBindableBase() {
    //companion
    
    companion object : IMarshaller<ProcessList> {
        override val _type: KClass<ProcessList> = ProcessList::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ProcessList  {
            val _id = RdId.read(buffer)
            val _items = RdMap.read(ctx, buffer, FrameworkMarshallers.Int, ProcessInfo)
            return ProcessList(_items).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ProcessList)  {
            value.rdid.write(buffer)
            RdMap.write(ctx, buffer, value._items)
        }
        
        
    }
    //fields
    val items: IMutableViewableMap<Int, ProcessInfo> get() = _items
    //methods
    //initializer
    init {
        _items.optimizeNested = true
    }
    
    init {
        _items.async = true
    }
    
    init {
        bindableChildren.add("items" to _items)
    }
    
    //secondary constructor
    constructor(
    ) : this(
        RdMap<Int, ProcessInfo>(FrameworkMarshallers.Int, ProcessInfo)
    )
    
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("ProcessList (")
        printer.indent {
            print("items = "); _items.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): ProcessList   {
        return ProcessList(
            _items.deepClonePolymorphic()
        )
    }
    //contexts
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:67]
 */
abstract class ProtocolSession (
    protected val _active: RdOptionalProperty<Boolean>,
    protected val _duration: RdProperty<Int?>
) : RdBindableBase() {
    //companion
    
    companion object : IAbstractDeclaration<ProtocolSession> {
        override fun readUnknownInstance(ctx: SerializationCtx, buffer: AbstractBuffer, unknownId: RdId, size: Int): ProtocolSession  {
            val objectStartPosition = buffer.position
            val _id = RdId.read(buffer)
            val _active = RdOptionalProperty.read(ctx, buffer, FrameworkMarshallers.Bool)
            val _duration = RdProperty.read(ctx, buffer, __IntNullableSerializer)
            val unknownBytes = ByteArray(objectStartPosition + size - buffer.position)
            buffer.readByteArrayRaw(unknownBytes)
            return ProtocolSession_Unknown(_active, _duration, unknownId, unknownBytes).withId(_id)
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
    //threading
}


class ProtocolSession_Unknown (
    _active: RdOptionalProperty<Boolean>,
    _duration: RdProperty<Int?>,
    override val unknownId: RdId,
    val unknownBytes: ByteArray
) : ProtocolSession (
    _active,
    _duration
), IUnknownInstance {
    //companion
    
    companion object : IMarshaller<ProtocolSession_Unknown> {
        override val _type: KClass<ProtocolSession_Unknown> = ProtocolSession_Unknown::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): ProtocolSession_Unknown  {
            throw NotImplementedError("Unknown instances should not be read via serializer")
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: ProtocolSession_Unknown)  {
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
        printer.println("ProtocolSession_Unknown (")
        printer.indent {
            print("active = "); _active.print(printer); println()
            print("duration = "); _duration.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): ProtocolSession_Unknown   {
        return ProtocolSession_Unknown(
            _active.deepClonePolymorphic(),
            _duration.deepClonePolymorphic(),
            unknownId,
            unknownBytes
        )
    }
    //contexts
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:129]
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
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:56]
 */
class TraceExportSession (
    val profile: TracingProfile,
    val providers: String,
    val predefinedProviders: List<PredefinedProvider>,
    duration: Int?,
    exportFilePath: String
) : ExportSession (
    duration,
    exportFilePath
) {
    //companion
    
    companion object : IMarshaller<TraceExportSession> {
        override val _type: KClass<TraceExportSession> = TraceExportSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): TraceExportSession  {
            val _id = RdId.read(buffer)
            val duration = buffer.readNullable { buffer.readInt() }
            val exportFilePath = buffer.readString()
            val profile = buffer.readEnum<TracingProfile>()
            val providers = buffer.readString()
            val predefinedProviders = buffer.readList { buffer.readEnum<PredefinedProvider>() }
            return TraceExportSession(profile, providers, predefinedProviders, duration, exportFilePath).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: TraceExportSession)  {
            value.rdid.write(buffer)
            buffer.writeNullable(value.duration) { buffer.writeInt(it) }
            buffer.writeString(value.exportFilePath)
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
        printer.println("TraceExportSession (")
        printer.indent {
            print("profile = "); profile.print(printer); println()
            print("providers = "); providers.print(printer); println()
            print("predefinedProviders = "); predefinedProviders.print(printer); println()
            print("duration = "); duration.print(printer); println()
            print("exportFilePath = "); exportFilePath.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): TraceExportSession   {
        return TraceExportSession(
            profile,
            providers,
            predefinedProviders,
            duration,
            exportFilePath
        )
    }
    //contexts
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:85]
 */
class TraceProtocolSession private constructor(
    private val _traceReceived: RdSignal<Trace>,
    val predefinedProviders: List<PredefinedProvider>,
    _active: RdOptionalProperty<Boolean>,
    _duration: RdProperty<Int?>
) : ProtocolSession (
    _active,
    _duration
) {
    //companion
    
    companion object : IMarshaller<TraceProtocolSession> {
        override val _type: KClass<TraceProtocolSession> = TraceProtocolSession::class
        
        @Suppress("UNCHECKED_CAST")
        override fun read(ctx: SerializationCtx, buffer: AbstractBuffer): TraceProtocolSession  {
            val _id = RdId.read(buffer)
            val _active = RdOptionalProperty.read(ctx, buffer, FrameworkMarshallers.Bool)
            val _duration = RdProperty.read(ctx, buffer, __IntNullableSerializer)
            val _traceReceived = RdSignal.read(ctx, buffer, Trace)
            val predefinedProviders = buffer.readList { buffer.readEnum<PredefinedProvider>() }
            return TraceProtocolSession(_traceReceived, predefinedProviders, _active, _duration).withId(_id)
        }
        
        override fun write(ctx: SerializationCtx, buffer: AbstractBuffer, value: TraceProtocolSession)  {
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
        printer.println("TraceProtocolSession (")
        printer.indent {
            print("traceReceived = "); _traceReceived.print(printer); println()
            print("predefinedProviders = "); predefinedProviders.print(printer); println()
            print("active = "); _active.print(printer); println()
            print("duration = "); _duration.print(printer); println()
        }
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): TraceProtocolSession   {
        return TraceProtocolSession(
            _traceReceived.deepClonePolymorphic(),
            predefinedProviders,
            _active.deepClonePolymorphic(),
            _duration.deepClonePolymorphic()
        )
    }
    //contexts
    //threading
}


/**
 * #### Generated from [DiagnosticsHostModel.kt:57]
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
