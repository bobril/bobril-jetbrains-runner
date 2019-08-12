package services

import com.intellij.openapi.components.NamedComponent
import configuration.JasmineData

interface JasmineService: NamedComponent {
    fun setJasmineData(data: JasmineData)
    fun getJasmineData(): JasmineData?
}