package com.kero.kebug
@Target(AnnotationTarget.FUNCTION)
annotation class Kebug(val kebugLevel:KebugLevel)
enum class KebugLevel{
    ERROR , DEBUG , VOLATILE
}