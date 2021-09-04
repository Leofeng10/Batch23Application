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

public class Today extends Fragment implements View.OnClickListener{

    private  View view;

    public static Today getInstance(){
        Today today = new Today();
        return today;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today, container, false);
        this.view = view;
        Button dayLeft = view.findViewById(R.id.buttonDayLeft);
        Button dayRight = view.findViewById(R.id.buttonDayRight);

        dayLeft.setOnClickListener(this);
        dayRight.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {
        TextView month1 = (TextView) view.findViewById(R.id.month1);
        TextView month2 = (TextView) view.findViewById(R.id.month2);
        TextView month3 = (TextView) view.findViewById(R.id.month3);
        TextView month4 = (TextView) view.findViewById(R.id.month4);
        TextView month5 = (TextView) view.findViewById(R.id.month5);
        TextView day1 = (TextView) view.findViewById(R.id.day1);
        TextView day2 = (TextView) view.findViewById(R.id.day2);
        TextView day3 = (TextView) view.findViewById(R.id.day3);
        TextView day4 = (TextView) view.findViewById(R.id.day4);
        TextView day5 = (TextView) view.findViewById(R.id.day5);
        TextView week1 = (TextView) view.findViewById(R.id.week1);
        TextView week2 = (TextView) view.findViewById(R.id.week2);
        TextView week3 = (TextView) view.findViewById(R.id.week3);
        TextView week4 = (TextView) view.findViewById(R.id.week4);
        TextView week5 = (TextView) view.findViewById(R.id.week5);
        String currentDay = week1.getText().toString() + " " + month1.getText().toString() + " " + day1.getText().toString();

        switch (v.getId()){
            case R.id.buttonDayRight:
                try {
                    String nextDay = nextDay(currentDay);
                    week1.setText(nextDay.substring(0,3));
                    month1.setText(nextDay.substring(4,7));
                    day1.setText(nextDay.substring(8,10));

                    nextDay = nextDay(nextDay);
                    week2.setText(nextDay.substring(0,3));
                    month2.setText(nextDay.substring(4,7));
                    day2.setText(nextDay.substring(8,10));

                    nextDay = nextDay(nextDay);
                    week3.setText(nextDay.substring(0,3));
                    month3.setText(nextDay.substring(4,7));
                    day3.setText(nextDay.substring(8,10));

                    nextDay = nextDay(nextDay);
                    week4.setText(nextDay.substring(0,3));
                    month4.setText(nextDay.substring(4,7));
                    day4.setText(nextDay.substring(8,10));

                    nextDay = nextDay(nextDay);
                    week5.setText(nextDay.substring(0,3));
                    month5.setText(nextDay.substring(4,7));
                    day5.setText(nextDay.substring(8,10));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.buttonDayLeft:
                try {
                    String lastDay = lastDay(currentDay);
                    week1.setText(lastDay.substring(0,3));
                    month1.setText(lastDay.substring(4,7));
                    day1.setText(lastDay.substring(8,10));

                    String nextDay = nextDay(lastDay);
                    week2.setText(nextDay.substring(0,3));
                    month2.setText(nextDay.substring(4,7));
                    day2.setText(nextDay.substring(8,10));

                    nextDay = nextDay(nextDay);
                    week3.setText(nextDay.substring(0,3));
                    month3.setText(nextDay.substring(4,7));
                    day3.setText(nextDay.substring(8,10));

                    nextDay = nextDay(nextDay);
                    week4.setText(nextDay.substring(0,3));
                    month4.setText(nextDay.substring(4,7));
                    day4.setText(nextDay.substring(8,10));

                    nextDay = nextDay(nextDay);
                    week5.setText(nextDay.substring(0,3));
                    month5.setText(nextDay.substring(4,7));
                    day5.setText(nextDay.substring(8,10));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
        }
    }

    public String nextDay (String currentDay) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy");
        cal.setTime(sdf.parse(currentDay + " 2021"));
        cal.add(Calendar.DATE, 1);
        return cal.getTime().toString().substring(0,10);
    }

    public String lastDay (String currentDay) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy");
        cal.setTime(sdf.parse(currentDay + " 2021"));
        cal.add(Calendar.DATE, -1);
        return cal.getTime().toString().substring(0,10);
    }




}
