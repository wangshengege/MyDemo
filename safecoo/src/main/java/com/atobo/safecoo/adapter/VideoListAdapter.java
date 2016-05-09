package com.atobo.safecoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.common.VideoThumbnailLoader;
import com.atobo.safecoo.entity.LocalVideoEntity;
import com.atobo.safecoo.ui.VideoPlayActivity;

import java.util.ArrayList;

/**
 * Created by ws on 2016/3/24.
 */
public class VideoListAdapter extends BaseAdapter{
    private ArrayList<LocalVideoEntity> items;
    private Context ctx;
    private int focusIndex;
    public VideoListAdapter(ArrayList<LocalVideoEntity> items, Context ctx) {
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
        ViewHolder hoder=null;
        if(convertView==null){
            convertView=LayoutInflater.from(ctx).inflate(R.layout.play_item,null);
            hoder=new ViewHolder(convertView);
            convertView.setTag(hoder);
        }else{
            hoder= (ViewHolder) convertView.getTag();
        }
        onBindViewHolder(hoder,position);
        return convertView;
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        final LocalVideoEntity entiity= (LocalVideoEntity) getItem(position);
        holder.tv_title.setText(entiity.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayActivity.startAction(ctx, entiity.getPath());
            }
        });
        if(focusIndex==position){
            holder.itemView.setSelected(true);
        }else{
            holder.itemView.setSelected(false);
        }
        VideoThumbnailLoader.getIns().display(entiity,holder.iv_mv);
    }
    private String getFileName(String path){
        String string = "";
        string=path.substring(path.lastIndexOf("/"),path.lastIndexOf("."));
        return  string;
    }

    class ViewHolder {
        ImageView iv_mv;
        TextView tv_num;
        TextView tv_title;
        View itemView;
        public ViewHolder(View view) {
            itemView=view;
            iv_mv= (ImageView) view.findViewById(R.id.iv_mv);
            tv_num= (TextView) view.findViewById(R.id.tv_num);
            tv_title= (TextView) view.findViewById(R.id.tv_title);
        }
    }
}
