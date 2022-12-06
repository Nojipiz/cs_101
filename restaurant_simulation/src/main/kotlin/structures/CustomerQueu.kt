package structures

class CustomerQueu<E>(comparator: Comparator<E?>?) : Iterable<E?> {
    private var rootNode: QueuNode<E>? = null
    private val criterian: Comparator<E?>?

    init {
        this.criterian = comparator
    }

    fun push(data: E) {
        rootNode = if (!isEmpty) {
            val currentNode = QueuNode(data)
            currentNode.nextNode = rootNode
            currentNode
        } else QueuNode(data)
    }

    fun pool() {
        var currentNode = rootNode
        var lastNode: QueuNode<E>? = null
        while (currentNode != null) {
            lastNode = if (currentNode.nextNode != null) currentNode else lastNode
            currentNode = currentNode.nextNode
        }
        if (lastNode != null) lastNode.nextNode = null else rootNode = null
    }

    val isEmpty: Boolean
        get() = rootNode == null

    fun peek(): E? {
        var currentNode = rootNode
        var nextNode = rootNode
        while (currentNode != null) {
            nextNode = if (currentNode.nextNode != null) currentNode.nextNode else nextNode
            currentNode = currentNode.nextNode
        }
        return nextNode?.data
    }

    fun search(data: E): E? {
        var currentNode = rootNode
        while (currentNode != null) {
            currentNode = if (criterian!!.compare(currentNode.data, data) == 0) return currentNode.data else currentNode.nextNode
        }
        return null
    }

    override fun iterator(): MutableIterator<E?> {
        return object : MutableIterator<E?> {
            var currentNode = rootNode
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
    inner class QueuNode<E>(var data: E) {
        var nextNode: QueuNode<E>? = null

    }
}
