package configuration.connection.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class AgentData: SuiteData() {
    var testsFailed: Int = 0
    var testsSkipped: Int = 0
    var testsFinished: Int = 0
    var totalTests: Int = 0
}