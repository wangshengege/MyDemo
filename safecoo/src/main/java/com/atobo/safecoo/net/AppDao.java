package com.atobo.safecoo.net;

import com.lidroid.xutils.http.RequestParams;

import arg.mylibrary.net.BaseDao;
import arg.mylibrary.net.JsonCallback;
import arg.mylibrary.net.RequestCall;
import arg.mylibrary.utils.LogTools;

/**
 * Created by ws on 2016/3/29.
 */
public class AppDao extends BaseDao {
    /**获取视频数据
     * @param type 视频类型（大范围 ）
     * @param pid 视频分类（小范围）
     * */
    public static void getVedio(String type,String pid,JsonCallback callBack){
        LogTools.i("AppDao","type:"+type+"pid:"+pid);
        RequestParams params=new RequestParams();
        params.addQueryStringParameter("type",type);
        params.addQueryStringParameter("pid",pid);
        request(new RequestCall(IPConfig.GET_VIODE), params, callBack);
    }
    /**获取游戏列表*/
    public static void getGames(JsonCallback callBack){
        request(new RequestCall(IPConfig.GETGAMES),callBack);
    }
    /**获取直播地址*/
    public static void getLive(JsonCallback callBack){
        request(new RequestCall(IPConfig.LIVE),callBack);
    }
}
