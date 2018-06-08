package com.example.gzf.sensorcare;

import android.support.v4.app.Fragment;
import android.widget.Toast;




public class MainActivity extends SingleFragmentActivity {
//    private RecyclerView mRecyclerView;
//    private List<Person> mPersonList = new ArrayList<>();
//    private PersonAdapter personAdapter;
//
//    Context mContext = MainActivity.this;
//    NotificationManager mNotificationManager;
//    Notification mNotification;
//    Notification.Builder builder;
//    Intent mIntent;
//    PendingIntent pi;
//
//    // Alert
//    private Vibrator vibrator;
//    private SoundPool soundPool;
//    int hit;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.list_fragment);
//        mRecyclerView=(RecyclerView)findViewById(R.id.recyler_view);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//
//        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        mIntent = new Intent(this, MainActivity.class);
//        pi = PendingIntent.getActivity(this,0,mIntent,0);
//        builder = new Notification.Builder(this);
//
//        if(savedInstanceState != null){
//            mPersonList = savedInstanceState.getParcelableArrayList("PersonList");
//        }
////        else {
////            mPersonList.add(new Person("姜人和","12:34:56:78:12:34",0,"422-3"));
////        }
//        new FetchPersonState().execute();
//
//        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
//        hit = soundPool.load(this, R.raw.my_alert, 0);
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        personAdapter = new PersonAdapter(mPersonList);
//        setupAdapter();
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState){
//        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList("PersonList", (ArrayList<Person>)mPersonList);
//    }

    @Override
    protected Fragment createFragment() {
        return new UserListFragment();
    }

    long startTime = 0;
    @Override
    public void onBackPressed() {

        long currentTime = System.currentTimeMillis();
        if ((currentTime - startTime) >= 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            startTime = currentTime;
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            finish();
        }
    }


//    private static final int NOTIFICATION_FLAG = 1;

//    public void testNotificationMethod(View view){
//        soundPool.play(hit, 5, 5, 0, 0, (float)1);
//        vibrator.vibrate(new long[]{100,1000,500,1000,500,2000,500,2000},-1);

//        // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        switch (view.getId()) {
//            // 默认通知 API16及之后可用
//            case R.id.btn1:
//                PendingIntent pendingIntent3 = PendingIntent.getActivity(this, 0,
//                        new Intent(Intent.ACTION_VIEW), 0);
//                // 通过Notification.Builder来创建通知，注意API Level
//                Intent hangIntent = new Intent();
//                hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setClass(this,MainActivity.class);
//                PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 0, hangIntent,PendingIntent.FLAG_CANCEL_CURRENT);
//                // API16之后才支持
//                Notification notify3 = new Notification.Builder(this)
//                        .setSmallIcon(R.drawable.danger)
//                        .setTicker("TickerText:" + "您有新短消息，请注意查收！")
//                        .setContentTitle("Notification Title")
//                        .setContentText("This is the notification message")
//                        .setContentIntent(pendingIntent3)
//                        .setFullScreenIntent(hangPendingIntent,true)
//                        .setNumber(1).build(); // 需要注意build()是在API
//                // level16及之后增加的，API11可以使用getNotificatin()来替代
//                notify3.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
//                manager.notify(NOTIFICATION_FLAG, notify3);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
//                Log.i("testNotification",notify3.tickerText.toString());
//                break;
//            // 自定义通知
//            case R.id.btn4:
//                // Notification myNotify = new Notification(R.drawable.message,
//                // "自定义通知：您有新短信息了，请注意查收！", System.currentTimeMillis());
//                Notification myNotify = new Notification();
//                myNotify.icon = R.drawable.message;
//                myNotify.tickerText = "TickerText:您有新短消息，请注意查收！";
//                myNotify.when = System.currentTimeMillis();
//                myNotify.flags = Notification.FLAG_NO_CLEAR;// 不能够自动清除
//                RemoteViews rv = new RemoteViews(getPackageName(),
//                        R.layout.my_notification);
//                rv.setTextViewText(R.id.text_content, "hello wrold!");
//                myNotify.contentView = rv;
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                PendingIntent contentIntent = PendingIntent.getActivity(this, 1,
//                        intent, 1);
//                myNotify.contentIntent = contentIntent;
//                manager.notify(NOTIFICATION_FLAG, myNotify);
//                break;
//            case R.id.btn5:
//                // 清除id为NOTIFICATION_FLAG的通知
//                manager.cancel(NOTIFICATION_FLAG);
//                // 清除所有的通知
//                // manager.cancelAll();
//                break;
//            default:
//                break;
//        }
//    }
}

