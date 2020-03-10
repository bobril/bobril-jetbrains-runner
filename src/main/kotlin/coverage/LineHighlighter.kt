package coverage

import com.intellij.openapi.editor.markup.EffectType
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import java.awt.Color

class LineHighlighter : Highlighter {
    override fun highlight(
        rangeHighlighter: RangeHighlighter,
        textAttributes: TextAttributes,
        color: Color,
        executed: Int
    ) {
        textAttributes.effectColor = color
        textAttributes.effectType = EffectType.BOLD_LINE_UNDERSCORE
    }
}