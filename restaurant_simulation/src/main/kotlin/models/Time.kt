package models

class Time {
    var hour: Int
    var minute: Int
    var second: Int
    var day: Int
    var week: Int

    constructor(week: Int, day: Int, hour: Int, minute: Int, seconds: Int) {
        this.week = week
        this.day = day
        this.hour = hour
        this.minute = minute
        second = seconds
    }

    constructor(hour: Int, minute: Int, seconds: Int) {
        week = 0
        day = 0
        this.hour = hour
        this.minute = minute
        second = seconds
    }

    fun increaseHour() {
        hour = hour + 1
    }

    fun increaseMinute() {
        minute = minute + 1
    }

    fun increaseSecond() {
        second = second + 1
    }

    fun increaseDay() {
        day = day + 1
    }

    fun increaseWeek() {
        week = week + 1
    }

    fun beforeThan(time: Time?): Boolean {
        return week < time!!.week || week == time.week && day < time.day || week == time.week && day == time.day && hour < time.hour || week == time.week && day == time.day && hour == time.hour && minute < time.minute || week == time.week && day == time.day && hour == time.hour && minute == time.minute && second < time.second
    }

    fun addTime(time: Time?) {
        second = second + time!!.second
        minute = minute + time.minute
        hour = hour + time.hour
        day = day + time.day
        week = week + time.week
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
            day++
        }
        if (day >= 8) {
            aditionalTime = day - 8
            day = aditionalTime
            week++
        }
    }

    override fun toString(): String {
        // TODO Auto-generated method stub
        return "semana $week dia $day a las $hour:$minute:$second"
    }
}