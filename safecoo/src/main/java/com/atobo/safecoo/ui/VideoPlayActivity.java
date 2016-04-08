package com.atobo.safecoo.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.service.voice.VoiceInteractionService;
import android.widget.MediaController;
import android.widget.VideoView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.ui.biz.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;
import java.net.URI;

import arg.mylibrary.ui.base.AbstractBaseActivity;
import arg.mylibrary.utils.LogTools;
import arg.mylibrary.utils.Tools;

/**
 * Created by ws on 2016/3/25.
 * 视频播放界面
 */
public class VideoPlayActivity extends BaseActivity {
    @ViewInject(R.id.videoView)
    private VideoView videoView;
    private MediaController mediaco;
    private String path;
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
        intent.putExtra("type",type);
        intent.putExtra("path",uri);
        ctx.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_videoplay);
        Intent intent=getIntent();
        int type=intent.getIntExtra("type",0);
        path=intent.getStringExtra("path");
        LogTools.logd(self,path);
        mediaco=new MediaController(this);
        videoView.setMediaController(mediaco);
        mediaco.setMediaPlayer(videoView);
        if(type==0){
            File file=new File(path);
            if(file.exists()) {
                videoView.setVideoPath(file.getAbsolutePath());
            }else{
                Tools.showToast(self,"文件不存在");
                finish();
            }
        }else{
            Uri uri=Uri.parse(path);
            videoView.setVideoURI(uri);
        }
        //让VideiView获取焦点
        videoView.requestFocus();
        videoView.start();

    }
}
