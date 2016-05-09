package com.atobo.safecoo.common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.atobo.safecoo.JXApplication;
import com.atobo.safecoo.entity.IpInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashSet;

import arg.mylibrary.biz.YSException;
import arg.mylibrary.net.NetworkUtil;
import arg.mylibrary.utils.LogTools;

/**
 * 作者: ws
 * 日期: 2016/4/22.
 * 介绍：
 */
public class NetTool {
    private Runtime run = Runtime.getRuntime();//获取当前运行环境，来执行ping，相当于windows的cmd
    private String locAddress;//存储本机ip，例：本地ip ：192.168.1.
    private Process proc = null;
    private String ping = "ping -c 1 -w 0.5 ";//其中 -c 1为发送的次数，-w 表示发送后等待响应的时间
    private int j;//存放ip最后一位地址 0-255
    private Context ctx;//上下文

    public NetTool(Context ctx) {
        this.ctx = ctx;
    }

    private HashSet<String> ipSet;
    private int tNum;//线程数
    private static final int GETINFO = 0x01;
    private static final int GETERROR = 0x02;
    private static final int GETEND = 0x03;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETINFO:
                    if (msg.obj instanceof IpInfo && getIpInfoListener != null) {
                        getIpInfoListener.GetIpInfo((IpInfo) msg.obj);
                    }
                    break;
                case GETEND:
                    if (getIpInfoListener != null) {
                        getIpInfoListener.GetFinsh();
                    }
                    break;
                case GETERROR:
                    if (getIpInfoListener != null) {
                        getIpInfoListener.GetError((YSException) msg.obj);
                    }
                    break;
            }
        }
    };
    private GetIpInfoListener getIpInfoListener;

    public GetIpInfoListener getGetIpInfoListener() {
        return getIpInfoListener;
    }

    public void setGetIpInfoListener(GetIpInfoListener getIpInfoListener) {
        this.getIpInfoListener = getIpInfoListener;
    }

    public interface GetIpInfoListener {
        void GetIpInfo(IpInfo info);

        void GetError(YSException e);

        void GetFinsh();
    }

    /**
     * 扫描局域网内ip，找到对应服务器
     */
    public synchronized void scan() {
        locAddress = getLocAddrIndex();//获取本地ip前缀
        if (locAddress.equals("")) {
            Toast.makeText(ctx, "扫描失败，请检查wifi网络", Toast.LENGTH_LONG).show();
            return;
        }
        for (int i = 0; i < 256; i++) {//创建256个线程分别去ping
            j = i;
            if (tNum > 35) {
                try {
                    wait(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            new Thread(new Runnable() {
                public void run() {
                    tNum++;
                    String p = NetTool.this.ping + locAddress + NetTool.this.j;
                    String current_ip = locAddress + NetTool.this.j;
                    try {
                        proc = run.exec(p);
                        int result = proc.waitFor();
                        if (result == 0) {
                            if (ipSet == null) {
                                ipSet = new HashSet<String>();
                            }
                            if (!ipSet.contains(current_ip)) {
                                ipSet.add(current_ip);
                                IpInfo info = new IpInfo();
                                info.setIpAddress(current_ip);
                                InetAddress add = InetAddress.getByName(current_ip);
                                String hostname = add.getHostName();
                                if (!hostname.equals(current_ip)) {
                                    info.setIpName(hostname);
                                }
                                LogTools.logd(NetTool.this, current_ip);
                                handler.sendMessage(getMsg(GETINFO, info));
                                tNum--;
                            }
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        tNum--;
                        handler.sendMessage(getMsg(GETERROR, new YSException(e1)));
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                        tNum--;
                        handler.sendMessage(getMsg(GETERROR, new YSException(e2)));
                    } finally {
                        proc.destroy();
                    }
                }
            }).start();
        }
        handler.sendMessage(getMsg(GETEND, ""));
    }

    private Message getMsg(int what, Object ob) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = ob;
        return msg;
    }

    //获取IP前缀
    public String getLocAddrIndex() {
        String str = NetworkUtil.getIP(ctx);
        if (!str.equals("")) {
            return str.substring(0, str.lastIndexOf(".") + 1);
        }
        return null;
    }

}
