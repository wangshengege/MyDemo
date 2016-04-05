package com.atobo.safecoo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.atobo.safecoo.R;

import arg.mylibrary.autolayout.AutoFrameLayout;

/**
 * Created by ZL on 2016/3/30.
 */
public class GameAdapter extends BaseAdapter {
    private Context ctx;
    private static final int[] GRES={R.drawable.gameleft,R.drawable.football,R.drawable.gameright};
    public GameAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return GRES.length;
    }

    @Override
    public Object getItem(int position) {
        return GRES[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView=new ImageView(ctx);
        imageView.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Gallery.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource((int)getItem(position));
        return imageView;
    }
}
