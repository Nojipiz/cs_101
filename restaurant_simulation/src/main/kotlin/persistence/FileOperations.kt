package persistence

import models.Time
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

object FileOperations {
    private const val WEEKS_TO_SIMULATE = 3
    private const val DAYS_PER_WEEKS = 7
    private const val DB_PATH = "./db/data/" //dejar la ruta del archivo
    @Throws(IOException::class)
    fun readFileArrivalGrpuos(TIME_SHORTENER: Int, name_file: String): ArrayList<Time> {
        val listLines: List<String>
        val listArrivalsClients = ArrayList<Time>()
        listLines = Files.readAllLines(Paths.get(DB_PATH + name_file + ".csv"))
        var dataHour: Array<String>
        for (i in listLines.indices) {
            dataHour = listLines[i].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

//			int miliSecondsHour = (Integer.parseInt(dataHour[0])*60*60*1000)/TIME_SHORTENER;
//			int miliSecondsMinute = (Integer.parseInt(dataHour[1])*60*1000)/TIME_SHORTENER;
//			int miliSecondsSeconds = (Integer.parseInt(dataHour[2])*1000)/TIME_SHORTENER;
            val hour = dataHour[0].toInt()
            val minute = dataHour[1].toInt()
            val seconds = dataHour[2].toInt()
            val time = Time(hour, minute, seconds)
            listArrivalsClients.add(time)
        }
        return listArrivalsClients
    }

    //	private void readDataArrivalsClientList(int week_i, int day_i) {
    //		String name_file= "arrivals"+"_"+"week"+week_i+"_"+"day"+day_i;
    //		try {
    //			listTimesArrivalsClients = FileOperations.readFileArrivalsClients(TIME_SHORTENER, name_file);
    //		} catch (IOException e) {
    //			e.printStackTrace();
    //		}
    //	}
    //	public static ArrayList<CostumerGroup> readFileArrivalGrpuos() throws IOException{
    @Throws(IOException::class)
    fun readFile(): ArrayList<Time> {
        var lineList: List<String>
        var name_file: String
        var timeData: Array<String>
        //		ArrayList<CostumerGroup> costumerGroupList = new ArrayList<>();
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
        //		return costumerGroupList;
        return timeList
    }
}