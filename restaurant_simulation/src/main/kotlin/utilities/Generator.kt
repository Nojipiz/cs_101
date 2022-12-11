package utilities

import kotlin.random.Random

object Generator {

    private val pseudorandomGenerate: Random = Random

    fun generateRandomNumber(min: Int, max: Int) = ((Math.random() * (max - min + 1)).toInt() + min)

    fun hasBeenBadCooked():Boolean {
        return when(pseudorandomGenerate.nextFloat()){
            in 0.0 .. 0.1 -> true
            else -> false
        }
    }

    fun clientRejectCook() = pseudorandomGenerate.nextBoolean()
}