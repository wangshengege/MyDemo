package com.atobo.safecoo.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.atobo.safecoo.R;
import com.atobo.safecoo.common.SafeCooConfig;
import com.atobo.safecoo.net.AppDao;
import com.atobo.safecoo.ui.biz.BackActivity;
import com.atobo.safecoo.utils.BrowserUtils;

import arg.mylibrary.net.RequestCall;
import arg.mylibrary.utils.JSONUtils;
import arg.mylibrary.utils.Tools;

/**
 * Created by ws on 2016/3/30.
 * 直播互动
 */
public class InteractionActivity extends BackActivity {
    private String url;
    public static void startAction(Context ctx){
        Intent intent=new Intent(ctx,InteractionActivity.class);
        ctx.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_interaction);
        if(!SafeCooConfig.PREVIEW)
        AppDao.getLive(self);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Tools.isEmpty(url)){
            AlertDialog.Builder builder=new AlertDialog.Builder(self);
            builder.setCancelable(false);
            builder.setTitle("提示");
            builder.setMessage("确定将退出直播？");
            builder.setNegativeButton("继续播放", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BrowserUtils.startB(self,url);
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.show();
        }
    }

    @Override
    public void onResponseSuccess(RequestCall call) {
        super.onResponseSuccess(call);
        if(call.getState()==200){
            url= JSONUtils.getJStr(call.getJson(),"url");
            if(!Tools.isEmpty(url)){
                BrowserUtils.startB(self,url);
            }
        }else{
            Tools.showToast(self,call.getMsg());
        }
    }
}
