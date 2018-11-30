package tools

import com.intellij.openapi.project.Project
import java.awt.event.MouseEvent
import javax.swing.JList

class TestsListSelectionListener(
    private val project: Project,
    private val testsListModel: TestsListModel,
    private val list: JList<String>): MouseClickedListener<String>(list) {

    override fun onClick(e: MouseEvent) {
        val failureStack = testsListModel.getFailureStack(list.selectedIndex)?: return

        println("Clicked:" + list.selectedIndex + "-" + failureStack.fileName)

        openFile(project, failureStack.fileName, failureStack.lineNumber, failureStack.columnNumber)
    }
}