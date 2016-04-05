package com.atobo.safecoo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.atobo.safecoo.MainActivity;
import com.atobo.safecoo.R;
import com.atobo.safecoo.net.AppDao;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import arg.mylibrary.net.RequestCall;
import arg.mylibrary.ui.base.AbstractBaseActivity;
import arg.mylibrary.utils.LogTools;
import arg.mylibrary.utils.Tools;

/**
 * Created by ZL on 2016/3/28.
 */
public class LoadingActivity extends AbstractBaseActivity {
    @ViewInject(R.id.tv_version)
    private TextView tv_version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_loading);
        /* AppDao.login(new RequestCallBack<String>() {
             @Override
             public void onSuccess(ResponseInfo<String> responseInfo) {
                 TextHtmlActivity.startAction(self, responseInfo.result);
             }

             @Override
             public void onFailure(HttpException e, String s) {
             }
         });*/
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(self, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    @Override
    public void onResponseSuccess(RequestCall call) {
        super.onResponseSuccess(call);
        Tools.showToast(self,call.getJson().toString());
    }

    @Override
    public void onResponseError(Throwable t, RequestCall call) {
        super.onResponseError(t, call);
        Tools.showToast(self, t.getMessage());
    }
}
