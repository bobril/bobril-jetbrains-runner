package coverage

import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import java.awt.Color

class ErrorStripeMarkHighlighter : Highlighter {
    override fun highlight(
        rangeHighlighter: RangeHighlighter,
        textAttributes: TextAttributes,
        color: Color,
        executed: Int
    ) {
        rangeHighlighter.errorStripeMarkColor = color
        rangeHighlighter.errorStripeTooltip =
            if (executed > 0) "Line executed $executed times" else "Line was not executed"
    }
}