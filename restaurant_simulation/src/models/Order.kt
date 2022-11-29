package models

import structures.QueuList

class Order {
    private val id: Long
    val orderItemQueue: QueuList<OrderItem?>

    init {
        id = idCounter++
        orderItemQueue = QueuList(null)
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