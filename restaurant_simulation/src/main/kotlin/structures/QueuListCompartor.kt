package structures

class QueuListCompartor : Comparator<Int> {
    override fun compare(arg0: Int, arg1: Int): Int {
        return arg0 - arg1
    }
}