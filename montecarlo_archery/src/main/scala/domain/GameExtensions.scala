package domain

extension (game: Game) {

  def getWinnerTeam(): (TeamName, Int) = {
    val competitorsStatesLast = game.rounds.last.playersRounds.map(_.initialState)
    val scoreA: Option[Int] = competitorsStatesLast.filter(_.team == TeamName.TeamA).map(_.score).reduceOption(_ + _)
    val scoreB: Option[Int] = competitorsStatesLast.filter(_.team == TeamName.TeamB).map(_.score).reduceOption(_ + _)

    if (scoreA.isEmpty)
      (TeamName.TeamB, scoreB.getOrElse(-1))
    if (scoreB.isEmpty)
      (TeamName.TeamA, scoreA.getOrElse(-1))
    if (scoreA.getOrElse(-1) > scoreB.getOrElse(-1))
      (TeamName.TeamA, scoreA.getOrElse(-1))
    else
      (TeamName.TeamB, scoreB.getOrElse(-1))
  }

  def getWinnerGender(): (Gender, Int) = {
    val competitorsStatesLast = game.rounds.last.playersRounds.map(_.initialState)
    val scoreMales: Option[Int] = competitorsStatesLast.filter(_.gender == Gender.Male).map(_.score).reduceOption(_ + _)
    val scoreFemales: Option[Int] =
      competitorsStatesLast.filter(_.gender == Gender.Female).map(_.score).reduceOption(_ + _)

    if (scoreFemales.isEmpty)
      (Gender.Male, scoreMales.getOrElse(-1))
    if (scoreMales.isEmpty)
      (Gender.Female, scoreFemales.getOrElse(-1))
    if (scoreMales.getOrElse(-1) > scoreFemales.getOrElse(-1))
      (Gender.Male, scoreMales.getOrElse(-1))
    else
      (Gender.Female, scoreFemales.getOrElse(-1))
  }
}
