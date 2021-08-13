package com.example.GoogleCalendar;

import java.util.Arrays;

public class EventInfo {
    public String[] eventtitles;
    public boolean isallday;
    public int id;
    public String accountname;
    public int noofdayevent;
    public long starttime;
    public long endtime;
    public EventInfo nextnode;
    public String title;
    public String timezone;
    public int eventcolor;
    public EventInfo(String[] eventtitles){
        this.eventtitles=eventtitles;
    }
    public EventInfo(){
    }

    @Override
    public String toString() {
        return "EventInfo{" +
                "eventtitles=" + Arrays.toString(eventtitles) +
                ", isallday=" + isallday +
                ", id=" + id +
                ", accountname='" + accountname + '\'' +
                ", noofdayevent=" + noofdayevent +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                ", nextnode=" + nextnode +
                ", title='" + title + '\'' +
                ", timezone='" + timezone + '\'' +
                ", eventcolor=" + eventcolor +
                '}';
    }
}
