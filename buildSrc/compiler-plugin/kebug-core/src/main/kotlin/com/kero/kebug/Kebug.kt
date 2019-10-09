package com.kero.kebug
@Target(AnnotationTarget.FUNCTION)
annotation class Kebug()
@Target(AnnotationTarget.FUNCTION)
annotation class Timber(val kebugLevel:KebugLevel)
@Target(AnnotationTarget.FUNCTION)
annotation class AndroidLog(val tag:String, val kebugLevel:KebugLevel)
enum class KebugLevel{
    E, D ,V , I ,W
}


