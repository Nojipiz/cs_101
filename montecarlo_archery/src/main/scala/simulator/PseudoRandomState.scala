package simulator

import scala.util.Random.apply
import scala.util.Random

import domain._

object PseudoRandomState {
  val randomGenerator:Random = Random()

  def getBoolean():Boolean = 
    randomGenerator.nextBoolean()

  def getResistance():Int = 
    COMPETITOR_RESISTANCE + (if (getBoolean()) COMPETITOR_RESISTANCE_VARIABLE else -COMPETITOR_RESISTANCE_VARIABLE)

  def getLuck(): Float = 
    COMPETITOR_MIN_LUCK +(COMPETITOR_MAX_LUCK - COMPETITOR_MIN_LUCK) * randomGenerator.nextFloat()

  def getGender(): Gender = 
    return if (getBoolean()) Gender.Male else Gender.Female
} 
