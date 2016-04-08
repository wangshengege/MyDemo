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
 * 安全视频
 */
public class SafeActivity extends BackActivity {
    @ViewInject(R.id.gv_videolist)
    private GridView gv_videolist;
    private PalyAdapter mAdapter;
    //数据源
    private ArrayList<VideoEntiity> items=new ArrayList<>();
    private int[] res={R.drawable.ic_aq1,R.drawable.ic_aq2,
            R.drawable.ic_aq3,R.drawable.ic_aq4,
            R.drawable.ic_aq5,R.drawable.ic_aq6,
            R.drawable.ic_aq7,R.drawable.ic_aq8};
    /***/
    public static void startAction(Context ctx) {
        Intent intent = new Intent(ctx, SafeActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_safe);
        mAdapter=new PalyAdapter(self,items);
        gv_videolist.setAdapter(mAdapter);
        gv_videolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 2) {
                    return;
                }
                if(FileAccessor.isExistExternalStore()) {
                    String path = String.format("%s/anku/ak%d.mp4", FileAccessor.getExternalStorePath(), position + 1);
                    VideoPlayActivity.startAction(self, path);
                }else{
                    Tools.showToast(self,"SD卡不可用");
                }
            }
        });
    }
}
