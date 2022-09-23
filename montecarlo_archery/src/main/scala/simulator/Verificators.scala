package simulator

import domain._
import simulator.PseudoRandomState

extension (comp: List[Competitor]) {
  def checkifSomeoneStillResistance(): Boolean =
    comp
      .find(competitor => competitor.resistance >= SHOOT_RESISTANCE_COST)
      .isDefined

  def getTired(): List[Competitor] =
    comp.map(competitor =>
      Competitor(
        team = competitor.team,
        name = competitor.name,
        resistance = competitor.resistance - PseudoRandomState
          .getTiredness(),
        experience = competitor.experience,
        luck = PseudoRandomState.getLuck(),
        gender = competitor.gender,
        score = competitor.score
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
}
