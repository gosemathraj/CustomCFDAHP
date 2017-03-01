package com.gosemathraj.customcalendar.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.gosemathraj.customcalendar.R;

import butterknife.BindView;

/**
 * Created by RajeshG on 01-03-2017.
 */

public class AddEventFragment extends Fragment{

    @BindView(R.id.appointmentType)
    Spinner appointmentType;

    @BindView(R.id.selectEndTime)
    TextView selectEndTime;

    @BindView(R.id.selectStartTime)
    TextView selectStartTime;

    @BindView(R.id.selectDate)
    TextView selectDate;

    @BindView(R.id.startTime)
    TextView startTime;

    @BindView(R.id.endTime)
    TextView endTime;

    @BindView(R.id.patientName)
    EditText patientName;

    @BindView(R.id.patientEmail)
    EditText patientEmail;

    @BindView(R.id.patientContactNo)
    EditText patientContact;

    @BindView(R.id.addEvent)
    Button addEvent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_event_fragment,container,false);

        return view;
    }
}
