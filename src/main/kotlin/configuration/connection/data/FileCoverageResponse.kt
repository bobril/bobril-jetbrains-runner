package configuration.connection.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
open class FileCoverageResponse {
    var status: String = "Unknown"
    var ranges: Array<Int>? = null

    override fun toString(): String {
        return "FileCoverageResponse: $status, ${ranges?.joinToString()}"
    }
}