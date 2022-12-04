package models.kitchen.plates

import models.timers.Time

class MainCourse(name: String, consumptionTime: Time, preparationTime: Time, cost: Double) : Plate(name, consumptionTime, preparationTime, cost) {
    override val id: Long = idCounter++

    companion object {
        private var idCounter: Long = 1
    }
}