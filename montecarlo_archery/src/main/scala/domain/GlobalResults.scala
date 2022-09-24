package domain

case class GlobalResults(
    winnerTeam: Option[(TeamName, Int)],
    winnerGender: Option[(Gender, Int)]
)
