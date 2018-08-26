package com.daleyzou.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * sharePreference封装
 */
public class PrefUtils {
    public static boolean getBoolean(Context cxt,String key,boolean defValue){
        SharedPreferences sp = cxt.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }

    public static void setBoolen(Context cxt,String key,boolean value){
        SharedPreferences sp = cxt.getSharedPreferences("config",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }

    public static String getString(Context cxt,String key,String value){
        SharedPreferences sp = cxt.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getString(key,value);
    }

    public static void setString(Context cxt,String key,String value){
        SharedPreferences sp = cxt.getSharedPreferences("config",Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    public static int getInt(Context cxt,String key,int value){
        SharedPreferences sp = cxt.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getInt(key,value);
    }

    public static void setInt(Context cxt,String key,int value){
        SharedPreferences sp = cxt.getSharedPreferences("config",Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).commit();
    }
}
