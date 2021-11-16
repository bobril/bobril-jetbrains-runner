package actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.application.ApplicationManager
import services.BbClientService

class BbActionGroup(): DefaultActionGroup() {
    override fun update(e: AnActionEvent) {
        super.update(e)
        val bbService = ApplicationManager.getApplication().getService(BbClientService::class.java)
        e.presentation.isEnabledAndVisible = bbService.getBbClient() != null

        if (this.childActionsOrStubs.isEmpty()) {
            addAction(SetCoverageAction(this))
        }
    }
}