package com.example.gzf.sensorcare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Date;
import java.util.GregorianCalendar;

public class UserFragment extends Fragment {
    private Person mPerson;
    private TextView mNameTextView;
    private TextView mLocationTextView;
    private TextView mStateTextView;
    private TextView mBleTextView;
    private EditText mEditText;
    private Switch mSwitchWatch;
    private Switch mSwitchDebug;
    private Button mButtonLog;

    private UserLocalInfo userLocalInfo;

    private static final String ARG_PERSON_BLE = "person_ble";
    private static final String DIALOG_MAKE_DEBUG = "DialogMakeDebug";
    private static final String DIALOG_MAKE_LOG = "DialogMakeLog";

    private static final int REQUEST_PASSWORD = 0;
    private static final int REQUEST_LOG = 1;

    public static UserFragment newInstance(String ble) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PERSON_BLE, ble);
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String personBle = null;
        if (getArguments() != null) {
            personBle = (String) getArguments().getSerializable(ARG_PERSON_BLE);
        } else {
            Log.i("UserFragment:", "can not get ble as id!");
        }
        PersonSet ps = PersonSet.get(getActivity());
        mPerson = ps.getPerson(personBle);
        userLocalInfo = ps.getUserLocalInfo(mPerson.getBle());
    }

    @Override
    public void onPause() {
        super.onPause();
        // 更新数据库的当前本地信息
        PersonSet.get(getActivity()).updateUserInfo(userLocalInfo);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_fragment, container, false);
        mNameTextView = (TextView) v.findViewById(R.id.person_name);
        mStateTextView = (TextView) v.findViewById(R.id.person_state);
        mLocationTextView = (TextView) v.findViewById(R.id.person_location);
        mBleTextView = (TextView) v.findViewById(R.id.person_ble);

        mSwitchWatch = (Switch) v.findViewById(R.id.is_watch);
        mSwitchWatch.setChecked(userLocalInfo.isWatched());
        mSwitchWatch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                userLocalInfo.setWatched(b);
            }
        });

        mSwitchDebug = (Switch) v.findViewById(R.id.is_debug);
        mSwitchDebug.setChecked(!userLocalInfo.existFlag);
        mSwitchDebug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                FragmentManager manager = getFragmentManager();
                MakeDebugFragment dialog = new MakeDebugFragment();
                dialog.setTargetFragment(UserFragment.this, REQUEST_PASSWORD);
                dialog.show(manager, DIALOG_MAKE_DEBUG);
            }
        });

        mButtonLog = (Button) v.findViewById(R.id.button_log);
        mButtonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                MakeLogFragment dialog = new MakeLogFragment();
                dialog.setTargetFragment(UserFragment.this, REQUEST_LOG);
                dialog.show(manager, DIALOG_MAKE_LOG);
            }
        });

        mEditText = (EditText) v.findViewById(R.id.text_record);
        if (userLocalInfo.getHelpText() != null) {
            mEditText.setText(userLocalInfo.getHelpText());
        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userLocalInfo.setHelpText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        updateUI();
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PASSWORD) {
            mSwitchDebug.setOnCheckedChangeListener(null);
            if (resultCode == Activity.RESULT_OK) {
                boolean flag = (boolean) data.getSerializableExtra(MakeDebugFragment.EXTRA_PASSWORD);
                if (!flag) {
                    mSwitchDebug.setChecked(!mSwitchDebug.isChecked());
                } else {
                    mSwitchDebug.setChecked(mSwitchDebug.isChecked());
                }
            } else {
                mSwitchDebug.setChecked(!mSwitchDebug.isChecked());
            }
            userLocalInfo.setExistFlag(!mSwitchDebug.isChecked());

            mSwitchDebug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    FragmentManager manager = getFragmentManager();
                    MakeDebugFragment dialog = new MakeDebugFragment();
                    dialog.setTargetFragment(UserFragment.this, REQUEST_PASSWORD);
                    dialog.show(manager, DIALOG_MAKE_DEBUG);
                }
            });
        } else if (requestCode == REQUEST_LOG) {
            if (resultCode == Activity.RESULT_OK) {
                String logContent = (String) data.getSerializableExtra(MakeLogFragment.EXTRA_LOG);
                logContent = logContent + " " + DateFormat.format("yyyy年 MMMM dd日 kk:mm:ss", new Date());
                String text = userLocalInfo.getHelpText();
                mEditText.setText(String.format("%s\n%s", text, logContent));
                userLocalInfo.setHelpText(String.format("%s\n%s", text, logContent));
            }
        }
    }

    private void updateUI() {
        mNameTextView.setText(mPerson.getName());
        mStateTextView.setText(mPerson.getState());
        mLocationTextView.setText(mPerson.getLocation());
        mBleTextView.setText(mPerson.getBle());
    }
}
