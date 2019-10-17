package com.kero.kebug

import com.google.auto.service.AutoService
import com.kero.kebug.KebugCommandLineProcessor.Companion.KEY_ENABLED
import com.kero.kebug.backend.KebugExtension
import com.kero.kebug.diagnostic.KebugDeclarationChecker
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.codegen.extensions.ClassBuilderInterceptorExtension
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey
import org.jetbrains.kotlin.container.StorageComponentContainer
import org.jetbrains.kotlin.container.useInstance
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor
import org.jetbrains.kotlin.platform.TargetPlatform
import org.jetbrains.kotlin.platform.jvm.isJvm

@AutoService(CommandLineProcessor::class)
class KebugCommandLineProcessor : CommandLineProcessor {
    override val pluginId= "kebug-plugin"
    override val pluginOptions = listOf(
        CliOption("enabled", "<true|false>", "it describe whether the compiler is enabled or not")
    )

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) {
        when(option.optionName){
            "enabled" ->configuration.put(KEY_ENABLED, value.toBoolean())
            else -> error("There is no extension named ${option.optionName}")
        }
    }
    companion object{
        val KEY_ENABLED= CompilerConfigurationKey<Boolean>("kebug.enabled")
    }
}
@AutoService(ComponentRegistrar::class)
class KebugComponentRegistrar: ComponentRegistrar {
    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
        val enabled = configuration[KEY_ENABLED]
        val collector = configuration.get(
            CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY,
            MessageCollector.NONE)

        ClassBuilderInterceptorExtension.registerExtension(
            project , KebugExtension(collector)
        )



    }
}
class KebugStorageComponentContributer:StorageComponentContainerContributor{
    override fun registerModuleComponents(
        container: StorageComponentContainer,
        platform: TargetPlatform,
        moduleDescriptor: ModuleDescriptor
    ) {
        if(!platform.isJvm()) return
        container.useInstance(KebugDeclarationChecker())
    }
}