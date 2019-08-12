package services.impl

import configuration.JasmineData
import services.JasmineService

class JasmineServiceImpl : JasmineService {
    private var data: JasmineData? = null

    override fun getJasmineData(): JasmineData? {
        return data
    }

    override fun setJasmineData(data: JasmineData) {
        this.data = data
    }
}