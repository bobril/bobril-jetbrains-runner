package configuration

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.NotNull
import services.BbService

class BbRunConfiguration(project: Project, factory: ConfigurationFactory, name: String) : RunConfigurationBase<String>(project, factory, name) {


    @NotNull
    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return BbSettingsEditor()
    }

    @Throws(RuntimeConfigurationException::class)
    override fun checkConfiguration() {

    }

    override fun getState(executor: Executor, executionEnvironment: ExecutionEnvironment): RunProfileState? {
        val bbService: BbService = ServiceManager.getService(BbService::class.java)?: return null
        bbService.getBbTools()?.setProject(project)

        return BbRunProfileState(executionEnvironment, project)
    }
}