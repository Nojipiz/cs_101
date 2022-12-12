package views

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Toolkit
import java.awt.event.ActionListener
import javax.swing.ImageIcon
import javax.swing.JFrame

class MainWindow(
    startSimulation : () -> Unit,
    getCookInformation: (index:Int) -> MutableList<Boolean>
) : JFrame() {
    private val SIZE = Dimension(500, 500);
    private val SCREEN_SIZE: Dimension = Toolkit.getDefaultToolkit().screenSize;
    private val mainPanel: MainPanel
    init {
        setLocation((SCREEN_SIZE.getWidth() / 2).toInt() - width / 2,
                (SCREEN_SIZE.getHeight() / 2).toInt() - height / 2)
        defaultCloseOperation = EXIT_ON_CLOSE
        size = SIZE
        title = "Pizza simulator 3.000 Turbo pro max!"
        iconImage = ImageIcon(javaClass.getResource("/img/Icon.png")).image
        mainPanel = MainPanel(startSimulation, getCookInformation )
        add(mainPanel, BorderLayout.CENTER)
    }

    fun showResults(text:String){
        mainPanel.showResults(text)
    }
}