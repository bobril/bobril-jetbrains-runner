package tools.listeners

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import java.awt.event.MouseEvent
import java.io.File
import javax.swing.JList

abstract class MouseListClickedListener<T>(private val list: JList<T>): MouseClickedListener() {
    override fun mouseClicked(e: MouseEvent?) {
        if (e == null || list.selectedIndex == -1) {
            return
        }

        onClick(e)
    }

    protected fun openFile(project: Project, filePathRelative: String, line: Int, col: Int) {
        val file = LocalFileSystem.getInstance().findFileByIoFile(File(project.basePath + "/" +filePathRelative))?: return

        ApplicationManager.getApplication().invokeAndWait({
            OpenFileDescriptor(project, file, line - 1, col - 1).navigate(true)
        }, ModalityState.NON_MODAL)
    }
}