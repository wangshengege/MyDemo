package com.atobo.safecoo.ui;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.atobo.safecoo.MainActivity;
import com.atobo.safecoo.R;
import com.atobo.safecoo.common.UmengMsgManager;
import com.atobo.safecoo.ui.biz.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by ws on 2016/3/28.
 * 加载界面
 */
public class LoadingActivity extends BaseActivity {
    @ViewInject(R.id.tv_version)
    private TextView tv_version;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_loading);
        /* AppDao.login(new RequestCallBack<String>() {
             @Override
             public void onSuccess(ResponseInfo<String> responseInfo) {
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
               MainActivity.startAction(self);
                finish();
            }
        }.start();
        UmengMsgManager.openPush();
    }
}
