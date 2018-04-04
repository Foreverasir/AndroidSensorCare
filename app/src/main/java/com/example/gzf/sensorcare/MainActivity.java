package com.example.gzf.sensorcare;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<Person> mPersonList = new ArrayList<>();
    private PersonAdapter personAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView=(RecyclerView)findViewById(R.id.recyler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        if(savedInstanceState != null){
            mPersonList = savedInstanceState.getParcelableArrayList("PersonList");
        }
//        else {
//            mPersonList.add(new Person("姜人和","12:34:56:78:12:34",0,"422-3"));
//        }
        new FetchPersonState().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        personAdapter = new PersonAdapter(mPersonList);
        setupAdapter();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("PersonList", (ArrayList<Person>)mPersonList);
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

    private void setupAdapter(){
        mRecyclerView.setAdapter(personAdapter);
    }

    public class PersonHolder extends RecyclerView.ViewHolder{
        private TextView mNameTextView;
        private TextView mLocationTextView;
        private TextView mStateTextView;
        private ImageView mImageView;
        private Person mPerson;
        public PersonHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item,parent,false));
            mNameTextView=(TextView)itemView.findViewById(R.id.person_name);
            mLocationTextView=(TextView)itemView.findViewById(R.id.person_location);
            mStateTextView=(TextView)itemView.findViewById(R.id.person_state);
            mImageView=(ImageView) itemView.findViewById(R.id.list_image);
        }

        public void bind(Person person){
            mPerson=person;
            mNameTextView.setText(mPerson.getName());
            mStateTextView.setText(mPerson.getState());
            mLocationTextView.setText(mPerson.getLocation());
            if(mPerson.getRawState()==0){
//                itemView.setBackgroundColor(0xFFFF9900);
                mImageView.setImageResource(R.drawable.safety);
            }else if(mPerson.getRawState()==4){
//                itemView.setBackgroundColor(0xFFFF9900);
                mImageView.setImageResource(R.drawable.warning);
            }
            else{
                mImageView.setImageResource(R.drawable.danger);
            }
        }
    }

    public class PersonAdapter extends RecyclerView.Adapter<PersonHolder>{
        private List<Person> mList;
        public PersonAdapter(List<Person> s){
            mList=s;
        }


        @Override
        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(MainActivity.this);
            return new PersonHolder(layoutInflater,parent);
        }

        @Override
        public void onBindViewHolder(PersonHolder holder, int position) {
            Person person=mList.get(position);
//            Log.i("index",person.toString());

            holder.bind(person);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }



    class FetchPersonState extends AsyncTask<Void,List<Person>,Void> {
        String URL="http://192.168.0.3:8000/android/";

        public String fetchData() throws IOException{
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(URL).build();
            try(Response response=client.newCall(request).execute()){
                return response.body().string();
            }
        }


        @Override
        protected void onProgressUpdate(List<Person>... values) {
            super.onProgressUpdate(values);
//            setupAdapter(mPersonList);

            mRecyclerView.setAdapter(new PersonAdapter(values[0]));
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (true){
                try{
                    //Log.i("test",fetchData());
                    String json=fetchData();
                    mPersonList= JSON.parseArray(json,Person.class);
                    Log.i("test",mPersonList.get(0).toString());
                    publishProgress(mPersonList);
                }
                catch (Exception e){
                    Log.d("error",e.toString());
                }
                try {
                    // Simulate network access.
                    Thread.sleep(2000);
                } catch (InterruptedException e) {}
            }
        }
    }
}

