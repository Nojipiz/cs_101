package test

import models.Time

object Test {
    @JvmStatic
    fun main(args: Array<String>) {
//		System.out.println(Utilities.randomNumber(-5, -2));
//		System.out.println(Utilities.randomNumber(-5, -2));
//		System.out.println(Utilities.randomNumber(-5, -2));
//		System.out.println(Utilities.randomNumber(-5, -2));
//		System.out.println(Utilities.randomNumber(-5, -2));
//		System.out.println(Utilities.randomNumber(-5, -2));
        val time = Time(0, 0, 1, 32, 40)
        println(time.beforeThan(Time(0, 0, 1, 32, 40)))
        //		time.addTime(new Time(0, 30, 40));
//		System.out.println(time);
    }
}