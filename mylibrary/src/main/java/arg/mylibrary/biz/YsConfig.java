package arg.mylibrary.biz;

/**
 * Created by ws on 2015/12/16.
 */
public class YsConfig {
    private static final String LOG = "dectorDoc.YsConfig";
    private boolean isDebug=true;
    private static YsConfig instance;

    private static YsConfig getInstance() {
        if (instance == null) {
            instance = new YsConfig();
        }
        return instance;
    }
    /**是不是debug*/
    public  static boolean isDebug() {
        return getInstance().isDebug;
    }
    /**消息提示音*/
    public static final String setting_news_alert="setting_news_alert";
    /**消息震动*/
    public static final String setting_news_vibrate="setting_news_vibrate";
    /**客服电话*/
    public static final String Custom_phone_numble="18715516236";
}
