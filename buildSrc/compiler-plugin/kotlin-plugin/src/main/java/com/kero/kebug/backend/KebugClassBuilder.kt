package com.kero.kebug.backend

import com.kero.kebug.ANDROID_LOG_ANNOT
import com.kero.kebug.KEBUG_ANNOT
import com.kero.kebug.TIMBER_ANNOT
import jdk.internal.org.objectweb.asm.Opcodes
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.codegen.ClassBuilder
import org.jetbrains.kotlin.codegen.DelegatingClassBuilder
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.js.descriptorUtils.nameIfStandardType
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.descriptorUtil.parents
import org.jetbrains.kotlin.resolve.jvm.diagnostics.JvmDeclarationOrigin
import org.jetbrains.org.objectweb.asm.MethodVisitor
import org.jetbrains.org.objectweb.asm.Type
import org.jetbrains.org.objectweb.asm.commons.InstructionAdapter
import java.lang.StringBuilder

class KebugClassBuilder(val kebugBuilder:ClassBuilder , val messageCollector: MessageCollector) : DelegatingClassBuilder() {
    override fun getDelegate() = kebugBuilder

    private fun log(msg:Any?){
        messageCollector.report(
            CompilerMessageSeverity.WARNING , msg.toString()
        )
    }

    val KEBUG_ANNOTATION= FqName(KEBUG_ANNOT)
    val TIMBER_FQ = FqName(TIMBER_ANNOT)
    val ANDROID_LOG = FqName(ANDROID_LOG_ANNOT)

    override fun newMethod(
        origin: JvmDeclarationOrigin,
        access: Int,
        name: String,
        desc: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val original  = super.newMethod(origin, access, name, desc, signature, exceptions)
        val function = origin.descriptor as? FunctionDescriptor ?: return original
        if(!function.annotations.hasAnnotation(KEBUG_ANNOTATION)){
            return original
        }
        return object : MethodVisitor(Opcodes.ASM5, original){
            override fun visitCode() {
                super.visitCode()
                InstructionAdapter(this).addLogToStartMethode(function)
            }
        }
    }



    private fun InstructionAdapter.addLogToStartMethode(function : FunctionDescriptor){

    }

}