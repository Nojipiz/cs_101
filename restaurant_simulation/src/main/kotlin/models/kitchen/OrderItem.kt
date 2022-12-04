package models.kitchen

import models.kitchen.plates.Plate

class OrderItem(plate: Plate, plateType: SpecialtyType, val idGroup: Long) {
    private val id: Long
    val plate: Plate
    val plateType: SpecialtyType

    init {
        id = idCounter++
        this.plate = plate
        this.plateType = plateType
    }

    companion object {
        private var idCounter: Long = 1
    }
}