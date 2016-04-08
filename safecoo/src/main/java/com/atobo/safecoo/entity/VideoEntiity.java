package com.atobo.safecoo.entity;

import org.json.JSONObject;

import arg.mylibrary.biz.JSONEntity;
import arg.mylibrary.utils.JSONUtils;

/**
 * Created by ws on 2016/3/30.
 * 视频信息封装类
 */
public class VideoEntiity extends JSONEntity {
    private String path="";
    private String viodeoImg;
    private String title="";
    private int type;

    public VideoEntiity() {
    }
    public VideoEntiity(JSONObject json) {
        title= JSONUtils.getJStr(json,"title");
        type=JSONUtils.getJNum(json, "sid");
        path=JSONUtils.getJStr(json, "vid");
        viodeoImg=JSONUtils.getJStr(json,"thumbnail");
    }
    /**视频预览图*/
    public String getViodeoImg() {
        return viodeoImg;
    }
    /**视频预览图*/
    public void setViodeoImg(String viodeoImg) {
        this.viodeoImg = viodeoImg;
    }

    /**
     * 视频所属类型
     */
    public int getType() {
        return type;
    }

    /**
     * 视频所属类型
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 视频地址
     */
    public String getPath() {
        return path;
    }

    /**
     * 视频地址
     */
    public void setPath(String path) {
        this.path = path;
    }


    /**
     * 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
