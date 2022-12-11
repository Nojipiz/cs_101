package views

import java.awt.*
import java.util.*
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities


class GraphPanel(private var scores: List<Double>) : JPanel() {
    private val padding = 25
    private val labelPadding = 25
    private val lineColor = Color(44, 102, 230, 180)
    private val pointColor = Color(100, 100, 100, 180)
    private val gridColor = Color(200, 200, 200, 200)
    private val pointWidth = 4
    private val numberYDivisions = 10
    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val xScale = (getWidth().toDouble() - 2 * padding - labelPadding) / (scores.size - 1)
        val yScale = (height.toDouble() - 2 * padding - labelPadding) / (maxScore - minScore)
        val graphPoints: MutableList<Point> = ArrayList()
        for (i in scores.indices) {
            val x1 = (i * xScale + padding + labelPadding).toInt()
            val y1 = ((maxScore - scores[i]) * yScale + padding).toInt()
            graphPoints.add(Point(x1, y1))
        }

        // draw white background
        g2.color = Color.WHITE
        g2.fillRect(
            padding + labelPadding,
            padding,
            getWidth() - 2 * padding - labelPadding,
            height - 2 * padding - labelPadding
        )
        g2.color = Color.BLACK

        // create hatch marks and grid lines for y axis.
        for (i in 0 until numberYDivisions + 1) {
            val x0 = padding + labelPadding
            val x1 = pointWidth + padding + labelPadding
            val y0 =
                height - (i * (height - padding * 2 - labelPadding) / numberYDivisions + padding + labelPadding)
            if (scores.size > 0) {
                g2.color = gridColor
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y0)
                g2.color = Color.BLACK
                val yLabel: String =
                    ((minScore + (maxScore - minScore) * (i * 1.0 / numberYDivisions)) * 100).toInt()
                        .div(100.0).toString() + ""
                val metrics = g2.fontMetrics
                val labelWidth = metrics.stringWidth(yLabel)
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + metrics.height / 2 - 3)
            }
            g2.drawLine(x0, y0, x1, y0)
        }

        // and for x axis
        for (i in scores.indices) {
            if (scores.size > 1) {
                val x0 =
                    i * (getWidth() - padding * 2 - labelPadding) / (scores.size - 1) + padding + labelPadding
                val y0 = height - padding - labelPadding
                val y1 = y0 - pointWidth
                if (i % ((scores.size / 20.0).toInt() + 1) == 0) {
                    g2.color = gridColor
                    g2.drawLine(x0, height - padding - labelPadding - 1 - pointWidth, x0, padding)
                    g2.color = Color.BLACK
                    val xLabel = i.toString() + ""
                    val metrics = g2.fontMetrics
                    val labelWidth = metrics.stringWidth(xLabel)
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.height + 3)
                }
                g2.drawLine(x0, y0, x0, y1)
            }
        }

        // create x and y axes
        g2.drawLine(
            padding + labelPadding,
            height - padding - labelPadding,
            padding + labelPadding,
            padding
        )
        g2.drawLine(
            padding + labelPadding,
            height - padding - labelPadding,
            getWidth() - padding,
            height - padding - labelPadding
        )
        val oldStroke = g2.stroke
        g2.color = lineColor
        g2.stroke = GRAPH_STROKE
        for (i in 0 until graphPoints.size - 1) {
            val x1 = graphPoints[i].x
            val y1 = graphPoints[i].y
            val x2 = graphPoints[i + 1].x
            val y2 = graphPoints[i + 1].y
            g2.drawLine(x1, y1, x2, y2)
        }
        g2.stroke = oldStroke
        g2.color = pointColor
        for (i in graphPoints.indices) {
            val x = graphPoints[i].x - pointWidth / 2
            val y = graphPoints[i].y - pointWidth / 2
            val ovalW = pointWidth
            val ovalH = pointWidth
            g2.fillOval(x, y, ovalW, ovalH)
        }
    }

    private val minScore: Double
        get() {
            var minScore = Double.MAX_VALUE
            for (score in scores) {
                minScore = Math.min(minScore, score)
            }
            return minScore
        }

    private val maxScore: Double
        get() {
            var maxScore = Double.MIN_VALUE
            for (score in scores) {
                maxScore = Math.max(maxScore, score)
            }
            return maxScore
        }

    fun setScores(scores: List<Double>) {
        this.scores = scores
        invalidate()
        this.repaint()
    }

    fun getScores(): List<Double> {
        return scores
    }

    companion object {
        private val GRAPH_STROKE: Stroke = BasicStroke(2f)
        fun createAndShowGui(
            scores: List<Double>
        ) {
            val mainPanel = GraphPanel(scores)
            mainPanel.preferredSize = Dimension(800, 600)
            val frame = JFrame("DrawGraph")
            frame.contentPane.add(mainPanel)
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
    }
}