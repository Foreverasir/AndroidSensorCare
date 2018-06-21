package com.example.gzf.sensorcare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.TextView;

public class UserFragment extends Fragment {
    private Person mPerson;
    private TextView mNameTextView;
    private TextView mLocationTextView;
    private TextView mStateTextView;
    private TextView mBleTextView;
    private CheckBox mCheckBox;
    private EditText mEditText;

    private UserLocalInfo userLocalInfo;

    private static final String ARG_PERSON_BLE = "person_ble";

    public static UserFragment newInstance(String ble){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PERSON_BLE,ble);
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
        }else {
            Log.i("UserFragment:","can not get ble as id!");
        }
        PersonSet ps = PersonSet.get(getActivity());
        mPerson = ps.getPerson(personBle);
        userLocalInfo = ps.getUserLocalInfo(mPerson.getBle());
    }

    @Override
    public void onPause(){
        super.onPause();
        // 更新数据库的当前本地信息
        PersonSet.get(getActivity()).updateUserInfo(userLocalInfo);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_fragment, container, false);
        mNameTextView = (TextView)v.findViewById(R.id.person_name);
        mStateTextView = (TextView)v.findViewById(R.id.person_state);
        mLocationTextView = (TextView)v.findViewById(R.id.person_location);
        mBleTextView = (TextView)v.findViewById(R.id.person_ble);
        mCheckBox = (CheckBox)v.findViewById(R.id.state_watch);
        mCheckBox.setChecked(userLocalInfo.isWatched);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                userLocalInfo.setWatched(b);
            }
        });

        mEditText = (EditText)v.findViewById(R.id.text_record);
        if(userLocalInfo.getHelpText()!=null){
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

    private void updateUI(){
        mNameTextView.setText(mPerson.getName());
        mStateTextView.setText(mPerson.getState());
        mLocationTextView.setText(mPerson.getLocation());
        mBleTextView.setText(mPerson.getBle());
    }
}
