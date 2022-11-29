package structures

class QueuList<E>(comparator: Comparator<E?>?) : Iterable<E?> {
    private var head: QueuNode<E>? = null
    private val comparator: Comparator<E?>?

    init {
        this.comparator = comparator
    }

    fun push(data: E) {
        head = if (!isEmpty) {
            val currentNode = QueuNode(data)
            currentNode.nextNode = head
            currentNode
        } else QueuNode(data)
    }

    fun pool() {
        var currentNode = head
        var lastNode: QueuNode<E>? = null
        while (currentNode != null) {
            lastNode = if (currentNode.nextNode != null) currentNode else lastNode
            currentNode = currentNode.nextNode
        }
        if (lastNode != null) lastNode.nextNode = null else head = null
    }

    val isEmpty: Boolean
        get() = head == null

    fun peek(): E? {
        var currentNode = head
        var nextNode = head
        while (currentNode != null) {
            nextNode = if (currentNode.nextNode != null) currentNode.nextNode else nextNode
            currentNode = currentNode.nextNode
        }
        return nextNode?.data
    }

    fun search(data: E): E? {
        var currentNode = head
        while (currentNode != null) {
            currentNode = if (comparator!!.compare(currentNode.data, data) == 0) return currentNode.data else currentNode.nextNode
        }
        return null
    }

    override fun iterator(): MutableIterator<E?> {
        return object : MutableIterator<E?> {
            var currentNode = head
            override fun hasNext(): Boolean {
                return currentNode != null
            }

            override fun next(): E? {
                val data = currentNode?.data
                currentNode = currentNode?.nextNode
                return data
            }

            override fun remove() {
            }
        }
    }
}