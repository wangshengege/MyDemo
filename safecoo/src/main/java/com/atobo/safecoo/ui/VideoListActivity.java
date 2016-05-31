package com.atobo.safecoo.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.adapter.VideoListAdapter;
import com.atobo.safecoo.entity.LocalVideoEntity;
import com.atobo.safecoo.ui.biz.BackActivity;
import com.atobo.safecoo.utils.FileOpenUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;

import arg.mylibrary.autolayout.AutoLinearLayout;
import arg.mylibrary.common.FileAccessor;
import arg.mylibrary.utils.LogTools;
import arg.mylibrary.utils.PxUtil;
import arg.mylibrary.utils.Tools;
import arg.mylibrary.utils.Utils;

/**
 * Created by ws on 2016/3/24.
 * 视频列表
 */
public class VideoListActivity extends BackActivity {
    @ViewInject(R.id.gv_videolist)
    private GridView gv_videolist;
    @ViewInject(R.id.ll_tab_group)//小分类标题
    private LinearLayout ll_tab_group;
    private VideoListAdapter mAdapter;
    private ArrayList<LocalVideoEntity> videoList;
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @ViewInject(R.id.iv_title)//标题图片
    private ImageView iv_title;
    /**
     * 0是sd卡，1是安酷视频
     */
    private int type;
    private String[] TAB = {"安全月", "国外"};
    //选择的标签
    private TextView selectTab;

    /***/
    public static void startAction(Context ctx) {
        Intent intent = new Intent(ctx, VideoListActivity.class);
        intent.putExtra("type", 0);
        ctx.startActivity(intent);
    }

    /**
     * @param type 0是sd卡，1是安酷视频
     */
    public static void startAction(Context ctx, int type) {
        Intent intent = new Intent(ctx, VideoListActivity.class);
        intent.putExtra("type", type);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_playhome);
        tv_title.setText("本地视频");
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        LogTools.logi(self, "type:" + type);
        if (type == 1) {
            initView();
            initData(0);
            if (videoList != null && videoList.size() > 0) {
                mAdapter = new VideoListAdapter(videoList, self);
                gv_videolist.setAdapter(mAdapter);
            } else {
                Tools.showToast(self, "未找到视频");
            }
        } else {
            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 1) {
                        Tools.showToast(self, "未找到视频");
                    } else {
                        mAdapter = new VideoListAdapter(videoList, self);
                        gv_videolist.setAdapter(mAdapter);
                    }
                }
            };
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    videoList = getVideoList();
                    if (videoList == null || videoList.size() < 1) {//没有数据的消息通知
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(2);
                    }
                }
            }.start();
        }
    }

    //初始化界面
    private void initView() {
        iv_title.setImageResource(R.drawable.ic_safe);
        tv_title.setText(getResources().getString(R.string.menu_menu2));
        //用一个图片模拟标签
       /* ImageView iv = new ImageView(self);
        iv.setLayoutParams(new AutoLinearLayout.LayoutParams(AutoLinearLayout.LayoutParams.WRAP_CONTENT, AutoLinearLayout.LayoutParams.WRAP_CONTENT));
        iv.setImageResource(R.drawable.ic_ybs_tb);
        ll_tab_group.addView(iv);*/
        initTab();
    }

    //初始化标签
    private void initTab() {
        for (int i = 0; i < TAB.length; i++) {
            TextView tv = new TextView(self);
            tv.setText(TAB[i]);
            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            tv.setLayoutParams(params);
            if (i == 0) {
                tv = getSelText(tv);
                selectTab = tv;
            } else {
                tv = getNorText(tv);
            }
            tv.setTag(i);
            tv.setOnClickListener(tabClick);
            ll_tab_group.addView(tv);
        }
    }

    private View.OnClickListener tabClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == selectTab) {
                return;
            }
            int type = (int) v.getTag();
            getNorText(selectTab);
            selectTab = getSelText((TextView) v);
            initData(type);
        }
    };

    //初始化数据
    private void initData(int type) {
        String dir = "/anku/.jjsp/";//急救视频的地址
        if (type == 1) {
            dir = "/anku/.gw/";//国外视频
        }
        if (FileAccessor.isExistExternalStore()) {
            if (videoList == null) {
                videoList = new ArrayList<>();
            } else {
                videoList.clear();
            }
            String fPath = FileAccessor.getExternalStorePath() + dir;
            LogTools.logi(self,"Dir:"+fPath);
            File f = new File(fPath);
            File[] flist = f.listFiles();
            if (flist != null && flist.length > 0) {
                for (File fitem : flist) {//遍历视频
                    String path = fitem.getAbsolutePath();
                    if (FileOpenUtils.getFileType(path) == FileOpenUtils.FileType.VODIO) {
                        LocalVideoEntity entiity = new LocalVideoEntity();
                        entiity.setPath(path);
                        String name = Utils.getFilename(path);//文件名字
                        entiity.setImagePath(fPath + name.substring(0, 3) + ".jpg");//图片地址
                        entiity.setTitle(name);
                        entiity.setDisplayName(name.substring(3, name.length() - 4));
                        entiity.setSize(fitem.length());
                        videoList.add(entiity);
                    }
                }
            } else {
                Tools.showToast(self,"未找到视频");
            }
            if(mAdapter!=null){
                mAdapter.notifyDataSetChanged();
            }
        } else {
            Tools.showToast(self, "SD卡不可用");
        }
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
                int width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH));
                int height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT));
                LocalVideoEntity entiity = new LocalVideoEntity();
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

    //未选中的选项卡
    private TextView getNorText(TextView tv) {
        tv.setTextColor(getResources().getColor(R.color.gray));
        tv.setTextSize(getTextSize());
        tv.setBackgroundResource(0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tv.getLayoutParams();
        layoutParams.setMargins(PxUtil.dip2px(self, 10), 0, 0, 0);
        return tv;
    }

    //根据屏幕分辨率获取文字大小
    private int getTextSize() {
       /*int w= PxUtil.getScreenWidth(self);
        LogTools.logi(PlayHomeActivity.this,"w:"+w);
        if (w<=1290){//低清
            return w/100;
        }else if(w>1290 && w< 1930){//高清
            return w/150;
        }else {//超清
            return w/200;
        }*/
        int textSize = PxUtil.sp2px(self, 6);
        LogTools.logi(self, "testSize:" + textSize);
        if (textSize < 15) {
            textSize = 17;
        } else if (textSize > 25) {
            textSize = 15;
        }
        return textSize;
    }

    //选中的选项卡
    private TextView getSelText(TextView tv) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tv.getLayoutParams();
        layoutParams.setMargins(PxUtil.dip2px(self, 10), 0, 0, 0);
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setTextSize(getTextSize());
        tv.setBackgroundResource(R.drawable.bg_tab_text);
        return tv;
    }
}
