package arg.mylibrary.net;

import com.lidroid.xutils.http.RequestParams;

import arg.mylibrary.utils.LogTools;

/**调用请求*/
public abstract class BaseDao {
	public static final String TAG=YsHttpUtils.TAG;
	private static YsHttpUtils utils;
	static{
		utils=new YsHttpUtils();
		setCurrentHttpCacheExpiry(5*1000);
	}
	/**设置缓存时间*/
	protected   static void setCurrentHttpCacheExpiry(long time){
		utils.setCurrentHttpCacheExpiry(time);
	}
	/**
	 * 请求数据
	 * @param call
	 * 			请求参数封装
	 * @param params
	 * 			请求数据
	 * @param jsonCallback
	 * 			请求返回
	 * */
	protected static void request(RequestCall call,RequestParams params,JsonCallback jsonCallback){
		if(call==null){
			LogTools.e(TAG, "RequestCall  is null");
			return;
		}
		utils.setHeader(IpConfig.getYsHeader());
		if(isPost(call.getUrl(), params)){
			utils.postBaseJSON(call, jsonCallback, params);
		}else{
			if(params==null){
				utils.getBaseJSON(call, jsonCallback);
			}else{
				utils.getBaseJSON(call, jsonCallback,params);
			}

		}
	}
	/**
	 * 请求数据
	 * @param call
	 * 			请求参数封装
	 * @param jsonCallback
	 * 			请求返回
	 * */
	protected static void request(RequestCall call,JsonCallback jsonCallback){
		request(call,null,jsonCallback);
	}
	/**判断是不是post*/
	private static boolean isPost(String url,RequestParams params){

		if(params==null || params.getQueryStringParams()!=null){
			LogTools.i(TAG, "get url:"+url);
			return false;
		}else{
			LogTools.i(TAG, "post url:"+url);
			return true;
		}
	}
}
