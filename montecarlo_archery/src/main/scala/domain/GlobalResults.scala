package domain

case class GlobalResults(
    winnerTeam: Option[(TeamName, Int, String)],
    winnerGender: Option[Gender]
)
