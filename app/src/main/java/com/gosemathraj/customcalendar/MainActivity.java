package com.gosemathraj.customcalendar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.gosemathraj.customcalendar.model.Events;
import com.gosemathraj.customcalendar.data.DbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MonthLoader.MonthChangeListener,WeekView.EventClickListener,
        WeekView.EmptyViewClickListener{

    @BindView(R.id.weekView)
    WeekView weekView;

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;

    private List<WeekViewEvent> weekViewEvents;
    private DbHelper dbHelper;

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        try{
            init();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void init() {
        weekViewEvents = new ArrayList<>();
        dbHelper = new DbHelper(this);
        //setEventData();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventData();
    }

    private void setEventData() {
        weekViewEvents.clear();
        List<Events> eventsList = dbHelper.getAllAppointments();
        for(int i = 0;i < eventsList.size();i++){
            WeekViewEvent we = new WeekViewEvent();
            we.setId(eventsList.get(i).getId());
            we.setName(eventsList.get(i).getEventName());

            Calendar s = Calendar.getInstance();
            s.set(Calendar.DAY_OF_MONTH,eventsList.get(i).getStartDay());
            s.set(Calendar.MONTH,eventsList.get(i).getStartMonth());
            s.set(Calendar.YEAR,eventsList.get(i).getStartYear());
            s.set(Calendar.HOUR_OF_DAY,eventsList.get(i).getStartHour());
            s.set(Calendar.MINUTE,eventsList.get(i).getStartMinute());

            Calendar e = Calendar.getInstance();
            e.set(Calendar.DAY_OF_MONTH,eventsList.get(i).getEndDay());
            e.set(Calendar.MONTH,eventsList.get(i).getEndMonth());
            e.set(Calendar.YEAR,eventsList.get(i).getEndYear());
            e.set(Calendar.HOUR_OF_DAY,eventsList.get(i).getEndHour());
            e.set(Calendar.MINUTE,eventsList.get(i).getEndMinute());

            we.setStartTime(s);
            we.setEndTime(e);

            if(eventsList.get(i).getEventName().split("--")[0].equals("Consultation")){
                we.setColor(getResources().getColor(R.color.consultation_color));
            }else{
                we.setColor(getResources().getColor(R.color.followup_color));
            }

            weekViewEvents.add(we);
        }
        weekView.notifyDatasetChanged();
    }

    private void setListeners() {
        weekView.setEmptyViewClickListener(this);
        weekView.setMonthChangeListener(this);
        weekView.setOnEventClickListener(this);
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        Log.d(LOG_TAG,String.valueOf(newMonth) + "/" + String.valueOf(newYear));
        List<WeekViewEvent> events = getEvents(newYear,newMonth);
        return events;
    }

    private List<WeekViewEvent> getEvents(int newYear, int newMonth) {
        List<WeekViewEvent> tempEvents = new ArrayList<>();
        tempEvents.clear();
        for(WeekViewEvent event : weekViewEvents){
            if(event.getStartTime().get(Calendar.MONTH) + 1 == newMonth &&
                    event.getStartTime().get(Calendar.YEAR) == newYear){
                tempEvents.add(event);
            }
        }
        return tempEvents;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

        Events e = new Events();
        e.setId(event.getId());
        e.setEventName(event.getName());

        e.setStartDay(event.getStartTime().get(Calendar.DAY_OF_MONTH));
        e.setStartMonth(event.getStartTime().get(Calendar.MONTH));
        e.setStartYear(event.getStartTime().get(Calendar.YEAR));
        e.setStartHour(event.getStartTime().get(Calendar.HOUR_OF_DAY));
        e.setStartMinute(event.getStartTime().get(Calendar.MINUTE));

        e.setEndDay(event.getEndTime().get(Calendar.DAY_OF_MONTH));
        e.setEndMonth(event.getEndTime().get(Calendar.MONTH));
        e.setEndYear(event.getEndTime().get(Calendar.YEAR));
        e.setEndHour(event.getEndTime().get(Calendar.HOUR_OF_DAY));
        e.setEndMinute(event.getEndTime().get(Calendar.MINUTE));

        Bundle bundle = new Bundle();
        bundle.putSerializable("event",e);
        bundle.putInt("fragmentId",2);

        Intent intent = new Intent(MainActivity.this,AddEventActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onEmptyViewClicked(Calendar time) {

        Events e = new Events();

        e.setStartDay(time.get(Calendar.DAY_OF_MONTH));
        e.setStartMonth(time.get(Calendar.MONTH));
        e.setStartYear(time.get(Calendar.YEAR));
        e.setStartHour(time.get(Calendar.HOUR_OF_DAY));
        e.setStartMinute(time.get(Calendar.MINUTE));

        e.setEndDay(time.get(Calendar.DAY_OF_MONTH));
        e.setEndMonth(time.get(Calendar.MONTH));
        e.setEndYear(time.get(Calendar.YEAR));
        e.setEndHour(time.get(Calendar.HOUR_OF_DAY) + 1);
        e.setEndMinute(time.get(Calendar.MINUTE));

        e.setEventName("");

        Bundle bundle = new Bundle();
        bundle.putSerializable("event",e);
        bundle.putInt("operationType",1);
        bundle.putInt("fragmentId",1);

        Intent intent = new Intent(this,AddEventActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id){
            case R.id.action_today:
                weekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    weekView.setNumberOfVisibleDays(1);

                    weekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    weekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    weekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    weekView.setNumberOfVisibleDays(3);

                    weekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    weekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    weekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    weekView.setNumberOfVisibleDays(7);

                    weekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    weekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    weekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.jump_to_date:
                    showCalendarDialog();
                    return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCalendarDialog() {
        CalendarView cal;
        final Calendar jumpDate = Calendar.getInstance();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.calendar_view);
        cal = (CalendarView) dialog.findViewById(R.id.calendarView);

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                jumpDate.set(Calendar.YEAR,year);
                jumpDate.set(Calendar.MONTH,month);
                jumpDate.set(Calendar.DAY_OF_MONTH,day);

                weekView.goToDate(jumpDate);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
