package com.atobo.safecoo.common;

import android.app.Notification;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.atobo.safecoo.JXApplication;
import com.atobo.safecoo.R;
import com.umeng.message.ALIAS_TYPE;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.IUmengUnregisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.message.tag.TagManager;

import arg.mylibrary.utils.LogTools;

/**
 * 作者: ws
 * 日期: 2016/4/14.
 * 介绍：
 */
public class UmengMsgManager {
    private static final String TAG=UmengMsgManager.class.getSimpleName();
    private static UmengMsgManager ourInstance;

    public static UmengMsgManager getInstance() {
        if(ourInstance==null){
            ourInstance = new UmengMsgManager();
        }
        return ourInstance;
    }

    private UmengMsgManager() {
    }
    private PushAgent mPushAgent;
    UmengMessageHandler messageHandler = new UmengMessageHandler(){
        /**
         * 参考集成文档的1.6.3
         * http://dev.umeng.com/push/android/integration#1_6_3
         * */
        @Override
        public void dealWithCustomMessage(final Context context, final UMessage msg) {
            new Handler().post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    // 对自定义消息的处理方式，点击或者忽略
                    boolean isClickOrDismissed = true;
                    if(isClickOrDismissed) {
                        //自定义消息的点击统计
                        UTrack.getInstance(JXApplication.getContext()).trackMsgClick(msg);
                    } else {
                        //自定义消息的忽略统计
                        UTrack.getInstance(JXApplication.getContext()).trackMsgDismissed(msg);
                    }
                    Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                }
            });
        }

        /**
         * 参考集成文档的1.6.4
         * http://dev.umeng.com/push/android/integration#1_6_4
         * */
        @Override
        public Notification getNotification(Context context,
                                            UMessage msg) {
            switch (msg.builder_id) {
                case 1:
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                    myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                    myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                    myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                    myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                    builder.setContent(myNotificationView)
                            .setSmallIcon(getSmallIconId(context, msg))
                            .setTicker(msg.ticker)
                            .setAutoCancel(true);

                    return builder.build();

                default:
                    //默认为0，若填写的builder_id并不存在，也使用默认。
                    return super.getNotification(context, msg);
            }
        }
    };
    /**
     * 该Handler是在BroadcastReceiver中被调用，故
     * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
     * 参考集成文档的1.6.2
     * http://dev.umeng.com/push/android/integration#1_6_2
     * */
    UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
        @Override
        public void dealWithCustomAction(Context context, UMessage msg) {
            Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
        }
    };
    public static void init(){
        getInstance().mPushAgent = PushAgent.getInstance(JXApplication.getContext());
        getInstance(). mPushAgent.setDebugMode(true);
        getInstance(). mPushAgent.setMessageHandler(getInstance().messageHandler);
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
        //参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        getInstance(). mPushAgent.setNotificationClickHandler(getInstance().notificationClickHandler);
//		if (MiPushRegistar.checkDevice(this)) {
//            MiPushRegistar.register(this, "2882303761517400865", "5501740053865");
//		}
    }
    public static void openPush(){
        if(getInstance().mPushAgent.isEnabled()){
            return;
        }
        getInstance().mPushAgent.onAppStart();
        //开启推送并设置注册的回调处理
        getInstance().mPushAgent.enable(new IUmengRegisterCallback() {

            @Override
            public void onRegistered(String registrationId) {
                LogTools.i(TAG,"开启成功，registrationId："+registrationId);
            }
        });
    }
    public static void closePush(){
        if(!getInstance().mPushAgent.isEnabled()){
            return;
        }
        //关闭推送并设置注销的回调处理
        getInstance(). mPushAgent.disable(new IUmengUnregisterCallback() {
            @Override
            public void onUnregistered(String s) {
                LogTools.i(TAG, "关闭成功,：" + s);
            }
        });
    }

    public   void addTag(String tag) {
        if (! getInstance().mPushAgent.isRegistered())
        {
            LogTools.d(TAG,"mPushAgent is not registed");
            return;
        }
     new AddTagTask(tag).execute();
    }
    public void setAlias(String alias){
        if (! getInstance().mPushAgent.isRegistered())
        {
            LogTools.d(TAG,"mPushAgent is not registed");
            return;
        }
        new AddAliasTask(alias, ALIAS_TYPE.WEIXIN).execute();
    }
    class AddTagTask extends AsyncTask<Void, Void, String>{

        String tagString;
        String[] tags;
        public AddTagTask(String tag) {
            // TODO Auto-generated constructor stub
            tagString = tag;
            tags = tagString.split(",");
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                TagManager.Result result = mPushAgent.getTagManager().add(tags);
                LogTools.d(TAG, result.toString());
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Fail";
        }

        @Override
        protected void onPostExecute(String result) {
            LogTools.d(TAG,"Add Tag:\n" + result);
        }
    }

    class AddAliasTask extends AsyncTask<Void, Void, Boolean> {

        String alias;
        String aliasType;

        public AddAliasTask(String aliasString,String aliasTypeString) {
            // TODO Auto-generated constructor stub
            this.alias = aliasString;
            this.aliasType = aliasTypeString;
        }

        protected Boolean doInBackground(Void... params) {
            try {
                return mPushAgent.addAlias(alias, aliasType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (Boolean.TRUE.equals(result))
                LogTools.i(TAG, "alias was set successfully.");

        }

    }
}
