package com.atobo.safecoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.common.SafeCooConfig;
import com.atobo.safecoo.entity.VideoEntiity;
import com.atobo.safecoo.utils.ImgLoadUtils;

import java.util.ArrayList;

import arg.mylibrary.common.CommonFunction;

/**
 * Created by ws on 2016/3/30.
 * 视频的适配器
 */
public class PalyAdapter extends BaseAdapter{
    private Context ctx;
    private ArrayList<VideoEntiity> res;
    private LayoutInflater inflater;
    public PalyAdapter(Context ctx, ArrayList<VideoEntiity> res) {
        this.ctx = ctx;
        this.res = res;
        inflater=LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return res.size();
    }

    @Override
    public Object getItem(int position) {
        return res.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.play_item,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        VideoEntiity en= (VideoEntiity) getItem(position);
        ImgLoadUtils.loadImageRes(en.getViodeoImg(),holder.iv_mv);
        if(!SafeCooConfig.PREVIEW){
            if(position<9){
                holder.tv_num.setText("00"+position);
            }else if(position>8 && position<99){
                holder.tv_num.setText("0"+position);
            }else{
                holder.tv_num.setText(""+position);
            }
        }
        holder.tv_title.setText(en.getTitle());
        return convertView;
    }
    class ViewHolder{
        ImageView iv_mv;
        TextView tv_num;
        TextView tv_title;
        public ViewHolder(View view) {
            iv_mv= (ImageView) view.findViewById(R.id.iv_mv);
            tv_num= (TextView) view.findViewById(R.id.tv_num);
            tv_title= (TextView) view.findViewById(R.id.tv_title);
        }
    }
}
