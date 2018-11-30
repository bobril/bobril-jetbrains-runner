package configuration.connection.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
open class TestData {
    var agents: List<AgentData>? = null
}