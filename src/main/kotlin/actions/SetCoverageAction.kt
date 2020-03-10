package actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Constraints
import com.intellij.openapi.components.ServiceManager
import services.BbClientService
import services.CoverageService

class SetCoverageAction(private val bbActionGroup: BbActionGroup) : AnAction("Set Coverage") {
    private var lineCoverageAction = SetLineCoverageAction()

    override fun actionPerformed(e: AnActionEvent) {
        val coverageService = ServiceManager.getService(CoverageService::class.java)
        coverageService.toggleIsCoverageShown()
        val bbService = ServiceManager.getService(BbClientService::class.java)

        bbService.getBbClient()?.setCoverage(coverageService.isCoverageShown)

        coverageService.displayHandler?.updateDisplays()

        e.presentation.text = if (coverageService.isCoverageShown) "Hide Code Coverage" else "Show Code Coverage"

        if (coverageService.isCoverageShown) {
            bbActionGroup.addAction(lineCoverageAction, Constraints.LAST)
        } else {
            bbActionGroup.remove(lineCoverageAction)
        }
    }
}