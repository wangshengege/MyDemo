package arg.mylibrary.utils;

import android.util.Log;

import arg.mylibrary.biz.YsConfig;

/**
 * 打印log相关类
 */
public class LogTools {
    private static final String LOG = "com.hengyi.dectorDoc";
    private boolean isDeubg=true;
    private static LogTools instance;

    private static LogTools getInstance() {
        if (instance == null) {
            instance = new LogTools();
        }
        return instance;
    }

    static {
        getInstance().isDeubg = YsConfig.isDebug();
    }

    public static void showLogi(String msg) {
        if(getInstance().isDeubg){
            Log.i(LOG, msg);
        }
    }

    public static void showLogi(String tag, String msg) {
        if(getInstance().isDeubg){
        Log.i(tag, msg);
        }
    }

    public static void showLogw(String msg) {
        if(getInstance().isDeubg){
        Log.w(LOG, msg);
        }
    }

    public static void showLoge(String msg) {
        if(getInstance().isDeubg){
        Log.e(LOG, msg);}
    }

    /**
     * @param cls 当前类
     * @param msg 日志内容
     */
    public static void logi(Object cls, String msg) {
        if(getInstance().isDeubg) {
            Log.i(cls.getClass().getSimpleName(), msg);
        }
    }

    /**
     * @param cls 当前类
     * @param msg 日志内容
     */
    public static void logw(Object cls, String msg) {
        if(getInstance().isDeubg) {
            Log.w(cls.getClass().getSimpleName(), msg);
        }
    }

    /**
     * @param cls 当前类
     * @param msg 日志内容
     */
    public static void logd(Object cls, String msg) {
        if(getInstance().isDeubg) {
            Log.d(cls.getClass().getSimpleName(), msg);
        }
    }

    /**
     * @param cls 当前类
     * @param msg 日志内容
     */
    public static void loge(Object cls, String msg) {
        if(getInstance().isDeubg) {
            Log.e(cls.getClass().getSimpleName(), msg);
        }
    }
    /**
     * @param tag 标记
     * @param msg 日志内容
     */
    public static void e(String tag, String msg) {
        if(getInstance().isDeubg) {
            Log.e(tag,msg);
        }
    }
    /**
     * @param tag 标记
     * @param msg 日志内容
     */
    public static void i(String tag, String msg) {
        if(getInstance().isDeubg) {
            Log.i(tag,msg);
        }
    }
    /**
     * @param tag 标记
     * @param msg 日志内容
     */
    public static void v(String tag, String msg) {
        if(getInstance().isDeubg) {
            Log.v(tag,msg);
        }
    }
    /**
     * @param tag 标记
     * @param msg 日志内容
     */
    public static void d(String tag, String msg) {
        if(getInstance().isDeubg) {
            Log.d(tag,msg);
        }
    }
}
