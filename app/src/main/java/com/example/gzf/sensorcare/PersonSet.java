package com.example.gzf.sensorcare;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonSet {
    private static PersonSet sPersonSet;
    private List<Person> personList;

    public static PersonSet get(Context context) {
        if (sPersonSet == null) {
            sPersonSet = new PersonSet(context);
        }
        return sPersonSet;
    }

    private PersonSet(Context context) {
        personList = new ArrayList<>();
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> list){
        personList = list;

    }
    public Person getPerson(String ble) {
        for (Person p : personList) {
            if (p.getBle().equals(ble)) {
                return p;
            }
        }
        return null;
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
