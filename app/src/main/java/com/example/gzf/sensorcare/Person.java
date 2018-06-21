package com.example.gzf.sensorcare;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;


public class Person implements Parcelable {
    private String name;
    private String ble;
    private int state;
    private String location;

    public void setName(String name) {
        this.name = name;
    }

    public void setBle(String ble) {
        this.ble = ble;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Person() {

    }

    protected Person(Parcel in) {
        name = in.readString();
        ble = in.readString();
        state = in.readInt();
        location = in.readString();
    }

    public Person(String name, String ble, int state, String location) {
        this.name = name;
        this.ble = ble;
        this.state = state;
        this.location = location;
    }

    public String getName() {
        return String.format("姓名：%s", name);
    }

    public String getRawName(){return name;}

    public String getBle() {
        return String.format("传感器mac地址：%s", ble);
    }

    public String getState() {
        if (state == 0) {
            return String.format("状态：静止");
        } else if (state == 4) {
            return String.format("状态：传感器断连");
        } else {
            return String.format("状态：离床");
        }
    }

    public int getRawState() {
        return state;
    }

    public String getLocation() {
        return String.format("位置：%s室", location);
    }

    public String toString() {
        return String.format("%s,%s,%d,%s", name, ble, state, location);
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(ble);
        dest.writeInt(state);
        dest.writeString(location);
    }
}
