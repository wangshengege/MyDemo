/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.atobo.safecoo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atobo.safecoo.common.SafeCooConfig;
import com.atobo.safecoo.ui.GameActivity;
import com.atobo.safecoo.ui.InteractionActivity;
import com.atobo.safecoo.ui.LocalFileActivity;
import com.atobo.safecoo.ui.PlayHomeActivity;
import com.atobo.safecoo.ui.SafeCooActivity;
import com.atobo.safecoo.ui.VideoListActivity;
import com.atobo.safecoo.ui.VideoPlayActivity;
import com.atobo.safecoo.ui.biz.BaseActivity;

import arg.mylibrary.common.SystemBarTintManager;
import arg.mylibrary.utils.Tools;

/**
 * ws
 * 主界面
 */
public class MainActivity extends BaseActivity {
    private static final int[][] BTNS_RES = {
            {R.id.ll_menu1, R.string.menu_menu1, R.drawable.ic_menu01},
            {R.id.ll_menu2, R.string.menu_menu2, R.drawable.ic_menu02},
            {R.id.ll_menu3, R.string.menu_menu3, R.drawable.ic_menu03},
            {R.id.ll_menu4, R.string.menu_menu4, R.drawable.ic_menu04},
            {R.id.ll_menu6, R.string.menu_menu5, R.drawable.ic_menu06},
            {R.id.ll_menu5, R.string.menu_menu6, R.drawable.ic_menu05},
    };

    public static void startAction(Context ctx) {
        Intent intent = new Intent(ctx, MainActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_main);
        showImmerseStatusBar(R.color.transparent);
        init();
    }

    private void init() {
        for (int i = 0; i < BTNS_RES.length; i++) {
            View v = findViewById(BTNS_RES[i][0]);
            ImageView iv = (ImageView) v.findViewById(R.id.iv_title);
            iv.setImageResource(BTNS_RES[i][2]);
            TextView tv = (TextView) v.findViewById(R.id.tv_title);
            tv.setText(BTNS_RES[i][1]);
            v.setTag(i);
            v.setOnClickListener(listener);
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();
            switch (index) {
                case 0://安酷大学
                    //BrowserUtils.checkApp(self,"");
                    SafeCooActivity.startAction(self);
                    // IjkVideoActicity.intentTo(self, IjkVideoActicity.PlayMode.landScape, IjkVideoActicity.PlayType.vid, videoId, false);
                    break;
                case 1://安酷tv
                    if(SafeCooConfig.PREVIEW && PlayHomeActivity.isDebug)
                        VideoListActivity.startAction(self,1);
                    else{
                   PlayHomeActivity.startAction(self, 0);}
                  //  PlayHomeActivity.startAction(self, 0);
                    break;
                case 2://直播互动
                    InteractionActivity.startAction(self);
                    //VideoPlayerActivity.startAction(self,"http://dlqncdn.miaopai.com/stream/MVaux41A4lkuWloBbGUGaQ__.mp4",2);
                    break;
                case 3://安全游戏
                    GameActivity.startAction(self);
                    break;
                case 4://本地文件
                    // PlayHomeActivity.startAction(self,1);
                   /* if(SafeCooConfig.PREVIEW){
                        PlayHomeActivity.startAction(self,1);
                    }else*/
                    LocalFileActivity.startAction(self);
                    break;
                case 5://精品推荐
                    PlayHomeActivity.startAction(self, 2);
                    break;
            }
        }
    };
    long time;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if (Tools.getTimeStamp()- time< 1000) {
                JXApplication.getInstance().exit();
                return true;
            } else {
                Tools.showToast(self, getResources().getString(R.string.press_again_exit));
                time = Tools.getTimeStamp();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
