package com.example.ben.colorsensorrgbstreamer6well;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class SensorDialogFragment extends DialogFragment implements CompoundButton.OnCheckedChangeListener {

    // Should be a factory method that instantiates this array to have the proper capacity
    private ArrayList<Boolean> sensorsToBeMeasured = new ArrayList<>(6);

    private int intervalData = 0;
    private int timeLengthData = 0;

    public interface SensorDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, ArrayList<Boolean> sensorsToBeMeasured,
                                   int intervalData, int timeData);
        //void onDialogNegativeClick(DialogFragment dialog);
    }

    SensorDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;
        activity = (Activity) context;

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SensorDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_sensor, null);

        ToggleButton sensor0Btn = view.findViewById(R.id.sensor0toggleButton);
        ToggleButton sensor1Btn = view.findViewById(R.id.sensor1toggleButton);
        ToggleButton sensor2Btn = view.findViewById(R.id.sensor2toggleButton);
        ToggleButton sensor3Btn = view.findViewById(R.id.sensor3toggleButton);
        ToggleButton sensor4Btn = view.findViewById(R.id.sensor4toggleButton);
        ToggleButton sensor5Btn = view.findViewById(R.id.sensor5toggleButton);

        EditText intervalEditText = view.findViewById(R.id.intervalEditText);
        EditText minutesEditText = view.findViewById(R.id.minutesEditText);
        EditText secondsEditText = view.findViewById(R.id.secondsEditText);

        // Set all buttons to be measured to false. Prevents out of bounds error when using set.
        sensorsToBeMeasured.add(0, false);
        sensorsToBeMeasured.add(1, false);
        sensorsToBeMeasured.add(2, false);
        sensorsToBeMeasured.add(3, false);
        sensorsToBeMeasured.add(4, false);
        sensorsToBeMeasured.add(5, false);

        sensor0Btn.setOnCheckedChangeListener(this);
        sensor1Btn.setOnCheckedChangeListener(this);
        sensor2Btn.setOnCheckedChangeListener(this);
        sensor3Btn.setOnCheckedChangeListener(this);
        sensor4Btn.setOnCheckedChangeListener(this);
        sensor5Btn.setOnCheckedChangeListener(this);

        sensor0Btn.setChecked(false);
        sensor1Btn.setChecked(false);
        sensor2Btn.setChecked(false);
        sensor3Btn.setChecked(false);
        sensor4Btn.setChecked(false);
        sensor5Btn.setChecked(false);

        //Add text change listener to EditTexts
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not Used
            }

            @Override
            public void afterTextChanged(Editable s) {
                View v = getView();
                EditText editText = (EditText) v;
                int value = 0;
                switch (v.getId()){
                    case R.id.intervalEditText:
                        break;
                    case R.id.minutesEditText:
                        break;
                    case R.id.secondsEditText:
                        break;
                    default:
                        break;
                }
            }
        };

        intervalEditText.addTextChangedListener(textWatcher);
        minutesEditText.addTextChangedListener(textWatcher);
        secondsEditText.addTextChangedListener(textWatcher);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.measure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(SensorDialogFragment.this, sensorsToBeMeasured, intervalData, timeLengthData);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SensorDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int index;
        switch(buttonView.getId()) {
            case R.id.sensor0toggleButton:
                index = 0;
                break;
            case R.id.sensor1toggleButton:
                index = 1;
                break;
            case R.id.sensor2toggleButton:
                index = 2;
                break;
            case R.id.sensor3toggleButton:
                index = 3;
                break;
            case R.id.sensor4toggleButton:
                index = 4;
                break;
            case R.id.sensor5toggleButton:
                index = 5;
                break;
            default:
                index = 0;//may cause bugs... do something better?
                break;
        }
        if (isChecked) {
            // The toggle is enabled
            sensorsToBeMeasured.set(index, true);
        } else {
            // The toggle is disabled
            sensorsToBeMeasured.set(index, false);
        }
    }
}
