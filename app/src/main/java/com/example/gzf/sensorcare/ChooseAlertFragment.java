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
import android.widget.CheckBox;
import android.widget.RadioButton;

public class ChooseAlertFragment extends DialogFragment {
    public static final String EXTRA_VIBRATE = "com.example.gzf.android.sensorcare.vibrate";
    public static final String EXTRA_SOUND = "com.example.gzf.android.sensorcare.sound";
    private static final String ARG_VIB_FLAG = "vibrate_flag";
    private static final String ARG_SOUND_FLAG = "sound_flag";

    private CheckBox radioButtonVibrate;
    private CheckBox radioButtonSound;

    private boolean isCurrentVibrate;
    private boolean isCurrentSound;

    public static ChooseAlertFragment newInstance(boolean vibrateFlag, boolean soundFlag) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_VIB_FLAG, vibrateFlag);
        args.putSerializable(ARG_SOUND_FLAG, soundFlag);

        ChooseAlertFragment fragment = new ChooseAlertFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_choose_alert, null);

        radioButtonVibrate = (CheckBox) v.findViewById(R.id.radio_vibrate);
        radioButtonSound = (CheckBox) v.findViewById(R.id.radio_sound);

        isCurrentVibrate = (boolean) (getArguments() != null ? getArguments().getSerializable(ARG_VIB_FLAG) : false);
        isCurrentSound = (boolean) (getArguments() != null ? getArguments().getSerializable(ARG_SOUND_FLAG) : false);

        radioButtonVibrate.setChecked(isCurrentVibrate);

        radioButtonSound.setChecked(isCurrentSound);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.choose_alert)
                .setPositiveButton(R.string.make_debug_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK,radioButtonVibrate.isChecked(),radioButtonSound.isChecked());
                    }
                })
                .setNegativeButton(R.string.make_debug_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
    }
    private void sendResult(int resultCode, boolean vibrateFlag,boolean soundFlag) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_VIBRATE, vibrateFlag);
        intent.putExtra(EXTRA_SOUND, soundFlag);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
