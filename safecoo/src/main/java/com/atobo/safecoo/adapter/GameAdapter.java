package com.atobo.safecoo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.entity.GameEntity;
import com.atobo.safecoo.utils.ImgLoadUtils;
import com.atobo.safecoo.view.coverflow.CoverFlowAdapter;
import com.atobo.safecoo.view.coverflow.CoverFlowView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import arg.mylibrary.utils.LogTools;
import arg.mylibrary.utils.Tools;

/**
 * Created by ws on 2016/3/30.
 */
public class GameAdapter extends CoverFlowAdapter implements GameEntity.LoadingListener {
    private Bitmap loadBm;
    private ArrayList<GameEntity> items;
    private CoverFlowView coverFlowView;
    public GameAdapter(Context ctx) {
        loadBm = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.geme_load);
    }
    public void setCoverFlowView(CoverFlowView coverFlowView){
    this.coverFlowView=coverFlowView;
    }
    public void setData(ArrayList<GameEntity> items) {
        this.items = items;
        for (GameEntity en : items) {
            en.setLoadingListener(this);
        }
    }

    @Override
    public int getCount() {
        return items == null || items.size() < 3 ? 3 : items.size();
    }

    @Override
    public Bitmap getImage(int position) {
        // BitmapFactory.decodeResource(ctx.getResources(), GRES[position])
        return items == null || items.get(position).getBitmap() == null ? loadBm : items.get(position).getBitmap();
    }

    @Override
    public void LoadBack(GameEntity.LoacBackState state, Bitmap bm) {
        if (state == GameEntity.LoacBackState.SUCCESS) {
            notifyDataSetChanged();
            if(coverFlowView!=null)
            coverFlowView.invalidate();
        }
    }
}
