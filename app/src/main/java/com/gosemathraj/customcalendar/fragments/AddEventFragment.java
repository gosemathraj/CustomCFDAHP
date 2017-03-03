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
import com.gosemathraj.customcalendar.model.Events;

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

    @BindView(R.id.updateEvent)
    Button updateEvent;

    private String am_or_pm;
    private Events event;
    private String appointmentTypeString = null;

    private OnAddEventClicked onAddEventClicked;
    private OnUpdateEventClicked onUpdateEventClicked;

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
        onAddEventClicked = (OnAddEventClicked) getActivity();
        onUpdateEventClicked = (OnUpdateEventClicked) getActivity();
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        event.setStartDay(day);
                        event.setStartMonth(month);
                        event.setStartYear(year);

                        startDate.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
                    }
                },event.getStartYear(), event.getStartMonth(), event.getStartDay());
                datePickerDialog.show();
            }
        });

        selectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        event.setEndDay(day);
                        event.setEndMonth(month);
                        event.setEndYear(year);

                        endDate.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
                    }
                },event.getEndYear(), event.getEndMonth(), event.getEndDay());
                datePickerDialog.show();
            }
        });

        selectStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                        event.setStartHour(hour);
                        event.setStartMinute(minutes);

                        startTime.setText(String.valueOf(hour) + ":" + String.valueOf(minutes));
                    }
                },event.getStartHour(),event.getStartMinute(),false);
                timePickerDialog.show();
            }
        });

        selectEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                        event.setEndHour(hour);
                        event.setEndMinute(minutes);

                        endTime.setText(String.valueOf(hour) + ":" + String.valueOf(minutes));
                    }
                },event.getEndHour(),event.getEndMinute(),false);
                timePickerDialog.show();
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                event.setEventName(buildEventString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("addEvent",event);
                onAddEventClicked.addEventClicked(bundle);
            }
        });

        updateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                event.setEventName(buildEventString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("updateEvent",event);
                onUpdateEventClicked.updateEventClicked(bundle);
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
        startDate.setText(String.valueOf(event.getStartDay()) + "/" + String.valueOf(event.getStartMonth()) + "/" + String.valueOf(event.getStartYear()));
        endDate.setText(String.valueOf(event.getEndDay()) + "/" + String.valueOf(event.getEndMonth()) + "/" + String.valueOf(event.getEndYear()));
        startTime.setText(String.valueOf(event.getStartHour() + ":" + String.valueOf(event.getStartMinute())));
        endTime.setText(String.valueOf(event.getEndHour() + ":" + String.valueOf(event.getEndMinute())));

        if(event.getEventName().length() > 0){
            appointmentTypeString = event.getEventName().split("--")[0];
            patientName.setText(event.getEventName().split("--")[1]);
            patientEmail.setText(event.getEventName().split("--")[2]);
            patientContact.setText(event.getEventName().split("--")[3]);
        }

        /*newStartTime.set(Calendar.DAY_OF_MONTH,time.get(Calendar.DAY_OF_MONTH));
        newStartTime.set(Calendar.MONTH,time.get(Calendar.MONTH));
        newStartTime.set(Calendar.YEAR,time.get(Calendar.YEAR));
        newStartTime.set(Calendar.HOUR_OF_DAY,time.get(Calendar.HOUR_OF_DAY));
        newStartTime.set(Calendar.MINUTE,time.get(Calendar.MINUTE));

        newEndTime.set(Calendar.DAY_OF_MONTH,time.get(Calendar.DAY_OF_MONTH));
        newEndTime.set(Calendar.MONTH,time.get(Calendar.MONTH));
        newEndTime.set(Calendar.YEAR,time.get(Calendar.YEAR));
        newEndTime.set(Calendar.HOUR_OF_DAY,time.get(Calendar.HOUR_OF_DAY) + 1);
        newEndTime.set(Calendar.MINUTE,time.get(Calendar.MINUTE));*/
    }

    private void getIntentData() {

        if(getArguments() != null){
            event = (Events) getArguments().getSerializable("editEvent");
            addEvent.setVisibility(View.GONE);
            updateEvent.setVisibility(View.VISIBLE);
        }else if(getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null){
            event = (Events) getActivity().getIntent().getExtras().getSerializable("event");
            addEvent.setVisibility(View.VISIBLE);
            updateEvent.setVisibility(View.GONE);
        }
    }

    public interface OnAddEventClicked{
        void addEventClicked(Bundle bundle);
    }

    public interface OnUpdateEventClicked{
        void updateEventClicked(Bundle bundle);
    }
}
