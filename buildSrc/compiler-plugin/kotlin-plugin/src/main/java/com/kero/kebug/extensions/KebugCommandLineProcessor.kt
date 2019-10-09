package com.kero.kebug.extensions

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

@AutoService(CommandLineProcessor::class)
class KebugCommandLineProcessor :CommandLineProcessor {
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