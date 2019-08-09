package configuration

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.filters.UrlFilter
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import configuration.filters.BbFileFilter
import configuration.filters.BbTestFilter
import configuration.filters.BbCompileFilter

class BbRunProfileState(environment: ExecutionEnvironment, private val project: Project, private val configuration: BbRunConfiguration): CommandLineState(environment) {
    override fun startProcess(): ProcessHandler {
        val commandLine = GeneralCommandLine(System.getProperty("user.home") + "\\AppData\\Roaming\\npm\\bb.cmd")
        commandLine.withWorkDirectory(configuration.customProjectRoot ?: project.basePath)
        this.addConsoleFilters(UrlFilter())
        this.addConsoleFilters(BbFileFilter(project))
        this.addConsoleFilters(BbTestFilter(project))
        this.addConsoleFilters(BbCompileFilter(project))
        val colored = ColoredProcessHandler(commandLine)
        colored.addProcessListener(BbProcessListener(project))
        colored.startNotify()
        return colored
    }
}