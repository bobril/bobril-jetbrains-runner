package configuration

import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import configuration.connection.BbClient
import java.io.File
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import configuration.connection.data.AgentData
import configuration.connection.data.CompilationFinishedData
import configuration.connection.data.FocusPlaceData
import services.BbService


class BbMessageHandler(private val project: Project) {
    private val bbService: BbService = ServiceManager.getService(BbService::class.java)

    fun handleClient(bbClient: BbClient) {
        bbClient.addOnCompilationStarted { onCompilationStarted() }
        bbClient.addOnCompilationFinished { onCompilationFinished(it) }
        bbClient.addTestUpdated { onTestUpdated(it) }
        bbClient.addOnFocusPlace { onFocusPlace(it) }
    }

    private fun onCompilationStarted() {
        val bbTools = bbService.getBbTools()?: return
        bbTools.setCompileStarted()
        println("CompilationStarted")
    }

    private fun onCompilationFinished(data: CompilationFinishedData) {
        val bbTools = bbService.getBbTools()?: return
        bbTools.setCompilationFinished(data)
        println("CompilationFinished")
    }

    private fun onTestUpdated(agentData: AgentData) {
        val bbTools = bbService.getBbTools()?: return
        bbTools.setTestUpdatedFinished(agentData)
        println("TestUpdated")
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
}