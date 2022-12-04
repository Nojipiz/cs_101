package models

class Rating(type: QualificationType, reference: Int, score: Int) {
    private val ratingId: Long
    val type: QualificationType
    private val code: Int
    val score: Int

    init {
        ratingId = idCounter++
        this.type = type
        this.code = reference
        this.score = score
    }

    fun getcode(): Int {
        return code
    }

    companion object {
        private var idCounter: Long = 1
    }
}