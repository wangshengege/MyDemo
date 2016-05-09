package com.atobo.safecoo.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.common.NetTool;
import com.atobo.safecoo.common.SmbManager;
import com.atobo.safecoo.entity.FileInfo;
import com.atobo.safecoo.entity.IpInfo;
import com.atobo.safecoo.ui.biz.BackActivity;
import com.atobo.safecoo.utils.FileOpenUtils;
import com.atobo.safecoo.utils.SafeCooUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import arg.mylibrary.biz.YSException;
import arg.mylibrary.common.FileAccessor;
import arg.mylibrary.utils.LogTools;
import arg.mylibrary.utils.Tools;

/**
 * 作者: ws
 * 日期: 2016/4/22.
 * 介绍：网上邻居
 */
public class SmbActivity extends BackActivity {
    private static final int FILEDIR = 3;
    private static final int IP = 4;
    private static final int ERROR = 5;
    private static final int END = 6;
    private static final int UPDATA=7;
    private static final int IPERROR = 8;
    @ViewInject(R.id.recycler)
    private RecyclerView recycler;
    @ViewInject(R.id.rv_ip)
    private RecyclerView rv_ip;
    private ArrayList<IpInfo> infos = new ArrayList<>();
    private NetTool netTool;
    private MyAdapter myAdapter;
    private ArrayList<FileInfo> fileInfos;
    private SmbManager smbManager;
    private FileAdapter fileAdapter;
    private ProgressDialog pd;
    private String rootSmb;
    private String upSmbUrl;
    private boolean ipIsShow;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FILEDIR://刷新文件夹列表
                    fileAdapter.notifyDataSetChanged();
                    if(pd!=null){
                        pd.dismiss();
                        pd=null;
                    }
                    break;
                case ERROR://提示出错信息
                    if(pd!=null){
                        pd.dismiss();
                        pd=null;
                    }
                    Tools.showToast(self, msg.obj.toString());
                    break;
                case IP://更新ip
                    myAdapter.notifyDataSetChanged();
                    break;
                case END://结束处理
                    if(pd!=null){
                        pd.dismiss();
                        pd=null;
                    }
                    break;
                case UPDATA:
                    Bundle bundle=msg.getData();
                    if (pd == null) {
                        pd = new ProgressDialog(self);
                        pd.setTitle("正在缓存");
                        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pd.setCanceledOnTouchOutside(false);
                        pd.setIndeterminate(false);
                        pd.setMax(bundle.getInt("contentLength"));
                        pd.show();
                    }
                    pd.setProgress(bundle.getInt("downedLength"));
                    if(bundle.getInt("contentLength")<=bundle.getInt("downedLength")){
                        pd.dismiss();
                    }
                    break;
                case IPERROR:
                    if(!ipIsShow) {
                        Tools.showToast(self, msg.obj.toString());
                    }
                    break;
            }
        }
    };

    public static void startAction(Context ctx) {
        Tools.toActivity(ctx, SmbActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_smb);
        recycler.setLayoutManager(new GridLayoutManager(self, 4));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(self);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_ip.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter();
        rv_ip.setAdapter(myAdapter);
        fileAdapter = new FileAdapter();
        recycler.setAdapter(fileAdapter);
        smbManager = new SmbManager();
    }

    @OnClick(R.id.btn_begen)
    private void begenSeachIp(View v) {
        infos.clear();
        pd=new ProgressDialog(self);
        pd.setIndeterminate(true);
        pd.setMessage("加载中。。。");
        pd.show();
        netTool = new NetTool(self);
        netTool.setGetIpInfoListener(ipInfoListener);
        netTool.scan();
        ipIsShow=false;
    }
    @OnClick(R.id.btn_add)
    private void add(View v){
        showAlert("");
    }
    private class MyAdapter extends RecyclerView.Adapter<SmbActivity.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.smb_item, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final IpInfo info = infos.get(position);
            holder.iv_icon.setImageResource(R.drawable.net_pc);
            holder.tv_ip.setText(info.getIpAddress());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//根据ip地址搜索文件夹
                    showAlert(info.getIpAddress());
                }
            });
        }

        @Override
        public int getItemCount() {
            return infos.size();
        }
    }

    //显示提示框
    private void showAlert(final String ip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(self);
        builder.setCancelable(false);
        builder.setTitle("设置连接信息");
        View view = getLayoutInflater().inflate(R.layout.setip_view, null);
        final EditText et_ip = (EditText) view.findViewById(R.id.et_ip);//ip地址编辑框
        et_ip.setText(ip);
        final EditText et_acc = (EditText) view.findViewById(R.id.et_account);
        final EditText et_psd = (EditText) view.findViewById(R.id.et_psd);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                smbManager.setIp(et_ip.getText().toString());
                smbManager.setAccount(et_acc.getText().toString());//设置登陆账户
                smbManager.setPrssword(et_psd.getText().toString());//设置登录密码
                rootSmb=smbManager.getSmbUrl();
                upSmbUrl=rootSmb;
                pd=new ProgressDialog(self);
                pd.setIndeterminate(true);
                pd.setMessage("加载中。。。");
                pd.show();
                smbManager.getFileDir("", fileDirListener);

            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    //获取文件列表的监听器
    SmbManager.FileDirListener fileDirListener = new SmbManager.FileDirListener() {
        @Override
        public void fileDir(List<FileInfo> dirs) {
            fileInfos = (ArrayList<FileInfo>) dirs;
            handler.sendEmptyMessage(FILEDIR);
        }

        @Override
        public void error(YSException e) {
            handler.sendMessage(SafeCooUtils.getMsg(ERROR, "获取文件列表失败"));
        }
    };
    //获取下载信息的监听
    SmbManager.DownloadListener downloadListener = new SmbManager.DownloadListener() {
        @Override
        public void download(int contentLength, int downedLength) {
            LogTools.logi(SmbActivity.this, "contentLength:"+contentLength+"-downedLength:"+downedLength);
            Message msg=new Message();
            msg.what=UPDATA;
            Bundle bundle=new Bundle();
            bundle.putInt("contentLength",contentLength/1024);
            bundle.putInt("downedLength",downedLength/1024);
            msg.setData(bundle);
            handler.sendMessage(msg);
        }

        @Override
        public void finsh(File file) {
            if(pd!=null){
                pd.dismiss();
                pd=null;
            }
            FileOpenUtils.openFile(self,file.getAbsolutePath());
        }

        @Override
        public void error(YSException e) {
            handler.sendMessage(SafeCooUtils.getMsg(ERROR, "获取文件失败"));
        }
    };
    //获取搜索ip地址的监听
    NetTool.GetIpInfoListener ipInfoListener = new NetTool.GetIpInfoListener() {
        @Override
        public void GetIpInfo(IpInfo info) {
            infos.add(info);
            handler.sendEmptyMessage(IP);
        }

        @Override
        public void GetError(YSException e) {
            handler.sendMessage(SafeCooUtils.getMsg(IPERROR, "获取局域网ip失败"));
            ipIsShow=true;
        }

        @Override
        public void GetFinsh() {
            handler.sendEmptyMessage(END);
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ip;
        View view;
        ImageView iv_icon;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tv_ip = (TextView) itemView.findViewById(R.id.tv_ip);
            iv_icon= (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }

    private class FileAdapter extends RecyclerView.Adapter<SmbActivity.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.smb_item, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final FileInfo info = fileInfos.get(position);
            if(info.isFile()){
                holder.iv_icon.setImageResource(FileOpenUtils.getFileType(info.getPath()).icon);
            }else{
                holder.iv_icon.setImageResource(R.drawable.format_folder);
            }
            holder.tv_ip.setText(info.getName());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upSmbUrl=info.getParent();
                    if (!info.isFile()) {
                        smbManager.getFileDir(info.getPath(), fileDirListener);
                    } else
                        smbManager.smbGet(info.getPath(), FileAccessor.getFilePathName().getAbsolutePath(), downloadListener);
                }
            });
        }

        @Override
        public int getItemCount() {
            return fileInfos == null ? 0 : fileInfos.size();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            LogTools.logi(self,"up:"+upSmbUrl+"root:"+rootSmb);
            if(upSmbUrl!=null &&   upSmbUrl.length()>=rootSmb.length()){
                smbManager.getFileDir(upSmbUrl, fileDirListener);
                upSmbUrl=upSmbUrl.substring(0, upSmbUrl.length()-1);
                upSmbUrl=upSmbUrl.substring(0,upSmbUrl.lastIndexOf("/")+1);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
