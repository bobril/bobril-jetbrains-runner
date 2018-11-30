package tools

import configuration.connection.data.*
import javax.swing.DefaultListModel

class SuiteWithStack(val suiteData: SuiteData, val testStackData: TestStackData?)

class TestsListModel: DefaultListModel<String>() {
    private var agentData: AgentData? = null
    private val failureTestPath: ArrayList<SuiteWithStack> = ArrayList()

    fun setFailureTests(agentData: AgentData) {
        this.clear()
        failureTestPath.clear()
        this.agentData = agentData
        if(agentData.failure) {
            traverseSuites(agentData, 0)
        }
    }

    fun getFailureStack(rowIndex: Int): TestStackData? {
        if (failureTestPath.size < rowIndex) {
            return null
        }

        return failureTestPath[rowIndex].testStackData
    }

    private fun traverseSuites(suiteData: SuiteData, depth: Int) {
        var nextDepth = depth
        val offset = Array(depth) { divider }.joinToString("")
        if (!suiteData.name.isEmpty()) {
            nextDepth++
            addElement(offset + suiteData)
            failureTestPath.add(SuiteWithStack(suiteData, suiteData.failures.firstOrNull()?.stack?.firstOrNull()))
        }

        if (!suiteData.failures.isEmpty()) {
            suiteData.failures.forEach { failureData ->
                addElement(offset + divider + failureData.message)
                failureTestPath.add(SuiteWithStack(suiteData, failureData.stack.firstOrNull()))
                failureData.stack.forEach{
                    addElement("$offset$divider${it.functionName}@${it.fileName}:${it.lineNumber}:${it.columnNumber}")
                    failureTestPath.add(SuiteWithStack(suiteData, it))
                }
            }
        } else {
            suiteData.nested?.filter { it.failure }?.forEach { traverseSuites(it, nextDepth) }
        }
    }

    companion object {
        val divider: String = "    "
    }
}