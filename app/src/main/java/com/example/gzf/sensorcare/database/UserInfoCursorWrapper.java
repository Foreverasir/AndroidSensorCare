package com.example.gzf.sensorcare.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.example.gzf.sensorcare.UserLocalInfo;
import com.example.gzf.sensorcare.database.UserInfoDbSchema.UserInfoTable;

public class UserInfoCursorWrapper extends CursorWrapper {
    public UserInfoCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public UserLocalInfo getUserInfo(){
        String bleString = getString(getColumnIndex(UserInfoTable.Cols.BLEID));
        int isWatched = getInt(getColumnIndex(UserInfoTable.Cols.ISWATCHED));
        int existFlag = getInt(getColumnIndex(UserInfoTable.Cols.EXISTFLAG));
        String helpText = getString(getColumnIndex(UserInfoTable.Cols.HELPTEXT));

        return new UserLocalInfo(bleString, isWatched > 0, existFlag > 0,helpText);
    }
}
