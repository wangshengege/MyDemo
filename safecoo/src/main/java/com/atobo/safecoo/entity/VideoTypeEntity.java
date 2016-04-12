package com.atobo.safecoo.entity;

import org.json.JSONObject;

import arg.mylibrary.biz.JSONEntity;
import arg.mylibrary.utils.JSONUtils;
import arg.mylibrary.utils.Tools;

/**
 * 作者: ws
 * 日期: 2016/4/6.
 * 介绍：视频类型封装类
 */
public class VideoTypeEntity extends JSONEntity {
    private int type;
    private String title;
    private int pid;
    public VideoTypeEntity() {

    }

    public VideoTypeEntity(JSONObject json) {
        type= JSONUtils.getJNum(json,"id");
        title=JSONUtils.getJStr(json, "name");
        pid=JSONUtils.getJNum(json,"pid");
    }
    /**所属类型*/
    public int getPid() {
        return pid;
    }
    /**所属类型*/
    public void setPid(int pid) {
        this.pid = pid;
    }

    /**视频类型*/
    public int getType() {
        return type;
    }
    /**视频类型*/
    public void setType(int type) {
        this.type = type;
    }
    /**视频类型名称*/
    public String getTitle() {
        return title;
    }
    /**视频类型名称*/
    public void setTitle(String title) {
        this.title = title;
    }
}
