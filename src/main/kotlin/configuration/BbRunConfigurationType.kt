package configuration

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.icons.AllIcons
import javax.swing.Icon


class BbRunConfigurationType : ConfigurationType {
    override fun getDisplayName(): String {
        return "bobril-build"
    }

    override fun getConfigurationTypeDescription(): String {
        return "bobril-build run Configuration Type"
    }

    override fun getIcon(): Icon {
        return AllIcons.General.Information
    }

    override fun getId(): String {
        return "BB_RUN_CONFIG"
    }

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf<ConfigurationFactory>(BbConfigurationFactory(this));
    }
}