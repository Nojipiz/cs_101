package models

import utilities.Utilities

class Invoice(time: Time, costumerGroup: CostumerGroup?, waiterQuialification: Quialification?) {
    val id: Long
    val time: Time
    val costumerGroup: CostumerGroup?
    val waiterQuialification: Quialification?
    var paymentMethod: PaymentMethod
    val paymentType: PaymentType

    init {
        id = idCounter++
        this.time = time
        this.costumerGroup = costumerGroup
        this.waiterQuialification = waiterQuialification
        val randomMethod = Utilities.randomNumber(0, 2)
        val randomType = Utilities.randomNumber(0, 1)
        paymentMethod = if (randomMethod == 0) PaymentMethod.AMERICANO else if (randomMethod == 1) PaymentMethod.DIVIDIDO else PaymentMethod.UNICO
        paymentMethod = if (randomMethod == 0) PaymentMethod.AMERICANO else if (randomMethod == 1) PaymentMethod.DIVIDIDO else PaymentMethod.UNICO
        paymentType = if (randomType == 1) PaymentType.EFECTIVO else PaymentType.TARJETA
    }

    companion object {
        private var idCounter: Long = 1
    }
}