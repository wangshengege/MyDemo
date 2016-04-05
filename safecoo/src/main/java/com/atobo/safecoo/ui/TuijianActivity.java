package com.atobo.safecoo.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.atobo.safecoo.R;
import com.atobo.safecoo.adapter.PalyAdapter;
import com.lidroid.xutils.view.annotation.ViewInject;

import arg.mylibrary.common.FileAccessor;
import arg.mylibrary.utils.Tools;

/**
 * Created by ZL on 2016/3/30.
 */
public class TuijianActivity extends BackActivity{
    @ViewInject(R.id.gv_videolist)
    private GridView gv_videolist;
    private PalyAdapter mAdapter;
    private int[] res={R.drawable.ic_jp1,R.drawable.ic_jp2,
            R.drawable.ic_jp3,R.drawable.ic_jp4,
            R.drawable.ic_jp5,R.drawable.ic_jp6,
            R.drawable.ic_jp7,R.drawable.ic_jp8,
            R.drawable.ic_jp9,R.drawable.ic_jp10,
            R.drawable.ic_jp11,R.drawable.ic_jp12,};
    /***/
    public static void startAction(Context ctx) {
        Intent intent = new Intent(ctx, TuijianActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_tuijian);
        mAdapter=new PalyAdapter(self,res);
        gv_videolist.setAdapter(mAdapter);
        gv_videolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>2){
                    return;
                }
                if(FileAccessor.isExistExternalStore()){
                String path=String.format("%s/anku/xyj%d.mp4", FileAccessor.getExternalStorePath(),position+1);
                VideoPlayActivity.startAction(self,path);}else{
                    Tools.showToast(self,"SD卡不可用");
                }
            }
        });
    }

}
