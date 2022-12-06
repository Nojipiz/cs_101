package utilities

object Generator {
    fun generateRandomNumber(min: Int, max: Int) = ((Math.random() * (max - min + 1)).toInt() + min)
}