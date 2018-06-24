package com.example.gzf.sensorcare;

import android.support.v4.app.Fragment;
import android.widget.Toast;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new UserListFragment();
    }

    long startTime = 0;
    @Override
    public void onBackPressed() {

        long currentTime = System.currentTimeMillis();
        if ((currentTime - startTime) >= 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出，退出后不再监测", Toast.LENGTH_SHORT).show();
            startTime = currentTime;
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            finish();
        }
    }
}

