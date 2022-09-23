package utils

import domain._

extension (round: Round) {
  def getCompetitors(): List[Competitor] = round.playersRounds.map(round => round.initialState)
}
