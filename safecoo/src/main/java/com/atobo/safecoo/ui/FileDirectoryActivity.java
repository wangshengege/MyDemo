package com.atobo.safecoo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.adapter.FileDirectoryAdapter;
import com.atobo.safecoo.entity.FileInfo;
import com.atobo.safecoo.ui.biz.BackActivity;
import com.atobo.safecoo.utils.FileOpenUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import arg.mylibrary.common.FileAccessor;
import arg.mylibrary.utils.Tools;

/**
 * 作者: ws
 * 日期: 2016/4/18.
 * 介绍：
 */
public class FileDirectoryActivity extends BackActivity {
    @ViewInject(R.id.recycler)
    private RecyclerView recycler;
    @ViewInject(R.id.tv_root)
    private TextView tv_root;
    private ArrayList<FileInfo> infos=new ArrayList<>();
    @ViewInject(R.id.ll_back)
    private View ll_back;
    //上一级目录
    private String rootDir;
    private FileDirectoryAdapter mAdapter;
    private List<Integer> FILE_TYPE;
    //根文件目录
    private static final String ROOT_DIR=FileAccessor.getExternalStore().getAbsolutePath();

    public static void startAction(Context ctx){
        Tools.toActivity(ctx, FileDirectoryActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_filedirectory);
        recycler.setLayoutManager(new LinearLayoutManager(self));
        mAdapter=new FileDirectoryAdapter(self,infos);
        mAdapter.setItemOnClickListener(new FileDirectoryAdapter.ItemOnClickListener() {
            @Override
            public void onItemClick(int postion) {
                FileInfo info=infos.get(postion);
                if(info.isFile()) {
                    FileOpenUtils.openFile(self,info.getPath());
                }else{
                    getInfos(info.getPath());
                }
            }
        });
        getInfos(FileAccessor.getExternalStorePath());
        recycler.setAdapter(mAdapter);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfos(new File(rootDir).getParent());
            }
        });
    }
    private void getInfos(String filePath){
        infos.clear();
        rootDir=filePath;
        File f = new File(filePath);
        tv_root.setText(f.getAbsolutePath());
        File[] files = f.listFiles();
        boolean root = filePath.equals(ROOT_DIR);
        if (!root) {//根目录不给返回上一级
            ll_back.setVisibility(View.VISIBLE);
        } else {
            ll_back.setVisibility(View.GONE);
        }
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                boolean isEnable=false;
                if(file.isFile()){//文件属于可读的几类
                    int fileCode=FileOpenUtils.getFileType(file.getAbsolutePath()).code;
                    if(fileCode>=0 && fileCode<13){
                        isEnable=true;
                    }
                }else{
                  File[] fis= file.listFiles();
                    if(fis.length>0){//文件夹不是空的
                        isEnable=true;
                    }
                }
                if(isEnable){//满足条件才添加
                    FileInfo info=new FileInfo();
                    info.setName(file.getName());
                    info.setPath(file.getAbsolutePath());
                    info.setTime(file.lastModified());
                    info.setIsFile(file.isFile());
                    infos.add(info);}
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
