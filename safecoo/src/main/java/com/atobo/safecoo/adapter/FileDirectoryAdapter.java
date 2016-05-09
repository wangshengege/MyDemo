package com.atobo.safecoo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.entity.FileInfo;
import com.atobo.safecoo.utils.FileOpenUtils;

import java.util.ArrayList;

import arg.mylibrary.utils.Tools;

/**
 * 作者: ws
 * 日期: 2016/4/18.
 * 介绍：文件目录适配器
 */
public class FileDirectoryAdapter extends RecyclerView.Adapter<FileDirectoryAdapter.ViewHolder> {
    LayoutInflater inflater;
    private ArrayList<FileInfo> infos;

    public FileDirectoryAdapter(Context ctx, ArrayList<FileInfo> infos) {
        inflater = LayoutInflater.from(ctx);
        this.infos = infos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.file_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        FileInfo info = infos.get(position);
        if(info.isFile()){
            holder.iv_icon.setImageResource(FileOpenUtils.getFileType(info.getPath()).icon);
        }else{
            holder.iv_icon.setImageResource(R.drawable.folder_yellow_full);
        }
        holder.tv_name.setText(info.getName());
        holder.tv_time.setText(Tools.getFormatTime(info.getTime(), "yyyy-MM-dd HH:mm"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Tools.isEmpty(listener)) {
                    listener.onItemClick(position);
                }
            }
        });
    }

   public interface ItemOnClickListener {
        public void onItemClick(int postion);
    }

    public void setItemOnClickListener(ItemOnClickListener listener) {
        this.listener = listener;
    }

    ItemOnClickListener listener;

    @Override
    public int getItemCount() {
        return infos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_time;
        View itemView;
        ImageView iv_icon;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            iv_icon= (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }
}
