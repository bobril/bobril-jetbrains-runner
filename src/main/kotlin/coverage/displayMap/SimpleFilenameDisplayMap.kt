package coverage.displayMap

import coverage.CoverageDisplay
import java.util.*

class SimpleFilenameDisplayMap: FilenameDisplayMap {
    private val map = HashMap<String, CoverageDisplay>()

    override fun add(filename: String, display: CoverageDisplay) {
        map[filename] = display
    }

    override fun remove(filename: String) {
        map.remove(filename)
    }

    override fun get(filename: String): CoverageDisplay? {
        return map[filename]
    }
}