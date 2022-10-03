package domain

import simulator._

extension (games: List[Game]) {

  def getWinnerTeam(): (TeamName, Int) = {
    val gamesResults = games.map(_.getWinnerTeam())
    val scoreA: Option[Int] = gamesResults.filter(_._1 == TeamName.TeamA).map(_._2).reduceOption(_ + _)
    val scoreB: Option[Int] = gamesResults.filter(_._1 == TeamName.TeamB).map(_._2).reduceOption(_ + _)

    if (scoreA.getOrElse(-1) > scoreB.getOrElse(-1))
      (TeamName.TeamA, scoreA.getOrElse(-1))
    else
      (TeamName.TeamB, scoreB.getOrElse(-1))
  }

  def getWinnerGender(): Gender = {
    val gamesWinner: List[Gender] = games.map(_.getMostWinsGender())
    gamesWinner.groupBy(identity).mapValues(_.size).maxBy(_._2)._1
  }
}

extension (game: Game) {

  def getMostWinsGender(): Gender = {
    val bestOfEachRound: List[Competitor] =
      game.rounds.flatMap(_.playersRounds.flatMap(_.shoots).map(_.competitorState).getBestOfMatch())
    val bestGenders: List[Gender] = bestOfEachRound.map(_.gender)
    bestGenders.groupBy(identity).mapValues(_.size).maxBy(_._2)._1
  }

  def getWinnerTeam(): (TeamName, Int) = {
    val playersLastRounds: List[CompetitorRound] = game.rounds.last.playersRounds
    val scoreA: Option[Int] =
      playersLastRounds
        .map(_.shoots.last.competitorState)
        .filter(_.team == TeamName.TeamA)
        .map(_.score)
        .reduceOption(_ + _)
    val scoreB: Option[Int] =
      playersLastRounds
        .map(_.shoots.last.competitorState)
        .filter(_.team == TeamName.TeamB)
        .map(_.score)
        .reduceOption(_ + _)

    if (scoreA.getOrElse(-1) > scoreB.getOrElse(-1))
      (TeamName.TeamA, scoreA.getOrElse(-1))
    else
      (TeamName.TeamB, scoreB.getOrElse(-1))
  }

  def getLuckiestPlayer(): Competitor =
    game.rounds.flatMap(_.playersRounds).map(_.initialState).maxBy(_.luck)

  def getMostExperiencedPlayer(): Competitor = {
    val states = game.rounds.flatMap(_.playersRounds).map(_.initialState)
    states.maxBy(_.experience)
  }
}
