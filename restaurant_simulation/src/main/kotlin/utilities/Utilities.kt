package utilities

object Utilities {
    fun randomNumber(min: Int, max: Int): Int {
        return (Math.random() * (max - min + 1)).toInt() + min
    }
}