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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.id.empty;

public class MainActivity extends AppCompatActivity implements MonthLoader.MonthChangeListener,WeekView.EventClickListener,
        WeekView.EmptyViewClickListener{

    @BindView(R.id.weekView)
    WeekView weekView;

    private List<WeekViewEvent> weekViewEvents;
    private WeekViewEvent weekViewEvent;
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
        Calendar startTime = Calendar.getInstance();
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
        weekViewEvents.add(weekViewEvent);
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
        Intent intent = new Intent(this,AddEventActivity.class);
        startActivityForResult(intent,1);
    }
}
