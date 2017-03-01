package com.gosemathraj.customcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gosemathraj.customcalendar.fragments.AddEventFragment;

/**
 * Created by RajeshG on 01-03-2017.
 */
public class AddEventActivity extends AppCompatActivity implements AddEventFragment.AddeventClicked{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_activity);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_base_container,new AddEventFragment()).commit();
        }
    }

    @Override
    public void onAddEventClicked(Bundle bundle) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtras(bundle);
        setResult(1,intent);
        finish();
    }
}
