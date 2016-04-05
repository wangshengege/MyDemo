package com.atobo.safecoo.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.adapter.VideoListAdapter;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import arg.mylibrary.ui.base.AbstractBaseActivity;
import arg.mylibrary.utils.LogTools;
import arg.mylibrary.utils.Tools;

/**
 * Created by ZL on 2016/3/24.
 */
public class VideoListActivity extends BackActivity {
    @ViewInject(R.id.gv_videolist)
    private GridView gv_videolist;
    private int focusIndex = 0;
    private ArrayList<String> item;
    private VideoListAdapter mAdapter;

    /***/
    public static void startAction(Context ctx) {
        Intent intent = new Intent(ctx, VideoListActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_videolist);
        item = getVList();
        if (item.size() < 1) {
            Tools.showToast(self, "没有视频");
        }
        mAdapter = new VideoListAdapter(item, self);
        mAdapter.setFocus(focusIndex);
        gv_videolist.setAdapter(mAdapter);
    }

    private ArrayList<String> getVList() {
        ArrayList<String> list = new ArrayList<>();
        ContentResolver contentResolver = self.getContentResolver();
        String[] projection = new String[]{MediaStore.Video.Media.DATA};
        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        int fileNum = cursor.getCount();
        for (int counter = 0; counter < fileNum; counter++) {
            list.add(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(item.size()<0){
            return false;
        }
        if (event.KEYCODE_DPAD_UP == keyCode) {  //如果按下的是上键
            if (focusIndex != 0) {
                //  recycler.getChildAt(focusIndex-1).requestFocus();
                focusIndex -= 1;
                mAdapter.setFocus(focusIndex);
                mAdapter.notifyDataSetChanged();
            }
            return true;
        }

        if (event.KEYCODE_DPAD_DOWN == keyCode) {  //如果按下的是下键
            if (focusIndex != item.size() - 1) {
                //  recycler.getChildAt(focusIndex+1).requestFocus();
                focusIndex += 1;
                mAdapter.setFocus(focusIndex);
                mAdapter.notifyDataSetChanged();
            }
            return true;
        }
        if (event.KEYCODE_DPAD_CENTER == keyCode) {
            VideoPlayActivity.startAction(self, item.get(focusIndex));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
