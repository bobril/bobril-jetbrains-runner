package services.impl

import configuration.connection.data.FileCoverageResponse
import coverage.DisplayHandler
import services.CoverageService

class CoverageServiceImpl : CoverageService {
    private var data = HashMap<String, FileCoverageResponse?>()
    private var _isCoverageShown = false
    private var _isLineCoverageShown = false
    override var displayHandler: DisplayHandler? = null

    override fun getCoverage(filePath: String): FileCoverageResponse? {
        return data[filePath]
    }

    override fun toggleIsCoverageShown() {
        _isCoverageShown = !_isCoverageShown
    }

    override fun toggleIsLineCoverageShown() {
        _isLineCoverageShown = !_isLineCoverageShown
    }

    override val isCoverageShown: Boolean
        get() = _isCoverageShown

    override val isLineCoverageShown: Boolean
        get() = _isLineCoverageShown

    override fun setCoverage(filePath: String, coverage: FileCoverageResponse?) {
        this.data[filePath] = coverage
    }
}