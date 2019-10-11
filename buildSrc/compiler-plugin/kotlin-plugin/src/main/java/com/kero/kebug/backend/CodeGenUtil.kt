package com.kero.kebug.backend

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.resolve.descriptorUtil.parents
import org.jetbrains.org.objectweb.asm.Type
import org.jetbrains.org.objectweb.asm.commons.InstructionAdapter

const val STRING_BUILDER_OWNER = "java/lang/StringBuilder"
const val CONSTRUCTOR_CALL="<init>"
const val CONSTRUCTOR_CALL_DESCRIPTION= "()V"
fun FunctionDescriptor.isTopLevelFunction():Boolean {
    val classParents = parents.filter {
        it is ClassDescriptor
    }
    return classParents.count() == 0
}
fun InstructionAdapter.createStringBuilder(){
    anew(Type.getType(StringBuilder::class.java))
    dup()
    invokespecial(STRING_BUILDER_OWNER, CONSTRUCTOR_CALL, CONSTRUCTOR_CALL_DESCRIPTION, false)
}
