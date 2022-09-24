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

  def startSimulation(value: StringProperty): Unit = {
    if (value.isEmpty().get()) return
    val rounds: Option[Int] = value.value.toIntOption
    rounds match {
      case Some(amount) => { executeSimulation(amount) }
      case _            => print("Not an int")
    }
  }

  def executeSimulation(amount: Int): Unit = {
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

}
