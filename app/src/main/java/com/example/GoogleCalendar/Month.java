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



import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.common.collect.ImmutableBiMap;

public class Month extends Fragment implements View.OnClickListener{

    private  View view;

    private static final ImmutableBiMap<Integer, String> mList =new ImmutableBiMap.Builder<Integer, String>()
            .put(1,"Jan")
            .put(2,"Feb")
            .put(3,"Mar")
            .put(4,"Apr")
            .put(5,"May")
            .put(6,"Jun")
            .put(7,"Jul")
            .put(8,"Aug")
            .put(9,"Sep")
            .put(10,"Oct")
            .put(11,"Nov")
            .put(12,"Dec")
            .build();

    public static Month getInstance(){
        Month month = new Month();
        return month;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month, container, false);
        this.view = view;
        Button monthLeft = (Button) view.findViewById(R.id.buttonMonthLeft);
        Button monthRight = (Button) view.findViewById(R.id.buttonMonthRight);

        monthLeft.setOnClickListener(this);
        monthRight.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        TextView month = (TextView) view.findViewById(R.id.monthText);
        String currentMonth = month.getText().toString();
        switch (v.getId()){
            case R.id.buttonMonthLeft:
                String lastmonth = lastMonth(currentMonth);
                month.setText(lastmonth);
                break;
            case R.id.buttonMonthRight:
                String nextmonth = nextMonth(currentMonth);
                month.setText(nextmonth);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    public String lastMonth (String currentMonth){
        if (currentMonth.equals("Select Month")){
            int month = Integer.parseInt(new SimpleDateFormat("MM").format(Calendar.getInstance().getTime()));
            String year = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
            return mList.get(month)+ " " + year;
        }
        else{
            int month = mList.inverse().get(currentMonth.substring(0,3));
            if (month == 1) {
                int year = Integer.parseInt(currentMonth.substring(4));
                return mList.get(12) + " " + String.valueOf(year - 1);
            }
            else{
                return mList.get(month-1) + " " + currentMonth.substring(4);
            }

        }
    }

    public String nextMonth (String currentMonth){
        if (currentMonth.equals("Select Month")){
            int month = Integer.parseInt(new SimpleDateFormat("MM").format(Calendar.getInstance().getTime()));
            String year = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
            return mList.get(month)+ " " + String.valueOf(year);
        }
        else{
            int month = mList.inverse().get(currentMonth.substring(0,3));
            if (month == 12) {
                int year = Integer.parseInt(currentMonth.substring(4));
                return mList.get(1) + " " + String.valueOf(year + 1);
            }
            else{
                return mList.get(month+1) + " " + currentMonth.substring(4);
            }

        }
    }
}
