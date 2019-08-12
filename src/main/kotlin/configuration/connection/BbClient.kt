package configuration.connection

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import configuration.connection.data.AgentData
import configuration.connection.data.CompilationFinishedData
import configuration.connection.data.FocusPlaceData
import configuration.connection.data.TestData
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class BbClient(private val url: String): TimerTask() {
    private var connection: HttpURLConnection? = null
    private var id: String? = null
    private val onCompilationStartedListeners = ArrayList<() -> Unit>()
    private val onCompilationFinishedListeners = ArrayList<(data: CompilationFinishedData) -> Unit>()
    private val onFocusPlaceListeners = ArrayList<(data: FocusPlaceData) -> Unit>()
    private val onTestUpdatedListeners = ArrayList<(data: AgentData) -> Unit>()
    private var connectionActive: Boolean = false

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

    fun terminateConnection() {
        this.connectionActive = false
        this.connection = null
    }

    override fun run() {
        if (connectionActive) {
            return
        }

        try {
            connectionActive = true
            while (connectionActive) {
                val connection = connect()?: return

                val response = getResponse(connection)

                processResponse(response)

                connection.disconnect()
            }
        } catch(exception: Exception) {
            println("Error:" + exception.message)
            exception.printStackTrace()
        } finally {
            this.connection = null
            connectionActive = false
        }
    }

    private fun connect(): HttpURLConnection? {
        val url = URL(this.url)

        this.connection = url.openConnection() as HttpURLConnection
        val connection = this.connection ?: return null
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Connection", "keep-alive")
        connection.setRequestProperty("Content-Type", "application/json")
        val wr = DataOutputStream(connection.outputStream)
        wr.writeBytes("{ \"id\": \"${this.id?:""}\"}")
        wr.flush()
        wr.close()

        val responseCode = connection.responseCode

        if (responseCode < 200 || responseCode >= 300) {
            println("Response Code : $responseCode")
            connection.disconnect()
            connectionActive = false
            this.connection = null
            this.id = null
            return null
        }

        return connection
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
                }
            }
        }
    }
}







