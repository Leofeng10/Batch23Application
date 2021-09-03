package com.example.GoogleCalendar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.time.Duration;
import java.util.Arrays;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.core.app.ActivityCompat;

/**
 * this class is to read event
 */
public class Utility {


    public static HashMap<LocalDate, EventInfo> localDateHashMap = new HashMap<>();

    public static HashMap<LocalDate, EventInfo> readCalendarEvent(Context context, LocalDate mintime, LocalDate maxtime) {


        int f = 1;
        String selection = "(( " + CalendarContract.Events.SYNC_EVENTS + " = " + f + " ) AND ( " + CalendarContract.Events.DTSTART + " >= " + mintime.toDateTimeAtStartOfDay().getMillis() + " ) AND ( " + CalendarContract.Events.DTSTART + " <= " + maxtime.toDateTimeAtStartOfDay().getMillis() + " ))";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            return null;
        }

        Cursor cursor = context.getContentResolver().query(
                CalendarContract.Events.CONTENT_URI,
                new String[]{"_id", //0
                        "title", //1
                        "description",//2
                        "dtstart", //3
                        "dtend", //4
                        "eventLocation", //5
                        "calendar_displayName", //6
                        CalendarContract.Events.ALL_DAY, //7
                        CalendarContract.Events.EVENT_COLOR, //8
                        CalendarContract.Events.CALENDAR_COLOR, //9
                        CalendarContract.Events.EVENT_TIMEZONE, //10
                        CalendarContract.Events.DURATION //11
                }, null,
                null, null);


        cursor.moveToFirst();
        // fetching calendars name


        // fetching calendars id
        String syncacc = null;
        while (cursor.moveToNext()) {
            Log.d("test", "3456345634563456345634");

            syncacc = cursor.getString(6);

            if (true) {

                LocalDate localDate = getDate(Long.parseLong(cursor.getString(3)));

                if (!localDateHashMap.containsKey(localDate)) {
                    //do not have an event that starts at the same time
                    EventInfo eventInfo = new EventInfo();
                    eventInfo.id = cursor.getInt(0);
                    eventInfo.starttime = cursor.getLong(3);
                    eventInfo.endtime = cursor.getLong(4);
                    if (cursor.getString(11)!=null) eventInfo.endtime = eventInfo.starttime+RFC2445ToMilliseconds(cursor.getString(11));
                    eventInfo.accountname=cursor.getString(6);
                    eventInfo.timezone = cursor.getString(10);
                    eventInfo.eventtitles = new String[]{cursor.getString(1)};
                    eventInfo.isallday = cursor.getInt(7) == 1 ? true : false;
                    eventInfo.title = cursor.getString(1);
                    eventInfo.eventcolor = cursor.getInt(8)==0? Color.parseColor("#009688"):cursor.getInt(8);
                    Log.d("time1", eventInfo.starttime + "  " + eventInfo.endtime);

                    long difference=eventInfo.endtime-eventInfo.starttime;

                    if (difference>86400000){
                        if (cursor.getInt(7)==0){
                            eventInfo.endtime=eventInfo.endtime+86400000l;
                        }

                        LocalDateTime localDate1=new LocalDateTime( eventInfo.starttime, DateTimeZone.forID(eventInfo.timezone)).withTime(0,0,0,0);
                        LocalDateTime localDate2=new LocalDateTime( eventInfo.endtime, DateTimeZone.forID(eventInfo.timezone)).withTime(23, 59, 59, 999);

                        int day = Days.daysBetween(localDate1,localDate2).getDays();
                        eventInfo.noofdayevent=day;
                        eventInfo.isallday=true;
                    } else if (difference<86400000) {
                        eventInfo.noofdayevent=0;
                    } else {
                        eventInfo.noofdayevent=1;
                    }

                    localDateHashMap.put(localDate, eventInfo);


                } else {
                    //there is an event that start at the same time
                    EventInfo eventInfo = localDateHashMap.get(localDate);
                    EventInfo prev = eventInfo;

                    //eventinfo is a linked list, iterate to the end of the list and append a new event
                    while (prev.nextnode!=null)prev=prev.nextnode;

                    String[] s = eventInfo.eventtitles;

                    boolean isneed = true;
                    for (int i = 0; i < s.length; i++) {
                        if (s[i].equals(cursor.getString(1))) {
                            isneed = false;
                            break;
                        }

                    }

                    if (isneed) {

                        String ss[] = Arrays.copyOf(s, s.length + 1);
                        ss[ss.length - 1] = cursor.getString(1);
                        eventInfo.eventtitles = ss;

                        EventInfo nextnode = new EventInfo();
                        nextnode.id = cursor.getInt(0);
                        nextnode.starttime =cursor.getLong(3);
                        nextnode.endtime = cursor.getLong(4);
                        Log.d("time2", nextnode.starttime + "  " + nextnode.endtime);
                        if (cursor.getString(11)!=null) nextnode.endtime = nextnode.starttime + RFC2445ToMilliseconds(cursor.getString(11));

                        nextnode.isallday = cursor.getInt(7) == 1 ? true : false;
                        nextnode.timezone = cursor.getString(10);
                        nextnode.title = cursor.getString(1);
                        nextnode.accountname=cursor.getString(6);
                        nextnode.eventcolor = cursor.getInt(8)==0? Color.parseColor("#009688"):cursor.getInt(8);
                        long difference = nextnode.endtime-nextnode.starttime;

                        if (difference > 86400000){
                            if (cursor.getInt(7)==0){
                                nextnode.endtime=nextnode.endtime+86400000l;
                            }
                            nextnode.isallday=true;
                            LocalDateTime localDate1=new LocalDateTime( nextnode.starttime, DateTimeZone.forID(nextnode.timezone)).withTime(0,0,0,0);
                            LocalDateTime localDate2=new LocalDateTime( nextnode.endtime, DateTimeZone.forID(nextnode.timezone)).withTime(23, 59, 59, 999);


                            int day = Days.daysBetween(localDate1,localDate2).getDays();

                            nextnode.noofdayevent=day;

                        } else if (difference<86400000){
                            eventInfo.noofdayevent=0;
                        } else {
                            eventInfo.noofdayevent=1;
                        }

                        //insert event manualy
                        EventInfo nextnextnode = new EventInfo();
                        nextnextnode.accountname = "fengzhunyi@gmail.com";
                        nextnextnode.id = 50;
                        nextnextnode.isallday = false;
                        nextnextnode.starttime =cursor.getLong(3);
                        nextnextnode.endtime = cursor.getLong(4);
                        nextnextnode.title = "leo create";
                        nextnextnode.timezone = "Asia/Shanghai";
                        nextnextnode.eventcolor = Color.parseColor("#009688");



                        prev.nextnode = nextnode;
                        nextnode.nextnode = nextnextnode;


                        localDateHashMap.put(localDate, eventInfo);
                    }

                }
            }


        }


//        LocalDate localDate = LocalDate.now();
//        EventInfo e = new EventInfo();
//        e.accountname = "fengzhunyi@gmail.com";
//        e.id = 49;
//        e.isallday = false;
//        e.starttime = cursor.getLong(3);
//        e.endtime = cursor.getLong(4);
//        e.title = "leo create";
//        e.timezone = "Asia/Shanghai";
//        e.eventcolor = Color.parseColor("#009688");
//        localDateHashMap.put(localDate, e);
//        Log.d("time", localDate.toString());

        return localDateHashMap;
    }

    /**
     * parse date to milliseconds
     * @param str
     * @return
     */
    public static long RFC2445ToMilliseconds(String str)
    {


        if(str == null || str.isEmpty())
            throw new IllegalArgumentException("Null or empty RFC string");

        int sign = 1;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        int len = str.length();
        int index = 0;
        char c;

        c = str.charAt(0);

        if (c == '-') {
            sign = -1;
            index++;
        } else if (c == '+') {
            index++;
        }


        if (len < index)
            return 0;

        c = str.charAt(index);

        if (c != 'P')
            throw new IllegalArgumentException("Duration.parse(str='" + str + "') expected 'P' at index="+ index);

        index++;
        c = str.charAt(index);
        if (c == 'T')
            index++;

        int n = 0;
        for (; index < len; index++)
        {
            c = str.charAt(index);

            if (c >= '0' && c <= '9') {
                n *= 10;
                n += ((int)(c-'0'));
            } else if (c == 'W') {
                weeks = n;
                n = 0;
            } else if (c == 'H') {
                hours = n;
                n = 0;
            } else if (c == 'M') {
                minutes = n;
                n = 0;
            } else if (c == 'S') {
                seconds = n;
                n = 0;
            } else if (c == 'D') {
                days = n;
                n = 0;
            } else if (c == 'T') {
            } else
                throw new IllegalArgumentException ("Duration.parse(str='" + str + "') unexpected char '" + c + "' at index=" + index);
        }

        long factor = 1000 * sign;
        long result = factor * ((7*24*60*60*weeks)
                + (24*60*60*days)
                + (60*60*hours)
                + (60*minutes)
                + seconds);

        return result;
    }


    public static LocalDate getDate(long milliSeconds) {
        Instant instantFromEpochMilli
                = Instant.ofEpochMilli(milliSeconds);
        return instantFromEpochMilli.toDateTime(DateTimeZone.getDefault()).toLocalDate();

    }
}