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

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.atobo.safecoo.ui.GameActivity;
import com.atobo.safecoo.ui.InteractionActivity;
import com.atobo.safecoo.ui.PlayHomeActivity;
import com.atobo.safecoo.ui.SafeActivity;
import com.atobo.safecoo.ui.SafeCooActivity;
import com.atobo.safecoo.ui.RecommendActivity;
import com.atobo.safecoo.ui.biz.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

import arg.mylibrary.biz.YSApplication;
import arg.mylibrary.utils.Tools;

/**
 * ws
 * 主界面
 * */
public class MainActivity extends BaseActivity {
    private static final int[][] BTNS_RES = {
            {R.id.ll_menu1, R.string.menu_menu1, R.drawable.ic_menu01},
            {R.id.ll_menu2, R.string.menu_menu2, R.drawable.ic_menu02},
            {R.id.ll_menu3, R.string.menu_menu3, R.drawable.ic_menu03},
            {R.id.ll_menu4, R.string.menu_menu4, R.drawable.ic_menu04},
            {R.id.ll_menu5, R.string.menu_menu5, R.drawable.ic_menu05},
            {R.id.ll_menu6, R.string.menu_menu6, R.drawable.ic_menu06},
    };
    @ViewInject(R.id.tv_title)
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_main);
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
                case 1://安酷演播室
                   PlayHomeActivity.startAction(self,0);
                    break;
                case 2://直播互动
                    InteractionActivity.startAction(self);
                    break;
                case 3://安全游戏
                    GameActivity.startAction(self);
                    break;
                case 4://安全视频
                   // SafeActivity.startAction(self);
                    PlayHomeActivity.startAction(self,1);
                    break;
                case 5://精品推荐
                    PlayHomeActivity.startAction(self,2);
                    //RecommendActivity.startAction(self);
                    break;
            }
        }
    };
    long time=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            YSApplication.getInstance().exit();
       /* if(Tools.getTimeStamp()-time<1000){
            YSApplication.getInstance().exit();
        }*/
        }
       /* int f = 0;
        if (focus == null) {
            f = 4;
        } else
            f = (int) focus.getTag();


        if (event.KEYCODE_DPAD_UP == keyCode) {  //如果按下的是上键
            if(f==3){
                btns[1].requestFocus();
            }
            if(f==4){
                btns[2].requestFocus();
            }
            return true;
        }

        if (event.KEYCODE_DPAD_DOWN == keyCode) {  //如果按下的是下键
                if(f==1){
                    btns[3].requestFocus();
                }
                if(f==2){
                    btns[4].requestFocus();
                }
            return true;
        }

        if (event.KEYCODE_DPAD_LEFT == keyCode) {  //如果按下的是左键
            if (f == 0) {
                btns[4].requestFocus();
            } else {
                btns[f - 1].requestFocus();
            }
            return true;
        }

        if (event.KEYCODE_DPAD_RIGHT == keyCode) {  //如果按下的是右键
            if (f == 4) {
                btns[0].requestFocus();
            } else {
                btns[f + 1].requestFocus();
            }
            return true;
        }
        if(event.KEYCODE_DPAD_CENTER == keyCode){
            listener.onClick( btns[f]);
            return true;
        }*/
        return super.onKeyDown(keyCode, event);
    }


}
