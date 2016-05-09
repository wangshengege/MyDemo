package arg.mylibrary.net;

import arg.mylibrary.biz.YSApplication;
import arg.mylibrary.common.ACache;
import arg.mylibrary.utils.Tools;

/**
 * 网络地址
 */
public class IpConfig {
    /**项目域名*/
  //  public static final String IP = "http://192.168.0.11:80";
    public static final String IP="http://www.safecoo.com";
    //public static final String PORT = "8787";
    /**项目文件夹*/
    public static final String FOLDER="safetym/safetyapp/index.php";



    /**
     * 设置请求头
     */
    public static void setYsHeader(YsHeader ysHeader) {
        ACache aCache = ACache.get(YSApplication.getContext());
        if (Tools.isEmpty(ysHeader)) {
            aCache.remove("YsHeader");
        } else {
            aCache.put("YsHeader", ysHeader);
        }
    }

    /**
     * 获取请求头
     */
    public static YsHeader getYsHeader() {
        YsHeader ysHeader = null;
        ACache aCache = ACache.get(YSApplication.getContext());
        ysHeader = (YsHeader) aCache.getAsObject("YsHeader");
        return ysHeader;
    }

    /**
     * 清除请求头
     */
    public static void cleanYsHeader() {
        YsHeader ysHeader = getYsHeader();
        if (ysHeader != null) {
            ysHeader.setToken("");
            ysHeader.setUserId("");
            setYsHeader(ysHeader);
        }
    }
}
