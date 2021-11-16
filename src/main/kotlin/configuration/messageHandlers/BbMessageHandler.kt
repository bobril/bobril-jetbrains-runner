package configuration.messageHandlers

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import configuration.Failure
import configuration.JasmineData
import configuration.connection.BbClient
import configuration.connection.data.AgentData
import configuration.connection.data.FocusPlaceData
import configuration.connection.data.SuiteData
import services.CoverageService
import services.JasmineService
import java.io.File


class BbMessageHandler(private val project: Project) {
    private val jasmineData: JasmineData = JasmineData()
    private val bbService: JasmineService = ApplicationManager.getApplication().getService(JasmineService::class.java)

    init {
        bbService.setJasmineData(jasmineData)
    }

    fun handleClient(bbClient: BbClient) {
        bbClient.addOnCompilationStarted { onCompilationStarted() }
        bbClient.addOnCompilationFinished { onCompilationFinished() }
        bbClient.addTestUpdated { onTestUpdated(it) }
        bbClient.addOnFocusPlace { onFocusPlace(it) }
        bbClient.addCoverageUpdatedListener { onCoverageUpdated() }
    }

    private fun onCompilationStarted() {
        println("CompilationStarted")
    }

    private fun onCompilationFinished() {
        println("CompilationFinished")
    }

    private fun onTestUpdated(agentData: AgentData) {
        println("TestUpdated")
        this.setFailureTests(agentData)
        DaemonCodeAnalyzer.getInstance(project).restart()
    }

    private fun onFocusPlace(data: FocusPlaceData) {
        println("Focus: " + data.fn)
        val file = LocalFileSystem.getInstance().findFileByIoFile(File(data.fn))
        if (file != null) {
            ApplicationManager.getApplication().invokeAndWait({
                OpenFileDescriptor(project, file, data.pos[0] - 1, data.pos[1] - 1).navigate(true)
            }, ModalityState.NON_MODAL)
        }
    }

    private fun onCoverageUpdated() {
        println("Coverage updated")
        val coverageService = ApplicationManager.getApplication().getService(CoverageService::class.java)
        coverageService.displayHandler?.updateDisplays()
    }

    private fun setFailureTests(agentData: AgentData) {
        this.jasmineData.failures = ArrayList()
        if(agentData.failure) {
            traverseSuites(agentData)
        }
    }

    private fun traverseSuites(suiteData: SuiteData) {
        if (suiteData.failures.isNotEmpty()) {
            suiteData.failures.forEach { failureData ->
                failureData.stack.forEach {
                    if (it.fileName.contains(Regex(".spec"))) {
                        this.jasmineData.failures.add(
                            Failure(
                                it.fileName,
                                it.lineNumber,
                                it.columnNumber,
                                failureData.message
                            )
                        )
                    }
                }
            }
        } else {
            suiteData.nested?.filter { it.failure }?.forEach { traverseSuites(it) }
        }
    }
}