package com.romanport.rpws;

import android.util.Log;

public class RpwsLog {

    public static void Log(String topic, String msg) {
        Log.i("RPWS-"+topic, msg);
    }

    public static void LogException(String topic, String msg, Exception ex) {
        StackTraceElement[] e = ex.getStackTrace();
        msg += ex.toString()+" @";
        for(int i = 0; i<e.length; i++) {
            msg += e[i].toString();
        }
        Log(topic, msg);
    }

}
