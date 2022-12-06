package views

import java.awt.Rectangle
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel

class MainPanel(
    val startSimulation : () -> Unit,
) : JPanel() {
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
    }
}