package view

import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.layout._
import scalafx.scene.control._
import scalafx.scene._
import scalafx.geometry._
import scalafx.geometry.Pos._
import scalafx.scene.paint.Color._
import viewmodel.ViewModel
import domain.Game
import domain.Game
import domain.GlobalResults
import domain.GlobalResults

object StartView extends JFXApp3 {

  val viewModel = ViewModel()

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title.value = "Montecarlo Simulation"
      scene = new Scene {
        content = new BorderPane {
          center = new HBox() {
            children = Seq(GameAmountChooser(), WinnerTeam())
            alignment = CenterLeft
          }
        }
      }
    }
  }

  def GameAmountChooser() = new HBox {
    val gamesAmmountField = new TextField()
    children = Seq(
      new Label("Cantidad de juegos"),
      gamesAmmountField,
      new Button("Iniciar") {
        onAction = _ => viewModel.startSimulation(gamesAmmountField.text)
      }
    )
  }

  def WinnerTeam() = new VBox {
    val title = new Label("Equipo Ganador")
    val winnerName = new Label("Ninguno")
    val winnerScore = new Label("Sin Score")
    viewModel.simulationGlobalResults.onChange { (_, _, results: GlobalResults) =>
      results.winnerTeam match {
        case Some(value) => {
          winnerName.setText(value._1.toString())
          winnerScore.setText(value._2.toString())
        }
        case _ => print("No value")
      }
    }
    children = Seq(title, winnerName, winnerScore)
  }
}
