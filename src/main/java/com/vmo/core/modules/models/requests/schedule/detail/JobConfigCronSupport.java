package com.vmo.core.modules.models.requests.schedule.detail;

import java.time.DayOfWeek;
import java.util.List;

public class JobConfigCronSupport {
    private List<Integer> seconds;
    private List<Integer> minutes;
    private List<Integer> hours;
    private List<DayOfWeek> daysOfWeek;
    private List<Integer> daysOfMonth;
    private List<Integer> months;

    public List<Integer> getSeconds() {
        return seconds;
    }

    public void setSeconds(List<Integer> seconds) {
        this.seconds = seconds;
    }

    public List<Integer> getMinutes() {
        return minutes;
    }

    public void setMinutes(List<Integer> minutes) {
        this.minutes = minutes;
    }

    public List<Integer> getHours() {
        return hours;
    }

    public void setHours(List<Integer> hours) {
        this.hours = hours;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public List<Integer> getDaysOfMonth() {
        return daysOfMonth;
    }

    public void setDaysOfMonth(List<Integer> daysOfMonth) {
        this.daysOfMonth = daysOfMonth;
    }

    public List<Integer> getMonths() {
        return months;
    }

    public void setMonths(List<Integer> months) {
        this.months = months;
    }
}
