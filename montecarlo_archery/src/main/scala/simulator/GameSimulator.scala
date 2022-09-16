package simulator

import domain._
import scala.util.Random.apply
import scala.util.Random

def getTeams(): List[Team] = 
  List(
    Team(name = TeamName.TeamA, competitors = getCompetitors(TeamName.TeamA.name)),
    Team(name = TeamName.TeamB, competitors = getCompetitors(TeamName.TeamB.name))
  )

def getCompetitors(teamName:String):List[Competitor] = 
  0.until(TEAM_MEMBERS).map(index => 
      Competitor(
        name = s"Competitor ${index+1} $teamName",
        resistance = PseudoRandomState.getResistance(), 
        experience = COMPETITOR_EXPERIENCE,
        luck = PseudoRandomState.getLuck(),
        gender = PseudoRandomState.getGender() 
      )
  ).toList

def runSimulation(amountOfGames: Int): Unit = {
  val teams:List[Team] = getTeams()
  val simulations:IndexedSeq[Unit] = 0.until(amountOfGames).map(element => 
      print(element)
  )
  println(teams)
}
  
