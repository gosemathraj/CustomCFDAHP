package com.gosemathraj.customcalendar;

import android.content.Intent;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.gosemathraj.customcalendar.Utils.SharedPref;
import com.gosemathraj.customcalendar.model.Events;
import com.gosemathraj.customcalendar.realm.RealmController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.R.id.empty;

public class MainActivity extends AppCompatActivity implements MonthLoader.MonthChangeListener,WeekView.EventClickListener,
        WeekView.EmptyViewClickListener{

    @BindView(R.id.weekView)
    WeekView weekView;


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
        Toast.makeText(this,"Event Clicked" + event.getStartTime(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewClicked(Calendar time) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("time",time);

        Intent intent = new Intent(this,AddEventActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(data != null){

                Calendar startTime = (Calendar) data.getExtras().getSerializable("startCalendar");
                Calendar endTime = (Calendar) data.getExtras().getSerializable("endCalendar");
                String eventName = data.getExtras().getString("eventName");

                WeekViewEvent newEvent = new WeekViewEvent(10,eventName,startTime,endTime);
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
        }
    }
}
