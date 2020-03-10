package actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.ServiceManager
import services.BbClientService
import services.CoverageService

class SetLineCoverageAction() : AnAction("Show Line Coverage") {
    override fun actionPerformed(e: AnActionEvent) {
        val coverageService = ServiceManager.getService(CoverageService::class.java)
        coverageService.toggleIsLineCoverageShown()
        e.presentation.text = if (coverageService.isLineCoverageShown) "Hide Line Coverage" else "Show Line Coverage"

        coverageService.displayHandler?.updateDisplays()
    }
}