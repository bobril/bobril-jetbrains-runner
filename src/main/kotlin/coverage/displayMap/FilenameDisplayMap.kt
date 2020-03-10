package coverage.displayMap

import coverage.CoverageDisplay

interface FilenameDisplayMap {
    fun add(filename: String, display: CoverageDisplay)

    fun remove(filename: String)

    operator fun get(filename: String): CoverageDisplay?
}