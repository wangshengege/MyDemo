package com.atobo.safecoo.net;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import arg.mylibrary.net.BaseDao;

/**
 * Created by ZL on 2016/3/29.
 */
public class AppDao extends BaseDao {
    public static void login(RequestCallBack<String> callBack){
         HttpUtils httpClient=new HttpUtils();
        RequestParams params=new RequestParams();
        params.addBodyParameter("login_form","raoyuqing");
        params.addBodyParameter("yuqing", "yuqing");
        httpClient.send(HttpRequest.HttpMethod.POST, "http://www.safecoo.com/start",
                params, callBack);
    }

}
