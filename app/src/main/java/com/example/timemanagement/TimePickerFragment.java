package com.example.timemanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment {
    private Button confirmButton;
    private TimePicker timePicker;
    private int hours;
    private int minutes;
    private String STARTorEND;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.time_picker_fragment, container, false);
        hours = getArguments().getInt("hours");
        minutes = getArguments().getInt("minutes");
        STARTorEND = getArguments().getString("STARTorEND");

        initializeViews(view);
        setEventListeners();

        return view;
    }

    private void initializeViews(View view){
        confirmButton = view.findViewById(R.id.timePickerButton);
        timePicker = view.findViewById(R.id.timePicker);
        timePicker.setCurrentHour(hours);
        timePicker.setCurrentMinute(minutes);
    }

    private void setEventListeners(){
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                updateMainActivityTime();
                closeFragment();
            }
        });

    }

    private void closeFragment(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    private void updateMainActivityTime(){
        String timeToDisplay = getCorrectTimeString();
        TextView time;

        if(STARTorEND.equals("START")){
            time = getActivity().findViewById(R.id.textView_startOfDay);
        } else {
            time = getActivity().findViewById(R.id.textView_endOfDay);
        }

        time.setText(timeToDisplay);
    }

    /**
     * formats Time String so hours and mins are always two digits
     * @return returns complete displayable Time with HOURS : MINS AM/PM as String
     */
    private String getCorrectTimeString(){
        if(timePicker.getCurrentHour() > 12){
            return String.format("%02d",timePicker.getCurrentHour()-12)
                    + ":"
                    + String.format("%02d",timePicker.getCurrentMinute())
                    + " PM";
        }
        return String.format("%02d",timePicker.getCurrentHour())
                + ":"
                + String.format("%02d",timePicker.getCurrentMinute())
                + " AM";
    }

}
