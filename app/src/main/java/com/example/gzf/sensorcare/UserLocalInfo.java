package com.example.gzf.sensorcare;

import android.content.ContentValues;

import com.example.gzf.sensorcare.database.UserInfoDbSchema;

public class UserLocalInfo {
    String bleId;
    boolean isWatched;
    boolean existFlag;
    String helpText;

    UserLocalInfo() {
        isWatched = false;
        existFlag = true;
        helpText = null;
    }

    public UserLocalInfo(String bleId, boolean isWatched, boolean existFlag, String helpText) {
        this.bleId = bleId;
        this.isWatched = isWatched;
        this.existFlag = existFlag;
        this.helpText = helpText;
    }

    public String getBleId() {
        return bleId;
    }

    public void setBleId(String bleId) {
        this.bleId = bleId;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    public boolean isExistFlag() {
        return existFlag;
    }

    public void setExistFlag(boolean existFlag) {
        this.existFlag = existFlag;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    private static ContentValues getContentValues(UserLocalInfo userLocalInfo) {
        ContentValues values = new ContentValues();
        values.put(UserInfoDbSchema.UserInfoTable.Cols.BLEID, userLocalInfo.getBleId());
        values.put(UserInfoDbSchema.UserInfoTable.Cols.ISWATCHED, userLocalInfo.isWatched());
        values.put(UserInfoDbSchema.UserInfoTable.Cols.EXISTFLAG, userLocalInfo.isExistFlag());
        values.put(UserInfoDbSchema.UserInfoTable.Cols.HELPTEXT, userLocalInfo.getHelpText());

        return values;
    }
}

