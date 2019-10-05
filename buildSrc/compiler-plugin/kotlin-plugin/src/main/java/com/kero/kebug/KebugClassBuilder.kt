package com.kero.kebug

import org.jetbrains.kotlin.codegen.ClassBuilder
import org.jetbrains.kotlin.codegen.DelegatingClassBuilder
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.jvm.diagnostics.JvmDeclarationOrigin
import org.jetbrains.org.objectweb.asm.MethodVisitor

class KebugClassBuilder(val kebugBuilder:ClassBuilder) : DelegatingClassBuilder() {
    override fun getDelegate() = kebugBuilder

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
        return original
    }
}