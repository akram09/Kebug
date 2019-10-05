package com.kero.kebug

import jdk.internal.org.objectweb.asm.Opcodes
import org.jetbrains.kotlin.codegen.ClassBuilder
import org.jetbrains.kotlin.codegen.DelegatingClassBuilder
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.jvm.diagnostics.JvmDeclarationOrigin
import org.jetbrains.org.objectweb.asm.MethodVisitor
import org.jetbrains.org.objectweb.asm.commons.InstructionAdapter

class KebugClassBuilder(val kebugBuilder:ClassBuilder) : DelegatingClassBuilder() {
    override fun getDelegate() = kebugBuilder


    val KEBUG_ANNOTATION= FqName("com.kero.kebug.Kebug")

    override fun newMethod(
        origin: JvmDeclarationOrigin,
        access: Int,
        name: String,
        desc: String,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val original  = super.newMethod(origin, access, name, desc, signature, exceptions)
        val function = origin as? FunctionDescriptor ?: return original
        if(!function.annotations.hasAnnotation(KEBUG_ANNOTATION)){
            return original
        }
        val kebugLevel = function.annotations.findAnnotation(KEBUG_ANNOTATION)?.allValueArguments
        error(kebugLevel.toString())
        return object : MethodVisitor(Opcodes.ASM5, original){
            override fun visitCode() {
                super.visitCode()
                // here we have the code of our methode
            }

            override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?) {
                super.visitMethodInsn(opcode, owner, name, descriptor)
            }
        }
    }

}