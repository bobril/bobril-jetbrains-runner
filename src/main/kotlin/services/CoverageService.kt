package services

import com.intellij.openapi.components.NamedComponent
import configuration.connection.data.FileCoverageResponse
import coverage.DisplayHandler

interface CoverageService: NamedComponent {
    val isCoverageShown: Boolean
    val isLineCoverageShown: Boolean
    var displayHandler: DisplayHandler?
    fun setCoverage(filePath: String, coverage: FileCoverageResponse?)
    fun getCoverage(filePath: String): FileCoverageResponse?
    fun toggleIsCoverageShown()
    fun toggleIsLineCoverageShown()
}