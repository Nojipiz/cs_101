package data;

import models.timers.Time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ArrivalTimes {
    private final static String[]  arrivalTimes =
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
    public static ArrayList<Time> getArrivalTime(){
        var splitTimeArray = Arrays.stream(arrivalTimes).map(x -> x.split(":")).collect(Collectors.toList());
        var timeList = new ArrayList<Time>();
        splitTimeArray.forEach(x -> timeList.add(new Time(Integer.parseInt(x[0]),Integer.parseInt(x[1]),Integer.parseInt(x[2]))));
        timeList.forEach(x -> System.out.println(x));
        return timeList;
    }

    public static void main(String[] args) {
        getArrivalTime();
    }
}
