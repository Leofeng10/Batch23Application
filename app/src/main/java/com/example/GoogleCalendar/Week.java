package com.example.GoogleCalendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Week extends Fragment implements View.OnClickListener {

    private  View view;

    public static Week getInstance(){
        Week week = new Week();
        return week;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.week, container, false);
        this.view = view;
        Button weekLeft = (Button) view.findViewById(R.id.buttonWeekLeft);
        Button weekRight = (Button) view.findViewById(R.id.buttonWeekRight);

        weekLeft.setOnClickListener(this);
        weekRight.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        TextView week = (TextView) view.findViewById(R.id.weekText);
        String currentWeek = week.getText().toString();
        switch (v.getId()){
            case R.id.buttonWeekLeft:
                String lastweek = null;
                try {
                    lastweek = lastWeek(currentWeek);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                week.setText(lastweek);
                break;
            case R.id.buttonWeekRight:
                String nextweek = null;
                try {
                    nextweek = nextWeek(currentWeek);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                week.setText(nextweek);
                break;
        }
    }

    public String lastWeek (String currentWeek) throws ParseException {
        if (currentWeek.equals("Select Week")){
            return "Aug 29 - Sep 03";
        }
        else{
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy");
            cal.setTime(sdf.parse("Sun "+ currentWeek.substring(0,6) + " 2021"));
            cal.add(Calendar.DATE, -7);
            String startWeek = cal.getTime().toString().substring(4,10);
            cal.add(Calendar.DATE, 6);
            String endWeek = cal.getTime().toString().substring(4,10);
            return startWeek + " - " + endWeek;
        }
    }
    public String nextWeek (String currentWeek) throws ParseException {
        if (currentWeek.equals("Select Week")){
            return "Aug 29 - Sep 03";
        }
        else{
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy");
            cal.setTime(sdf.parse("Sun "+ currentWeek.substring(0,6) + " 2021"));
            cal.add(Calendar.DATE, 7);
            String startWeek = cal.getTime().toString().substring(4,10);
            cal.add(Calendar.DATE, 6);
            String endWeek = cal.getTime().toString().substring(4,10);
            return startWeek + " - " + endWeek;
        }
    }
}
