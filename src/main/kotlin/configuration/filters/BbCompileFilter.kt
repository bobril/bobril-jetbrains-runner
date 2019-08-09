package configuration.filters

import com.intellij.execution.filters.Filter
import com.intellij.execution.filters.RegexpFilter
import com.intellij.openapi.editor.markup.EffectType
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.project.Project
import java.awt.Color
import java.util.regex.Pattern

class BbCompileFilter(project: Project) : RegexpFilter(project,
    CONSOLE_FILTER_REGEXP
) {
    private val buildErrorsPattern = Pattern.compile("(\\d+)?(no)?(?= errors)", Pattern.MULTILINE)
    private val buildWarningsPattern = Pattern.compile("(\\d+)?(no)?(?= warnings)", Pattern.MULTILINE)

    override fun applyFilter(line: String, entireLength: Int): Filter.Result? {
        val errorsMatcher = buildErrorsPattern.matcher(line)
        val warningsMatcher = buildWarningsPattern.matcher(line)
        if (!errorsMatcher.find() || !warningsMatcher.find()) {
            return null
        }

        val color = when {
            errorsMatcher.group(2) != "no" -> Color.RED
            warningsMatcher.group(2) != "no" -> Color.ORANGE
            else -> Color.GREEN
        }

        val highlightStartOffset = entireLength - line.length
        val highlightEndOffset = highlightStartOffset + line.length
        return Filter.Result(
            highlightStartOffset,
            highlightEndOffset,
            null,
            TextAttributes(color, null,  null, EffectType.SEARCH_MATCH, 0))
    }

    companion object {
        private const val CONSOLE_FILTER_REGEXP = RegexpFilter.FILE_PATH_MACROS + "\\(" + RegexpFilter.LINE_MACROS + "," + RegexpFilter.COLUMN_MACROS + "\\):"
    }
}