package com.atobo.safecoo.entity;

import android.graphics.Bitmap;
import android.view.View;

import com.atobo.safecoo.utils.ImgLoadUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONObject;

import arg.mylibrary.utils.JSONUtils;
import arg.mylibrary.utils.LogTools;

/**
 * 作者: ws
 * 日期: 2016/4/20.
 * 介绍：游戏封装类
 */
public class GameEntity implements ImageLoadingListener {

    private String imgUrl;
    private Bitmap bitmap;
    private String gameUrl;

    public GameEntity() {
    }

    public GameEntity(JSONObject json) {
        setImgUrl(JSONUtils.getJStr(json,"thumbnail"));
        gameUrl=JSONUtils.getJStr(json,"url");
    }
    /**游戏连接*/
    public String getGameUrl() {
        return gameUrl;
    }
    /**游戏连接*
    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    /**
     * 图片
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * 图片
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * 图片地址
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 图片地址
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        if (bitmap == null) {
            ImageSize imageSize = new ImageSize(1490, 674);
            //显示图片的配置
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .cacheInMemory(false)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImgLoadUtils.getImageLoader().loadImage(imgUrl,imageSize,options ,this);
        }
    }

    public LoadingListener listener;

    public void setLoadingListener(LoadingListener listener) {
        this.listener = listener;
    }

    public interface LoadingListener {
        void LoadBack(LoacBackState state, Bitmap bm);
    }

    public enum LoacBackState {
        SUCCESS, FAILED;
    }

    @Override
    public void onLoadingStarted(String s, View view) {

    }

    @Override
    public void onLoadingFailed(String s, View view, FailReason failReason) {
        if (listener != null) {
            listener.LoadBack(LoacBackState.FAILED, bitmap);
        }
    }

    @Override
    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
        setBitmap(bitmap);
        if (listener != null) {
            listener.LoadBack(LoacBackState.SUCCESS, bitmap);
        }
    }

    @Override
    public void onLoadingCancelled(String s, View view) {

    }
}
