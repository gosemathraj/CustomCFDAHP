package com.gosemathraj.customcalendar.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gosemathraj.customcalendar.R;
import com.gosemathraj.customcalendar.interfaces.OnEventDone;
import com.gosemathraj.customcalendar.model.Events;
import com.gosemathraj.customcalendar.data.DbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by RajeshG on 01-03-2017.
 */

public class AddEventFragment extends Fragment{

    @BindView(R.id.appointmentType) Spinner appointmentType;
    @BindView(R.id.patientName) EditText patientName;
    @BindView(R.id.patientContactNo) EditText patientContact;
    @BindView(R.id.patientEmail) EditText patientEmail;
    @BindView(R.id.startDate) TextView startDate;
    @BindView(R.id.endDate) TextView endDate;
    @BindView(R.id.startTime) TextView startTime;
    @BindView(R.id.endTime) TextView endTime;
    @BindView(R.id.changeStartDate) TextView changeStartDate;
    @BindView(R.id.changeEndDate) TextView changeEndDate;
    @BindView(R.id.changeStartTime) TextView changeStartTime;
    @BindView(R.id.changeEndTime) TextView changeEndTime;

    private Events event;
    private String appointmentTypeString = null;
    private int operationType;

    private OnEventDone onEventDone;
    private ArrayAdapter<String> spinnerAdapter;
    private DbHelper dbHelper;

    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
    final SimpleDateFormat dateSdf = new SimpleDateFormat("d/MM/yyyy");
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void init() {
        onEventDone = (OnEventDone) getActivity();
        dbHelper = new DbHelper(getActivity());
        getIntentData();
        initSpinnerData();
        setInitialData();
        setOnClickListeners();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.popup_menu,menu);

        MenuItem add = menu.findItem(R.id.add_event);
        MenuItem save = menu.findItem(R.id.save_event);

        if(operationType == 1){
            add.setVisible(true);
            save.setVisible(false);
        }else if(operationType == 2){
            add.setVisible(false);
            save.setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id){
            case R.id.add_event :
                event.setEventName(buildEventString());
                addAppointmentToDb();
                onEventDone.eventDone();
                break;

            case R.id.save_event :
                event.setEventName(buildEventString());
                saveAppointmentToDb();
                onEventDone.eventDone();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAppointmentToDb() {
        dbHelper.updateAppointment(event);
    }

    private void addAppointmentToDb() {
        dbHelper.addAppointment(event);
    }

    private void initSpinnerData() {
        spinnerAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item
                                                                        ,new String[]{"Consultation","Follow up"});
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        changeStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        event.setStartDay(day);
                        event.setStartMonth(month);
                        event.setStartYear(year);

                        try {
                            Date d = dateSdf.parse(String.valueOf(event.getStartDay()) + "/" + String.valueOf(event.getStartMonth() + 1) + "/" + String.valueOf(event.getStartYear()));
                            startDate.setText(new SimpleDateFormat("E, d MMM, yyy").format(d).toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },event.getStartYear(), event.getStartMonth(), event.getStartDay());
                datePickerDialog.show();
            }
        });

        changeEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        event.setEndDay(day);
                        event.setEndMonth(month);
                        event.setEndYear(year);

                        try {
                            Date d = dateSdf.parse(String.valueOf(event.getEndDay()) + "/" + String.valueOf(event.getEndMonth() + 1) + "/" + String.valueOf(event.getEndYear()));
                            endDate.setText(new SimpleDateFormat("E, d MMM, yyy").format(d).toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },event.getEndYear(), event.getEndMonth(), event.getEndDay());
                datePickerDialog.show();
            }
        });

        changeStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                        event.setStartHour(hour);
                        event.setStartMinute(minutes);

                        try {
                            Date d = sdf.parse(String.valueOf(hour) + ":" + String.valueOf(minutes));
                            startTime.setText(new SimpleDateFormat("hh:mm aa").format(d).toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },event.getStartHour(),event.getStartMinute(),false);
                timePickerDialog.show();
            }
        });

        changeEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                        event.setEndHour(hour);
                        event.setEndMinute(minutes);

                        try {
                            Date d = sdf.parse(String.valueOf(hour) + ":" + String.valueOf(minutes));
                            endTime.setText(new SimpleDateFormat("hh:mm aa").format(d).toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },event.getEndHour(),event.getEndMinute(),false);
                timePickerDialog.show();
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

        try{

            Date d = dateSdf.parse(String.valueOf(event.getStartDay()) + "/" + String.valueOf(event.getStartMonth() + 1) + "/" + String.valueOf(event.getStartYear()));
            startDate.setText(new SimpleDateFormat("E, d MMM, yyy").format(d).toString());

            d = dateSdf.parse(String.valueOf(event.getEndDay()) + "/" + String.valueOf(event.getEndMonth() + 1) + "/" + String.valueOf(event.getEndYear()));
            endDate.setText(new SimpleDateFormat("E, d MMM, yyy").format(d).toString());

            d = sdf.parse(String.valueOf(event.getStartHour() + ":" + String.valueOf(event.getStartMinute())));
            startTime.setText(new SimpleDateFormat("hh:mm aa").format(d).toString());

            d = sdf.parse(String.valueOf(event.getEndHour() + ":" + String.valueOf(event.getEndMinute())));
            endTime.setText(new SimpleDateFormat("hh:mm aa").format(d).toString());
        }catch(ParseException e){
            e.printStackTrace();
        }

        if(event.getEventName().length() > 0){
            appointmentTypeString = event.getEventName().split("--")[0];
            patientName.setText(event.getEventName().split("--")[1]);
            patientEmail.setText(event.getEventName().split("--")[2]);
            patientContact.setText(event.getEventName().split("--")[3]);
        }

        appointmentType.setSelection(spinnerAdapter.getPosition(appointmentTypeString));
    }

    private void getIntentData() {

        if(getArguments() != null){
            event = (Events) getArguments().getSerializable("event");
            operationType = getArguments().getInt("operationType");
        }else if(getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null){
            event = (Events) getActivity().getIntent().getExtras().getSerializable("event");
            operationType = getActivity().getIntent().getExtras().getInt("operationType");
        }
    }
}
