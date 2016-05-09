package com.atobo.safecoo.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Message;

/**
 * 作者: ws
 * 日期: 2016/4/6.
 * 介绍：项目使用的工具类
 */
public class SafeCooUtils {
    /**
     * 判断当前设备是手机还是平板
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    public static Message getMsg(int what,Object obj){
        Message msg=new Message();
        msg.what=what;
        msg.obj=obj;
        return msg;
    }
}
