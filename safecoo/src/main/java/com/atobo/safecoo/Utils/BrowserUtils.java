package com.atobo.safecoo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.util.List;

/**
 * Created by ws on 2016/4/5.
 */
public class BrowserUtils {
    /**启动浏览器*/
    public static void startBrower(Context context,String uri)
    {
        startBrower(context, Uri.parse(uri));
    }
    public static void startBrower(Context context,Uri uri)
    {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
         /*其实可以不用添加该Category*/
       // intent.addCategory("android.intent.category.BROWSABLE");
        intent.setData(uri);
     //   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         /*如果想用浏览器打开本地html文件的话，则只能通过显式intent启动浏览器*/
        boolean explicitMode=false;
        String scheme=uri.getScheme();
        if(scheme!=null&&(scheme.startsWith("file") || scheme.startsWith("content"))) {
            explicitMode=true;
        }
        if(explicitMode) {
            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        } else {
           // intent.addCategory("android.intent.category.BROWSABLE");
        }
        context.startActivity(intent);
    }
    /**启动第三方浏览器*/
    public static void startBrower(Context context,String uri,String packageName, String className)
    {
        startBrower(context,Uri.parse(uri),packageName,className);
    }
    public static void startBrower(Context context,Uri uri,String packageName, String className)
    {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(packageName,className);
        context.startActivity(intent);
    }
    /**检测app是否已安装*/
     public static boolean checkApp(Context context, String packageName){
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packs = pm.getInstalledPackages(0);
        for (PackageInfo pi : packs){
            if (pi.applicationInfo.packageName.equals(packageName)){
                return true;
            }
        }
        return false;
    }
    public static void startB(Context context,String url){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }
}
