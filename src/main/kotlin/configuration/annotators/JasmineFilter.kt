package configuration.annotators

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import configuration.Failure
import java.util.regex.Pattern

class JasmineFilter(regex: String) {
    private val pattern: Pattern = Pattern.compile(regex)

    fun apply(line: String, it: Failure, element: PsiElement, holder: AnnotationHolder): Boolean {
        val expectedMatch = pattern.matcher(it.message)
        if (expectedMatch.find()) {
            setError(it.message, element, it, line, holder)
            return true
        }

        return false
    }

    private fun setError(
        error: String,
        element: PsiElement,
        it: Failure,
        line: String,
        holder: AnnotationHolder
    ) {
        val lineOffset = StringUtil.lineColToOffset(element.text, it.line - 1, 0)
        val endOffset = lineOffset + line.length - 1
        val startOffset = lineOffset + it.col - 1
        val range = TextRange(
            startOffset,
            if (endOffset > startOffset) endOffset else startOffset + 1
        )

        val annotation = holder.createErrorAnnotation(range, error)
        annotation.setNeedsUpdateOnTyping(true)
    }
}