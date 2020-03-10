package configuration.connection

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import configuration.connection.data.*
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class BbClient(private val host: String): TimerTask() {
    private var longPoolingConnection: HttpURLConnection? = null
    private var id: String? = null
    private val onCompilationStartedListeners = ArrayList<() -> Unit>()
    private val onCompilationFinishedListeners = ArrayList<(data: CompilationFinishedData) -> Unit>()
    private val onFocusPlaceListeners = ArrayList<(data: FocusPlaceData) -> Unit>()
    private val onTestUpdatedListeners = ArrayList<(data: AgentData) -> Unit>()
    private val onCoverageUpdatedListeners = ArrayList<() -> Unit>()
    private var connectionActive: Boolean = false
    private var messages = ArrayList<BbMessage<BbMessageValue>>()

    fun addOnCompilationStarted(listener: () -> Unit) {
        onCompilationStartedListeners.add(listener)
    }

    fun addOnCompilationFinished(listener: (data: CompilationFinishedData) -> Unit) {
        onCompilationFinishedListeners.add(listener)
    }

    fun addOnFocusPlace(listener: (data: FocusPlaceData) -> Unit) {
        onFocusPlaceListeners.add(listener)
    }

    fun addTestUpdated(listener: (data: AgentData) -> Unit) {
        onTestUpdatedListeners.add(listener)
    }

    fun addCoverageUpdatedListener(listener: () -> Unit) {
        onCoverageUpdatedListeners.add(listener)
    }

    fun terminateConnection() {
        this.connectionActive = false
        this.longPoolingConnection = null
    }

    override fun run() {
        if (connectionActive) {
            return
        }

        try {
            connectionActive = true
            while (connectionActive) {

                val messages = arrayListOf<BbMessage<BbMessageValue>>()
                messages.addAll(this.messages)
                this.messages.clear()

                val connection = this.createConnection("main")
                longPoolingConnection = connection
                this.post(connection, if (messages.size > 0) BbRequest(this.id?: "", messages) else SimpleBbRequest(this.id?: ""))

                val response = getResponse(connection)
                val responseCode = connection.responseCode

                if (responseCode < 200 || responseCode >= 300) {
                    println("Response Code : $responseCode")
                    connectionActive = false
                    this.id = null
                    break
                }

                processResponse(response)

                connection.disconnect()
            }
        } catch(exception: Exception) {
            println("Error:" + exception.message)
            exception.printStackTrace()
        } finally {
            this.longPoolingConnection?.disconnect()
            this.longPoolingConnection = null
            connectionActive = false
        }
    }

    fun setCoverage(value: Boolean) {
        messages.add(BbMessage("setCoverage", BbMessageValue(value)))
        this.longPoolingConnection?.disconnect()
    }

    fun getCoverage(file: String): FileCoverageResponse? {
        try {
            val connection = this.createConnection("getFileCoverage")
            this.post(connection, GetFileCoverageRequest(file))

            if (connection.responseCode < 200 || connection.responseCode >= 300) {
                return null
            }

            val response = getResponse(connection)
            val objectMapper = ObjectMapper()
            return objectMapper.readValue<FileCoverageResponse>(response, FileCoverageResponse::class.java)
        } catch (exception: Exception) {
            println("Error:" + exception.message)
            exception.printStackTrace()
        }

        return null
    }

    private fun createConnection(endPoint: String): HttpURLConnection {
        val url = URL(this.host + "bb/api/" + endPoint)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Connection", "keep-alive")
        connection.setRequestProperty("Content-Type", "application/json")
        return connection
    }

    private fun post(connection: HttpURLConnection, data: Any) {
        val objectMapper = ObjectMapper()

        val wr = DataOutputStream(connection.outputStream)
        wr.writeBytes(objectMapper.writeValueAsString(data))
        wr.flush()
        wr.close()

        val responseCode = connection.responseCode

        if (responseCode < 200 || responseCode >= 300) {
            println("Response Code : $responseCode")
            connection.disconnect()
        }
    }

    private fun getResponse(connection: HttpURLConnection): String {
        val reader = BufferedReader(
            InputStreamReader(connection.inputStream)
        )
        var inputLine = reader.readLine()
        val response = StringBuffer()

        while (inputLine != null) {
            response.append(inputLine)
            inputLine = reader.readLine()

        }

        reader.close()

        return response.toString()
    }

    private fun processResponse(response: String) {
        val objectMapper = ObjectMapper()

        val bbResponse1 = objectMapper.readTree(response)
        this.id = bbResponse1.get("id").asText()
        val messageNodes = bbResponse1.get("m")
        if (bbResponse1.has("close") && bbResponse1.get("close").asBoolean(false)) {
            this.id = null
            return
        }

        if (messageNodes != null && messageNodes.isArray) {
            (messageNodes as ArrayNode).forEach {
                val messageType = it.get("m").asText()
                val messageDataNode = it.get("d")

                when (messageType) {
                    "compilationStarted" -> onCompilationStartedListeners.forEach { it() }
                    "compilationFinished" -> {
                        val compilationFinishedData = objectMapper.convertValue(messageDataNode, CompilationFinishedData::class.java)
                        if (compilationFinishedData != null) {
                            onCompilationFinishedListeners.forEach {  it(compilationFinishedData) }
                        }
                    }
                    "focusPlace" -> {
                        val focusPlaceData  = objectMapper.convertValue(messageDataNode, FocusPlaceData::class.java)
                        if (focusPlaceData != null) {
                            onFocusPlaceListeners.forEach {  it(focusPlaceData) }
                        }
                    }
                    "testUpdated" -> {
                        val agentData =  objectMapper.convertValue(messageDataNode, TestData::class.java)?.agents?.firstOrNull()
                        if (agentData != null) {
                            onTestUpdatedListeners.forEach {  it(agentData) }
                        }
                    }
                    "coverageUpdated" -> {
                        onCoverageUpdatedListeners.forEach {  it() }
                    }
                }
            }
        }
    }
}

private class GetFileCoverageRequest(val fileName: String)
private class BbRequest(val id: String, val m: ArrayList<BbMessage<BbMessageValue>>?)
private class SimpleBbRequest(val id: String)
private class BbMessageValue(val value: Boolean)





