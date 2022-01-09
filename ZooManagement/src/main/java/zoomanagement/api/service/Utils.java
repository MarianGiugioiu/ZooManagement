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
    public static List<DaySchedule> getScheduleList(String schedule) {
        List<DaySchedule> scheduleList = new ArrayList<>();
        String[] days = schedule.split(";");
        for (String day : days) {
            String[] dayAndHours = day.split("-");
            String[] daysRange = dayAndHours[0].split(":");
            List<Integer> daysToIntRange = new ArrayList<>();
            for (String dayOfWeek : daysRange) {
                daysToIntRange.add(daysOfWeek.get(dayOfWeek));
            }
            String[] startTimeList = dayAndHours[1].split(":");
            LocalTime startTime = LocalTime.of(Integer.parseInt(startTimeList[0]), Integer.parseInt(startTimeList[1]));

            String[] endTimeList = dayAndHours[2].split(":");
            LocalTime endTime = LocalTime.of(Integer.parseInt(endTimeList[0]), Integer.parseInt(endTimeList[1]));

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

    public static boolean scheduleContains(List<DaySchedule> scheduleList, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Integer dayToInt = daysOfWeek.get(startDateTime.getDayOfWeek().toString().toLowerCase());
        LocalTime startTime = LocalTime.of(startDateTime.getHour(), startDateTime.getMinute());
        LocalTime endTime = LocalTime.of(endDateTime.getHour(), endDateTime.getMinute());
        for (DaySchedule daySchedule : scheduleList) {
            if (daySchedule.getDay() == dayToInt) {
                if (startTime.isAfter(daySchedule.getStartTime()) && endTime.isBefore(daySchedule.getEndTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean validDietSchedule(String schedule) {
        String[] scheduleParts = schedule.split("-");
        if (scheduleParts.length != 3) return false;
        String[] days = scheduleParts[0].split(":");
        String[] startTime = scheduleParts[1].split(":");
        String[] endTime = scheduleParts[2].split(":");
        if (days.length != 2 || startTime.length != 2 || endTime.length != 2) return false;

        if (!daysOfWeek.containsKey(days[0]) || !daysOfWeek.containsKey(days[1])) {
            return false;
        } else {
            if (daysOfWeek.get(days[0]) >= daysOfWeek.get(days[1])) {
                return false;
            }
        }

        return true;
    }
}
