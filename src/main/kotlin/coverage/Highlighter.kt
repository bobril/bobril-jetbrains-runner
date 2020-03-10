package coverage

import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import java.awt.Color

interface Highlighter {
    fun highlight(rangeHighlighter: RangeHighlighter, textAttributes: TextAttributes, color: Color, executed: Int)
}