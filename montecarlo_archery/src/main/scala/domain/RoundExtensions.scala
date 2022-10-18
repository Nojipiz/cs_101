package domain

extension (rounds: List[Round]) {

  /*
    According to the point "If one shooter wins 9 points of experience, he losses only one point
    of resistance in the next two rounds!"
    This functions checks the history of rounds, to check if the competitor has achieve the 9 points in the last round!
    if he did, we return a true and if he did'nt we return a false.
  */
  def thisCompetitorGetTiredOne(competitor: Competitor): Boolean = {
    val element = List(2, 3)
    if (rounds.size < 3) return false
    val lastRounds: List[Round] = rounds.takeRight(4)
    val lastRoundsExperience: List[Int] =
      lastRounds
        .flatMap(_.playersRounds)
        .filter(_.initialState.name == competitor.name)
        .map(_.initialState.experience)
    return lastRoundsExperience match {
      case ValidFourElements(value)  => true
      case ValidThreeElements(value) => true
      case _                         => false
    }
  }
}

object ValidThreeElements {

  /** This will match if the input is something like this: (13, 16, 19), because 19 (minus 10) is a multiple of 9, that
    * means that this is the first round after a 9 points of experience win for this competitor! Will return Some(value)
    * which means that this is a valid "Just lose one tireness point". This also can match with someting like (22, 25,
    * 28) or (25, 25, 28) or (13, 13, 19)
    */
  def unapply(lastThree: List[Int]): Option[List[Int]] = {
    if (lastThree.size != 3)
      return None
    val lastElement = lastThree.last
    if (lastThree.count(_ == lastElement) > 1)
      return None
    if (isMultipleOfNineExperience(lastElement)) {
      return Some(lastThree)
    }
    return None
  }

  def isMultipleOfNineExperience(number: Int): Boolean = ((number - 10) % 9 == 0)
}

object ValidFourElements {

  /** This will match if the input is something like this: (13, 16, 19, *), because 19 (minus 10) is a multiple of 9,
    * that means that this is the second round after a 9 points of experience win for this competitor! Will return
    * Some(value) which means that this is a valid "Just lose one tireness point". This also can match with someting
    * like (13,16,19, 19) or (22, 25, 28, *) This function also checks for the posibility of only 3 numbers match ( Just
    * in case of human programming error c: )
    */
  def unapply(lastFour: List[Int]): Option[List[Int]] = {
    if (lastFour.size != 4)
      return None
    val lastPossibleNumber = lastFour(2)
    if (lastPossibleNumber - 3 != lastFour(1))
      return None
    if (isMultipleOfNineExperience(lastPossibleNumber)) {
      return Some(lastFour)
    }
    return lastFour.takeRight(3) match {
      case ValidThreeElements(value) => Some(List())
      case _                         => None
    }
  }

  def isMultipleOfNineExperience(number: Int): Boolean = ((number - 10) % 9 == 0)
}
