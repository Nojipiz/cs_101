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
import scalafx.application.Platform

object StartView extends JFXApp3 {

  val viewModel = ViewModel()

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title.value = "Montecarlo Simulation"
      scene = new Scene {
        content = new BorderPane {
          center = new HBox() {
            children = Seq(GameAmountChooser(), WinnerTeam(), WinnerGender())
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
        onAction = _ => viewModel.startSimulation(1)
      }
    )
  }

  def WinnerTeam() = new VBox {
    val title = new Label("Equipo Ganador")
    val winnerTeamName = new Label("Ninguno")
    val winnerTeam = new Label("Sin Score")
    viewModel.simulationGlobalResults.onChange { (_, _, results: GlobalResults) =>
      results.winnerTeam match {
        case Some(value) => {
          winnerTeamName.setText(value._1.toString())
          winnerTeam.setText(value._2.toString())
        }
        case _ => print("No Team value")
      }
    }
    children = Seq(title, winnerTeamName, winnerTeam)
  }

  def WinnerGender() = new VBox {
    val title = new Label("Genero Ganador")
    val winnerGender = new Label("Ninguno")
    viewModel.simulationGlobalResults.onChange { (_, _, results: GlobalResults) =>
      results.winnerGender match {
        case Some(value) => {
          winnerGender.setText(value.toString())
        }
        case _ => print("No Team value")
      }
    }
    children = Seq(title, winnerGender)
  }
}
