package coverage

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.vfs.VirtualFile
import services.BbClientService
import services.CoverageService
import java.awt.Color

class CoverageDisplay(private val editor: Editor) : DocumentListener {
    private val coverageHighlighter = CoverageHighlighter(editor)

    override fun beforeDocumentChange(event: DocumentEvent) {
        super.beforeDocumentChange(event)
        clear()
    }

    fun redrawFile(file: VirtualFile) {
        ApplicationManager.getApplication().invokeLater {
            redrawFileInternal(file)
        }
    }

    private fun redrawFileInternal(file: VirtualFile) {
        clearInternal()

        val bbService = ServiceManager.getService(BbClientService::class.java)
        val client = bbService.getBbClient()?: return
        val coverage = client.getCoverage(file.path)

        println("coverage: $coverage")
        val coverageService = ServiceManager.getService(CoverageService::class.java)
        coverageService.setCoverage(file.path, coverage)

        val ranges = coverage?.ranges?: return

        var rangeIdx = 0

        while(rangeIdx < ranges.size) {
            val type = ranges[rangeIdx]
            val startLineIdx = ranges[rangeIdx + 1]
            val endLineIdx = ranges[rangeIdx + 3]
            val startCharIdx = ranges[rangeIdx + 2]
            val endCharIdx = ranges[rangeIdx + 4]
            val executed = ranges[rangeIdx + 5]
            val executedCondition = if (ranges.size > rangeIdx + 6) ranges[rangeIdx + 6] else 0

            var covType = 0
            if (ranges[rangeIdx] != 1) {
                if (executed != 0) {
                    covType = 2
                }
            } else {
                if (executed != 0) {
                    covType++
                }

                if (executedCondition != 0) {
                    covType++
                }
            }

            val color = when (covType) {
                0 -> {
                    Color.RED
                }
                1 -> {
                    Color.ORANGE
                }
                else -> {
                    Color.GREEN
                }
            }

            coverageHighlighter.highlightLines(
                Color(color.red, color.green, color.blue, 126),
                startLineIdx,
                endLineIdx,
                startCharIdx,
                endCharIdx,
                executed
            )

            rangeIdx += if (type != 1) 6 else 7
        }
    }

    fun clear() {
        ApplicationManager.getApplication().invokeLater {
            clearInternal()
        }
    }

    private fun clearInternal() {
        coverageHighlighter.clear()
    }
}