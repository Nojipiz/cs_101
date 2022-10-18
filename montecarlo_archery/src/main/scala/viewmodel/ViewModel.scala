package viewmodel

import scalafx.beans.property.StringProperty
import domain._
import scalafx.beans.property.ObjectProperty.apply
import scalafx.beans.property.ObjectProperty
import simulator._
import scalafx.application.Platform

class ViewModel {

  val simulationGlobalResults: ObjectProperty[GlobalResults] = ObjectProperty(GlobalResults(None, None))
  val isLoading:ObjectProperty[Boolean] = ObjectProperty(false)
  var lastSimulation: List[Game] = List()

  def startSimulation(amount: Int): Unit = {
    new Thread(){
      override def run(): Unit = {
        Platform.runLater{isLoading.set(true)}
        lastSimulation = runSimulation(amount)
        val winnerTeam = lastSimulation.getWinnerTeam()
        val winnerGender = lastSimulation.getWinnerGender()
        Platform.runLater{
          simulationGlobalResults.set(
            GlobalResults(
            winnerTeam = Some(winnerTeam),
            winnerGender = Some(winnerGender))
          )
          isLoading.set(false)
        }
      }
    }.start()
  }

  def getLuckiestPlayerOfGame(game: String): String = {
    val gameNumber: Option[Int] = game.toIntOption
    gameNumber match {
      case Some(value) => {
        if (value > lastSimulation.length - 1) return "Nadie"
        lastSimulation(value).getLuckiestPlayer().name
      }
      case _ => "Error converting"
    }
  }

  def getMostExperiencedPlayerOfGame(game: String): String = {
    val gameNumber: Option[Int] = game.toIntOption
    gameNumber match {
      case Some(value) => {
        if (value > lastSimulation.length - 1) return "Nadie"
        lastSimulation(value).getMostExperiencedPlayer().name
      }
      case _ => "Error converting"
    }
  }

  def getMostVictoriesGenderOfGame(game: String): String = {
    val gameNumber: Option[Int] = game.toIntOption
    gameNumber match {
      case Some(value) => {
        if (value > lastSimulation.length - 1) return "Ninguno"
        lastSimulation(value).getMostWinsGender().toString()
      }
      case _ => "Error converting"
    }
  }

  def getWinnerTeam(game: String): String = {
    val gameNumber: Option[Int] = game.toIntOption
    gameNumber match {
      case Some(value) => {
        if (value > lastSimulation.length - 1) return "Ninguno"
        lastSimulation(value).getWinnerTeam()._1.toString()
      }
      case _ => "Error converting"
    }
  }

  def getCompetitorInformationOfGame(competitorName: String): List[(Int, Int)] = {
    lastSimulation.getGamePointsOfCompetitor(competitorName)
  }
}
