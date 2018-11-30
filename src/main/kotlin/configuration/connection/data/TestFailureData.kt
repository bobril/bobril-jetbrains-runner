package configuration.connection.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class TestFailureData {
    lateinit var message: String
    lateinit var stack: List<TestStackData>
}