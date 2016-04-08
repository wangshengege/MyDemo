package com.atobo.safecoo.net;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import arg.mylibrary.net.BaseDao;
import arg.mylibrary.net.JsonCallback;
import arg.mylibrary.net.RequestCall;

/**
 * Created by ws on 2016/3/29.
 */
public class AppDao extends BaseDao {
    /**获取视频数据
     * @param type 视频类型（大范围 ）
     * @param pid 视频分类（小范围）
     * */
    public static void getVedio(String type,String pid,JsonCallback callBack){
        RequestParams params=new RequestParams();
        params.addQueryStringParameter("type",type);
        params.addQueryStringParameter("pid",pid);
        request(new RequestCall(IPConfig.GET_VIODE), params, callBack);
    }

}
