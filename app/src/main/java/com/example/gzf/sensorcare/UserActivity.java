package com.example.gzf.sensorcare;

import android.support.v4.app.Fragment;

public class UserActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new UserFragment();
    }
}
