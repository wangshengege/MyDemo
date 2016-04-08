package com.atobo.safecoo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.adapter.PalyAdapter;
import com.atobo.safecoo.entity.VideoEntiity;
import com.atobo.safecoo.ui.biz.BackActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

import arg.mylibrary.common.FileAccessor;
import arg.mylibrary.utils.Tools;

/**
 * Created by ws on 2016/3/30.
 * 精品推荐
 */
public class RecommendActivity extends BackActivity {
    @ViewInject(R.id.gv_videolist)
    private GridView gv_videolist;
    private PalyAdapter mAdapter;
    //数据源
    private ArrayList<VideoEntiity> items;
    private int[] res = {R.drawable.ic_jp1, R.drawable.ic_jp2,
            R.drawable.ic_jp3, R.drawable.ic_jp4,
            R.drawable.ic_jp5, R.drawable.ic_jp6,
            R.drawable.ic_jp7, R.drawable.ic_jp8,
            R.drawable.ic_jp9, R.drawable.ic_jp10,
            R.drawable.ic_jp11, R.drawable.ic_jp12,};

    /***/
    public static void startAction(Context ctx) {
        Intent intent = new Intent(ctx, RecommendActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_tuijian);
        items=new ArrayList<>();
        mAdapter = new PalyAdapter(self, items);
        gv_videolist.setAdapter(mAdapter);
        gv_videolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 2) {
                    return;
                }
                if (FileAccessor.isExistExternalStore()) {
                    String path = String.format("%s/anku/xyj%d.mp4", FileAccessor.getExternalStorePath(), position + 1);
                    VideoPlayActivity.startAction(self, path);
                } else {
                    Tools.showToast(self, "SD卡不可用");
                }
            }
        });
    }

}
