package domain

case class Team(
  val name: TeamName,
  val competitors: List[Competitor]
)

enum TeamName(val name:String):
  case TeamA extends TeamName("TeamA")
  case TeamB extends TeamName("TeamB")
