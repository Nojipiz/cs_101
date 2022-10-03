package view

import scalafx.scene._
import scalafx.scene.layout._
import scalafx.stage.Stage
import scalafx.scene.chart._
import scalafx.collections.ObservableBuffer

def initCompetitorInformationView(data: List[(Int, Int)]): Unit = {
  val stage = new Stage() {
    title = "Información del jugador"
    scene = CompetitorInformationScene(data)
  }.show()
}

def CompetitorInformationScene(data: List[(Int, Int)]): Scene = {
  val pointsData = ObservableBuffer(
    XYChart.Data[Number, Number](0, 0)
  )
  data
    .foreach(point => {
      val dataChart = XYChart.Data[Number, Number](point._1, point._2)
      pointsData.add(dataChart)
    })
  val series = XYChart.Series[Number, Number]("Jugador", pointsData)
  val chart = LineChart(new NumberAxis(), new NumberAxis())
  chart.getData().add(series)
  new Scene {
    root = chart
  }
}
