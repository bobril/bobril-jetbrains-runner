package configuration.connection.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class CompilationFinishedData {
    var errors: Int = 0
    var warnings: Int = 0
    var time: Float = 0f
    var messages: List<CompilationFinishedMessage>? = null
}