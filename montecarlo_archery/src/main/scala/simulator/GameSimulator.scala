package simulator
import domain._
import utils._
import scala.util.Random.apply
import scala.util.Random

/*
  In this function, we check for the last 3 luckiest of each team, and checks if they has been the last luckiest for 2 times in a row in the rounds history.
  If this happen, we gonna simulate another shoot fot this competitor.
  (Juan, Juan, Pedro) -> This isn't valid
  (Jose, Juan, Juan) -> This is valid, Juan wins an extra shoot
*/
def simulateExtraShootForLuckiests(historyRounds: List[Round], competitors: List[Competitor]): List[Shoot] = {
  val (luckyA, luckyB) = competitors.getLuckiestEachTeam()
  val shoots: List[Shoot] = List(simulateShoot(luckyA), simulateShoot(luckyB))
  val lastestLuckiests: List[Competitor] = historyRounds
    .takeRight(3)
    .flatMap(_.extraShoots)
    .map(_.competitorState);
  val extraShootLuckyA = lastestLuckiests.checkIfHasTwoLucksBefore(luckyA)
  val extraShootLuckyB = lastestLuckiests.checkIfHasTwoLucksBefore(luckyB)
  if (extraShootLuckyA)
    shoots :+ simulateShoot(luckyA, true)
  else if (extraShootLuckyB)
    shoots :+ simulateShoot(luckyB, true)
  else
    shoots
}

/*
  The core function of the simulation, just calls to the random generator and gets a new number, that returns and this is the shoot score
*/
def simulateArrowWithMontecarlo(competitor: Competitor): Int = {
  return competitor.gender match {
    case Gender.Male =>
      PseudoRandomState.getMaleMontecarloShoot()
    case Gender.Female =>
      PseudoRandomState.getFemaleMontecarloShoot()
  }
}

/*
  This function works in case of a tie between two of more players, and wee nee to determine the best.
  So we run a simple shoot simulation until someone wins!
*/
def simulateTieBreakerForBest(bests: List[Competitor]): Competitor = {
  val shoots = bests.map(competitor => simulateShoot(competitor))
  val tieBreakerBest: List[Competitor] = shoots.map(_.competitorState).getBestOfMatch()
  if (tieBreakerBest.length > 1)
    simulateTieBreakerForBest(bests)
  else
    tieBreakerBest.last
}

/*
  This function creates a Shoot structure with the result of the simulation and the updated competitor state copy.
  It's just the result of the Shoot
*/
def simulateShoot(competitor: Competitor, threeInARow: Boolean = false): Shoot = {
  val updatedCompetitor = competitor.copy(
    resistance = competitor.resistance - SHOOT_RESISTANCE_COST,
    score = competitor.score + simulateArrowWithMontecarlo(competitor)
  )
  Shoot(updatedCompetitor, threeInARow)
}

/*
  Recursive function, it will push the competitors to shoot until they tire (or die in the worst of cases :c)
*/
def simulateShoots(historyShoot: List[Shoot]): List[Shoot] = {
  val current: Shoot = simulateShoot(historyShoot.last.competitorState)
  if (current.competitorState.resistance < 5)
    return historyShoot;
  val updatedHistoryShoots: List[Shoot] =
    historyShoot :+ current
  return simulateShoots(updatedHistoryShoots)
}

/*
  This function will execute all the shoots per round of a competitor, and returns a structure that contains all.
*/
def simulateCompetitorRound(competitor: Competitor): CompetitorRound = {
  val initialShoot = simulateShoot(competitor)
  val shoots: List[Shoot] = simulateShoots(List(initialShoot))
  CompetitorRound(
    competitor,
    shoots
  )
}

/*
  This is like the after - match time, we gonna see the best of the match, and give him the extra shoot, and later give to him the score for win the round!
  Also, the competitors get tired and claims a new luck from the universe (just a joke, the luck comes from a pseudorandom generator).
*/
def updateCompetitorsStatisticsAfterRound(
    competitors: List[Competitor],
    historyRounds: List[Round]
): List[Competitor] = {
  val bestsOfMatch = competitors.getBestOfMatch()
  val best: Competitor = if (bestsOfMatch.length == 1) {
    bestsOfMatch(0)
  } else {
    simulateTieBreakerForBest(bestsOfMatch)
  }
  competitors
    .map { competitor =>
      if (competitor.name == best.name) competitor.copy(experience = competitor.experience + SCORE_FOR_WIN_ROUND)
      else competitor
    }
    .getTiredAndNewLuck(historyRounds)
}

/*
  This function runs the rounds in the game, there are 2 ways to stop this, the firstone is that noone has enough resistance to continue,
  and the secondone is that pass the round number 10.
  This will return a list of rounds, that is essentially a Game!
*/
def simulateRounds(competitors: List[Competitor], historyRounds: List[Round], round: Int): List[Round] = {
  if (competitors.checkifSomeoneStillResistance() && round <= 10) {
    val shoots = competitors.map(competitor => simulateCompetitorRound(competitor))
    val shootsOfLuckiest: List[Shoot] = simulateExtraShootForLuckiests(historyRounds, competitors)
    val currentRound = Round(shoots, shootsOfLuckiest)
    val updatedHistory: List[Round] = historyRounds :+ currentRound
    val updatedCompetitors = updateCompetitorsStatisticsAfterRound(competitors, historyRounds)
    return simulateRounds(
      updatedCompetitors,
      updatedHistory,
      round + 1
    )
  }
  return historyRounds
}
/*
  The root function, from here, all the 20000 game will be simulated
*/
def runSimulation(amountOfGames: Int): List[Game] = {
  val competitors = getAllCompetitors()
  0.until(amountOfGames)
    .map(_ =>
      val rounds = simulateRounds(competitors, List(), 0)
      Game(rounds)
    )
    .toList
}
