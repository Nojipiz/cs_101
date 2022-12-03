package models

class ClaseLeturaEscritura(name: String, price: Long, provider: String, imgPath: String) {
    val id: Long
    val name: String
    val price: Long
    val provider: String
    val imgPath: String

    init {
        id = idCounter++
        this.name = name
        this.price = price
        this.provider = provider
        this.imgPath = imgPath
    }

    companion object {
        var idCounter: Long = 1
    }
}