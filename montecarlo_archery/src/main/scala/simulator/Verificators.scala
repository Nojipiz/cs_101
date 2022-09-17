package simulator

import domain._
import simulator.PseudoRandomState

extension(comp:List[Competitor]){
  def checkifSomeoneStillResistance(): Boolean =
    comp.find(competitor => 
        competitor.resistance >= SHOOT_RESISTANCE_COST
    ).isDefined

  def getTired(): List[Competitor] = comp.map(competitor => 
    Competitor(
      team = competitor.team,
      name = competitor.name,
      resistance = competitor.resistance - PseudoRandomState.getTiredness(),
      experience = competitor.experience,
      luck = PseudoRandomState.getLuck(),
      gender = competitor.gender,
      score = competitor.score
      )
    )

}
