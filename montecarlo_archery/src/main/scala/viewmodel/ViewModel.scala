package viewmodel

import scalafx.beans.property.StringProperty
import domain._
import scalafx.beans.property.ObjectProperty.apply
import scalafx.beans.property.ObjectProperty
import simulator._
import scalafx.application.Platform

class ViewModel {

  val simulationGlobalResults: ObjectProperty[GlobalResults] = ObjectProperty(GlobalResults(None, None))
  var lastSimulation: List[Game] = List()

  def startSimulation(amount: Int): Unit = {
    lastSimulation = runSimulation(amount)
    val winnerTeam = lastSimulation.getWinnerTeam()
    val winnerGender = lastSimulation.getWinnerGender()
    simulationGlobalResults.set(
      GlobalResults(
        winnerTeam = Some(winnerTeam),
        winnerGender = Some(winnerGender)
      )
    )
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
}
