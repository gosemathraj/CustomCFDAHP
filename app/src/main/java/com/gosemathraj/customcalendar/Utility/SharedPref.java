package com.gosemathraj.customcalendar.Utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by RajeshG on 01-03-2017.
 */

public class SharedPref {

    private static SharedPref sharedPref;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static SharedPref getInstance(Context context){
        if(sharedPref == null){
            sharedPref = new SharedPref(context);
        }
        return sharedPref;
    }

    public SharedPref(Context context){
        sharedPreferences = context.getSharedPreferences("appointmentPreferences",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setCount(int count){
        editor.putInt("count",count);
        editor.commit();
    }

    public int getCount(){
        return sharedPreferences.getInt("count",-1);
    }
}
