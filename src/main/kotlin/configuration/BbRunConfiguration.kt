package configuration

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.JDOMExternalizerUtil
import org.jdom.Element
import org.jetbrains.annotations.NotNull

class BbRunConfiguration(project: Project, factory: ConfigurationFactory, name: String) : RunConfigurationBase<String>(project, factory, name) {
    var customProjectRoot: String? = null

    @NotNull
    override fun getConfigurationEditor(): SettingsEditor<out BbRunConfiguration> {
        return BbSettingsEditor()
    }

    override fun getState(executor: Executor, executionEnvironment: ExecutionEnvironment): RunProfileState? {
        return BbRunProfileState(executionEnvironment, project, this)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        customProjectRoot = JDOMExternalizerUtil.readField(element, KEY_CUSTOM_PROJECT_ROOT);
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        JDOMExternalizerUtil.writeField(element, KEY_CUSTOM_PROJECT_ROOT, customProjectRoot);
    }

    companion object {
        val KEY_CUSTOM_PROJECT_ROOT = "CUSTOM_PROJECT_ROOT"
    }
}