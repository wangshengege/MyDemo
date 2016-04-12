package com.atobo.safecoo.ui.biz;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import arg.mylibrary.biz.YSException;
import arg.mylibrary.net.RequestCall;
import arg.mylibrary.ui.base.AbstractBaseActivity;
import arg.mylibrary.utils.Tools;

/**
 * 作者: ws
 * 日期: 2016/4/6.
 * 介绍： activity的基类
 */
public abstract class BaseActivity extends AbstractBaseActivity {
    /**设置是否提示网络请求失败的提示*/
    private boolean isShowNoNetToast=true;
    private final String mPageName = this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if(SafeCooUtils.isTablet(self)){//平板
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        }*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
    }
    /**设置是否提示网络请求失败的提示*/
    protected void setShowNoNetToast(boolean isShow){
        isShowNoNetToast=isShow;
    }
    @Override
    public void onResponseError(Throwable t, RequestCall call) {
        super.onResponseError(t, call);
        if(isShowNoNetToast){
            Tools.showToast(self, YSException.NO_AVAILABLE_NETWORK);
        }
    }
}
