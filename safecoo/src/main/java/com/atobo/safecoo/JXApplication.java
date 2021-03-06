package com.atobo.safecoo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.atobo.safecoo.common.PolyvDemoService;
import com.atobo.safecoo.common.SafeCooConfig;
import com.atobo.safecoo.common.UmengMsgManager;
import com.atobo.safecoo.ftp.FtpServerService;
import com.atobo.safecoo.ui.PlayHomeActivity;
import com.atobo.safecoo.ui.ftp.FtpActivity;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.SDKUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.net.InetAddress;

import arg.mylibrary.biz.YSApplication;
import arg.mylibrary.utils.LogTools;
import arg.mylibrary.utils.Tools;

/**
 * Created by ws on 2016/3/29.
 */
public class JXApplication extends YSApplication {
    private static JXApplication instance;
    private static Context ctx;
    public static JXApplication getInstance() {
        if (instance == null) {
            instance = new JXApplication();
        }
        return instance;
    }

    /** 获取全局的上下文 */
    public static Context getContext() {
        return ctx;
    }
    private final int NOTIFICATIONID = 7890;
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

    public void initPolyvCilent() {
        File saveDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            saveDir = new File(Environment.getExternalStorageDirectory().getPath() + "/safecoo/Download");
            if (saveDir.exists() == false)
                saveDir.mkdir();
        }

        //网络方式取得SDK加密串，（推荐）
//		new LoadConfigTask().execute();
        PolyvSDKClient client = PolyvSDKClient.getInstance();
        String str = "AFyHvzQeJCove288nqDLb/qPvFxMr/adCrx3lOeQGNnfHEWnwqBJdJxiNBaEl+Ji9zWiNJfuK0F2ycu4QMnjnNS+kJMw8Z2jpIYyIR1yEZaNObfGmPJblJ7vUA7IbxHvoEy9dZR90rElGaybUnyswA==";
        //设置SDK加密串    iPGXfu3KLEOeCW4KXzkWGl1UYgrJP7hRxUfsJGldI6DEWJpYfhaXvMA+32YIYqAOocWd051v5XUAU17LoVlgZCSEVNkx11g7CxYadcFPYPozslnQhFjkxzzjOt7lUPsW
//		client.setConfig("你的SDK加密串");
        client.setConfig(str);
        //下载文件的目录
        client.setDownloadDir(saveDir);
        //初始化数据库服务
        client.initDatabaseService(this);
        //启动服务
        client.startService(getApplicationContext(), PolyvDemoService.class);
        //启动Bugly
        client.initCrashReport(getApplicationContext());
        //启动Bugly后，在学员登录时设置学员id
//		client.crashReportSetUserId(userId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = getApplicationContext();
        Logger.init("jx").setMethodCount(3);//设置方法个数
      //  UmengMsgManager.init();
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "safecoo/Cache");
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(2)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                        // .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                        // You can pass your own memory cache implementation/
                .memoryCache(new WeakMemoryCache()).memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                        // .discCacheFileNameGenerator(new Md5FileNameGenerator())//
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100)
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000))
                .writeDebugLogs() // Remove for release app
                .build();

        // Initialize ImageLoader with configuration
        ImageLoader.getInstance().init(config);
        if(!PlayHomeActivity.isDebug) {
            initPolyvCilent();
        }
    }

    private class LoadConfigTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String config = SDKUtil.getUrl2String("http://demo.polyv.net/demo/appkey.php", false);
            if (TextUtils.isEmpty(config)) {
                try {
                    throw new Exception("没有取到数据");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return config;
        }

        @Override
        protected void onPostExecute(String config) {
            PolyvSDKClient client = PolyvSDKClient.getInstance();
            client.setConfig(config);
        }
    }
    public void setupNotification(Context context) {
        // Get NotificationManager reference
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nm = (NotificationManager) context
                .getSystemService(ns);

        // get ip address
        InetAddress address = FtpServerService.getLocalInetAddress();
        if (address == null) {
            LogTools.loge(this, "Unable to retreive the local ip address");
            return;
        }
        String iptext = "ftp://" + address.getHostAddress() + ":"
                + FtpServerService.getPort() + "/";

        // Instantiate a Notification
        int icon = R.drawable.notification;
        CharSequence tickerText = String.format("%s\n%s",
                context.getString(R.string.notif_server_starting), iptext);
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);

        // Define Notification's message and Intent
        CharSequence contentTitle = context.getString(R.string.notif_title);
        CharSequence contentText = iptext;

        Intent notificationIntent = new Intent(context, FtpActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText,
                contentIntent);
        notification.flags |= Notification.FLAG_ONGOING_EVENT;

        // Pass Notification to NotificationManager
        nm.notify(NOTIFICATIONID, notification);

        LogTools.showLoge( "Notication setup done");
    }

    public void clearNotification(Context context) {
        LogTools.showLoge("Clearing the notifications");
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nm = (NotificationManager) context
                .getSystemService(ns);
        nm.cancelAll();
        LogTools.showLoge( "Cleared notification");
    }
}
