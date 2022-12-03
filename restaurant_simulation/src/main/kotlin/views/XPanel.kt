package views

import presenters.Event
import java.awt.BorderLayout
import java.awt.Button
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.ActionListener
import javax.swing.JPanel

class XPanel(westW: Int, centerW: Int, eastW: Int, northH: Int, centerH: Int, southH: Int, listener: ActionListener) : JPanel() {
    init {
        layout = BorderLayout()
        size = Constants.SIZE
        preferredSize = Constants.SIZE
        init(westW, centerW, eastW, northH, centerH, southH, listener)
        isVisible = true
    }

    private fun init(westW: Int, centerW: Int, eastW: Int, northH: Int, centerH: Int, southH: Int, listener: ActionListener) {
        createPanels(westW, centerW, eastW, northH, centerH, southH, listener, this)
    }

    private fun createPanels(westW: Int, centerW: Int, eastW: Int, northH: Int, centerH: Int, southH: Int, listener: ActionListener, panel: JPanel) {
        val backgroundColor = Constants.PRIMARY_COLOR
        val northColor = Constants.PRIMARY_COLOR
        val southColor = Constants.PRIMARY_COLOR
        val eastColor = Constants.PRIMARY_COLOR
        val westColor = Constants.PRIMARY_COLOR
        val centerColor = Constants.PRIMARY_COLOR
        //		Color northColor = Color.black;
//		Color southColor = Color.MAGENTA;
//		Color eastColor = Color.white;
//		Color westColor = Color.GREEN;
//		Color centerColor = Color.yellow;
        panel.background = backgroundColor
        val size = panel.size
        val divisionsHeight = northH + centerH + southH
        val divisionsWidth = westW + centerW + eastW
        val north = JPanel(BorderLayout())
        north.background = northColor
        north.size = Dimension(size.getWidth().toInt(),
                size.getHeight().toInt() * northH / divisionsHeight)
        north.preferredSize = Dimension(size.getWidth().toInt(),
                size.getHeight().toInt() * northH / divisionsHeight)
        modifyNorthPanel(listener, north)
        panel.add(north, BorderLayout.NORTH)
        val center = JPanel(BorderLayout())
        center.background = centerColor
        center.size = Dimension(size.getWidth().toInt() * centerW / divisionsWidth,
                size.getHeight().toInt() * centerH / divisionsHeight)
        center.preferredSize = Dimension(size.getWidth().toInt() * centerW / divisionsWidth,
                size.getHeight().toInt() * centerH / divisionsHeight)
        modifyCenterPanel(listener, center)
        panel.add(center, BorderLayout.CENTER)
        val south = JPanel(BorderLayout())
        south.background = southColor
        south.preferredSize = Dimension(size.getWidth().toInt(),
                size.getHeight().toInt() * southH / divisionsHeight)
        south.size = Dimension(size.getWidth().toInt(),
                size.getHeight().toInt() * southH / divisionsHeight)
        modifySouthPanel(listener, south)
        panel.add(south, BorderLayout.SOUTH)
        val east = JPanel(BorderLayout())
        east.background = eastColor
        east.size = Dimension(size.getWidth().toInt() * eastW / divisionsWidth,
                size.getHeight().toInt() * centerH / divisionsHeight)
        east.preferredSize = Dimension(size.getWidth().toInt() * eastW / divisionsWidth,
                size.getHeight().toInt() * centerH / divisionsHeight)
        modifyEastPanel(listener, east)
        panel.add(east, BorderLayout.EAST)
        val west = JPanel(BorderLayout())
        west.background = westColor
        west.size = Dimension(size.getWidth().toInt() * westW / divisionsWidth,
                size.getHeight().toInt() * centerH / divisionsHeight)
        west.preferredSize = Dimension(size.getWidth().toInt() * westW / divisionsWidth,
                size.getHeight().toInt() * centerW / divisionsHeight)
        modifyWestPanel(listener, west)
        panel.add(west, BorderLayout.WEST)
    }

    private fun modifyWestPanel(listener: ActionListener, west: JPanel) {}
    private fun modifyEastPanel(listener: ActionListener, east: JPanel) {}
    private fun modifyNorthPanel(listener: ActionListener, north: JPanel) {}
    private fun modifyCenterPanel(listener: ActionListener, center: JPanel) {}
    protected fun modifySouthPanel(listener: ActionListener?, south: JPanel) {
        val panelButtons = JPanel(GridLayout(3, 1))
        panelButtons.background = Constants.PRIMARY_COLOR
        val button = Button()
        button.addActionListener(listener)
        button.actionCommand = Event.BOTON1.toString()
        panelButtons.add(button)
        panelButtons.add(button)
        val button2 = Button()
        button2.addActionListener(listener)
        button2.actionCommand = Event.BOTON2.toString()
        panelButtons.add(button2)
        south.add(panelButtons, BorderLayout.CENTER)
    }

    companion object {
        private const val serialVersionUID = 1L
    }
}