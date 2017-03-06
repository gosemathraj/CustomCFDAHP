package com.gosemathraj.customcalendar.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gosemathraj.customcalendar.R;
import com.gosemathraj.customcalendar.interfaces.OnEventDone;
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

public class EventDetailsFragment extends Fragment{

    @BindView(R.id.appointmentType) TextView appointmentType;
    @BindView(R.id.patientName) TextView patientName;
    @BindView(R.id.patientEmail) TextView patientEmail;
    @BindView(R.id.patientMobile) TextView patientMobile;
    @BindView(R.id.startFrom) TextView startFrom;
    @BindView(R.id.endTill) TextView endTill;

    private Events event;
    private OnEventDone onEventDone;
    final SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy H:mm");
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_details_fragment,container,false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this,view);

        try{
            init();
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    private void init() {
        onEventDone = (OnEventDone) getActivity();
        getIntentData();
        setData();
    }

    private void showDialog() {
        AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete event ?");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                RealmController.getInstance().deleteAppointment(event.getId());
                onEventDone.eventDone();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void setData() {
        appointmentType.setText(event.getEventName().split("--")[0]);
        patientName.setText(event.getEventName().split("--")[1]);
        patientEmail.setText(event.getEventName().split("--")[2]);
        patientMobile.setText(event.getEventName().split("--")[3]);

        try {
            Date d = sdf.parse(String.valueOf(event.getStartDay()) + "/" + String.valueOf(event.getStartMonth() + 1) + "/" + String.valueOf(event.getStartYear())
            +" "+String.valueOf(event.getStartHour() + ":" + String.valueOf(event.getStartMinute())));

            startFrom.setText(new SimpleDateFormat("E, d MMM, yyy  hh:mm aa").format(d).toString());

            d = sdf.parse(String.valueOf(event.getEndDay()) + "/" + String.valueOf(event.getEndMonth() + 1) + "/" + String.valueOf(event.getEndYear())
                    +" "+String.valueOf(event.getEndHour() + ":" + String.valueOf(event.getEndMinute())));

            endTill.setText(new SimpleDateFormat("E, d MMM, yyy  hh:mm aa").format(d).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void getIntentData() {
        if(getActivity().getIntent().getExtras() != null){
            event = (Events) getActivity().getIntent().getExtras().getSerializable("event");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.events_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.edit_event :
                Bundle bundle = new Bundle();
                bundle.putSerializable("event",event);
                bundle.putInt("operationType",2);
                AddEventFragment frag = new AddEventFragment();
                frag.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_base_container,frag).commit();
                break;

            case R.id.delete_event :
                showDialog();
                break;

            case R.id.add_new_event :
                event.setEventName("");
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("event",event);
                bundle1.putInt("operationType",1);
                AddEventFragment frag1 = new AddEventFragment();
                frag1.setArguments(bundle1);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_base_container,frag1).commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
