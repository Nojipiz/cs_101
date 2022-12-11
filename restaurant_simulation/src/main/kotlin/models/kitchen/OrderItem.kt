package models.kitchen

import models.kitchen.plates.Plate

data class OrderItem(
    val plate: Plate,
    val plateType: SpecialtyType,
    val idGroup: Long
    ) {

    companion object {
        private var idCounter: Long = 1
    }

    private val id: Long = idCounter++
}