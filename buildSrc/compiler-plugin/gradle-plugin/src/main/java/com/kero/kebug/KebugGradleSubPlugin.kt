package com.kero.kebug

import com.google.auto.service.AutoService
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

@AutoService(KotlinGradleSubplugin::class)
class KebugGradleSubPlugin :KotlinGradleSubplugin<AbstractCompile>{
    override fun apply(
        project: Project,
        kotlinCompile: AbstractCompile,
        javaCompile: AbstractCompile?,
        variantData: Any?,
        androidProjectHandler: Any?,
        kotlinCompilation: KotlinCompilation<KotlinCommonOptions>?
    ): List<SubpluginOption> {
        val extension = project.extensions.findByType(KebugGradleExtension::class.java) ?: KebugGradleExtension()
        val extensionSubPluginOption = SubpluginOption("enabled", extension.enabled.toString())
        return listOf(extensionSubPluginOption)
    }

    override fun getCompilerPluginId() = "kebug-plugin"

    override fun getPluginArtifact(): SubpluginArtifact {
        return SubpluginArtifact(
            "com.kero.kebug", "kebug-plugin","0.0.1"
        )
    }

    override fun isApplicable(project: Project, task: AbstractCompile)
       = project.plugins.hasPlugin(KebugGradlePlugin::class.java)

}