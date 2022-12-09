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
    fun readFile(): MutableList<Time> {
        val timeList = mutableListOf<Time>()
        var lineList: List<String>
        var timeData: Array<String>
        var name_file: String

        (1..WEEKS_TO_SIMULATE).forEach { week ->
            (1..DAYS_PER_WEEKS).forEach { day ->
                name_file = "arrivals" + "_" + "week" + week + "_" + "day" + day
                lineList = Files.readAllLines(Paths.get(DB_PATH + name_file + ".csv"))
                lineList.forEachIndexed() { indexed, line ->
                    timeData = lineList[indexed].split(":".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    timeList.add(
                        Time(
                            timeData[0].toInt(),
                            timeData[1].toInt(),
                            timeData[2].toInt()
                        )
                    )
                }
            }
        }
        return timeList
    }
}