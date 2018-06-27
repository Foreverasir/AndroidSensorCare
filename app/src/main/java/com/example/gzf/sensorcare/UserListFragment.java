package com.example.gzf.sensorcare;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.gzf.sensorcare.toolmodels.WriteLog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

public class UserListFragment extends Fragment {
    private static final String DIALOG_CHOOSE_ALERT = "choose_alert";
    private static final String SAVED_DEBUG_VISIBLE = "see_debug";

    private RecyclerView mRecyclerView;
    private PersonAdapter personAdapter;

    private List<Person> personList = new ArrayList<>();
    private List<Person> personExistList = new ArrayList<>();
    PersonSet personSet;
    private ArrayMap<String, UserLocalInfo> personInfoList;

    // 是否隐藏开发测试用的Tag
    private boolean seeDebugTagFlag;


    // 用于通知
    NotificationManager mNotificationManager;
    Notification mNotification;
    Notification.Builder builder;
    PendingIntent pi;
    private static final int NOTIFICATION_ID_1 = 1;

    // Alert
    private Vibrator vibrator;
    private SoundPool soundPool;
    int hit;
    private boolean vibrateFlag;
    private boolean soundFlag;
    private static final int REQUEST_ALERT = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        seeDebugTagFlag = false;
        // TODO：下面两个标志考虑加入数据库
        vibrateFlag = false;
        soundFlag = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        if (savedInstanceState != null) {
            seeDebugTagFlag = savedInstanceState.getBoolean(SAVED_DEBUG_VISIBLE);
        }

        updateUI();

        //调用RecyclerView#addOnItemTouchListener方法能添加一个RecyclerView.OnItemTouchListener对象
        mRecyclerView.addOnItemTouchListener(new RecyclerViewClickListener(getActivity(), new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String personBle;
                if(seeDebugTagFlag) {
                    personBle = personList.get(position).getBle();
                } else {
                    personBle = personExistList.get(position).getBle();
                }
                Intent intent = UserPagerActivity.newIntent(getActivity(), personBle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(), "Long Click " + personList.get(position), Toast.LENGTH_SHORT).show();
            }
        }));

        new FetchPersonState().execute();

        vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        hit = soundPool.load(getContext(), R.raw.my_alert, 0);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_DEBUG_VISIBLE, seeDebugTagFlag);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.list_fragment, menu);

        MenuItem item = menu.findItem(R.id.show_debug);
        if (seeDebugTagFlag) {
            item.setTitle(R.string.hide_debug);
            item.setIcon(R.drawable.ic_hide_debug);
        } else {
            item.setTitle(R.string.show_debug);
            item.setIcon(R.drawable.ic_see_debug);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_debug:
                seeDebugTagFlag = !seeDebugTagFlag;
                getActivity().invalidateOptionsMenu();
                updateUI();
                return true;
            case R.id.choose_alert:
                FragmentManager manager = getFragmentManager();
                ChooseAlertFragment dialog = ChooseAlertFragment.newInstance(vibrateFlag,soundFlag);
                dialog.setTargetFragment(UserListFragment.this,REQUEST_ALERT);
                dialog.show(manager,DIALOG_CHOOSE_ALERT);
            case R.id.write_log:
                WriteLog writeLog = new WriteLog();
                writeLog.setFileDir("SensorCareLog");
                String content = "LOG:";
                for(Person p:personList){
                    content= String.format("%s\n%s", content, personInfoList.get(p.getBle()).getHelpText());
                }
                writeLog.setLog(content);
                try {
                    writeLog.writeLogToFile(WriteLog.TXT_TAIL);
                    Toast.makeText(getActivity(), "实验日志写入文件", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "实验日志写入失败，请进入设置开启权限", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_ALERT){
            if(resultCode == Activity.RESULT_OK){
                vibrateFlag = (boolean)data.getSerializableExtra(ChooseAlertFragment.EXTRA_VIBRATE);
                soundFlag = (boolean)data.getSerializableExtra(ChooseAlertFragment.EXTRA_SOUND);
                updateUI();
            }
        }
    }

    private void updateUI() {
        personSet = PersonSet.get(getActivity());
        personList = personSet.getPersonList();
        personExistList = personSet.getExistPersonList();
        personInfoList = personSet.getPersonInfoListFromDataBase();

        if (personAdapter == null) {
            personAdapter = new PersonAdapter(personList);
            if (!seeDebugTagFlag) {
                personAdapter.setmList(personExistList);
                personAdapter.notifyDataSetChanged();
            }
            mRecyclerView.setAdapter(personAdapter);
        } else {
            personAdapter.notifyDataSetChanged();
        }
    }

    public class PersonHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private TextView mLocationTextView;
        private TextView mStateTextView;
        private ImageView mImageView;
        private ImageView mIsWatchedWView;
        private Person mPerson;

        public PersonHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item, parent, false));
            mNameTextView = (TextView) itemView.findViewById(R.id.person_name);
            mLocationTextView = (TextView) itemView.findViewById(R.id.person_location);
            mStateTextView = (TextView) itemView.findViewById(R.id.person_state);
            mImageView = (ImageView) itemView.findViewById(R.id.list_image);
            mIsWatchedWView = (ImageView) itemView.findViewById(R.id.ic_watched);

        }

        public void bind(Person person) {
            mPerson = person;
            mNameTextView.setText(mPerson.getName());
            mStateTextView.setText(mPerson.getState());
            mLocationTextView.setText(mPerson.getLocation());
            mIsWatchedWView.setVisibility(personInfoList.get(person.getBle()).isWatched ? View.VISIBLE : View.GONE);

            int id = getResources().getIdentifier("warning.png","drawable",getActivity().getPackageName());

            if (mPerson.getRawState() == 0) {
                mImageView.setImageResource(R.drawable.safety);
            } else if (mPerson.getRawState() == 4) {
                mImageView.setImageResource(R.drawable.warning);
            } else {
                mImageView.setImageResource(R.drawable.danger);
            }
        }
    }

    public class PersonAdapter extends RecyclerView.Adapter<PersonHolder> {
        private List<Person> mList;

        public PersonAdapter(List<Person> s) {
            mList = s;
        }

        @Override
        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            return new PersonHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PersonHolder holder, int position) {
            Person person = mList.get(position);
            holder.bind(person);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public void setmList(List<Person> mList) {
            this.mList = mList;
        }
    }


    class FetchPersonState extends AsyncTask<Void, List<Person>, Void> {
        String URL = "http://47.106.85.26/android/";

        public String fetchData() throws IOException {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(URL).build();
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        }

        @Override
        protected void onProgressUpdate(List<Person>... values) {
            super.onProgressUpdate(values);
            personSet.setPersonList(personList);
            personInfoList = personSet.getPersonInfoList();
            personExistList = personSet.getExistPersonList();
            if (seeDebugTagFlag) {
                personAdapter.setmList(personList);
            } else {
                personAdapter.setmList(personExistList);
            }
            personAdapter.notifyDataSetChanged();

            // 报警方式：音频、震动与通知
            for (Person p : personList) {
                if (p.getRawState() > 0) {
                    if (personInfoList.get(p.getBle()).isWatched) {
                        /* 音频 */
                        if(soundFlag) {
                            soundPool.play(hit, 5, 5, 0, 0, (float) 1);
                        }
                        /* 震动 */
                        if(vibrateFlag) {
                            vibrator.vibrate(new long[]{100, 1000, 500, 1000, 500, 2000}, -1);
                        }
                        /* 通知 */
                        mNotificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                        // TODO:明确参数含义
                        pi = PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(), MainActivity.class), 0);
                        builder = new Notification.Builder(getActivity());

                        builder.setContentTitle("SensorCare")
                                .setContentText("报警：" + p.getRawName() + "传感器发生变化" + p.getState())
                                .setSubText("原" + p.getLocation())
                                .setTicker("离床检测系统报警")
                                //设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示
                                .setSmallIcon(R.mipmap.ic_launcher)
                                //设置默认声音和震动
                                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                                .setAutoCancel(true)//点击后取消
                                .setWhen(System.currentTimeMillis())//设置通知时间
                                .setPriority(Notification.PRIORITY_HIGH)//高优先级
                                .setContentIntent(pi);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
                        }
                        mNotification = builder.build();
                        mNotificationManager.notify(NOTIFICATION_ID_1, mNotification);
                    }
                }
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                try {
                    String json = fetchData();
                    personList = JSON.parseArray(json, Person.class);
                    //Log.i("test",personList.get(0).toString());
                    publishProgress(personList);
                } catch (Exception e) {
                    Log.d("error", e.toString());
                }
                try {
                    // Simulate network access.
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
