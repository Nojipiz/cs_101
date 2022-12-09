package models

class Waiter(
    val id: Long
    ) {

    companion object {
        private const val MAX_ASSIGNE_TABLES = 3
    }

    private var assignedTables: Int = 0

    var isAvaliable: Boolean = true

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

}