package configuration

import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import configuration.connection.BbClient
import services.BbService
import java.util.*
import java.util.regex.Pattern

class BbProcessListener(private val project: Project): ProcessListener {
    private val bbMessageHandler = BbMessageHandler(project)
    private val bbService: BbService = ServiceManager.getService(BbService::class.java)
    private var timer: Timer? = null
    private var bbClient: BbClient? = null

    override fun processTerminated(event: ProcessEvent) {
        println("processTerminated : ${event.text}")
    }

    override fun processWillTerminate(event: ProcessEvent, willBeDestroyed: Boolean) {
        println("processWillTerminate : ${event.text}")
        bbClient?.terminateConnection()
        timer?.cancel()
        bbService.getBbTools()?.setCompileStopped()
    }

    override fun startNotified(event: ProcessEvent) {
        println("startNotified : ${event.text}")
        bbService.getBbTools()?.setCompileStarted()
    }

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        println("onTextAvailable : ${event.text}")
        val matcher = PATTERN.matcher(event.text)
        if (matcher.find()) {
            val bbService = ServiceManager.getService(BbService::class.java)
            bbService?.getBbTools()?.setBobrilUrl(matcher.group(0))
            val bbClient = BbClient("${matcher.group(0)}bb/api/main")
            this.bbClient = bbClient
            bbMessageHandler.handleClient(bbClient)
            timer = Timer()
            timer?.schedule(bbClient, 0, 3000)
        }
    }

    companion object {
        val PATTERN: Pattern = Pattern.compile("(http.*):(\\d*)\\/")
    }
}