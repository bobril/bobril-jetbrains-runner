package actions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Constraints
import com.intellij.openapi.application.ApplicationManager
import services.BbClientService
import services.CoverageService

class SetCoverageAction(private val bbActionGroup: BbActionGroup) : AnAction("Toggle Coverage") {
    private var lineCoverageAction = SetLineCoverageAction()

    override fun actionPerformed(e: AnActionEvent) {
        val coverageService = ApplicationManager.getApplication().getService(CoverageService::class.java)
        coverageService.toggleIsCoverageShown()

        if (coverageService.isCoverageShown) {
            bbActionGroup.addAction(lineCoverageAction, Constraints.LAST)
            e.presentation.icon = AllIcons.Actions.Checked
        } else {
            bbActionGroup.remove(lineCoverageAction)
            e.presentation.icon = null
        }

        val bbService = ApplicationManager.getApplication().getService(BbClientService::class.java)
        bbService.getBbClient()?.setCoverage(coverageService.isCoverageShown)
        coverageService.displayHandler?.updateDisplays()
    }
}