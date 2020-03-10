package configuration.connection

import java.net.HttpURLConnection
import java.net.URL

class LongPoolingClient(private val url: String) {

    fun start() {
        val connection = connect();
        val stream = connection.inputStream
        stream.read()
    }

    private fun connect(): HttpURLConnection {
        val url = URL(this.url)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.setRequestProperty("Connection", "keep-alive")
        connection.setRequestProperty("Content-Type", "application/json")
        return connection
    }
}