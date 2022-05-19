package com.vmo.core.common.utils.schedule;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleUtils {
    //validated input before call this!!
    public static String buildCron(
            List<Integer> seconds,
            List<Integer> minutes,
            List<Integer> hours,
            List<DayOfWeek> daysOfWeek,
            List<Integer> daysOfMonth,
            List<Integer> months
    ) {
        return (toCronFromNumbers(seconds) +
                toCronFromNumbers(minutes) +
                toCronFromNumbers(hours) +
                toCronFromWeekDays(daysOfWeek) +
                toCronFromNumbers(daysOfMonth) +
                toCronFromNumbers(months))
                .trim();
    }

    public static String buildCronEverydayAt(Integer hour, Integer minute, Integer second) {
        validate(hour, minute, second);
        return toCron(second) + toCron(minute) + toCron(hour) + "* * *";
    }

    private static String toCron(Integer number) {
        if (number == null) return "* ";
        return number + " ";
    }

    private static String toCron(DayOfWeek day) {
        if (day == null) return "* ";
        return day.name().substring(0, 3);
    }

    private static String toCronFromNumbers(List<Integer> numbers) {
        if (numbers == null || numbers.size() < 1) return "* ";
        return String.join(",", numbers.stream().map(number -> String.valueOf(number))
                .collect(Collectors.toList())) + " ";
    }

    private static String toCronFromWeekDays(List<DayOfWeek> days) {
        if (days == null || days.size() < 1) return "* ";
        return String.join(",", days.stream().map(day -> toCron(day))
                .collect(Collectors.toList())) + " ";
    }

    private static void validate(Integer hours, Integer minutes, Integer seconds) {
        if ((hours != null && (hours < 0 || hours > 24)) ||
                (minutes != null && (minutes < 0 || minutes > 60)) ||
                (seconds != null && (seconds < 0 || seconds > 60))
        ) {
            throw new IllegalArgumentException();
        }
    }
}
