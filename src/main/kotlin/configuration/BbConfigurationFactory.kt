package configuration

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project

class BbConfigurationFactory constructor(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return BbRunConfiguration(project, this, "bb")
    }

    override fun getName(): String {
        return FACTORY_NAME
    }

    override fun getId(): String {
        return FACTORY_NAME
    }

    companion object {
        private val FACTORY_NAME = "BobrilRunnerConfiguration"
    }
}