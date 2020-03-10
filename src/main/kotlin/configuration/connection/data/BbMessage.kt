package configuration.connection.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
open class BbMessage<T> {
    lateinit var m: String
    var d: T? = null

    constructor(message: String, data: T) {
        m = message
        d = data
    }

    constructor() {
    }
}