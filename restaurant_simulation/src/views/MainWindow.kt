package views

import java.awt.BorderLayout
import java.awt.event.ActionListener
import javax.swing.ImageIcon
import javax.swing.JFrame

class MainWindow(lister: ActionListener) : JFrame() {
    private val mainPanel: XPanel

    init {
        setLocation((Constants.SCREEN_SIZE.getWidth() / 2).toInt() - width / 2,
                (Constants.SCREEN_SIZE.getHeight() / 2).toInt() - height / 2)
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Constants.SIZE
        title = TITLE
        iconImage = ImageIcon(javaClass.getResource(ICON_PATH)).image
        mainPanel = XPanel(0, 3, 0, 2, 3, 1, lister)
        add(mainPanel, BorderLayout.CENTER)
    }

    companion object {
        private const val serialVersionUID = 1L
        private const val ICON_PATH = "/img/Icon.png"
        private const val TITLE = "Programa"
    }
}