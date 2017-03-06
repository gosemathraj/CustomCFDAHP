package com.gosemathraj.customcalendar.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gosemathraj.customcalendar.model.Events;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RajeshG on 06-03-2017.
 */

public class DbHelper extends SQLiteOpenHelper{

    private final static String DATABASE_NAME = "appointment.db";
    private final static int DATABASE_VERSION = 1;

    private final static String TABLE_NAME = "appointment";
    private final static String APPOINTMENT_ID = "appointment_id";
    private final static String APPOINTMENT_NAME = "appoint_name";
    private final static String APPOINTMENT_START_DAY = "start_day";
    private final static String APPOINTMENT_START_MONTH = "start_month";
    private final static String APPOINTMENT_START_YEAR = "start_year";
    private final static String APPOINTMENT_START_HOUR = "start_hour";
    private final static String APPOINTMENT_START_MINUTE = "start_minute";
    private final static String APPOINTMENT_END_DAY = "end_day";
    private final static String APPOINTMENT_END_MONTH = "end_month";
    private final static String APPOINTMENT_END_YEAR = "end_year";
    private final static String APPOINTMENT_END_HOUR = "end_hour";
    private final static String APPOINTMENT_END_MINUTE = "end_minute";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + TABLE_NAME + "(" +
                        APPOINTMENT_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        APPOINTMENT_NAME + " TEXT NOT NULL, " +
                        APPOINTMENT_START_DAY + " INTEGER NOT NULL, " +
                        APPOINTMENT_START_MONTH + " INTEGER NOT NULL, " +
                        APPOINTMENT_START_YEAR + " INTEGER NOT NULL, " +
                        APPOINTMENT_START_HOUR + " INTEGER NOT NULL, " +
                        APPOINTMENT_START_MINUTE + " INTEGER NOT NULL, " +
                        APPOINTMENT_END_DAY + " INTEGER NOT NULL, " +
                        APPOINTMENT_END_MONTH + " INTEGER NOT NULL, " +
                        APPOINTMENT_END_YEAR + " INTEGER NOT NULL, " +
                        APPOINTMENT_END_HOUR + " INTEGER NOT NULL, " +
                        APPOINTMENT_END_MINUTE + " INTEGER NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
    }

    public void addAppointment(Events event){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(APPOINTMENT_NAME,event.getEventName());
        contentValues.put(APPOINTMENT_START_DAY,event.getStartDay());
        contentValues.put(APPOINTMENT_START_MONTH,event.getStartMonth());
        contentValues.put(APPOINTMENT_START_YEAR,event.getStartYear());
        contentValues.put(APPOINTMENT_START_HOUR,event.getStartHour());
        contentValues.put(APPOINTMENT_START_MINUTE,event.getStartMinute());
        contentValues.put(APPOINTMENT_END_DAY,event.getEndDay());
        contentValues.put(APPOINTMENT_END_MONTH,event.getEndMonth());
        contentValues.put(APPOINTMENT_END_YEAR,event.getEndYear());
        contentValues.put(APPOINTMENT_END_HOUR,event.getEndHour());
        contentValues.put(APPOINTMENT_END_MINUTE,event.getEndMinute());

        db.insert(TABLE_NAME,null,contentValues);
    }

    public List<Events> getAllAppointments(){
        SQLiteDatabase db = getReadableDatabase();
        List<Events> events = new ArrayList<>();

        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        data.moveToFirst();

        while(data.isAfterLast() == false){
            Events e = new Events();
            e.setId(data.getInt(data.getColumnIndex(APPOINTMENT_ID)));
            e.setEventName(data.getString(data.getColumnIndex(APPOINTMENT_NAME)));
            e.setStartDay(data.getInt(data.getColumnIndex(APPOINTMENT_START_DAY)));
            e.setStartMonth(data.getInt(data.getColumnIndex(APPOINTMENT_START_MONTH)));
            e.setStartYear(data.getInt(data.getColumnIndex(APPOINTMENT_START_YEAR)));
            e.setStartHour(data.getInt(data.getColumnIndex(APPOINTMENT_START_HOUR)));
            e.setStartMinute(data.getInt(data.getColumnIndex(APPOINTMENT_START_MINUTE)));
            e.setEndDay(data.getInt(data.getColumnIndex(APPOINTMENT_END_DAY)));
            e.setEndMonth(data.getInt(data.getColumnIndex(APPOINTMENT_END_MONTH)));
            e.setEndYear(data.getInt(data.getColumnIndex(APPOINTMENT_END_YEAR)));
            e.setEndHour(data.getInt(data.getColumnIndex(APPOINTMENT_END_HOUR)));
            e.setEndMinute(data.getInt(data.getColumnIndex(APPOINTMENT_END_MINUTE)));

            events.add(e);
            data.moveToNext();
        }

        return events;
    }

    public Integer deleteAppointment(long id){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME,APPOINTMENT_ID + " =? ",new String[]{Long.toString(id)});
    }

    public void updateAppointment(Events event){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(APPOINTMENT_NAME,event.getEventName());
        contentValues.put(APPOINTMENT_START_DAY,event.getStartDay());
        contentValues.put(APPOINTMENT_START_MONTH,event.getStartMonth());
        contentValues.put(APPOINTMENT_START_YEAR,event.getStartYear());
        contentValues.put(APPOINTMENT_START_HOUR,event.getStartHour());
        contentValues.put(APPOINTMENT_START_MINUTE,event.getStartMinute());
        contentValues.put(APPOINTMENT_END_DAY,event.getEndDay());
        contentValues.put(APPOINTMENT_END_MONTH,event.getEndMonth());
        contentValues.put(APPOINTMENT_END_YEAR,event.getEndYear());
        contentValues.put(APPOINTMENT_END_HOUR,event.getEndHour());
        contentValues.put(APPOINTMENT_END_MINUTE,event.getEndMinute());

        db.update(TABLE_NAME,contentValues,APPOINTMENT_ID + " =? ",new String[]{Long.toString(event.getId())});
    }
}
