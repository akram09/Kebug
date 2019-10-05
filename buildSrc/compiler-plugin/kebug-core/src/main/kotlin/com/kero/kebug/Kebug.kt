package com.kero.kebug
@Target(AnnotationTarget.FUNCTION)
annotation class Kebug()
enum class KebugLevel{
    ERROR , DEBUG , VOLATILE
}