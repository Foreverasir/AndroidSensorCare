package com.example.gzf.sensorcare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;

import com.example.gzf.sensorcare.database.UserInfoBaseHelper;
import com.example.gzf.sensorcare.database.UserInfoCursorWrapper;
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
    private ArrayMap<String, UserLocalInfo> personInfoList;

    public static PersonSet get(Context context) {
        if (sPersonSet == null) {
            sPersonSet = new PersonSet(context);
        }
        return sPersonSet;
    }

    private PersonSet(Context context) {
        personList = new ArrayList<>();
        personInfoList = new ArrayMap<>();

        mContext = context.getApplicationContext();
        mDatabase = new UserInfoBaseHelper(mContext).getWritableDatabase();
    }

    // 获取非Debug测试用的数目
    public List<Person> getExistPersonList() {
        List<Person> result = new ArrayList<>();
        for (Person p : personList) {
            if (personInfoList.get(p.getBle()).isExistFlag()) {
                result.add(p);
            }
        }
        return result;
    }


    public List<Person> getPersonList() {
        return personList;
    }

    public ArrayMap<String, UserLocalInfo> getPersonInfoList() {
        return personInfoList;
    }

    public ArrayMap<String, UserLocalInfo> getPersonInfoListFromDataBase() {
        personInfoList = new ArrayMap<>();
        UserInfoCursorWrapper cursor = queryUserInfoList(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                personInfoList.put(cursor.getUserInfo().getBleId(), cursor.getUserInfo());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return personInfoList;
    }

    public Person getPerson(String ble) {
        for (Person p : personList) {
            if (p.getBle().equals(ble)) {
                return p;
            }
        }
        return null;
    }

    public UserLocalInfo getUserLocalInfo(String ble) {
        return personInfoList.get(ble);
    }

    public UserLocalInfo getUserLocalInfoFromDatabase(String ble) {
        UserInfoCursorWrapper cursor = queryUserInfoList(UserInfoTable.Cols.BLEID + " = ?", new String[]{ble});
        try {

            if(cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getUserInfo();
        } finally {
            cursor.close();
        }
    }

    public void setPersonList(List<Person> list) {
        personList = list;

        // 对personList每项，查询数据库，若存在不处理，否则添加。
        if (personList != null) {
            if(personInfoList.isEmpty()){// 保证一下从数据库取过数据
                personInfoList = getPersonInfoListFromDataBase();
            }
            for (Person p : personList) {
                if(getUserLocalInfoFromDatabase(p.getBle())==null){
                    addUserInfo(new UserLocalInfo(p.getBle(),false,true," "));
                    personInfoList.put(p.getBle(),new UserLocalInfo(p.getBle(),false,true," "));
                }
            }
        }
    }

    public void addUserInfo(UserLocalInfo userLocalInfo){
        ContentValues values =getContentValues(userLocalInfo);
        mDatabase.insert(UserInfoTable.NAME,null,values);
    }

    public void updateUserInfo(UserLocalInfo userLocalInfo){
        String bleId = userLocalInfo.getBleId();
        ContentValues values = getContentValues(userLocalInfo);
        mDatabase.update(UserInfoTable.NAME,values,UserInfoTable.Cols.BLEID + " = ?",new String[]{bleId});
    }

    private static ContentValues getContentValues(UserLocalInfo userLocalInfo) {
        ContentValues values = new ContentValues();
        values.put(UserInfoDbSchema.UserInfoTable.Cols.BLEID, userLocalInfo.getBleId());
        values.put(UserInfoDbSchema.UserInfoTable.Cols.ISWATCHED, userLocalInfo.isWatched() ? 1 : 0);
        values.put(UserInfoDbSchema.UserInfoTable.Cols.EXISTFLAG, userLocalInfo.isExistFlag() ? 1 : 0);
        values.put(UserInfoDbSchema.UserInfoTable.Cols.HELPTEXT, userLocalInfo.getHelpText());

        return values;
    }

    private UserInfoCursorWrapper queryUserInfoList(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                UserInfoTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new UserInfoCursorWrapper(cursor);
    }
}