package simulator

import domain._
import simulator.PseudoRandomState

extension (comp: List[Competitor]) {

  def checkifSomeoneStillResistance(): Boolean =
    comp
      .find(competitor => competitor.resistance >= SHOOT_RESISTANCE_COST)
      .isDefined

  /*
    According to the point "If one shooter wins 9 points of experience, he losses only one point
    of resistance in the next two rounds!"
    This function reduces the resistance of the shooters, but before checks if someone has win 9 points of experience
    And set a new luck value!
  */
  def getTiredAndNewLuck(
      historyRounds: List[Round]
  ): List[Competitor] =
    comp.map(competitor => {
      val shouldTireOne = historyRounds.thisCompetitorGetTiredOne(competitor)
      val tiredness = if (shouldTireOne) 1 else PseudoRandomState.getTiredness()
      competitor.copy(
        resistance = competitor.resistance - tiredness,
        luck = PseudoRandomState.getLuck()
      )
    })

  /*
    Obtains the luckiest player of each team, and returns a tuple with two competitors.
  */
  def getLuckiestEachTeam(): (Competitor, Competitor) = {
    val bestTeamA = comp
      .filter(competitor => competitor.team == TeamName.TeamA)
      .maxBy(_.luck)
    val bestTeamB = comp
      .filter(competitor => competitor.team == TeamName.TeamB)
      .maxBy(_.luck)
    (bestTeamA, bestTeamB);
  }

  /*
    According to the point "If a player wins three extra shoots in a row, he can make another shoot without decrease the resistance amount"
    So this function checks if this competitor has been the winner in the last 2 rounds (which means that this is the thirdone).
  */
  def checkIfHasTwoLucksBefore(competitor: Competitor): Boolean = {
    if (comp.length <= 2)
      return false
    if (comp.length <= 4) return comp.count(_ == competitor) == 2
    else {
      val notInFirst: Boolean = comp.take(2).forall(_ != competitor)
      val timesInARow: Int = comp.takeRight(4).count(_ == competitor)
      return notInFirst && (timesInARow == 2)
    }
  }

  def getBestOfMatch(): List[Competitor] = {
    val maxScore = comp.map(_.score).max
    comp.filter(_.score == maxScore)
  }
}
