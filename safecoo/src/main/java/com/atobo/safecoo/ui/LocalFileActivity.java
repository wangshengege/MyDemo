package com.atobo.safecoo.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.atobo.safecoo.R;
import com.atobo.safecoo.ui.biz.BackActivity;
import com.atobo.safecoo.ui.ftp.FtpActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

import arg.mylibrary.utils.Tools;

/**
 * 作者: ws
 * 日期: 2016/4/18.
 * 介绍：本地文件
 */
public class LocalFileActivity extends BackActivity implements View.OnClickListener{
    @ViewInject(R.id.btn_local)
    private Button btn_local;
    @ViewInject(R.id.btn_ftp)
    private Button btn_ftp;
    @ViewInject(R.id.btn_smb)
    private Button btn_smb;
    public static void startAction(Context ctx){
        Tools.toActivity(ctx,LocalFileActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_localfile);
        initView();
    }
    private void initView(){
        btn_local.setOnClickListener(this);
        btn_ftp.setOnClickListener(this);
        btn_smb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_local:
                FileDirectoryActivity.startAction(self);
                break;
            case R.id.btn_ftp:
                FtpActivity.startAction(self);
                break;
            case R.id.btn_smb:
                SmbActivity.startAction(self);
                break;
        }
    }
}
