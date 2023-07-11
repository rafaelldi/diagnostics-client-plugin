@file:Suppress("EXPERIMENTAL_API_USAGE","EXPERIMENTAL_UNSIGNED_LITERALS","PackageDirectoryMismatch","UnusedImport","unused","LocalVariableName","CanBeVal","PropertyName","EnumEntryName","ClassName","ObjectPropertyName","UnnecessaryVariable","SpellCheckingInspection")
package com.github.rafaelldi.diagnosticsclientplugin.model

import com.jetbrains.rd.framework.ISerializers
import com.jetbrains.rd.framework.base.ISerializersOwner
import com.jetbrains.rd.framework.base.RdExtBase
import com.jetbrains.rd.util.string.PrettyPrinter


/**
 * #### Generated from [DiagnosticsHostModel.kt:8]
 */
class DiagnosticsHostRoot private constructor(
) : RdExtBase() {
    //companion
    
    companion object : ISerializersOwner {
        
        override fun registerSerializersCore(serializers: ISerializers)  {
            DiagnosticsHostRoot.register(serializers)
            DiagnosticsHostModel.register(serializers)
        }
        
        
        
        
        
        const val serializationHash = -6848181392064581580L
        
    }
    override val serializersOwner: ISerializersOwner get() = DiagnosticsHostRoot
    override val serializationHash: Long get() = DiagnosticsHostRoot.serializationHash
    
    //fields
    //methods
    //initializer
    //secondary constructor
    //equals trait
    //hash code trait
    //pretty print
    override fun print(printer: PrettyPrinter)  {
        printer.println("DiagnosticsHostRoot (")
        printer.print(")")
    }
    //deepClone
    override fun deepClone(): DiagnosticsHostRoot   {
        return DiagnosticsHostRoot(
        )
    }
    //contexts
    //threading
    override val extThreading: ExtThreadingKind get() = ExtThreadingKind.Default
}
