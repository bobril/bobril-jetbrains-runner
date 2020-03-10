package coverage

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import services.CoverageService
import java.awt.Color
import kotlin.math.max
import kotlin.math.min

class CoverageHighlighter(private val editor: Editor) {
    private val highlights = arrayListOf<RangeHighlighter>()

    fun highlightLines(color: Color, fromLine: Int, toLine: Int, fromChar: Int, toChar: Int, executed: Int) {
        val document = editor.document
        if (toLine <= document.lineCount) {
            val attributes = TextAttributes()
            val highlighter = createRangeHighlighter(fromLine, toLine, fromChar, toChar, attributes)
            val coverageService = ServiceManager.getService(CoverageService::class.java)

            val highlighters = arrayListOf(SideHighlighter(), ErrorStripeMarkHighlighter())
            if (coverageService.isLineCoverageShown) {
                highlighters.add(LineHighlighter())
            }

            highlighters.forEach { it.highlight(highlighter, attributes, color, executed) }
            highlights.add(highlighter)
        }
    }

    fun clear() {
        val model = editor.markupModel
        for (rangeHighlighter in highlights) {
            model.removeHighlighter(rangeHighlighter)
        }
        highlights.clear()
    }

    private fun createRangeHighlighter(
        fromLine: Int,
        toLine: Int,
        fromChar: Int,
        toChar: Int,
        attributes: TextAttributes
    ): RangeHighlighter {
        val document = editor.document
        val startOffset = getOffset(document, fromLine, fromChar)
        val endOffset = getOffset(document, toLine, toChar)

        return editor.markupModel.addRangeHighlighter(
            startOffset, endOffset, 3333, attributes, HighlighterTargetArea.EXACT_RANGE
        )
    }

    private fun getOffset(document: Document, line: Int, charOffset: Int): Int {
        val toLineStartOffset = document.getLineStartOffset(max(0, line))
        val toLineEndOffset = document.getLineEndOffset(max(0, line))
        val toLineLength = toLineEndOffset - toLineStartOffset
        return toLineStartOffset + min(toLineLength, charOffset)
    }
}