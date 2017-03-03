package com.gosemathraj.customcalendar.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
    private OnDeleteEventClicked onDeleteEventClicked;
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
        onDeleteEventClicked = (OnDeleteEventClicked) getActivity();
        getIntentData();
        setOnClickListener();
        setData();
    }

    private void setOnClickListener() {
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("editEvent",event);
                AddEventFragment frag = new AddEventFragment();
                frag.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_base_container,frag).commit();
            }
        });
    }

    private void showDialog() {
        AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete event ?");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("deleteEvent",event);
                onDeleteEventClicked.deleteEventClicked(bundle);
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

    public interface OnDeleteEventClicked{
        void deleteEventClicked(Bundle bundle);
    }
}
