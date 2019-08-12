package configuration

import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import configuration.connection.BbClient
import configuration.messageHandlers.BbMessageHandler
import java.util.*
import java.util.regex.Pattern

class BbProcessListener(project: Project): ProcessListener {
    private val bbMessageHandler = BbMessageHandler(project)
    private var timer: Timer? = null
    private var bbClient: BbClient? = null

    override fun processTerminated(event: ProcessEvent) {
        println("processTerminated : ${event.text}")
    }

    override fun processWillTerminate(event: ProcessEvent, willBeDestroyed: Boolean) {
        println("processWillTerminate : ${event.text}")
        bbClient?.terminateConnection()
        timer?.cancel()
    }

    override fun startNotified(event: ProcessEvent) {
        println("startNotified : ${event.text}")
    }

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        val matcher = PATTERN.matcher(event.text)
        if (matcher.find()) {
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