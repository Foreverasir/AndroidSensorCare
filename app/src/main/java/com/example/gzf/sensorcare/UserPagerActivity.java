package com.example.gzf.sensorcare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;
import java.util.UUID;

public class UserPagerActivity extends AppCompatActivity {
    private static final String EXTRA_ID = "com.example.gzf.sensorcare.person_ble";

    private ViewPager mViewPager;
    private List<Person> mPersonList;

    private ImageButton btnToFirst;
    private ImageButton btnToLast;
    private ImageButton btnOk;

    public static Intent newIntent(Context packageContext, String personId) {
        Intent intent = new Intent(packageContext, UserPagerActivity.class);
        intent.putExtra(EXTRA_ID, personId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        mViewPager = (ViewPager)findViewById(R.id.activity_view_pager);
        btnToFirst = (ImageButton)findViewById(R.id.btn_to_first);
        btnToLast = (ImageButton)findViewById(R.id.btn_to_last);
        btnOk = (ImageButton)findViewById(R.id.btn_ok);
        btnToFirst.setImageDrawable(getResources().getDrawable(R.drawable.ic_to_first));
        btnToLast.setImageDrawable(getResources().getDrawable(R.drawable.ic_to_last));
        btnOk.setImageDrawable(getResources().getDrawable(R.drawable.ic_btn_ok));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String personBle =(String)getIntent().getSerializableExtra(EXTRA_ID);

        //TODO: 尚未考虑实时更新，目前将此段放入onResume()来使得activity出栈后会执行
        mPersonList = PersonSet.get(this).getPersonList();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Person p = mPersonList.get(position);
                return UserFragment.newInstance(p.getBle());
            }

            @Override
            public int getCount() {
                return mPersonList.size();
            }
        });

        for (int i = 0; i < mPersonList.size(); i++) {
            if (mPersonList.get(i).getBle().equals(personBle)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        // Jump ro First , Jump to Last.
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == 0){
                    btnToFirst.setVisibility(View.INVISIBLE);
                }else if (position == mPersonList.size()-1){
                    btnToLast.setVisibility(View.INVISIBLE);
                }else{
                    btnToLast.setVisibility(View.VISIBLE);
                    btnToFirst.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnToFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0,true);
            }
        });
        btnToLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mPersonList.size()-1,true);
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
