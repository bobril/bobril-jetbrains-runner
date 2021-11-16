package configuration

import builders.CommandLineBbBuilder
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.filters.UrlFilter
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import configuration.filters.BbCompileFilter
import configuration.filters.BbFileFilter
import configuration.filters.BbTestFilter
import coverage.DisplayHandler
import listeners.CodeCoverageFileListener
import services.CoverageService

class BbRunProfileState(environment: ExecutionEnvironment, private val project: Project, private val configuration: BbRunConfiguration): CommandLineState(environment) {
    override fun startProcess(): ProcessHandler {
        val commandLineBuilder = CommandLineBbBuilder(configuration, project)
        val commandLine = commandLineBuilder.build()
        this.addConsoleFilters(UrlFilter())
        this.addConsoleFilters(BbFileFilter(project))
        this.addConsoleFilters(BbTestFilter(project))
        this.addConsoleFilters(BbCompileFilter(project))
        val colored = ColoredProcessHandler(commandLine)
        colored.addProcessListener(BbProcessListener(project))
        colored.startNotify()

        val coverageService = ApplicationManager.getApplication().getService(CoverageService::class.java)
        val displayHandler = DisplayHandler(project)
        displayHandler.initializeMap()
        displayHandler.updateDisplays()
        coverageService.displayHandler = displayHandler

        project.messageBus.connect(project).subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, CodeCoverageFileListener(displayHandler))

//        EditorFactory.getInstance().eventMulticaster.addDocumentListener(CoverageDisplay())

        return colored
    }
}