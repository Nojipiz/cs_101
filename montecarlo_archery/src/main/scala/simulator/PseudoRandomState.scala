package simulator

import scala.util.Random.apply
import scala.util.Random
import domain._
import breeze.stats.distributions.Gaussian
import breeze.stats.distributions.Rand.VariableSeed.randBasis

object PseudoRandomState {

  val gaussianDist = Gaussian(mu = COMPETITOR_RESISTANCE_MEAN, sigma = COMPETITOR_RESISTANCE_DESV)
  private val randomGenerator: Random = Random()
  private val maleMontecarloRandomGenerator: Random = Random()
  private val femaleMontecarloRandomGenerator: Random = Random()

  def getBoolean(): Boolean = randomGenerator.nextBoolean()

  def getResistance(): Int = gaussianDist.get().toInt

  def getLuck(): Float = COMPETITOR_MIN_LUCK + (COMPETITOR_MAX_LUCK - COMPETITOR_MIN_LUCK) * randomGenerator.nextFloat()

  def getGender(): Gender =
    return if (getBoolean()) Gender.Male
    else Gender.Female

  def getTiredness(): Int =
    return if (getBoolean()) 1 else 2

  def getMaleMontecarloShoot(): Int = {
    val shootValue = maleMontecarloRandomGenerator.nextInt(100)
    if (shootValue < 20)
      10
    else if (shootValue < 53)
      9
    else if (shootValue < 93)
      8
    else
      0
  }

  def getFemaleMontecarloShoot(): Int = {
    val shootValue = femaleMontecarloRandomGenerator.nextInt(100)
    if (shootValue < 30)
      10
    else if (shootValue < 68)
      9
    else if (shootValue < 95)
      8
    else
      0
  }
}
