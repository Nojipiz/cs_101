package domain

case class Competitor(
  team: TeamName,
  name:String,
  resistance: Int,
  experience: Int,
  luck: Float,
  gender: Gender,
  score: Int
)

enum Gender: 
  case Male, Female
