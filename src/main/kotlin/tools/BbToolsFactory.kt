package tools

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class BbToolsFactory : ToolWindowFactory {
    // Create the tool window content.
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val bbTools = BbTools()
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(bbTools.content, "", false)
        toolWindow.contentManager.addContent(content)
    }
}