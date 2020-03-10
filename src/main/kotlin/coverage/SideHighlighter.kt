package coverage

import com.intellij.openapi.editor.markup.LineMarkerRenderer
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import java.awt.Color

class SideHighlighter: Highlighter {
    override fun highlight(
        rangeHighlighter: RangeHighlighter,
        textAttributes: TextAttributes,
        color: Color,
        executed: Int
    ) {
        rangeHighlighter.lineMarkerRenderer =
            LineMarkerRenderer { editor, graphics, rectangle ->
                val origColor = graphics.color
                try {
                    graphics.color = color
                    val lineHeight = editor.lineHeight
                    graphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height + lineHeight)
                    graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height + lineHeight)
                } finally {
                    graphics.color = origColor
                }
            }
    }
}