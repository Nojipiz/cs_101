package models

import utilities.Utilities

class CostumerGroup(arrivalTime: Time?) {
    val id: Long
    val clientList: Array<Client?>
    val arrivalTime: Time?
    var departureTime: Time?

    init {
        id = idCounter++
        val randomNumber = Utilities.randomNumber(1, 4)
        this.arrivalTime = arrivalTime
        departureTime = null
        clientList = arrayOfNulls(randomNumber)
        createClientList()
    }

    private fun createClientList() {
        for (i in clientList.indices) {
            clientList[i] = Client()
        }
    }

    companion object {
        private var idCounter: Long = 1
    }
}