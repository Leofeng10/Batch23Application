package com.example.GoogleCalendar.weekview;

import java.util.Calendar;

public interface DateTimeInterpreter {
    String interpretday(Calendar date);
    String interpretDate(Calendar date);
    String interpretTime(int hour);
}
