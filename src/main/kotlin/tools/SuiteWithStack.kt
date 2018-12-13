package tools

import configuration.connection.data.SuiteData
import configuration.connection.data.TestFailureData
import configuration.connection.data.TestStackData

enum class TestRowType { SuiteData, Failure, Stack }

class SuiteWithStack(val suiteData: SuiteData, val offset: String, val rowType: TestRowType, val failureData: TestFailureData?, val testStackData: TestStackData?) {
    override fun toString(): String {
        if (rowType == TestRowType.Stack && testStackData != null) {
            return "$offset${divider}${testStackData.functionName}@${testStackData.fileName}:${testStackData.lineNumber}:${testStackData.columnNumber}"
        }

        if (rowType == TestRowType.Failure && failureData != null) {
            return offset + divider + failureData.message
        }

        return offset + suiteData
    }

    companion object {
        const val divider: String = "    "
    }
}