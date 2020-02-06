package builders

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import configuration.BbRunConfiguration

class CommandLineBbBuilder(private val configuration: BbRunConfiguration, private val project: Project) {
    private val commandLine = GeneralCommandLine(System.getProperty("user.home") + "\\AppData\\Roaming\\npm\\bb.cmd")

    fun build(): GeneralCommandLine {
        val projectRoot = if (configuration.customProjectRoot.isNullOrEmpty()) project.basePath else configuration.customProjectRoot
        commandLine.withWorkDirectory(projectRoot)

        if (!configuration.bundle) {
            return commandLine
        }

        addParameter("b")
        addCommand("f", configuration.fast)
        addCommand("b", configuration.beautify)
        addCommand("c", configuration.compress)
        addCommand("m", configuration.mangle)

        if (configuration.newBundler) {
            addCommand("x", configuration.newBundler)
        }

        return commandLine
    }

    private fun turnOnCommand(command: String)
    {
        addCommand(command, 1)
    }

    private fun turnOFFCommand(command: String)
    {
        addCommand(command, 0)
    }

    private fun addCommand(command: String, value: Number) {
        addParameter("-$command")
        addParameter(value.toString())
    }

    private fun addCommand(command: String, value: Boolean) {
        if (value) {
            turnOnCommand(command)
        } else {
            turnOFFCommand(command)
        }
    }

    private fun addParameter(command: String) {
//        sb.append(" $command")
        commandLine.addParameter(command)
    }
}