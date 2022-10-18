package domain

case class Competitor(
    team: TeamName,
    name: String,
    resistance: Int,
    experience: Int,
    luck: Float,
    gender: Gender,
    score: Int
)

enum Gender(val logoUrl:String):
  case Male extends Gender("https://d1nhio0ox7pgb.cloudfront.net/_img/g_collection_png/standard/256x256/symbol_male.png")
  case Female extends Gender("https://upload.wikimedia.org/wikipedia/commons/thumb/4/42/Pink_female_symbol.svg/2048px-Pink_female_symbol.svg.png")
