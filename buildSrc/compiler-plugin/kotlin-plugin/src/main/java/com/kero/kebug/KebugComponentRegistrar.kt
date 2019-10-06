package com.kero.kebug

import com.google.auto.service.AutoService
import com.kero.kebug.KebugCommandLineProcessor.Companion.KEY_ENABLED
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.codegen.extensions.ClassBuilderInterceptorExtension
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration

@AutoService(ComponentRegistrar::class)
class KebugComponentRegistrar: ComponentRegistrar{
    override fun registerProjectComponents(project: MockProject, configuration: CompilerConfiguration) {
        val enabled = configuration[KEY_ENABLED]
        if(enabled ==false){
            return
        }
        ClassBuilderInterceptorExtension.registerExtension(
            project ,KebugExtension(configuration.get(
                CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY,
                MessageCollector.NONE))
        )

    }
}