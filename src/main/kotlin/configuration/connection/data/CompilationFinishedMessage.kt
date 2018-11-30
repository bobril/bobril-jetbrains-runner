package configuration.connection.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class CompilationFinishedMessage {
    lateinit var fileName: String
    lateinit var text: String
    var isError: Boolean = false
    var code: Int = 0
    lateinit var pos: List<Int>

    override fun toString(): String {
        return "$fileName: $text"
    }
}