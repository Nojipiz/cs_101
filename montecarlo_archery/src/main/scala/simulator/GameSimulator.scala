package simulator
import domain._
import utils._
import scala.util.Random.apply
import scala.util.Random

def simulateExtraShootForLuckiests(historyRounds: List[Round], competitors: List[Competitor]): List[Shoot] = {
  val (luckyA, luckyB) = competitors.getLuckiestEachTeam()
  val shoots: List[Shoot] = List(simulateShoot(luckyA), simulateShoot(luckyB))
  val lastestLuckiests: List[Competitor] = historyRounds.takeRight(3).flatMap(_.extraShoots).map(_.competitorState);
  val extraShootLuckyA = lastestLuckiests.checkIfHasTwoLucksBefore(luckyA)
  val extraShootLuckyB = lastestLuckiests.checkIfHasTwoLucksBefore(luckyB)
  if (extraShootLuckyA)
    shoots :+ simulateShoot(luckyA, true)
  else if (extraShootLuckyB)
    shoots :+ simulateShoot(luckyB, true)
  else
    shoots
}

def simulateArrowWithMontecarlo(competitor: Competitor): Int = {
  return competitor.gender match {
    case Gender.Male =>
      PseudoRandomState.getMaleMontecarloShoot()
    case Gender.Female =>
      PseudoRandomState.getFemaleMontecarloShoot()
  }
}

def simulateShoot(competitor: Competitor, threeInARow: Boolean = false): Shoot = {
  val updatedCompetitor = Competitor(
    team = competitor.team,
    name = competitor.name,
    resistance = competitor.resistance - SHOOT_RESISTANCE_COST,
    experience = competitor.experience,
    luck = competitor.luck,
    gender = competitor.gender,
    score = competitor.score + simulateArrowWithMontecarlo(competitor)
  )
  Shoot(updatedCompetitor, threeInARow)
}

def simulateShoots(historyShoot: List[Shoot]): List[Shoot] = {
  val current: Shoot = simulateShoot(historyShoot.last.competitorState)
  val updatedHistoryShoots: List[Shoot] =
    historyShoot :+ current
  if (current.competitorState.resistance < 5)
    return updatedHistoryShoots;
  return simulateShoots(updatedHistoryShoots)
}

def simulateCompetitorRound(competitor: Competitor): CompetitorRound = {
  val initialShoot = simulateShoot(competitor)
  val shoots: List[Shoot] = simulateShoots(List(initialShoot))
  CompetitorRound(
    competitor,
    shoots
  )
}

def simulateTieBreakerForBest(bests: List[Competitor]): Competitor = {
  val shoots = bests.map(competitor => simulateShoot(competitor))
  val tieBreakerBest: List[Competitor] = shoots.map(_.competitorState).getBestOfMatch()
  if (tieBreakerBest.length > 1)
    simulateTieBreakerForBest(bests)
  else
    tieBreakerBest.last
}

def updateCompetitorsStatisticsAfterRound(competitors: List[Competitor]): List[Competitor] = {
  val tiredAndLuckyCompetitors = competitors.getTiredAndNewLuck()
  val bestsOfMatch = competitors.getBestOfMatch()
  val best: Competitor = if (bestsOfMatch.length == 1) bestsOfMatch.last else simulateTieBreakerForBest(bestsOfMatch)
  tiredAndLuckyCompetitors.map { case best => best.copy(experience = best.experience + SCORE_FOR_WIN_ROUND) }
}

def simulateRounds(competitors: List[Competitor], historyRounds: List[Round], round: Int): List[Round] = {
  if (competitors.checkifSomeoneStillResistance() && round <= 10) {
    val shoots = competitors.map(competitor => simulateCompetitorRound(competitor))
    val shootsOfLuckiest: List[Shoot] = simulateExtraShootForLuckiests(historyRounds, competitors)
    val currentRound = Round(shoots, shootsOfLuckiest)
    val updatedHistory: List[Round] = historyRounds :+ currentRound
    val updatedCompetitors = updateCompetitorsStatisticsAfterRound(competitors)
    return simulateRounds(
      updatedCompetitors,
      updatedHistory,
      round + 1
    )
  }
  return historyRounds
}

def runSimulation(amountOfGames: Int): Game = {
  val competitors = getAllCompetitors()
  val rounds = simulateRounds(competitors, List(), 0)
  Game(rounds)
}
