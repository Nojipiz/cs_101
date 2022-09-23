package domain

case class Round(
    playersRounds: List[CompetitorRound],
    extraShoots: List[Shoot]
)
