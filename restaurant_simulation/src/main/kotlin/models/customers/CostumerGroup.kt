package models.customers

import models.timers.Time
import utilities.Generator

class CostumerGroup(arrivalTime: Time?) {
    val customerGroupId: Long
    val customerList: Array<Customer?>
    val arrivalTime: Time?
    var departureTime: Time?

    init {
        customerGroupId = idCounter++
        val randomNumber = Generator.generateRandomNumber(1, 4)
        this.arrivalTime = arrivalTime
        departureTime = null
        customerList = arrayOfNulls(randomNumber)
        createCustomersList()
    }

    private fun createCustomersList() {
        for (i in customerList.indices) {
            customerList[i] = Customer()
        }
    }

    companion object {
        private var idCounter: Long = 1
    }
}