package com.example.GoogleCalendar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Months;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class GooglecalenderView extends LinearLayout {
    private Context context;
    private ViewPager2 viewPager;
    private MonthChangeListner monthChangeListner;
    private int currentmonth = 0;
    private LocalDate mindate, maxdate;
    private HashMap<LocalDate, EventInfo> eventuser = new HashMap<>();
    private int mDefaultEventColor = Color.parseColor("#9fc6e7");
    public GooglecalenderView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.viewpagerlay, this);
        this.context = context;


    }

    public GooglecalenderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.viewpagerlay, this);
        this.context = context;


    }

    public GooglecalenderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.viewpagerlay, this);
        this.context = context;


    }

    public GooglecalenderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.viewpagerlay, this);
        this.context = context;


    }

    public void setMonthChangeListner(MonthChangeListner monthChangeListner) {
        this.monthChangeListner = monthChangeListner;
    }

    public int calculateCurrentMonth(LocalDate currentmonthda) {
        if (currentmonthda == null||mindate==null) return 0;
        LocalDate mindateobj = mindate.toDateTimeAtStartOfDay().dayOfMonth().withMinimumValue().toLocalDate();
        LocalDate current = currentmonthda.dayOfMonth().withMaximumValue();
        int months = Months.monthsBetween(mindateobj, current).getMonths();
        return months;
    }


    public void setCurrentmonth(LocalDate currentmonthda) {
        if (mindate==null)return;
        currentmonth = calculateCurrentMonth(currentmonthda);
        if (viewPager.getCurrentItem() != currentmonth) {
            viewPager.setCurrentItem(currentmonth, false);
            //  viewPager.getAdapter().notifyDataSetChanged();
        }
        updategrid();
    }

    public void init(HashMap<LocalDate, EventInfo> eventhashmap, LocalDate mindate, LocalDate maxdate) {

        eventuser = new HashMap<>(eventhashmap);
        viewPager = findViewById(R.id.viewpager);

        this.mindate = mindate;
        this.maxdate = maxdate;
        DateTime mindateobj = mindate.toDateTimeAtStartOfDay();
        DateTime maxdateobj = maxdate.toDateTimeAtStartOfDay();
        int months = Months.monthsBetween(mindateobj, maxdateobj).getMonths();

        final ArrayList<MonthModel> arrayList = new ArrayList<>();
        HashMap<LocalDate, EventInfo> eventhash = new HashMap<>();

        for (int i = 0; i <= months; i++) {

            int firstday = mindateobj.dayOfMonth().withMinimumValue().dayOfWeek().get();
            if (firstday == 7) firstday = 0;
            int lastday = mindateobj.dayOfMonth().withMaximumValue().dayOfWeek().get();
            MonthModel month = new MonthModel();
            month.setMonthnamestr(mindateobj.toString("MMMM"));
            month.setMonth(mindateobj.getMonthOfYear());
            month.setNoofday(mindateobj.dayOfMonth().getMaximumValue());
            month.setYear(mindateobj.getYear());
            month.setFirstday(firstday);
            int currentyear = new LocalDate().getYear();
            ArrayList<DayModel> dayModelArrayList = new ArrayList<>();
            DateTime startday = mindateobj.dayOfMonth().withMinimumValue().withTimeAtStartOfDay();
            LocalDate minweek = startday.dayOfWeek().withMinimumValue().toLocalDate().minusDays(1);
            while (minweek.compareTo(startday.dayOfMonth().withMaximumValue().toLocalDate()) < 0) {
                if (minweek.getMonthOfYear() == minweek.plusDays(6).getMonthOfYear()) {
                    String lastpattern = minweek.getYear() == currentyear ? "d MMM" : "d MMM YYYY";

                    String s[] = {"tojigs" + minweek.toString("d").toUpperCase() + " - " + minweek.plusDays(6).toString(lastpattern).toUpperCase()};

                    if (!eventhash.containsKey(minweek)) eventhash.put(minweek, new EventInfo(s));

                    minweek = minweek.plusWeeks(1);

                } else {

                    String lastpattern = minweek.getYear() == currentyear ? "d MMM" : "d MMM YYYY";
                    String s[] = {"tojigs" + minweek.toString("d MMM").toUpperCase() + " - " + minweek.plusDays(6).toString(lastpattern).toUpperCase()};


                    if (!eventhash.containsKey(minweek)) eventhash.put(minweek, new EventInfo(s));

                    minweek = minweek.plusWeeks(1);
                }


            }

            for (int j = 1; j <= month.getNoofday(); j++) {

                DayModel dayModel = new DayModel();
                dayModel.setDay(startday.getDayOfMonth());
                dayModel.setMonth(startday.getMonthOfYear());
                dayModel.setYear(startday.getYear());
                if (eventuser.containsKey(startday.toLocalDate())) {
                    if (eventhash.containsKey(startday.toLocalDate())) {
                        EventInfo eventInfo=eventhash.get(startday.toLocalDate());
                        List<String> list = Arrays.asList(eventInfo.eventtitles);
                        list = new ArrayList<>(list);
                        for (String s : eventuser.get(startday.toLocalDate()).eventtitles) {
                            list.add(s);
                        }
                        String[] mStringArray = new String[list.size()];
                        String[] s = list.toArray(mStringArray);
                        eventInfo.eventtitles=s;
                        eventhash.put(startday.toLocalDate(), eventInfo);

                    } else {
                        eventhash.put(startday.toLocalDate(), eventuser.get(startday.toLocalDate()));
                    }
                    dayModel.setEventlist(true);

                }

                if (startday.toLocalDate().equals(new LocalDate())) {
                    dayModel.setToday(true);
                    currentmonth = i;
                } else {
                    dayModel.setToday(false);
                }
                dayModelArrayList.add(dayModel);

                if (j == 1) {
                    EventInfo eventInfo1=new EventInfo();

                    String s[] = {"start"};
                    eventInfo1.eventtitles=s;
                    if (eventhash.containsKey(startday.toLocalDate())) {
                        EventInfo eventInfo=eventhash.get(startday.toLocalDate());
                        List<String> list = Arrays.asList(eventInfo.eventtitles);
                        list = new ArrayList<>(list);
                        list.add(0, "start");
                        String[] mStringArray = new String[list.size()];
                        s = list.toArray(mStringArray);
                        eventInfo.eventtitles=s;
                        eventInfo1=eventInfo;

                    }

                    eventhash.put(startday.toLocalDate(), eventInfo1);
                }
                startday = startday.plusDays(1);

            }
            month.setDayModelArrayList(dayModelArrayList);
            arrayList.add(month);
            mindateobj = mindateobj.plusMonths(1);

        }


        final MonthPagerAdapter myPagerAdapter = new MonthPagerAdapter(context, arrayList);

        viewPager.setAdapter(myPagerAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

                MainActivity mainActivity = (MainActivity) context;
                currentmonth = position;

                if (!mainActivity.isAppBarClosed()) {

                    adjustheight();
                    if (mainActivity.mNestedView.getVisibility()==VISIBLE)EventBus.getDefault().post(new MessageEvent(new LocalDate(myPagerAdapter.monthModels.get(position).getYear(), myPagerAdapter.monthModels.get(position).getMonth(), 1)));
                    else {
                        MainActivity.lastdate=new LocalDate(myPagerAdapter.monthModels.get(position).getYear(), myPagerAdapter.monthModels.get(position).getMonth(), 1);
                    }

                    updategrid();

                    if (monthChangeListner != null)
                        monthChangeListner.onmonthChange(myPagerAdapter.monthModels.get(position));
                }

            }
        });

        LocalDate todaydate = LocalDate.now();
        if (!eventhash.containsKey(todaydate)) {

            eventhash.put(todaydate,new EventInfo( new String[]{"todaydate"}));
        } else {

            List<String> list = Arrays.asList(eventhash.get(todaydate).eventtitles);
            list = new ArrayList<>(list);

            String[] mStringArray = new String[list.size()];
            EventInfo eventInfo=eventhash.get(todaydate);
            eventInfo.eventtitles=list.toArray(mStringArray);
            eventhash.put(todaydate, eventInfo);
        }
        Map<LocalDate, EventInfo> treeMap = new TreeMap<LocalDate, EventInfo>(eventhash);
        HashMap<LocalDate, Integer> indextrack = new HashMap<>();
        int i = 0;
        ArrayList<EventModel> eventModelslist = new ArrayList<>();
        for (HashMap.Entry<LocalDate, EventInfo> localDateStringEntry : treeMap.entrySet()) {

            EventInfo tempinfo=localDateStringEntry.getValue();
            for (String s : localDateStringEntry.getValue().eventtitles) {

                if (s == null) continue;
                int type = 0;
                if (s.startsWith("todaydate")) type = 2;
                else if (s.equals("start")) type = 1;
                else if (s.startsWith("tojigs")) type = 3;

                if (type == 2 && eventModelslist.get(eventModelslist.size() - 1).getType() == 0 && eventModelslist.get(eventModelslist.size() - 1).getLocalDate().equals(localDateStringEntry.getKey())) {

                } else {
                    if (type == 0 && eventModelslist.size() > 0 && eventModelslist.get(eventModelslist.size() - 1).getType() == 0 && !eventModelslist.get(eventModelslist.size() - 1).getLocalDate().equals(localDateStringEntry.getKey())) {

                        eventModelslist.add(new EventModel("dup", localDateStringEntry.getKey(), 100));
                        i++;
                    } else if ((type == 3) && eventModelslist.size() > 0 && eventModelslist.get(eventModelslist.size() - 1).getType() == 0) {
                        eventModelslist.add(new EventModel("dup", eventModelslist.get(eventModelslist.size() - 1).getLocalDate(), 100));
                        i++;
                    } else if ((type == 1) && eventModelslist.size() > 0 && eventModelslist.get(eventModelslist.size() - 1).getType() == 0) {
                        eventModelslist.add(new EventModel("dup", eventModelslist.get(eventModelslist.size() - 1).getLocalDate(), 200));
                        i++;
                    } else if (type == 0 && eventModelslist.size() > 0 && (eventModelslist.get(eventModelslist.size() - 1).getType() == 1)) {
                        eventModelslist.add(new EventModel("dup", localDateStringEntry.getKey(), 200));
                        i++;
                    } else if (type == 2 && eventModelslist.size() > 0 && eventModelslist.get(eventModelslist.size() - 1).getType() == 0) {
                        eventModelslist.add(new EventModel("dup", eventModelslist.get(eventModelslist.size() - 1).getLocalDate(), 100));
                        i++;
                    }
                    String ss=s;
                    int color=mDefaultEventColor;
                    if (type==0){
                        EventInfo myinfo=eventhashmap.get(localDateStringEntry.getKey());
                        while (myinfo!=null&&!myinfo.title.equals(s)){
                            myinfo=myinfo.nextnode;
                        }
                        color=myinfo.eventcolor==0?mDefaultEventColor:myinfo.eventcolor;
                        if (myinfo.noofdayevent>1){
                            ss=ss+" ("+localDateStringEntry.getKey().toString("d MMMM")+"-"+localDateStringEntry.getKey().plusDays(myinfo.noofdayevent-1).toString("d MMMM")+")";
                        }
                        else if (myinfo.isallday==false){
                            LocalDateTime start=new LocalDateTime(myinfo.starttime, DateTimeZone.forID(myinfo.timezone));
                            LocalDateTime end=new LocalDateTime(myinfo.endtime, DateTimeZone.forID(myinfo.timezone));
                            String sf=start.toString("a").equals(end.toString("a"))?"":"a";

                            ss=ss+" ("+start.toString("h:mm "+sf+"")+"-"+end.toString("h:mm a")+")";

                        }


                    }
                    EventModel mModel=new EventModel(ss, localDateStringEntry.getKey(), type);
                    mModel.setColor(color);
                    eventModelslist.add(mModel);
                    indextrack.put(localDateStringEntry.getKey(), i);
                    i++;

                }
            }
        }
        EventBus.getDefault().post(new AddEvent(eventModelslist, indextrack, arrayList));
    }

    public void updategrid() {
        final MonthPagerAdapter myPagerAdapter = (MonthPagerAdapter) viewPager.getAdapter();
        if (myPagerAdapter != null) {
            final int position = viewPager.getCurrentItem();
            RecyclerView recyclerView = (RecyclerView) viewPager.getChildAt(0);


            MonthPagerAdapter.MonthViewHolder monthViewHolder = (MonthPagerAdapter.MonthViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
            if (monthViewHolder != null && monthViewHolder.jCalendarMonthTopView != null ) {

                monthViewHolder.jCalendarMonthTopView.requestLayout();
                monthViewHolder.jCalendarMonthTopView.invalidate();

            }
        }
    }

    public void adjustheight() {
        if (mindate==null)return;
        final MonthPagerAdapter myPagerAdapter = (MonthPagerAdapter) viewPager.getAdapter();
        if (myPagerAdapter != null) {
            final int position = viewPager.getCurrentItem();
            int size = myPagerAdapter.monthModels.get(position).getDayModelArrayList().size() + myPagerAdapter.monthModels.get(position).getFirstday();
            int numbercolumn = size % 7 == 0 ? size / 7 : (size / 7) + 1;
            ViewGroup.LayoutParams params = getLayoutParams();
            int setheight = 75 + (context.getResources().getDimensionPixelSize(R.dimen.itemheight) * numbercolumn) + context.getResources().getDimensionPixelSize(R.dimen.tendp) + getStatusBarHeight();
            if (params.height == setheight) return;
            params.height = setheight;
            setLayoutParams(params);
        }

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    class MonthPagerAdapter extends RecyclerView.Adapter<MonthPagerAdapter.MonthViewHolder> {

        private ArrayList<MonthModel> monthModels;
        private LayoutInflater mInflater;
        private Context context;

        MonthPagerAdapter(Context context, ArrayList<MonthModel> data) {
            this.context = context;
            this.mInflater = LayoutInflater.from(context);
            this.monthModels = data;
        }

        @NonNull
        @Override
        public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.fraglay, parent, false);
            return new MonthViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MonthViewHolder holder, int position) {

            MonthModel monthtemp = monthModels.get(position);

            holder.jCalendarMonthTopView.initdata(monthtemp.getDayModelArrayList(), monthtemp.getFirstday(), monthtemp.getMonth(), monthtemp.getYear());

        }

        @Override
        public int getItemCount() {
            return monthModels.size();
        }


        class MonthViewHolder extends RecyclerView.ViewHolder {

            JCalendarMonthTopView jCalendarMonthTopView;
            MonthViewHolder(View itemView) {
                super(itemView);
                jCalendarMonthTopView = itemView.findViewById(R.id.jcalendarmonthview);


            }
        }
    }

}
