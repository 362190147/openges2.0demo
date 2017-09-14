package com.cjw.util;

import com.cjw.fad.BuildConfig;

/**
 * Created by cjw on 2017/8/23.
 */

public class Log {
    public static boolean DEBUG = BuildConfig.DEBUG;


    public static void e(String tag,String msg){
        if(DEBUG)
            android.util.Log.e(tag, msg);
    }

    public static void d(String tag,String msg){
        if(DEBUG)
            android.util.Log.d(tag, msg);
    }

    public static void v(String tag,String msg){
        if(DEBUG)
            android.util.Log.v(tag, msg);
    }

    public static void w(String tag,String msg){
        if(DEBUG)
            android.util.Log.w(tag, msg);
    }
}
