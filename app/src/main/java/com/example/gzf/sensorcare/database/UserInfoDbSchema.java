package com.example.gzf.sensorcare.database;

public class UserInfoDbSchema {
    public static final class UserInfoTable {
        public static final String NAME = "UserInfo";

        public static final class Cols {
            public static final String BLEID = "bleId";
            public static final String ISWATCHED = "isWatched";
            public static final String EXISTFLAG = "existFlag";
            public static final String HELPTEXT = "helpText";
        }
    }
}
