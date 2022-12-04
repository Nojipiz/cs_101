package models.customers

import models.Time
import utilities.Utilities

class CostumerGroup(arrivalTime: Time?) {
    val customerGroupId: Long
    val customerList: Array<Customer?>
    val arrivalTime: Time?
    var departureTime: Time?

    init {
        customerGroupId = idCounter++
        val randomNumber = Utilities.randomNumber(1, 4)
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