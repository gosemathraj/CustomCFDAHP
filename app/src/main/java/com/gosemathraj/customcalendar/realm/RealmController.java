package com.gosemathraj.customcalendar.realm;

import android.app.Application;

import com.gosemathraj.customcalendar.model.Events;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by RajeshG on 01-03-2017.
 */

public class RealmController {

    private static RealmController realmController;
    private Realm realm;

    public static RealmController getInstance(){
        if(realmController == null){
            realmController = new RealmController();
        }
        return realmController;
    }

    public RealmController() {
        this.realm = Realm.getDefaultInstance();
    }

    public Realm getRealm(){
        return realm;
    }

    public void clearAllAppointments(){

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    public void addAppointment(Events event){
        realm.beginTransaction();

        Events events = realm.createObject(Events.class,event.getId());

        events.setStartDay(event.getStartDay());
        events.setStartMonth(event.getStartMonth());
        events.setStartYear(event.getStartYear());
        events.setStartHour(event.getStartHour());
        events.setStartMinute(event.getStartMinute());

        events.setEndDay(event.getEndDay());
        events.setEndMonth(event.getEndMonth());
        events.setEndYear(event.getEndYear());
        events.setEndHour(event.getEndHour());
        events.setEndMinute(event.getEndMinute());

        events.setEventName(event.getEventName());
        realm.commitTransaction();
    }

    public void updateAppointment(Events event){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(event);
        realm.commitTransaction();
    }

    public void deleteAppointment(long id){
        realm.beginTransaction();

        RealmResults<Events> results = realm.where(Events.class)
                .equalTo("id", id)
                .findAll();

        results.deleteAllFromRealm();
        realm.commitTransaction();
    }

    public RealmResults<Events> getAllAppointments(){
        RealmResults<Events> events = realm.where(Events.class).findAll();
        return events;
    }
}
