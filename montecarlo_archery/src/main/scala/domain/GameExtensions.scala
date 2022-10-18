package domain

import simulator._

extension (games: List[Game]) {

  /*
    Here, we sum all the scores of each team, and returns the name, the score and image of the team with more score
  */
  def getWinnerTeam(): (TeamName, Int, String) = {
    val gamesResults = games.map(_.getWinnerTeam())
    val scoreA: Option[Int] = gamesResults.filter(_._1 == TeamName.TeamA).map(_._2).reduceOption(_ + _)
    val scoreB: Option[Int] = gamesResults.filter(_._1 == TeamName.TeamB).map(_._2).reduceOption(_ + _)

    if (scoreA.getOrElse(-1) > scoreB.getOrElse(-1))
      (
        TeamName.TeamA,
        scoreA.getOrElse(-1),
        "data:image/png;base64,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"
      )
    else
      (
        TeamName.TeamB,
        scoreB.getOrElse(-1),
        "data:image/png;base64,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"
      )
  }
  /*
    Here, we sum all the scores of each gender, and returns the winner;
  */
  def getWinnerGender(): Gender = {
    val gamesWinner: List[Gender] = games.map(_.getMostWinsGender())
    gamesWinner.groupBy(identity).mapValues(_.size).maxBy(_._2)._1
  }

  /*
    Set an index to a list of scores of each game.
    This is usefull to generate the data for the graphic of player vs each game score.
  */
  def getGamePointsOfCompetitor(competitorName: String): List[(Int, Int)] =
    games.zipWithIndex.map((game: Game, index: Int) => {
      (index, game.getTotalCompetitorScore(competitorName))
    })
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

  def getTotalCompetitorScore(competitorName: String): Int =
    game.rounds.last.playersRounds
      .filter(_.initialState.name == competitorName)
      .last
      .shoots
      .map(_.competitorState.score)
      .reduce(_ + _)
}
