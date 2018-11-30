package configuration.connection.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class TestStackData {
    var lineNumber: Int = 0
    var columnNumber: Int = 0
    lateinit var fileName: String
    lateinit var functionName: String
    lateinit var args: List<String>
}