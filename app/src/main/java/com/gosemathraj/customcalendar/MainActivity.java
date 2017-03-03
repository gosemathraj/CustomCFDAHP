package com.gosemathraj.customcalendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CalendarView;
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
import static com.gosemathraj.customcalendar.R.id.end;
import static com.gosemathraj.customcalendar.R.id.start;
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
        startActivityForResult(intent,1);

        Toast.makeText(this,"Event Clicked" + event.getStartTime(),Toast.LENGTH_SHORT).show();
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
        bundle.putInt("fragmentId",1);

        Intent intent = new Intent(this,AddEventActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1 &&  data != null){
            if(resultCode == 1){

                WeekViewEvent newEvent = new WeekViewEvent();
                Events event = (Events) data.getExtras().getSerializable("addEvent");

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.DAY_OF_MONTH,event.getStartDay());
                startTime.set(Calendar.MONTH,event.getStartMonth());
                startTime.set(Calendar.YEAR,event.getStartYear());
                startTime.set(Calendar.HOUR_OF_DAY,event.getStartHour());
                startTime.set(Calendar.MINUTE,event.getStartMinute());

                Calendar endTime = Calendar.getInstance();
                endTime.set(Calendar.DAY_OF_MONTH,event.getEndDay());
                endTime.set(Calendar.MONTH,event.getEndMonth());
                endTime.set(Calendar.YEAR,event.getEndYear());
                endTime.set(Calendar.HOUR_OF_DAY,event.getEndHour());
                endTime.set(Calendar.MINUTE,event.getEndMinute());

                int count = SharedPref.getInstance(this).getCount();
                if(count == -1){
                    event.setId(0);
                }else{
                    event.setId(count + 1);
                }

                newEvent.setId(event.getId());
                newEvent.setStartTime(startTime);
                newEvent.setEndTime(endTime);
                newEvent.setName(event.getEventName());

                if(event.getEventName().split("--")[0].equals("Consultation")){
                    newEvent.setColor(getResources().getColor(R.color.consultation_color));
                }else{
                    newEvent.setColor(getResources().getColor(R.color.followup_color));
                }

                weekViewEvents.add(newEvent);
                weekView.notifyDatasetChanged();

                RealmController.getInstance().addAppointment(event);
                SharedPref.getInstance(this).setCount(count + 1);
            }else if(resultCode == 2){
                Events e = (Events) data.getExtras().getSerializable("deleteEvent");
                for(int i = 0;i < weekViewEvents.size();i++){
                    if(weekViewEvents.get(i).getId() == e.getId()){
                        weekViewEvents.remove(i);
                        break;
                    }
                }
                weekView.notifyDatasetChanged();
                RealmController.getInstance().deleteAppointment(e.getId());
            }else if(resultCode == 3){
                Events e = (Events) data.getExtras().getSerializable("updateEvent");
                for(int i = 0;i < weekViewEvents.size();i++){
                    if(weekViewEvents.get(i).getId() == e.getId()){
                        WeekViewEvent w = weekViewEvents.get(i);
                        w.setId(e.getId());
                        w.setName(e.getEventName());

                        Calendar start = Calendar.getInstance();
                        start .set(Calendar.DAY_OF_MONTH,e.getStartDay());
                        start .set(Calendar.MONTH,e.getStartMonth());
                        start .set(Calendar.YEAR,e.getStartYear());
                        start .set(Calendar.HOUR_OF_DAY,e.getStartHour());
                        start .set(Calendar.MINUTE,e.getStartMinute());

                        Calendar end = Calendar.getInstance();
                        end.set(Calendar.DAY_OF_MONTH,e.getEndDay());
                        end.set(Calendar.MONTH,e.getEndMonth());
                        end.set(Calendar.YEAR,e.getEndYear());
                        end.set(Calendar.HOUR_OF_DAY,e.getEndHour());
                        end.set(Calendar.MINUTE,e.getEndMinute());

                        w.setStartTime(start);
                        w.setEndTime(end);

                        if(e.getEventName().split("--")[0].equals("Consultation")){
                            w.setColor(getResources().getColor(R.color.consultation_color));
                        }else{
                            w.setColor(getResources().getColor(R.color.followup_color));
                        }

                        break;
                    }
                }
                weekView.notifyDatasetChanged();
                RealmController.getInstance().updateAppointment(e);
            }
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
