package com.example.gzf.sensorcare;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;

import com.example.gzf.sensorcare.database.UserInfoBaseHelper;
import com.example.gzf.sensorcare.database.UserInfoDbSchema;
import com.example.gzf.sensorcare.database.UserInfoDbSchema.UserInfoTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonSet {
    private static PersonSet sPersonSet;
    /* 由于实时从服务器获取数据，故不存储于本地*/
    private List<Person> personList;

    /* 辅助信息存储在本机，而非从服务器获取 */
    /* 6.20使用SQLite数据库存储 */
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private ArrayMap<String,UserLocalInfo> personInfoList;

    public static PersonSet get(Context context) {
        if (sPersonSet == null) {
            sPersonSet = new PersonSet(context);
        }
        return sPersonSet;
    }

    private PersonSet(Context context) {
        personList = new ArrayList<>();

        mContext = context.getApplicationContext();
        mDatabase = new UserInfoBaseHelper(mContext).getWritableDatabase();
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public ArrayMap<String, UserLocalInfo> getPersonInfoList() {
        return personInfoList;
    }

    public void setPersonList(List<Person> list){
        personList = list;

        // 对personList每项，查询数据库，若存在不处理，否则添加。
        if(personList!=null){
            for(Person p : personList){

            }
        }

        if(personList!=null){
            if(personInfoList.isEmpty()){
                for(Person p : personList){
                    personInfoList.put(p.getBle(),new UserLocalInfo());
                }
            } else {
                // TODO:此处应该添加检查是否有item被服务器删除，与服务器同步
                // for()...
                for(Person p :personList){
                    if(!personInfoList.containsKey(p.getBle())){
                        personInfoList.put(p.getBle(),new UserLocalInfo());
                    }
                }
            }
        }

    }
    public Person getPerson(String ble) {
        for (Person p : personList) {
            if (p.getBle().equals(ble)) {
                return p;
            }
        }
        return null;
    }

    public UserLocalInfo getUserLocalInfo(String ble){
        return personInfoList.get(ble);
    }

    // 2018.6.11更新，UUID暂且弃用
    public Person getPersonByUUID(UUID id) {
        for (Person p : personList) {
            if (p.getmId().equals(id)) {
                return p;
            }
        }
        return null;
    }
}
