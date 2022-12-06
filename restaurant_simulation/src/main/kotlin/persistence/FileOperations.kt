package persistence

import models.timers.Time
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object FileOperations {
    private const val WEEKS_TO_SIMULATE = 3
    private const val DAYS_PER_WEEKS = 7
    private const val DB_PATH = "./db/data/" //dejar la ruta del archivo

    @Throws(IOException::class)
    fun readFile(): ArrayList<Time> {
        var lineList: List<String>
        var name_file: String
        var timeData: Array<String>
        val timeList = ArrayList<Time>()
        for (week_i in 1..WEEKS_TO_SIMULATE) {
            for (day_i in 1..DAYS_PER_WEEKS) {
                name_file = "arrivals" + "_" + "week" + week_i + "_" + "day" + day_i
                lineList = Files.readAllLines(Paths.get(DB_PATH + name_file + ".csv"))
                for (i in lineList.indices) {
                    timeData = lineList[i].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                    //				int miliSecondsHour = (Integer.parseInt(dataHour[0])*60*60*1000)/TIME_SHORTENER;
                    //				int miliSecondsMinute = (Integer.parseInt(dataHour[1])*60*1000)/TIME_SHORTENER;
                    //				int miliSecondsSeconds = (Integer.parseInt(dataHour[2])*1000)/TIME_SHORTENER;
                    val hour = timeData[0].toInt()
                    val minute = timeData[1].toInt()
                    val seconds = timeData[2].toInt()

//					CostumerGroup costumerGroup = new CostumerGroup(new Time(week_i, day_i, hour, minute, seconds));
                    val time = Time(week_i, day_i, hour, minute, seconds)
                    //					costumerGroupList.add(costumerGroup);
                    timeList.add(time)
                }
            }
        }
        return timeList
    }
}