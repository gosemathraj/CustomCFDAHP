package com.gosemathraj.customcalendar;

import android.content.Intent;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.gosemathraj.customcalendar.Utility.SharedPref;
import com.gosemathraj.customcalendar.model.Events;
import com.gosemathraj.customcalendar.realm.RealmController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

import static android.R.attr.data;
import static com.gosemathraj.customcalendar.R.id.delete;
import static com.gosemathraj.customcalendar.R.id.startTime;

public class MainActivity extends AppCompatActivity implements MonthLoader.MonthChangeListener,WeekView.EventClickListener,
        WeekView.EmptyViewClickListener{

    @BindView(R.id.weekView)
    WeekView weekView;

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;

    private WeekViewEvent weekViewEvent;
    private List<WeekViewEvent> weekViewEvents;

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
        setEventData();
        setListeners();
    }

    private void setEventData() {
        /*Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY,1);
        startTime.set(Calendar.MINUTE,30);
        startTime.set(Calendar.DAY_OF_MONTH,4);
        startTime.set(Calendar.MONTH,2);
        startTime.set(Calendar.YEAR,2017);

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY,2);
        endTime.set(Calendar.MINUTE,45);
        endTime.set(Calendar.DAY_OF_MONTH,5);
        endTime.set(Calendar.MONTH,2);
        endTime.set(Calendar.YEAR,2017);

        weekViewEvent = new WeekViewEvent(1,"New month Event Added",startTime,endTime);
        weekViewEvents.add(weekViewEvent);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY,3);
        startTime.set(Calendar.MINUTE,30);
        startTime.set(Calendar.DAY_OF_MONTH,2);
        startTime.set(Calendar.MONTH,4);
        startTime.set(Calendar.YEAR,2017);

        endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY,5);
        endTime.set(Calendar.MINUTE,45);
        endTime.set(Calendar.DAY_OF_MONTH,2);
        endTime.set(Calendar.MONTH,4);
        endTime.set(Calendar.YEAR,2017);

        weekViewEvent = new WeekViewEvent(2,"Next month event added",startTime,endTime);
        weekViewEvents.add(weekViewEvent);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY,3);
        startTime.set(Calendar.MINUTE,30);
        startTime.set(Calendar.DAY_OF_MONTH,2);
        startTime.set(Calendar.MONTH,3);
        startTime.set(Calendar.YEAR,2017);

        endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY,5);
        endTime.set(Calendar.MINUTE,45);
        endTime.set(Calendar.DAY_OF_MONTH,2);
        endTime.set(Calendar.MONTH,3);
        endTime.set(Calendar.YEAR,2017);

        weekViewEvent = new WeekViewEvent(2,"Next month event added",startTime,endTime);
        weekViewEvents.add(weekViewEvent);*/

        RealmResults<Events> eventsList = RealmController.getInstance().getAllAppointments();
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

        Intent intent = new Intent(this,AddEventActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

        Toast.makeText(this,"Event Clicked" + event.getStartTime(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewClicked(Calendar time) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("time",time);
        bundle.putInt("fragmentId",1);

        Intent intent = new Intent(this,AddEventActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == 1 && data != null){
            if(requestCode == 1){
                Calendar startTime = (Calendar) data.getExtras().getSerializable("startCalendar");
                Calendar endTime = (Calendar) data.getExtras().getSerializable("endCalendar");
                String eventName = data.getExtras().getString("eventName");

                WeekViewEvent newEvent = new WeekViewEvent(10,eventName,startTime,endTime);
                if(eventName.split("--")[0].equals("Consultation")){
                    newEvent.setColor(getResources().getColor(R.color.consultation_color));
                }else{
                    newEvent.setColor(getResources().getColor(R.color.followup_color));
                }
                weekViewEvents.add(newEvent);
                weekView.notifyDatasetChanged();

                int count = SharedPref.getInstance(this).getCount();
                Events events = new Events();
                if(count == -1){
                    events.setId(0);
                }else{
                    events.setId(count + 1);
                }
                events.setEventName(eventName);

                events.setStartDay(startTime.get(Calendar.DAY_OF_MONTH));
                events.setStartMonth(startTime.get(Calendar.MONTH));
                events.setStartYear(startTime.get(Calendar.YEAR));
                events.setStartHour(startTime.get(Calendar.HOUR_OF_DAY));
                events.setStartMinute(startTime.get(Calendar.MINUTE));


                events.setEndDay(endTime.get(Calendar.DAY_OF_MONTH));
                events.setEndMonth(endTime.get(Calendar.MONTH));
                events.setEndYear(endTime.get(Calendar.YEAR));
                events.setEndHour(endTime.get(Calendar.HOUR_OF_DAY));
                events.setEndMinute(endTime.get(Calendar.MINUTE));

                RealmController.getInstance().addAppointment(events);
                SharedPref.getInstance(this).setCount(count + 1);
            }
        }else if(resultCode == 2){
            Events e = (Events) data.getExtras().getSerializable("deleteEvent");
            for(int i = 0;i < weekViewEvents.size();i++){
                if(weekViewEvents.get(i).getId() == e.getId()){
                    weekViewEvents.remove(i);
                }
            }
            weekView.notifyDatasetChanged();
        }
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
        }
        return super.onOptionsItemSelected(item);
    }
}
