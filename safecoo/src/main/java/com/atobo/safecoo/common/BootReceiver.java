package com.atobo.safecoo.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.atobo.safecoo.ui.LoadingActivity;

import arg.mylibrary.biz.YSApplication;
import arg.mylibrary.utils.LogTools;
import arg.mylibrary.utils.Tools;

/**
 * Created by ZL on 2016/3/29.
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override

    public void onReceive(Context context, Intent intent) {

// TODO Auto-generated method stub
        LogTools.logi(this, "BootReceiver启动");
        Intent startintent = YSApplication.getContext().getPackageManager().getLaunchIntentForPackage(Tools.getPackageName(YSApplication.getContext()));
        context.startActivity(startintent );
/*
        Intent bootStartIntent = new Intent(context, LoadingActivity.class);

        bootStartIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(bootStartIntent);*/

    }
}
