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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.gosemathraj.customcalendar.R;
import com.gosemathraj.customcalendar.Utility.SharedPref;
import com.gosemathraj.customcalendar.model.Events;
import com.gosemathraj.customcalendar.realm.RealmController;

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
    @BindView(R.id.selectStartDate) TextView selectStartDate;
    @BindView(R.id.selectEndDate) TextView selectEndDate;
    @BindView(R.id.startDate) TextView startDate;
    @BindView(R.id.endDate) TextView endDate;
    @BindView(R.id.selectStartTime) TextView selectStartTime;
    @BindView(R.id.selectEndTime) TextView selectEndTime;
    @BindView(R.id.startTime) TextView startTime;
    @BindView(R.id.endTime) TextView endTime;

    private Events event;
    private String appointmentTypeString = null;
    private int operationType;

    private OnAddEventClicked onAddEventClicked;
    private OnUpdateEventClicked onUpdateEventClicked;

    private ArrayAdapter<String> spinnerAdapter;

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

        onAddEventClicked = (OnAddEventClicked) getActivity();
        onUpdateEventClicked = (OnUpdateEventClicked) getActivity();
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
                /*Bundle bundle = new Bundle();
                bundle.putSerializable("addEvent",event);*/
                onAddEventClicked.addEventClicked();
                break;

            case R.id.save_event :
                event.setEventName(buildEventString());
                saveAppointmentToDb();
               /* Bundle bundle1 = new Bundle();
                bundle1.putSerializable("updateEvent",event);*/
                onUpdateEventClicked.updateEventClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAppointmentToDb() {
        RealmController.getInstance().updateAppointment(event);
    }

    private void addAppointmentToDb() {
        int count = SharedPref.getInstance(getActivity()).getCount();
        if(count == -1){
            event.setId(0);
        }else{
            event.setId(count + 1);
        }
        RealmController.getInstance().addAppointment(event);
        SharedPref.getInstance(getActivity()).setCount(count + 1);
    }

    private void initSpinnerData() {
        spinnerAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item
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

        selectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        event.setEndDay(day);
                        event.setEndMonth(month);
                        event.setEndYear(year);

                        try {
                            Date d = dateSdf.parse(String.valueOf(event.getStartDay()) + "/" + String.valueOf(event.getStartMonth() + 1) + "/" + String.valueOf(event.getStartYear()));
                            endDate.setText(new SimpleDateFormat("E, d MMM, yyy").format(d).toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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

        selectEndTime.setOnClickListener(new View.OnClickListener() {
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

    public interface OnAddEventClicked{
        void addEventClicked();
    }

    public interface OnUpdateEventClicked{
        void updateEventClicked();
    }
}
