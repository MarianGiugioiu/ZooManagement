package zoomanagement.api.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import zoomanagement.api.DTO.DaySchedule;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {
    private static Map<String, Integer> daysOfWeek = Map.ofEntries(
            Map.entry("monday", 1),
            Map.entry("tuesday", 2),
            Map.entry("wednesday", 3),
            Map.entry("thursday", 4),
            Map.entry("friday", 5),
            Map.entry("saturday", 6),
            Map.entry("sunday", 7)
    );

    //Creates a list of DayScheduled based on the corresponding String
    public static List<DaySchedule> getScheduleList(String schedule) {
        List<DaySchedule> scheduleList = new ArrayList<>();
        //Splits the string in substrings: day:day-hour:minute-hour:minute
        String[] days = schedule.split(";");
        for (String day : days) {
            //Splits the string in: day:day, hour:minute, hour:minute
            String[] dayAndHours = day.split("-");

            //Gets range of days
            String[] daysRange = dayAndHours[0].split(":");
            List<Integer> daysToIntRange = new ArrayList<>();
            for (String dayOfWeek : daysRange) {
                daysToIntRange.add(daysOfWeek.get(dayOfWeek));
            }

            //Gets start time
            String[] startTimeList = dayAndHours[1].split(":");
            LocalTime startTime = LocalTime.of(Integer.parseInt(startTimeList[0]), Integer.parseInt(startTimeList[1]));

            //Gets end time
            String[] endTimeList = dayAndHours[2].split(":");
            LocalTime endTime = LocalTime.of(Integer.parseInt(endTimeList[0]), Integer.parseInt(endTimeList[1]));

            //Checks if there is one or more days in range of days
            if (daysToIntRange.size() == 1) {
                scheduleList.add(new DaySchedule(daysToIntRange.get(0), startTime, endTime));
            } else {
                for (int i = daysToIntRange.get(0); i <= daysToIntRange.get(1); i++) {
                    scheduleList.add(new DaySchedule(i, startTime, endTime));
                }
            }
        }
        return scheduleList;
    }

    //Checks if an interval of time is in list of DaySchedule
    public static boolean scheduleContains(List<DaySchedule> scheduleList, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        //Gets day of week from date
        Integer dayToInt = daysOfWeek.get(startDateTime.getDayOfWeek().toString().toLowerCase());

        //Creates start time and end time
        LocalTime startTime = LocalTime.of(startDateTime.getHour(), startDateTime.getMinute());
        LocalTime endTime = LocalTime.of(endDateTime.getHour(), endDateTime.getMinute());

        //Checks day and times are contained in the schedule
        for (DaySchedule daySchedule : scheduleList) {
            if (daySchedule.getDay() == dayToInt) {
                if (startTime.isAfter(daySchedule.getStartTime()) && endTime.isBefore(daySchedule.getEndTime())) {
                    return true;
                }
            }
        }
        return false;
    }
}
