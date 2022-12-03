package models

class Waiter {
    // TODO Auto-generated method stub
    val id: Long
    private var assignedTables: Int
    var isAvaliable: Boolean
        private set

    init {
        id = idCounter++
        assignedTables = 0
        isAvaliable = true
    }

    fun catchTable() {
        assignedTables++
        if (assignedTables == MAX_ASSIGNE_TABLES) {
            isAvaliable = false
        }
    }

    fun releaseTable() {
        assignedTables--
        if (assignedTables == 2) {
            isAvaliable = true
        }
    }

    companion object {
        private var idCounter: Long = 1
        private const val MAX_ASSIGNE_TABLES = 3
    }
}