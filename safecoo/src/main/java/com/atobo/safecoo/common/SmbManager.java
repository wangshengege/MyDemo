package com.atobo.safecoo.common;

import android.os.Looper;

import com.atobo.safecoo.entity.FileInfo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import arg.mylibrary.biz.YSException;
import arg.mylibrary.utils.LogTools;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

/**
 * 作者: ws
 * 日期: 2016/4/25.
 * 介绍：
 */
public class SmbManager {
    private String ip = "";//pc的Ip地址
    private String account = "";//账户
    private String prssword = "";//密码

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPrssword() {
        return prssword;
    }

    public void setPrssword(String prssword) {
        this.prssword = prssword;
    }

    /**
     * 获取文件夹列表
     *
     * @param remoteUrl 文件夹父目录
     */
    public void getFileDir(String remoteUrl, FileDirListener listener) {
        if (!remoteUrl.contains("smb")) {
            remoteUrl = String.format("%s/%s/", getSmbUrl(), remoteUrl);
        }
        new GetFileDirThread(remoteUrl, listener).start();
    }

    /**
     * 获取smburl
     */
    public String getSmbUrl() {
        return String.format("smb://%s:%s@%s/", account, prssword, ip);
    }

    /**
     * 下载共享文件
     *
     * @param remoteUrl 共享的文件
     * @param localDir  下载的文件夹
     */
    public void smbGet(String remoteUrl, String localDir, DownloadListener downloadListener) {
        if (!remoteUrl.contains("smb")) {
            remoteUrl = String.format("%s/%s/", getSmbUrl(), remoteUrl);
        }
        new DownThread(remoteUrl, localDir + "/", downloadListener).start();
    }

    public interface FileDirListener {
        void fileDir(List<FileInfo> dirs);

        void error(YSException e);
    }

    public interface DownloadListener {
        void download(int contentLength, int downedLength);

        void finsh(File file);

        void error(YSException e);
    }

    private class GetFileDirThread extends Thread {
        private String remoteUrl;
        private FileDirListener listener;
        private List<FileInfo> lists;

        public GetFileDirThread(String remoteUrl, FileDirListener listener) {
            this.remoteUrl = remoteUrl;
            this.listener = listener;
        }

        @Override
        public void run() {
            super.run();
            Looper.prepare();
            SmbFile remoteFile;
            try {
                remoteFile = new SmbFile(remoteUrl);
                LogTools.logi(SmbManager.this, remoteUrl);
                remoteFile.connect();
                SmbFile[] files = remoteFile.listFiles();
                if (listener != null) {
                    for (SmbFile f : files) {
                        if (lists == null) {
                            lists = new ArrayList<>();
                        }
                        FileInfo info = new FileInfo();
                        info.setName(f.getName());
                        info.setPath(f.getPath());
                        info.setIsFile(!f.getName().endsWith("/"));
                        info.setParent(f.getParent());
                        lists.add(info);
                    }
                    listener.fileDir(lists);
                }
            } catch (MalformedURLException e) {
                if (listener != null) {
                    listener.error(new YSException(e));
                }
                LogTools.logw(SmbManager.this, e.getMessage());
            } catch (SmbException e) {
                if (listener != null) {
                    listener.error(new YSException(e));
                }
                LogTools.logw(SmbManager.this, e.getMessage());
            } catch (Exception e) {
                if (listener != null) {
                    listener.error(new YSException(e));
                }
                LogTools.logw(SmbManager.this, e.getMessage());
            }
        }
    }

    private class DownThread extends Thread {
        private String remoteUrl;
        private String localDir;
        private DownloadListener downloadListener;
        private int downedLength;

        public DownThread(String remoteUrl, String localDir, DownloadListener downloadListener) {
            this.remoteUrl = remoteUrl;
            this.localDir = localDir;
            this.downloadListener = downloadListener;
        }

        @Override
        public void run() {
            super.run();
            Looper.prepare();
            InputStream in = null;
            OutputStream out = null;
            try {
                SmbFile remoteFile = new SmbFile(remoteUrl);
                LogTools.logi(SmbManager.this, remoteUrl);
                remoteFile.connect();
                if (remoteFile == null) {
                    if (downloadListener != null) {
                        downloadListener.error(new YSException(new Throwable("共享文件不存在")));
                    }
                    LogTools.logi(SmbManager.this, "共享文件不存在");
                    return;
                }
                String fileName = remoteFile.getName();
                File localFile = new File(localDir + File.separator + fileName);
                if (localFile.isFile() && localFile.length() == remoteFile.length()) {//文件已存在不用重新下载
                    if (downloadListener != null) {
                        downloadListener.finsh(localFile);
                    }
                } else {
                    if (!localFile.exists()) {
                        localFile.createNewFile();
                    }
                    in = new BufferedInputStream(new SmbFileInputStream(remoteFile));
                    out = new BufferedOutputStream(new FileOutputStream(localFile));
                    byte[] buffer = new byte[1024];
                    while (in.read(buffer) != -1 && downedLength != remoteFile.length() ) {
                        out.write(buffer);
                        downedLength += buffer.length;
                        if (downloadListener != null) {
                            downloadListener.download((int)remoteFile.length(), downedLength);
                            if (downedLength >= remoteFile.length()) {
                                downloadListener.finsh(localFile);
                            }
                        }
                        if(remoteFile.length()-downedLength<1024){
                            buffer = new  byte[(int)(remoteFile.length()-downedLength)];
                        }else
                        buffer = new byte[1024];
                    }
                }
            } catch (SmbException e) {
                if (downloadListener != null) {
                    downloadListener.error(new YSException(e));
                }
                LogTools.logw(SmbManager.this, e.getMessage());
            } catch (Exception e) {
                if (downloadListener != null) {
                    downloadListener.error(new YSException(e));
                }
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
