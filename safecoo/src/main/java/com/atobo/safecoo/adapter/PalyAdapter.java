package com.atobo.safecoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.atobo.safecoo.R;

/**
 * Created by ZL on 2016/3/30.
 */
public class PalyAdapter extends BaseAdapter{
    private Context ctx;
    private int[] res;
    private LayoutInflater inflater;
    public PalyAdapter(Context ctx, int[] res) {
        this.ctx = ctx;
        this.res = res;
        inflater=LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return res.length;
    }

    @Override
    public Object getItem(int position) {
        return res[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.play_item,null);
        ImageView imageView= (ImageView) view.findViewById(R.id.iv_mv);
        imageView.setImageResource((int)getItem(position));
        return view;
    }
}
