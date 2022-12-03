package models

class ClaseSecundaria1(name: String) {
    val id: Long
    val name: String

    init {
        id = idCounter++
        this.name = name
    }

    companion object {
        var idCounter: Long = 1
    }
}