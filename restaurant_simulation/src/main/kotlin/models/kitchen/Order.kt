package models.kitchen

import structures.CustomerQueu

class Order {
    private val id: Long
    val orderItemQueue: CustomerQueu<OrderItem?>

    init {
        id = idCounter++
        orderItemQueue = CustomerQueu(null)
        //		this.orderItemList = new ArrayList<>();
    }

    fun addItem(item: OrderItem?) {
        orderItemQueue.push(item)
    }

    //	this.id = idCounter++;		
    val isDOne: Boolean
        get() = if (orderItemQueue.isEmpty) {
            true
        } else false

    companion object {
        private var idCounter: Long = 1
    }
}