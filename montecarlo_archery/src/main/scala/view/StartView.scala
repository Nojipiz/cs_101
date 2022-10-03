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
import javafx.collections.FXCollections

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
              InformationCompetitorsPerGame(),
              CompetitorInformationDetail()
            )
            alignment = CenterLeft
          }
        }
      }
    }
  }

  def GameAmountChooser() = new HBox {
    children = Seq(
      new Label("Cantidad de juegos = 20000"),
      new Button("Iniciar") {
        onAction = _ => viewModel.startSimulation(20000)
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

  def InformationCompetitorsPerGame() = new VBox {
    val gameSelector = new TextField()
    val luckiestCompetitor = new Label("")
    val mostExperiencedCompetitor = new Label("")
    val mostVictoriesGender = new Label("")
    val winnerTeam = new Label("")
    gameSelector
      .textProperty()
      .addListener { (observable, oldValue, newValue) =>
        if (!newValue.isBlank()) {
          val luckiest = viewModel.getLuckiestPlayerOfGame(newValue)
          luckiestCompetitor.setText(luckiest)
          val experienced = viewModel.getMostExperiencedPlayerOfGame(newValue)
          mostExperiencedCompetitor.setText(experienced)
          val winnerGender = viewModel.getMostVictoriesGenderOfGame(newValue)
          mostVictoriesGender.setText(winnerGender)
          val winnerTeamText = viewModel.getWinnerTeam(newValue)
          winnerTeam.setText(winnerTeamText)
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
      mostExperiencedCompetitor,
      new Label("Genero con mas victorias") {
        style = "-fx-font-weight: bold"
      },
      mostVictoriesGender,
      new Label("Equipo Ganador") {
        style = "-fx-font-weight: bold"
      },
      winnerTeam
    )
  }

  def CompetitorInformationDetail() = new VBox {
    val gameSelector = new TextField()
    val competitors = FXCollections.observableArrayList("Player One", "Player Two")
    val competitorSelector = new ChoiceBox(competitors)
    children = Seq(
      new Label("Información de jugador") {
        style = "-fx-font-weight: bold"
      },
      new Label("Juego"),
      gameSelector,
      new Label("Jugador"),
      competitorSelector
    )
  }
}
