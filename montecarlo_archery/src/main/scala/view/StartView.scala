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
import scalafx.application.Platform
import domain._
import scalafx.collections.ObservableArray
import scalafx.collections.ObservableBuffer
import javafx.event.ActionEvent
import javafx.event.EventHandler

object StartView extends JFXApp3 {

  val viewModel = ViewModel()

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title.value = "Montecarlo Simulation"
      scene = new Scene {
        content = new BorderPane {
          center = new VBox() {
            children = Seq(
              GameAmountChooser(),
              new HBox {
                children = Seq(WinnerTeam(), WinnerGender())
              },
              LuckiestCompetitorPerGame()
            )
            alignment = CenterLeft
          }
        }
      }
    }
  }

  def GameAmountChooser() = new HBox {
    children = Seq(
      new Label("Cantidad de juegos = 10"),
      new Button("Iniciar") {
        onAction = _ => viewModel.startSimulation(10)
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

  def LuckiestCompetitorPerGame() = new VBox {
    val gameSelector = new TextField()
    val luckiestCompetitor = new Label("")
    val mostExperiencedCompetitor = new Label("")
    gameSelector
      .textProperty()
      .addListener { (observable, oldValue, newValue) =>
        if (!newValue.isBlank()) {
          val luckiest = viewModel.getLuckiestPlayerOfGame(newValue)
          luckiestCompetitor.setText(luckiest)
          val experienced = viewModel.getMostExperiencedPlayerOfGame(newValue)
          mostExperiencedCompetitor.setText(experienced)
        }
      };
    children = Seq(
      new Label("Seleccionar un juego") {
        style = "-fx-font-weight: bold"
      },
      gameSelector,
      new Label("Jugador con mas suerte") {
        style = "-fx-font-weight: bold"
      },
      luckiestCompetitor,
      new Label("Jugador con mas experiencia") {
        style = "-fx-font-weight: bold"
      },
      mostExperiencedCompetitor
    )
  }
}
