package configuration.filters

import com.intellij.execution.filters.Filter
import com.intellij.execution.filters.HyperlinkInfo
import com.intellij.execution.filters.OpenFileHyperlinkInfo
import com.intellij.execution.filters.RegexpFilter
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import java.io.File
import java.util.regex.Pattern

class BbFileFilter(private val project: Project) : RegexpFilter(project,
    CONSOLE_FILTER_REGEXP
) {
    private val filePatter = Pattern.compile("([0-9 \\- a-z_A-Z./]+)\\((\\d+),(\\d+)\\)", Pattern.MULTILINE)

    override fun applyFilter(line: String, entireLength: Int): Filter.Result? {

        val matcher = filePatter.matcher(line)
        if (!matcher.find()) {
            return null
        }

        val filePath = matcher.group(1) ?: return null
        val lineNumber = matcher.group(2)
        val columnNumber = matcher.group(3)

        var line1 = 0
        var column = 0
        try {
            line1 = Integer.parseInt(lineNumber)
            column = Integer.parseInt(columnNumber)
        } catch (e: NumberFormatException) {
            // Do nothing, so that line and column will remain at their initial zero values.
        }

        if (line1 > 0) line1 -= 1
        if (column > 0) column -= 1
        // Calculate the offsets relative to the entire text.
        val highlightStartOffset = entireLength - line.length
        val highlightEndOffset = highlightStartOffset + filePath.length + lineNumber.length + columnNumber.length + 5
        val info = createOpenFileHyperlink(filePath, line1, column)
        return Filter.Result(highlightStartOffset, highlightEndOffset, info)
    }

    override fun createOpenFileHyperlink(fileName: String?, line: Int, column: Int): HyperlinkInfo? {
        if (fileName == null) {
            return null
        }

        val file = LocalFileSystem.getInstance().findFileByIoFile(File(fileName)) ?: return null

        return OpenFileHyperlinkInfo(project, file, line, column)
    }

    companion object {
        private const val CONSOLE_FILTER_REGEXP = RegexpFilter.FILE_PATH_MACROS + "\\(" + RegexpFilter.LINE_MACROS + "," + RegexpFilter.COLUMN_MACROS + "\\):"

    }
}