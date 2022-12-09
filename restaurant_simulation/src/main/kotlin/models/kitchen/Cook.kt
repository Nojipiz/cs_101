package models.kitchen

import models.timers.Time
import utilities.Generator

class Cook(specialy: SpecialtyType) {
    var cookId: Int = 0
    private var isAvailable: Boolean
    val specialy: SpecialtyType
    private var specialsAssignetCount: Int
    var nextFreeTime: Time?
        private set
    val orderItemList: Array<OrderItem?>

    var efficency: MutableList<Boolean> = mutableListOf()

    init {
        this.specialy = specialy
        isAvailable = true
        specialsAssignetCount = 0
        nextFreeTime = Time(1, 2, 0)
        orderItemList = arrayOfNulls(SPECIAL_PLATE_LIMIT)
    }

    fun isAvailable(specialtyType: SpecialtyType?, currentTime: Time?): Boolean {
        if (specialy == specialtyType && specialsAssignetCount < SPECIAL_PLATE_LIMIT) {
            isAvailable = true
        } else if (nextFreeTime?.beforeThan(currentTime) == true) {
            resetOrderItemList()
            specialsAssignetCount = 0
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
            specialsAssignetCount++
        }
        val badCooked = Generator.hasBeenBadCooked()
        efficency.add(badCooked)
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
        private const val SPECIAL_PLATE_LIMIT = 2
    }
}