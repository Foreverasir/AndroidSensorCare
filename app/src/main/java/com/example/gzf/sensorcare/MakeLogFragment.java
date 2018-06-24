package com.example.gzf.sensorcare;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Date;

public class MakeLogFragment extends DialogFragment {
    public static final String EXTRA_LOG = "com.example.gzf.android.sensorcare.log";

    String logContent;

    private RadioButton radioButtonTP;
    private RadioButton radioButtonTN;
    private RadioButton radioButtonFP;
    private RadioButton radioButtonDC;
    private RadioGroup radioGroup;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_log, null);

        radioButtonTP = (RadioButton) v.findViewById(R.id.radioButton);
        radioButtonTN = (RadioButton) v.findViewById(R.id.radioButton2);
        radioButtonFP = (RadioButton) v.findViewById(R.id.radioButton3);
        radioButtonDC = (RadioButton) v.findViewById(R.id.radioButton4);
        radioGroup = (RadioGroup) v.findViewById(R.id.radio_group);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.make_log_title)
                .setPositiveButton(R.string.make_debug_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int id=radioGroup.getCheckedRadioButtonId();
                        switch (id){
                            case R.id.radioButton:
                                logContent=getResources().getString(R.string.true_positive);break;
                            case R.id.radioButton2:
                                logContent=getResources().getString(R.string.true_negative);break;
                            case R.id.radioButton3:
                                logContent=getResources().getString(R.string.false_positive);break;
                            case R.id.radioButton4:
                                logContent=getResources().getString(R.string.tag_disconnect);break;
                                default: break;
                        }
                        sendResult(Activity.RESULT_OK, logContent);
                    }
                })
                .setNegativeButton(R.string.make_debug_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_CANCELED, null);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, String log) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_LOG, log);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
