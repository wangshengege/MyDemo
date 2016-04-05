package com.atobo.safecoo.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.adapter.PalyAdapter;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by ZL on 2016/3/30.
 */
public class PlayActivity extends BackActivity {
    @ViewInject(R.id.gv_videolist)
    private GridView gv_videolist;
    private PalyAdapter mAdapter;
    private int[] res={R.drawable.ic_ybs1,R.drawable.ic_ybs2,
            R.drawable.ic_ybs3,R.drawable.ic_ybs4,
            R.drawable.ic_ybs5,R.drawable.ic_ybs6,
            R.drawable.ic_ybs7,R.drawable.ic_ybs8,
            R.drawable.ic_ybs9,R.drawable.ic_ybs10,
            R.drawable.ic_ybs11,R.drawable.ic_ybs12,};
    /***/
    public static void startAction(Context ctx) {
        Intent intent = new Intent(ctx, PlayActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_playhome);
        mAdapter=new PalyAdapter(self,res);
        gv_videolist.setAdapter(mAdapter);
        gv_videolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>2){
                    return;
                }
                String path=String.format("/mnt/internal_sd/anku/ak%d.mp4",position+1);
                VideoPlayActivity.startAction(self,path);
            }
        });
    }
}
