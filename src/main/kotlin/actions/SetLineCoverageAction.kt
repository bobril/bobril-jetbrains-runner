package actions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import services.CoverageService

class SetLineCoverageAction() : AnAction("Show Line Coverage") {
    override fun actionPerformed(e: AnActionEvent) {
        val coverageService = ApplicationManager.getApplication().getService(CoverageService::class.java)
        coverageService.toggleIsLineCoverageShown()
        e.presentation.text = if (coverageService.isLineCoverageShown) "Hide Line Coverage" else "Show Line Coverage"
        e.presentation.icon = if (coverageService.isLineCoverageShown) AllIcons.Actions.Checked else null

        coverageService.displayHandler?.updateDisplays()
    }
}