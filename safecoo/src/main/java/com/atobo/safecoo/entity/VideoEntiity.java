package com.atobo.safecoo.entity;

/**
 * Created by ZL on 2016/3/30.
 */
public class VideoEntiity {
    private String path;
    private int viodeoIcon;
    private String title;
    /**视频地址*/
    public String getPath() {
        return path;
    }
    /**视频地址*/
    public void setPath(String path) {
        this.path = path;
    }
    /**视频图标*/
    public int getViodeoIcon() {
        return viodeoIcon;
    }
    /**视频图标*/
    public void setViodeoIcon(int viodeoIcon) {
        this.viodeoIcon = viodeoIcon;
    }
    /**标题*/
    public String getTitle() {
        return title;
    }
    /**标题*/
    public void setTitle(String title) {
        this.title = title;
    }
}
