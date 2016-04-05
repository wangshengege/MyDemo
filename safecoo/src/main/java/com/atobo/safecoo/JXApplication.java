package com.atobo.safecoo;

import android.content.Intent;
import android.util.Log;

import arg.mylibrary.biz.YSApplication;

/**
 * Created by ZL on 2016/3/29.
 */
public class JXApplication extends YSApplication {
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        super.uncaughtException(thread, ex);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Log.e("hy", "uncaughtException, thread: " + thread + " name: " + thread.getName() + " id: " + thread.getId()
                + "exception: " + ex);
        // 退出应用：
        exit();
    }
}
