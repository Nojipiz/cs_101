package simulator

import domain._
import simulator.Verificators.checkifSomeoneStillResistance
import scala.util.Random.apply
import scala.util.Random

def simulateShoot(competitor: Competitor):Shoot = {
  val updatedCompetitor = Competitor(
    team = competitor.team,
    name = competitor.name, 
    resistance = competitor.resistance - SHOOT_RESISTANCE_COST,
    experience = competitor.experience,
    luck = competitor.luck,
    gender = competitor.gender,
    score = competitor.score + 1
    )
  Shoot(updatedCompetitor) 
}

def simulateShoots(historyShoot:List[Shoot]):List[Shoot] = {
  val current:Shoot = simulateShoot(historyShoot.last.competitorState)
  val updatedHistoryShoots:List[Shoot] = historyShoot :+ current
  if(current.competitorState.resistance < 5)
    return updatedHistoryShoots;
  return simulateShoots(updatedHistoryShoots)
}

def simulateCompetitorRound(competitor: Competitor):CompetitorRound = {
  val initialShoot = simulateShoot(competitor)
  val shoots = simulateShoots(List(initialShoot))
  CompetitorRound(
    competitor,
    shoots
  )
}

def simulateRounds(competitors:List[Competitor], historyRounds:List[Round]):List[Round] = {
  if(competitors.checkifSomeoneStillResistance()){
    val shoots = competitors.map(competitor => 
      simulateCompetitorRound(competitor)
    )
    val currentRound = Round(shoots)
    val updatedHistory = historyRounds :+ currentRound
    return simulateRounds(competitors, updatedHistory)
  }
  return historyRounds
}

def runSimulation(amountOfGames: Int): Unit = {
  val competitors = getAllCompetitors()
  val round = simulateRounds(competitors, List())
  print(round)
}
