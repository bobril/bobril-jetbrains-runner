package configuration.connection.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
open class SuiteData {
    lateinit var name: String
    var isSuite: Boolean = false
    var failure: Boolean = false
    var duration: Float = 0f
    lateinit var failures: List<TestFailureData>
    var nested: List<SuiteData>? = null

    override fun toString(): String {
        return name
    }
}