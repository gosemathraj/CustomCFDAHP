package com.gosemathraj.customcalendar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gosemathraj.customcalendar.R;
import com.gosemathraj.customcalendar.model.Events;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RajeshG on 01-03-2017.
 */

public class EventDetailsFragment extends Fragment{

    @BindView(R.id.appointmentType)
    TextView appointmentType;

    @BindView(R.id.patientName)
    TextView patientName;

    @BindView(R.id.patientEmail)
    TextView patientEmail;

    @BindView(R.id.patientMobile)
    TextView patientMobile;

    @BindView(R.id.startFrom)
    TextView startFrom;

    @BindView(R.id.endTill)
    TextView endTill;

    @BindView(R.id.delete)
    TextView delete;

    @BindView(R.id.edit)
    TextView edit;

    private Events event;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_details_fragment,container,false);
        ButterKnife.bind(this,view);

        try{
            init();
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    private void init() {
        getIntentData();
        setData();
    }

    private void setData() {
        appointmentType.setText(event.getEventName().split("--")[0]);
        patientName.setText(event.getEventName().split("--")[1]);
        patientEmail.setText(event.getEventName().split("--")[2]);
        patientMobile.setText(event.getEventName().split("--")[3]);
        startFrom.setText(
                String.valueOf(event.getStartDay()) + "/" + String.valueOf(event.getStartMonth()) + "/" + String.valueOf(event.getStartYear()) +
                        "  " + String.valueOf(event.getStartHour() + ":" + String.valueOf(event.getStartMinute()))
        );
        endTill.setText(
                String.valueOf(event.getEndDay()) + "/" + String.valueOf(event.getEndMonth()) + "/" + String.valueOf(event.getEndYear()) +
                        "  " + String.valueOf(event.getEndHour() + ":" + String.valueOf(event.getEndMinute()))
        );

    }

    private void getIntentData() {
        if(getActivity().getIntent().getExtras() != null){
            event = (Events) getActivity().getIntent().getExtras().getSerializable("event");
        }
    }
}
