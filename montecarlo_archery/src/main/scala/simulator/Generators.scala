package simulator

import domain._

def getAllCompetitors(): List[Competitor] = getCompetitors(TeamName.TeamA) ++ getCompetitors(TeamName.TeamB)

/*
  Just the initial generator function, to give the competitors the start values. 
*/
private def getCompetitors(teamName: TeamName): List[Competitor] =
  0.until(5)
    .map(index =>
      Competitor(
        team = teamName,
        name = s"Competitor ${index + 1} $teamName",
        resistance = PseudoRandomState.getResistance(),
        experience = COMPETITOR_EXPERIENCE,
        luck = 0,
        gender = PseudoRandomState.getGender(),
        score = 0
      )
    )
    .toList
