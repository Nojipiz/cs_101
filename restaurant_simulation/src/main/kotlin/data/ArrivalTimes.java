package data;

import models.timers.Time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.LoggingPermission;
import java.util.stream.Collectors;

public abstract class ArrivalTimes {

    private final static Random random = new Random();

    private final static String[]  arrivalTimesOne =
            {
                    "00:00:00",
                    "00:13:31",
                    "00:21:43",
                    "00:25:22",
                    "00:33:43",
                    "00:41:32",
                    "00:48:56",
                    "00:53:54",
                    "01:04:35",
                    "01:21:03",
                    "01:33:23",
                    "01:37:31",
                    "01:45:45",
                    "01:52:53",
                    "01:59:25",
                    "02:09:59",
                    "02:15:51",
                    "02:23:06",
                    "02:35:32",
                    "02:42:06",
                    "02:48:36",
                    "02:55:58",
                    "03:04:56",
                    "03:15:42",
                    "03:24:46",
                    "03:31:45",
                    "03:37:13",
                    "03:43:08",
                    "03:50:54",
                    "03:57:53"
            };
    private final static String[]  arrivalTimesTwo =
            {
                    "00:00:00",
                    "00:14:31",
                    "00:22:43",
                    "00:24:22",
                    "00:34:43",
                    "00:40:32",
                    "00:47:56",
                    "00:52:54",
                    "01:03:35",
                    "01:20:03",
                    "01:32:23",
                    "01:37:31",
                    "01:44:45",
                    "01:53:53",
                    "01:59:25",
                    "02:08:59",
                    "02:14:51",
                    "02:22:06",
                    "02:34:32",
                    "02:42:36",
                    "02:48:16",
                    "02:51:38",
                    "03:06:26",
                    "03:15:42",
                    "03:25:46",
                    "03:30:45",
                    "03:36:43",
                    "03:40:38",
                    "03:49:24",
                    "03:52:13"
            };
    private final static String[]  arrivalTimesThree =
            {
                    "00:00:00",
                    "00:13:31",
                    "00:20:43",
                    "00:23:22",
                    "00:32:43",
                    "00:41:32",
                    "00:46:56",
                    "00:53:54",
                    "01:04:35",
                    "01:20:03",
                    "01:33:23",
                    "01:37:31",
                    "01:44:45",
                    "01:51:53",
                    "01:59:25",
                    "02:07:59",
                    "02:13:51",
                    "02:22:06",
                    "02:33:32",
                    "02:40:06",
                    "02:43:36",
                    "02:51:58",
                    "03:04:56",
                    "03:17:42",
                    "03:27:46",
                    "03:32:45",
                    "03:36:13",
                    "03:44:08",
                    "03:51:54",
                    "03:55:53"
            };

    public static ArrayList<Time> getArrivalTime(){
        var option = random.nextBoolean();
        var data = arrivalTimesOne;
        if(option)
            data = arrivalTimesTwo;
        var splitTimeArray = Arrays.stream(data).map(x -> x.split(":")).collect(Collectors.toList());
        var timeList = new ArrayList<Time>();
        splitTimeArray.forEach(x -> timeList.add(new Time(Integer.parseInt(x[0]),Integer.parseInt(x[1]),Integer.parseInt(x[2]))));
        timeList.forEach(System.out::println);
        return timeList;
    }
}
