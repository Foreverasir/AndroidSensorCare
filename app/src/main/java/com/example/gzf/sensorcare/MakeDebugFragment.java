package com.example.gzf.sensorcare;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class MakeDebugFragment extends DialogFragment {
    private EditText mEditText;

    String password;
    boolean passFlag;

    public static final String EXTRA_PASSWORD = "com.example.gzf.android.sensorcare.password";
    private static final String SENSOR_PASSWORD = "123456";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_make_debug, null);

        password=" ";
        passFlag = false;

        mEditText = (EditText) v.findViewById(R.id.editText_password);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.make_debug_title)
                .setPositiveButton(R.string.make_debug_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(password.equals(SENSOR_PASSWORD)){
                            passFlag = true;
                            Log.i("Flag:","true");
                        }
                        sendResult(Activity.RESULT_OK, passFlag);
                    }
                })
                .setNegativeButton(R.string.make_debug_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_CANCELED, false);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, boolean flag) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PASSWORD, flag);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
