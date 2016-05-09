package com.atobo.safecoo.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.common.SafeCooConfig;
import com.atobo.safecoo.net.IPConfig;
import com.atobo.safecoo.ui.biz.BackActivity;
import com.atobo.safecoo.utils.BrowserUtils;

import arg.mylibrary.common.FileAccessor;
import arg.mylibrary.net.IpConfig;
import arg.mylibrary.net.NetworkUtil;
import arg.mylibrary.utils.LogTools;
import arg.mylibrary.utils.Tools;

/**
 * Created by ws on 2016/3/29.
 * 安酷大学
 */
public class SafeCooActivity extends BackActivity implements View.OnClickListener {
    private int[] res = {R.id.iv_dx1, R.id.iv_dx2, R.id.iv_dx3, R.id.iv_dx4, R.id.iv_dx5, R.id.iv_dx6, R.id.iv_dx7, R.id.iv_dx8, R.id.iv_dx9};

    public static void startAction(Context ctx) {
        Tools.toActivity(ctx, SafeCooActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_safecoo);
        for (int i = 0; i < res.length; i++) {
            View v = findViewById(res[i]);
            v.setTag(i);
            v.setOnClickListener(this);
            if (i == 8 && !SafeCooConfig.PREVIEW) {
                ImageView iv = (ImageView) v;
                iv.setImageResource(R.drawable.ic_bdsp);
            }
        }


    }

    @Override
    public void onClick(View v) {
        int index = (int) v.getTag();
        if (SafeCooConfig.PREVIEW) {
            offline(index);
        } else {
            online(index);
        }

    }

    //在线版本的数据
    private void online(int index) {
        String url = "";
        if(index<8 && index>=0){
            url=String.format("%s/%s/anku0%d/story_html5.html", IPConfig.IP,IPConfig.WEB_FOLDER,index+1);
        }else if(index==8){
            url= IPConfig.SAFECOO;
        }
        BrowserUtils.startB(self, url);
    }

    //离线版本
    private void offline(int index) {
        if (index < 8) {
            // String path=String.format("file:///mnt/internal_sd/anku/anku0%d/story_html5.html",index+1);
            String path = String.format("http://%s:8080/anku0%d/story_html5.html", NetworkUtil.getIP(self), index + 1);
            /*String path=String.format("file://%s/anku/anku0%d/story_html5.html", FileAccessor.getExternalStorePath(),index+1);
            LogTools.logi(self, path);*/
            BrowserUtils.startB(self, path);
        } else {
            String path = "http://" + NetworkUtil.getIP(self) + ":8080/anku1/story_html5.html";
            // String path=String.format("file://%s/anku/anku1/story_html5.html", FileAccessor.getExternalStorePath());
            LogTools.logi(self, path);
            BrowserUtils.startB(self, path);
            //   Utils.startBrowser(self, "http://localhost:8080/anku1/story_html5.html");
        }
    }
}
