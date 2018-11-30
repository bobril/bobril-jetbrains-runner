package configuration.connection.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
open class BbMessage<T> {
    lateinit var m: String
    var d: T? = null
}