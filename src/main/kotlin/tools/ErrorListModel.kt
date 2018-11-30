package tools

import configuration.connection.data.CompilationFinishedMessage
import javax.swing.DefaultListModel

class ErrorListModel: DefaultListModel<CompilationFinishedMessage>() {
    private var errors: List<CompilationFinishedMessage> = ArrayList()

    fun setErrors(messages: List<CompilationFinishedMessage>?) {
        this.clear()
        errors = messages?: ArrayList()
        messages?.forEach {
            this.addElement(it)
        }
    }

    fun getErrors(): List<CompilationFinishedMessage> {
        return errors
    }
}