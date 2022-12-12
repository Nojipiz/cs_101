package models.cashBox

import models.customers.CostumerGroup
import models.rating.Rating
import models.timers.Time
import utilities.Generator

class Invoice(time: Time, costumerGroup: CostumerGroup?, waiterRating: Rating?) {
    val id: Long
    val time: Time
    val costumerGroup: CostumerGroup?
    val waiterRating: Rating?
    var paymentMethod: PaymentMethod
    val paymentType: PaymentType

    init {
        id = idCounter++
        this.time = time
        this.costumerGroup = costumerGroup
        this.waiterRating = waiterRating
        val randomMethod = Generator.generateRandomNumber(0, 2)
        val randomType = Generator.generateRandomNumber(0, 2)
        paymentMethod = if (randomMethod == 0) PaymentMethod.AMERICANO else if (randomMethod == 1) PaymentMethod.DIVIDIDO else PaymentMethod.UNICO
        paymentType = if (randomType == 0) PaymentType.CASH else if(randomType == 1) PaymentType.BANK_TRANSACTION else PaymentType.CREDIT_CARD
    }

    companion object {
        private var idCounter: Long = 1
    }
}