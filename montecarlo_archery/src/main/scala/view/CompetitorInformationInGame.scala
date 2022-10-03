package view

import scalafx.scene._
import scalafx.scene.layout._
import scalafx.stage.Stage
import scalafx.scene.chart._
import scalafx.collections.ObservableBuffer

def initCompetitorInformationView(): Unit = {
  val stage = new Stage() {
    title = "Información del jugador"
    scene = CompetitorInformationScene()
  }.show()
}

def CompetitorInformationScene(): Scene = {
  val data = ObservableBuffer(
    XYChart.Data[Number, Number](2, 4),
    XYChart.Data[Number, Number](1, 2)
  )
  val series = XYChart.Series[Number, Number]("test", data)
  val chart = LineChart(new NumberAxis(), new NumberAxis())
  chart.getData().add(series)
  new Scene {
    root = chart
  }
}
