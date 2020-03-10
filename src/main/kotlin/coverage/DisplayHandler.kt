package coverage

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.util.*

class DisplayHandler(private val project: Project) {
    private val map = HashMap<VirtualFile, CoverageDisplay>()

    fun initializeMap() {
        for (file in FileEditorManager.getInstance(project).openFiles) {
            var editor: Editor? = null
            for (fileEditor in FileEditorManager.getInstance(project).getEditors(file)) {
                if (fileEditor is TextEditor) {
                    editor = fileEditor.editor
                    break
                }
            }
            if (editor != null) {
                addDisplayForEditor(editor, file)
            }
        }
    }

    fun updateDisplays() {
        map.forEach{
            it.value.redrawFile(it.key)
        }
    }

    fun addDisplayForEditor(editor: Editor, file: VirtualFile) {
        removeDisplayForFile(file)
        val display = CoverageDisplay(editor)
        editor.document.addDocumentListener(display)
        display.redrawFile(file)
        this.map[file] = display
    }

    fun removeDisplayForFile(file: VirtualFile) {
        this.map[file]?.clear()
        this.map.remove(file)
    }
}