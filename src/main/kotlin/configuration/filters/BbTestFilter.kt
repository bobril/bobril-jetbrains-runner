package configuration.filters

import com.intellij.execution.filters.Filter
import com.intellij.execution.filters.HyperlinkInfo
import com.intellij.execution.filters.OpenFileHyperlinkInfo
import com.intellij.execution.filters.RegexpFilter
import com.intellij.openapi.editor.markup.EffectType
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import java.awt.Color
import java.io.File
import java.util.regex.Pattern

class BbTestFilter(project: Project) : RegexpFilter(project,
    CONSOLE_FILTER_REGEXP
) {
    private val testSuccessPattern = Pattern.compile("(Tests on)(.*)(Failed: )(\\d+)", Pattern.MULTILINE)

    override fun applyFilter(line: String, entireLength: Int): Filter.Result? {
        val matcher = testSuccessPattern.matcher(line)
        if (!matcher.find()) {
            return null
        }
        val isSuccess = matcher.group(4) == "0"
        val highlightStartOffset = entireLength - line.length
        val highlightEndOffset = highlightStartOffset + line.length
        return Filter.Result(
            highlightStartOffset,
            highlightEndOffset,
            null,
            TextAttributes(if (isSuccess) Color.GREEN else Color.RED,
                null,
                null,
                EffectType.SEARCH_MATCH, 0))
    }

    companion object {
        private const val CONSOLE_FILTER_REGEXP = RegexpFilter.FILE_PATH_MACROS + "\\(" + RegexpFilter.LINE_MACROS + "," + RegexpFilter.COLUMN_MACROS + "\\):"
    }
}