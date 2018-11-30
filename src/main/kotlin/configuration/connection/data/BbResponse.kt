package configuration.connection.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class BbResponse<T> {
    lateinit var id: String
    lateinit var m: Array<BbMessage<T>>
    var close: Boolean? = null
}