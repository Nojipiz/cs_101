package views

import java.awt.BorderLayout
import java.awt.event.ActionListener
import javax.swing.ImageIcon
import javax.swing.JFrame

class MainWindow() : JFrame() {

    private val mainPanel: MainPanel
    init {
        setLocation((Constants.SCREEN_SIZE.getWidth() / 2).toInt() - width / 2,
                (Constants.SCREEN_SIZE.getHeight() / 2).toInt() - height / 2)
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Constants.SIZE
        title = "RestaurantSimulation"
        iconImage = ImageIcon(javaClass.getResource("/img/Icon.png")).image
        mainPanel = MainPanel()
        add(mainPanel, BorderLayout.CENTER)
    }
}