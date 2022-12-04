package models.kitchen

import models.timers.Time

class Cook(specialy: SpecialtyType) {
    val cookId: Long = idCounter++
    private var isAvailable: Boolean
    val specialy: SpecialtyType
    private var assignedSpeciality: Int
    var nextFreeTime: Time?
        private set
    val orderItemList: Array<OrderItem?>

    init {
        this.specialy = specialy
        isAvailable = true
        assignedSpeciality = 0
        nextFreeTime = Time(1, 1, 0, 0, 0)
        orderItemList = arrayOfNulls(SPECIAL_PLATE_LIMIT)
    }

    fun isAvailable(specialtyType: SpecialtyType?, currentTime: Time?): Boolean {
        if (specialy == specialtyType && assignedSpeciality < SPECIAL_PLATE_LIMIT) {
            isAvailable = true
        } else if (nextFreeTime!!.beforeThan(currentTime)) {
            resetOrderItemList()
            assignedSpeciality = 0
            isAvailable = true
        }
        return isAvailable
    }

    fun cookPlate(orderItem: OrderItem?, currentSimulation: Time?) {
        addOrderItem(orderItem)
        nextFreeTime = orderItem?.plate?.preparationTime
        nextFreeTime!!.addTime(currentSimulation)
        isAvailable = false
        if (specialy == orderItem?.plateType) {
            assignedSpeciality++
        }
    }

    private fun addOrderItem(orderItem: OrderItem?) {
        if (orderItemList[0] != null) {
            orderItemList[0] = orderItem
        } else {
            orderItemList[1] = orderItem
        }
    }

    private fun resetOrderItemList() {
        for (i in orderItemList.indices) {
            orderItemList[i] = null
        }
    }

    companion object {
        private var idCounter: Long = 0
        private const val SPECIAL_PLATE_LIMIT = 2
    }
}