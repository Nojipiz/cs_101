package models.kitchen

import models.timers.Time
import utilities.Generator

class Cook(specialy: SpecialtyType) {
    var cookId: Int = 0
    private var isAvailable: Boolean = true
    val specialy: SpecialtyType
    private var specialsAssignetCount: Int
    var nextFreeTime: Time?
        private set
    val orderItemList: Array<OrderItem?>
    var efficency: MutableList<Boolean> = mutableListOf()

    init {
        this.specialy = specialy
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

    /**
     * Here the chef cooks the order and the determine of is was a bad cook or not, and if the client will receive the cook or will reject it
     */
    fun cookPlate(orderItem: OrderItem?, currentSimulation: Time?) {
        isAvailable = false
        addOrderItem(orderItem)
        nextFreeTime = orderItem?.plate?.preparationTime
        nextFreeTime!!.addTime(currentSimulation)
        if (specialy == orderItem?.plateType) {
            specialsAssignetCount++
        }
        val badCooked = Generator.hasBeenBadCooked()
        efficency.add(badCooked)
        if (badCooked && Generator.clientRejectCook()) {
            addOrderItem(orderItem?.copy())
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
        private const val SPECIAL_PLATE_LIMIT = 2
    }
}