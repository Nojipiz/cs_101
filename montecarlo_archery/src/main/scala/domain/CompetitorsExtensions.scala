package simulator

import domain._
import simulator.PseudoRandomState
import spire.math.Complex.apply

extension (comp: List[Competitor]) {
  def checkifSomeoneStillResistance(): Boolean =
    comp
      .find(competitor => competitor.resistance >= SHOOT_RESISTANCE_COST)
      .isDefined

  def getTiredAndNewLuck(): List[Competitor] =
    comp.map(competitor =>
      competitor.copy(
        resistance = competitor.resistance - PseudoRandomState.getTiredness(),
        luck = PseudoRandomState.getLuck()
      )
    )

  def getLuckiestEachTeam(): (Competitor, Competitor) = {
    val bestTeamA = comp
      .filter(competitor => competitor.team == TeamName.TeamA)
      .maxBy(_.luck)
    val bestTeamB = comp
      .filter(competitor => competitor.team == TeamName.TeamB)
      .maxBy(_.luck)
    (bestTeamA, bestTeamB);
  }

  /** At this point the list must have a even length, so it can be 2, 4, 6
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
