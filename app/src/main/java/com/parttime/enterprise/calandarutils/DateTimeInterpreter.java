package com.parttime.enterprise.calandarutils;

import java.util.Calendar;

/**
 * Created by vikash on 2019.
 */
public interface DateTimeInterpreter {
    String interpretDate(Calendar date);
    String interpretTime(int hour);
    String interpretWeek(int date);
}
