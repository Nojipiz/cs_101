package models.timers

class Time(
    var hour: Int,
    var minute: Int,
    var second: Int
) {

    fun increaseHour() {
        hour += 1
    }

    fun increaseMinute() {
        minute = minute + 1
    }

    fun increaseSecond() {
        second += 1
    }

    fun beforeThan(time: Time?): Boolean {
        if (time == null)
            return false
        return hour <= time.hour
    }

    fun addTime(time: Time?) {
        second += time!!.second
        minute += time.minute
        hour += time.hour
        var aditionalTime: Int
        if (second >= 60) {
            aditionalTime = second - 60
            second = aditionalTime
            minute++
        }
        if (minute >= 60) {
            aditionalTime = minute - 60
            minute = aditionalTime
            hour++
        }
        if (hour >= 24) {
            aditionalTime = hour - 24
            hour = aditionalTime
        }
    }

    override fun toString(): String {
        return "$hour:$minute:$second"
    }
}