package models.kitchen.plates

import models.timers.Time

open class Plate(name: String, consumptionTime: Time, preparationTime: Time, cost: Double) {
    open val id: Long
    val name: String
    val consumptionTime: Time
    val preparationTime: Time
    val cost: Double

    init {
        id = idCounter++
        //		this.type = type;
        this.name = name
        this.consumptionTime = consumptionTime
        this.preparationTime = preparationTime
        this.cost = cost
    }

    companion object {
        var idCounter: Long = 0
            private set
    }
}