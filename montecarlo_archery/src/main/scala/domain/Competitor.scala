package domain

case class Competitor(
  val name:String,
  val resistance: Int,
  var experience: Int,
  var luck: Float,
  val gender: Gender,
)

enum Gender: 
  case Male, Female
