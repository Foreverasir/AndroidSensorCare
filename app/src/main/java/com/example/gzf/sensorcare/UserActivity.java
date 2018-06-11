package com.example.gzf.sensorcare;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;


public class UserActivity extends SingleFragmentActivity {
    private static final String EXTRA_ID = "com.example.gzf.sensorcare.person_ble";

    @Override
    protected Fragment createFragment() {
        String personBle = (String) getIntent().getSerializableExtra(EXTRA_ID);
        return UserFragment.newInstance(personBle);
    }

    public static Intent newIntent(Context packageContext, String personId) {
        Intent intent = new Intent(packageContext, UserActivity.class);
        intent.putExtra(EXTRA_ID, personId);
        return intent;
    }
}
