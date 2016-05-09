package com.atobo.safecoo.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.ui.biz.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;

import arg.mylibrary.utils.LogTools;
import arg.mylibrary.utils.Tools;

/**
 * 作者: ws
 * 日期: 2016/4/21.
 * 介绍：
 */
public class VideoPlayActivity extends BaseActivity {
    @ViewInject(R.id.videoView)
    private VideoView videoView;
    private MediaController mediaco;
    private String path;
    private int type;
    public static void startAction(Context ctx,String path){
        if(Tools.isEmpty(path)){
            Tools.showToast(ctx,"路径错误");
            return;
        }
        Intent intent=new Intent(ctx,VideoPlayActivity.class);
        intent.putExtra("type",0);
        intent.putExtra("path",path);
        ctx.startActivity(intent);
    }
    /**播放网络格式
     * @param type 置为非0的数为网络地址，0为本地
     * */
    public static void startAction(Context ctx,String uri,int type){
        if(Tools.isEmpty(uri)){
            Tools.showToast(ctx,"路径错误");
            return;
        }
        Intent intent=new Intent(ctx,VideoPlayActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("path", uri);
        ctx.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_videoplay);
        Intent intent=getIntent();
        type=intent.getIntExtra("type", 0);
        path=intent.getStringExtra("path");
        LogTools.logd(self, path);
        mediaco=new MediaController(this);
        videoView.setMediaController(mediaco);
        mediaco.setMediaPlayer(videoView);
        setData();
    }
    //设置地址。播放
    private void setData(){
        if(type==0){
            File file=new File(path);
            if(file.exists()) {
                videoView.setVideoURI(Uri.fromFile(file));
            }else{
                Tools.showToast(self,"文件不存在");
                finish();
            }
        }else{
            Uri uri=Uri.parse(path);
            videoView.setVideoURI(uri);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(layoutParams);
        videoView.requestFocus();
        videoView.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(videoView.isPlaying()){
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
        System.gc();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(mediaco!=null && mediaco.isShowing()){
            return mediaco.dispatchKeyEvent(event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
