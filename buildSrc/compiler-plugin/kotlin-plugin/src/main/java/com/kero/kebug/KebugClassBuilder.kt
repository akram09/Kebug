package com.kero.kebug

import jdk.internal.org.objectweb.asm.Opcodes
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.codegen.ClassBuilder
import org.jetbrains.kotlin.codegen.DelegatingClassBuilder
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.js.descriptorUtils.nameIfStandardType
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.resolve.jvm.diagnostics.JvmDeclarationOrigin
import org.jetbrains.org.objectweb.asm.MethodVisitor
import org.jetbrains.org.objectweb.asm.Type
import org.jetbrains.org.objectweb.asm.Type.LONG_TYPE
import org.jetbrains.org.objectweb.asm.commons.InstructionAdapter

class KebugClassBuilder(val kebugBuilder:ClassBuilder , val messageCollector: MessageCollector) : DelegatingClassBuilder() {
    override fun getDelegate() = kebugBuilder

    private fun log(msg:String){
        messageCollector.report(
            CompilerMessageSeverity.ERROR , msg
        )
    }

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
        return object : MethodVisitor(Opcodes.ASM5, original){
            override fun visitCode() {
                super.visitCode()
                InstructionAdapter(this).addLogToStartMethode(function)
            }

            override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, descriptor: String?) {
                super.visitMethodInsn(opcode, owner, name, descriptor)
            }
        }
    }
    private fun InstructionAdapter.addLogToStartMethode(function : FunctionDescriptor){
        getstatic("java/lang/System", "out" , "Ljava/io/PrintStream")
        anew(Type.getType("java/lang/StringBuilder"))
        dup()
        invokespecial("java/lang/StringBuilder", "<init>", "()V", false)
        visitLdcInsn("⇢ ${function.name}(")
        invokevirtual(
            "java/lang/StringBuilder",
            "append",
            "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
            false
        )
        function.valueParameters.forEachIndexed { i, parameter ->
            visitLdcInsn("${parameter.name}=")
            invokevirtual("java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false)

            val varIndex = i + 1
            when (parameter.type.unwrap().nameIfStandardType.toString()) {
                "Int" -> {
                    visitVarInsn(Opcodes.ILOAD, varIndex)
                    invokevirtual("java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false)
                }
                "Long" -> {
                    visitVarInsn(Opcodes.ILOAD, varIndex)
                    invokevirtual("java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false)
                }
                else -> {
                    visitVarInsn(Opcodes.ALOAD, varIndex)
                    invokevirtual(
                        "java/lang/StringBuilder",
                        "append",
                        "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
                        false
                    )
                }
            }

            if (i < function.valueParameters.lastIndex) {
                visitLdcInsn(", ")
            } else {
                // if this is the last one, we should append a close-paren instead of a comma
                visitLdcInsn(")")
            }
            invokevirtual(
                "java/lang/StringBuilder",
                "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
                false
            )
        }

        invokevirtual("java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false)

        invokevirtual("java/io/PrintStream", "println", "(Ljava/lang/String;)V", false)

        invokestatic("java/lang/System", "currentTimeMillis", "()J", false)
        store(6001, LONG_TYPE)
    }

}