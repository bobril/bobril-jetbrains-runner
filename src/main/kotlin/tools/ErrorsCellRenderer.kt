package tools

import configuration.connection.data.CompilationFinishedMessage
import java.awt.Component
import javax.swing.DefaultListCellRenderer
import javax.swing.JList
import javax.swing.JPanel

class ErrorsCellRenderer(): DefaultListCellRenderer() {
    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val message = value as CompilationFinishedMessage
        val sb = StringBuilder()
        sb.append("<html><body><div>")
        sb.append("<div style=\"padding-left: 1.5em;text-indent:-1.5em;${(if (!isSelected) "color:red;" else "")}\">") // message
        sb.append(message.text)
        sb.append("</div>") // message end
        sb.append("<div style=\"padding-left: 1.5em;text-indent:-1.5em;font-size: 0.9em;\">>") // file
        sb.append(message.fileName)
        sb.append("</div>") // file end
        sb.append("</div></body></html>")

        return super.getListCellRendererComponent(list, sb, index, isSelected, cellHasFocus)
    }
}