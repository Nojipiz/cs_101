package simulator

import scala.util.Random.apply
import scala.util.Random
import domain._
import breeze.stats.distributions.Gaussian
import breeze.stats.distributions.Rand.VariableSeed.randBasis

object PseudoRandomState {

  val gaussianDist = Gaussian(mu = COMPETITOR_RESISTANCE_MEAN, sigma = COMPETITOR_RESISTANCE_DESV) 
  val randomGenerator:Random = Random()

  def getBoolean():Boolean = 
    randomGenerator.nextBoolean()

  def getResistance():Int = gaussianDist.get().toInt

  def getLuck(): Float = 
    COMPETITOR_MIN_LUCK + (COMPETITOR_MAX_LUCK - COMPETITOR_MIN_LUCK) * randomGenerator.nextFloat()

  def getGender(): Gender = 
    return if (getBoolean()) Gender.Male else Gender.Female

  def getTiredness(): Int = 
    return if (getBoolean()) 1 else 2
} 
