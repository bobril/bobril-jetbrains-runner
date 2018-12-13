package tools

import configuration.connection.data.*
import javax.swing.DefaultListModel

class TestsListModel: DefaultListModel<SuiteWithStack>() {
    private var agentData: AgentData? = null

    fun setFailureTests(agentData: AgentData) {
        this.clear()
        this.agentData = agentData
        if(agentData.failure) {
            traverseSuites(agentData, 0)
        }
    }

    private fun traverseSuites(suiteData: SuiteData, depth: Int) {
        var nextDepth = depth
        val offset = Array(depth) { SuiteWithStack.divider }.joinToString("")
        if (!suiteData.name.isEmpty()) {
            nextDepth++
            addElement(SuiteWithStack(suiteData, offset,TestRowType.SuiteData, suiteData.failures.firstOrNull(), suiteData.failures.firstOrNull()?.stack?.firstOrNull() ))
        }

        if (!suiteData.failures.isEmpty()) {
            suiteData.failures.forEach { failureData ->
                addElement(SuiteWithStack(suiteData, offset,TestRowType.Failure, failureData, failureData.stack.firstOrNull() ))
                failureData.stack.forEach{ addElement(SuiteWithStack(suiteData, offset,TestRowType.Stack, failureData, it )) }
            }
        } else {
            suiteData.nested?.filter { it.failure }?.forEach { traverseSuites(it, nextDepth) }
        }
    }
}