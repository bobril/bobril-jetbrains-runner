package configuration.annotators

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiElement
import services.JasmineService

class JasmineAnnotator : Annotator {
    private val filters : List<JasmineFilter> = listOf(
        JasmineFilter("(?<=Expected)(.*)"),
        JasmineFilter("(?<=Error:)(.*)")
    )

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val jasmineService = ApplicationManager.getApplication().getService(JasmineService::class.java)
        val jasmineData = jasmineService.getJasmineData()?: return
        val nameRegex = Regex(element.containingFile.name);
        val textLines = element.text.split("\n")

        jasmineData.failures.forEach{ failure ->
            if (failure.fileName.contains(nameRegex) && textLines.size >= failure.line) {
                val line = textLines[failure.line- 1]

                filters.forEach filter@{
                    if (it.apply(line, failure, element, holder)) {
                        return@filter
                    }
                }
            }
        }
    }
}