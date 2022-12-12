package views

import java.awt.Dimension
import java.awt.Rectangle
import java.awt.Toolkit
import javax.swing.*


class MainPanel(
    val startSimulation: () -> Unit,
    val getCookInformation: (index: Int) -> MutableList<Boolean>
) : JPanel() {
    private val SIZE = Dimension(500, 500);
    private val SCREEN_SIZE: Dimension = Toolkit.getDefaultToolkit().screenSize;
    private lateinit var textArea: JTextArea

    init {
        init()
        isVisible = true
    }

    private fun init() {
        layout = null
        size = SIZE
        preferredSize = SIZE

        val startSimulation = JButton("Start Simulation")
        startSimulation.bounds = Rectangle(100, 100, 300, 50)
        startSimulation.addActionListener {
            SwingUtilities.invokeLater { startSimulation() }
        }
        add(startSimulation)

        val chefTitle = JLabel("Chef Selector")
        chefTitle.bounds = Rectangle(100, 180, 100, 25)
        add(chefTitle)

        val chefSpinner = JSpinner(SpinnerNumberModel(0, 0, 10, 1))
        chefSpinner.bounds = Rectangle(100, 200, 100, 25)
        add(chefSpinner)

        val getGraphValue = JButton("Get performance")
        getGraphValue.addActionListener {
            SwingUtilities.invokeLater {
                val list =
                    getCookInformation((chefSpinner.model.value as Int))
                        .map { return@map if (it) 1.0 else 0.0 }
                GraphPanel.createAndShowGui(list)
            }
        }
        getGraphValue.bounds = Rectangle(60, 240, 180, 40)
        add(getGraphValue)



        textArea = JTextArea()
        textArea.isEditable = false
        textArea.bounds = Rectangle(20, 300, 500, 600)

        val scroll = JScrollPane(textArea)
        scroll.bounds = Rectangle(20, 300, 500, 600)
        add(scroll)
    }

    fun showResults(text: String) {
        textArea.text = textArea.text + "\n" + text
    }
}