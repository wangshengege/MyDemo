package com.atobo.safecoo.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.GridView;
import android.widget.TextView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.adapter.VideoListAdapter;
import com.atobo.safecoo.entity.LocalVideoEntity;
import com.atobo.safecoo.ui.biz.BackActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import arg.mylibrary.utils.Tools;

/**
 * Created by ws on 2016/3/24.
 * 视频列表
 */
public class VideoListActivity extends BackActivity {
    @ViewInject(R.id.gv_videolist)
    private GridView gv_videolist;
    private int focusIndex = 0;
    private VideoListAdapter mAdapter;
    private ArrayList<LocalVideoEntity> videoList;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    private Handler handler;
    /***/
    public static void startAction(Context ctx) {
        Intent intent = new Intent(ctx, VideoListActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_playhome);
        tv_title.setText("本地视频");

        handler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    Tools.showToast(self, "没有视频");
                }else{
                    mAdapter = new VideoListAdapter(videoList, self);
                    gv_videolist.setAdapter(mAdapter);
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                super.run();
                videoList=getVideoList();
                if (videoList==null || videoList.size() < 1) {//没有数据的消息通知
                    handler.sendEmptyMessage(1);
                }else{
                    handler.sendEmptyMessage(2);
                }
            }
        }.start();

    }

    private ArrayList<LocalVideoEntity> getVideoList() {
        ArrayList<LocalVideoEntity> videoList = null;
        Cursor cursor = getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                null, null);
        if (cursor != null) {
            videoList = new ArrayList<>();
            while (cursor.moveToNext()) {
               /* int id = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media._ID));*/
                String title = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                String displayName = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                String path = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                long duration = cursor
                        .getInt(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                long size = cursor
                        .getLong(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                int width=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH));
                int height=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT));
                LocalVideoEntity entiity=new LocalVideoEntity();
                entiity.setPath(path);
                entiity.setTitle(title);
                entiity.setDisplayName(displayName);
                entiity.setDuration(duration);
                entiity.setSize(size);
                entiity.setWidth(width);
                entiity.setHeight(height);
                videoList.add(entiity);
            }
        }
        return videoList;
    }

}
