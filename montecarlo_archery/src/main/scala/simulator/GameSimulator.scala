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

def simulateRounds(competitors: List[Competitor], historyRounds: List[Round], round: Int): List[Round] = {
  if (competitors.checkifSomeoneStillResistance() && round <= 10) {
    val shoots = competitors.map(competitor => simulateCompetitorRound(competitor))
    val shootsOfLuckiest: List[Shoot] = simulateExtraShootForLuckiests(historyRounds, competitors)
    val currentRound = Round(shoots, shootsOfLuckiest)
    val updatedHistory: List[Round] = historyRounds :+ currentRound
    val tiredCompetitors = competitors.getTired()
    return simulateRounds(
      tiredCompetitors,
      updatedHistory,
      round + 1
    )
  }
  return historyRounds
}

def runSimulation(amountOfGames: Int): Unit = {
  val competitors = getAllCompetitors()
  val round = simulateRounds(competitors, List(), 0)
  print(round)
}
