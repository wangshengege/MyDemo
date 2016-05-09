package com.atobo.safecoo.entity;

/**
 * 作者: ws
 * 日期: 2016/4/18.
 * 介绍：文件信息
 */
public class FileInfo {
    private String path;
    private long time;
    private String name;
    private boolean isFile;
    private String parent;
    /**父路径*/
    public String getParent() {
        return parent;
    }
    /**父路径*/
    public void setParent(String parent) {
        this.parent = parent;
    }

    /**是否是文件*/
    public boolean isFile() {
        return isFile;
    }
    /**是否是文件*/
    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }

    /**文件名字*/
    public String getName() {
        return name;
    }
    /**文件名字*/
    public void setName(String name) {
        this.name = name;
    }

    /**文件路径*/
    public String getPath() {
        return path;
    }
    /**文件路径*/
    public void setPath(String path) {
        this.path = path;
    }
    /**文件最后操作时间*/
    public long getTime() {
        return time;
    }
    /**文件最后操作时间*/
    public void setTime(long time) {
        this.time = time;
    }
}
