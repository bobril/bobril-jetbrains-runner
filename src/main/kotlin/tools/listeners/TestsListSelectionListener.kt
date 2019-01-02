package tools.listeners

import com.intellij.openapi.project.Project
import tools.SuiteWithStack
import java.awt.event.MouseEvent
import javax.swing.JList

class TestsListSelectionListener(
    private val project: Project,
    private val list: JList<SuiteWithStack>): MouseListClickedListener<SuiteWithStack>(list) {

    override fun onClick(e: MouseEvent) {
        val failureStack = list.selectedValue?.testStackData?: return

        println("Clicked:" + list.selectedIndex + "-" + failureStack.fileName)

        openFile(project, failureStack.fileName, failureStack.lineNumber, failureStack.columnNumber)
    }
}