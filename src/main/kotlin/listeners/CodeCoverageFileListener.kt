package listeners

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile
import coverage.DisplayHandler

class CodeCoverageFileListener(private val displayHandler: DisplayHandler): FileEditorManagerListener {
    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        super.fileOpened(source, file)
        val selectedTextEditor = source.selectedTextEditor?: return
        displayHandler.addDisplayForEditor(selectedTextEditor, file)
    }

    override fun fileClosed(source: FileEditorManager, file: VirtualFile) {
        super.fileClosed(source, file)
        displayHandler.removeDisplayForFile(file)
    }
}