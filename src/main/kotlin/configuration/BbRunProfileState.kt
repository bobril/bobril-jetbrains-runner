package configuration

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.filters.UrlFilter
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import configuration.connection.BbClient
import services.BbService
import java.nio.charset.Charset
import java.util.*
import java.util.regex.Pattern

class BbRunProfileState(environment: ExecutionEnvironment, private val project: Project): CommandLineState(environment) {
    override fun startProcess(): ProcessHandler {
        val commandLine = GeneralCommandLine(System.getProperty("user.home") + "\\AppData\\Roaming\\npm\\bb.cmd")
        commandLine.withWorkDirectory(project.basePath)
        commandLine.charset = Charset.forName("UTF-8")
        this.addConsoleFilters(UrlFilter())
        val colored = ColoredProcessHandler(commandLine)
        colored.addProcessListener(BbProcessListener(project))
        colored.startNotify()
        return colored
    }
}