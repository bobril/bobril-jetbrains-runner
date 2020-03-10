package configuration.annotators

import com.intellij.coverage.CoverageHelper
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import services.CoverageService

class CodeCoverageAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val coverageService = ServiceManager.getService(CoverageService::class.java)
        val ranges = coverageService.getCoverage(element.containingFile.virtualFile.path)?.ranges?: return

        val textLines = element.text.split("\n")

        for (rangeIdx in ranges.indices) {

            val startLineIdx = ranges[rangeIdx + 1]
            val endLineIdx = ranges[rangeIdx + 3]
            val startCharIdx = ranges[rangeIdx + 2]
            val endCharIdx = ranges[rangeIdx + 4]

            val startLine = textLines[startLineIdx - 1]
            val lineOffset = StringUtil.lineColToOffset(element.text, startLineIdx - 1, 0)
            val endOffset = lineOffset + startLine.length - 1 // lineOffset + line.length - 1
            val startOffset = lineOffset + startCharIdx - 1
            val range = TextRange(
                startOffset,
                if (endOffset > startOffset) endOffset else startOffset + 1
            )

            val annotation = holder.createAnnotation(HighlightSeverity.ERROR, range, "")
            annotation.setNeedsUpdateOnTyping(true)
        }
    }
}