package views

import java.awt.Rectangle
import javax.swing.*


class MainPanel(
    val startSimulation : () -> Unit,
) : JPanel() {

    private lateinit var textArea:JTextArea

    init {
        init()
        isVisible = true
    }

    private fun init() {
        layout = null
        size = Constants.SIZE
        preferredSize = Constants.SIZE

        val startSimulation = JButton("Start Simulation")
        startSimulation.bounds = Rectangle(100, 100, 300, 50)
        startSimulation.addActionListener{
            startSimulation()
        }
        add(startSimulation)

        val chefTitle = JLabel("Chef Selector")
        chefTitle.bounds = Rectangle(100, 180, 100, 25)
        add(chefTitle)

        val chefSpinner = JSpinner(SpinnerNumberModel(0, 0, 10, 1))
        chefSpinner.bounds = Rectangle(100, 200, 100, 25)
        add(chefSpinner)

        val getGraphValue = JButton("Get performance")
        getGraphValue.addActionListener{
            print("TODO!")
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

    fun showResults(text:String){
        textArea.text = textArea.text + "\n" + text
    }
}