package com.kero.kebug

import org.gradle.api.Plugin
import org.gradle.api.Project

class KebugGradlePlugin:Plugin<Project>  {
    override fun apply(p0: Project) {
        p0.extensions.create("kebug", KebugGradleExtension::class.java)
    }
}
open class KebugGradleExtension{
    var enabled:Boolean = true
}