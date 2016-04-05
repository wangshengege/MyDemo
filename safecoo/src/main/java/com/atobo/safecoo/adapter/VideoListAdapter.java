package com.atobo.safecoo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.ui.VideoPlayActivity;

import java.util.ArrayList;

import arg.mylibrary.utils.PxUtil;

/**
 * Created by ZL on 2016/3/24.
 */
public class VideoListAdapter extends BaseAdapter{
    private ArrayList<String> items;
    private Context ctx;
    private int focusIndex;
    public VideoListAdapter(ArrayList<String> items, Context ctx) {
        this.items = items;
        this.ctx = ctx;
    }
    public void setFocus(int index){
        focusIndex=index;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder hoder=null;
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.video_item,null);
            hoder=new ViewHoder(convertView);
            convertView.setTag(hoder);
        }else{
            hoder= (ViewHoder) convertView.getTag();
        }
        onBindViewHolder(hoder,position);
        return convertView;
    }

    public void onBindViewHolder(ViewHoder holder, final int position) {
        holder.tv_title.setText(getFileName(items.get(position)));
        holder.tv_subTitle.setText(items.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayActivity.startAction(ctx,items.get(position));
            }
        });
        if(focusIndex==position){
            holder.itemView.setSelected(true);
        }else{
            holder.itemView.setSelected(false);
        }
    }
    private String getFileName(String path){
        String string = "";
        string=path.substring(path.lastIndexOf("/"),path.lastIndexOf("."));
        return  string;
    }

    class ViewHoder {
        TextView tv_title;
        TextView tv_subTitle;
        View itemView;
        public ViewHoder(View itemView) {
            this.itemView=itemView.findViewById(R.id.ll_item);
            tv_title= (TextView) itemView.findViewById(R.id.tv_title);
            tv_subTitle= (TextView) itemView.findViewById(R.id.tv_subtitle);
        }
    }
}
