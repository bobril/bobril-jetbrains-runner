package configuration.connection.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class FocusPlaceData {
    lateinit var fn: String
    lateinit var pos: Array<Int>
}