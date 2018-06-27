package com.example.gzf.sensorcare.toolmodels;


import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class WriteLog {
    private String fileDir;//Set the dir name for file
    private String log;// Log output time stamp

    public static final String CSV_TAIL = ".csv";
    public static final String TXT_TAIL = ".txt";

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public void writeLogToFile(String tail) throws IOException {
        Calendar c = Calendar.getInstance();
        String filename = fileDir + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.HOUR) + "-" + c.get(Calendar.MINUTE)+ "-" + c.get(Calendar.SECOND) + tail;
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + fileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, filename);
        FileOutputStream fos;
        byte[] data = log.getBytes();
        fos = new FileOutputStream(file);
        fos.write(data);
        fos.flush();
        fos.close();
    }
}
