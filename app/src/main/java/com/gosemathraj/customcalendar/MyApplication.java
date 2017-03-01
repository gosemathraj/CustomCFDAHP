package com.gosemathraj.customcalendar;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmConfiguration.Builder;

/**
 * Created by RajeshG on 01-03-2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext()                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          );
        RealmConfiguration realmConfiguration = new Builder()
                                                .name("appointmentRealm")
                                                .schemaVersion(0)
                                                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
