package com.gosemathraj.customcalendar.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gosemathraj.customcalendar.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by RajeshG on 01-03-2017.
 */

public class AddEventFragment extends Fragment{

    @BindView(R.id.appointmentType)
    Spinner appointmentType;

    @BindView(R.id.patientName)
    EditText patientName;

    @BindView(R.id.patientContactNo)
    EditText patientContact;

    @BindView(R.id.patientEmail)
    EditText patientEmail;

    @BindView(R.id.selectStartDate)
    TextView selectStartDate;

    @BindView(R.id.selectEndDate)
    TextView selectEndDate;

    @BindView(R.id.startDate)
    TextView startDate;

    @BindView(R.id.endDate)
    TextView endDate;

    @BindView(R.id.selectStartTime)
    TextView selectStartTime;

    @BindView(R.id.selectEndTime)
    TextView selectEndTime;

    @BindView(R.id.startTime)
    TextView startTime;

    @BindView(R.id.endTime)
    TextView endTime;

    @BindView(R.id.addEvent)
    Button addEvent;

    private Calendar time;
    private Calendar newStartTime = Calendar.getInstance();
    private Calendar newEndTime = Calendar.getInstance();
    private String appointmentTypeString = null;

    private AddeventClicked addeventClicked;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_event_fragment,container,false);
        ButterKnife.bind(this,view);

        try{
            init();
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    private void init() {
        addeventClicked = (AddeventClicked) getActivity();
        getIntentData();
        initSpinnerData();
        setInitialData();
        setOnClickListeners();
    }

    private void initSpinnerData() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item
                                                                        ,new String[]{"Consultation","Follow up"});
        appointmentType.setAdapter(spinnerAdapter);
    }

    private void setOnClickListeners() {
        appointmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                appointmentTypeString = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                appointmentTypeString = "Consultation";
            }
        });

        selectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        newStartTime.set(Calendar.DAY_OF_MONTH,day);
                        newStartTime.set(Calendar.MONTH,month);
                        newStartTime.set(Calendar.YEAR,year);
                    }
                },calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        selectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        newEndTime.set(Calendar.DAY_OF_MONTH,day);
                        newEndTime.set(Calendar.MONTH,month);
                        newEndTime.set(Calendar.YEAR,year);
                    }
                },calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        selectStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                        newStartTime.set(Calendar.HOUR_OF_DAY,hour);
                        newStartTime.set(Calendar.MINUTE,minutes);
                    }
                },time.get(Calendar.HOUR_OF_DAY),time.get(Calendar.MINUTE),false);
                timePickerDialog.show();
            }
        });

        selectEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                        newEndTime.set(Calendar.HOUR_OF_DAY,hour);
                        newEndTime.set(Calendar.MINUTE,minutes);
                    }
                },time.get(Calendar.HOUR_OF_DAY) + 1,time.get(Calendar.MINUTE),false);
                timePickerDialog.show();
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("startCalendar",newStartTime);
                bundle.putSerializable("endCalendar",newEndTime);
                bundle.putString("eventName",buildEventString());

                addeventClicked.onAddEventClicked(bundle);
            }
        });
    }

    private String buildEventString() {
        return(
                appointmentTypeString + "--" +
                patientName.getText().toString() + "--" +
                patientEmail.getText().toString() + "--" +
                String.valueOf(patientContact.getText().toString())
        );
    }

    private void setInitialData() {
        startTime.setText(String.valueOf(time.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(time.get(Calendar.MINUTE)));
    }

    private void getIntentData() {
        if(getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null){
            time = (Calendar) getActivity().getIntent().getExtras().getSerializable("time");
        }
    }

    public interface AddeventClicked{
        void onAddEventClicked(Bundle bundle);
    }
}
