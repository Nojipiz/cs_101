package models.kitchen

import structures.CustomerQueu

class Order {
    private val id: Long = idCounter++
    val orderItemQueue: CustomerQueu<OrderItem?> = CustomerQueu(null)

    fun addItem(item: OrderItem?) {
        orderItemQueue.push(item)
    }

    companion object {
        private var idCounter: Long = 1
    }
}