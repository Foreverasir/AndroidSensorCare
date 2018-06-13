package com.example.gzf.sensorcare;

import android.content.Context;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonSet {
    private static PersonSet sPersonSet;
    private List<Person> personList;

    /* 辅助信息存储在本机，而非从服务器获取 */
    private ArrayMap<String,UserLocalInfo> personInfoList;

    public static PersonSet get(Context context) {
        if (sPersonSet == null) {
            sPersonSet = new PersonSet(context);
        }
        return sPersonSet;
    }

    private PersonSet(Context context) {
        personList = new ArrayList<>();
        personInfoList = new ArrayMap<>();
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public ArrayMap<String, UserLocalInfo> getPersonInfoList() {
        return personInfoList;
    }

    public void setPersonList(List<Person> list){
        personList = list;
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

    public class UserLocalInfo{
        boolean isWatched;
        boolean existFlag;
        String helpText;

        UserLocalInfo() {
            isWatched = false;
            existFlag = true;
            helpText = null;
        }

        public UserLocalInfo(boolean isWatched, boolean existFlag, String helpText) {
            this.isWatched = isWatched;
            this.existFlag = existFlag;//是否在界面中显示，此处之后完善
            this.helpText = helpText;
        }

        public boolean isWatched() {
            return isWatched;
        }

        public void setWatched(boolean watched) {
            isWatched = watched;
        }

        public String getHelpText() {
            return helpText;
        }

        public void setHelpText(String helpText) {
            this.helpText = helpText;
        }
    }
}
