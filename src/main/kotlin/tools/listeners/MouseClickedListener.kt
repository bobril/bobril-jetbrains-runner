package tools.listeners

import java.awt.event.MouseEvent
import java.awt.event.MouseListener

abstract class MouseClickedListener: MouseListener {
    override fun mouseReleased(e: MouseEvent?) {
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseClicked(e: MouseEvent?) {
        if (e == null) {
            return
        }

        onClick(e)
    }

    abstract fun onClick(e: MouseEvent)

    override fun mouseExited(e: MouseEvent?) {
    }

    override fun mousePressed(e: MouseEvent?) {
    }
}