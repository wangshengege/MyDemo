package com.atobo.safecoo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.Utils.BrowserUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import arg.mylibrary.ui.base.AbstractBaseActivity;

/**
 * Created by ZL on 2016/3/30.
 * 安全游戏
 */
public class GameActivity extends AbstractBaseActivity implements View.OnClickListener{
    @ViewInject(R.id.gallery)
    private ImageView gallery;
    @ViewInject(R.id.btn_ph)
    private Button btn_ph;
    @ViewInject(R.id.btn_rm)
    private Button btn_rm;
    @ViewInject(R.id.btn_fl)
    private Button btn_fl;
    @ViewInject(R.id.ll_back)
    private View ll_back;
    public static void startAction(Context ctx){
        Intent intent=new Intent(ctx,GameActivity.class);
        ctx.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_game);
        /*gallery.setAdapter(new GameAdapter(self));
        gallery.setSelection(2);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/
        gallery.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ph://排行
            break;
            case R.id.btn_rm://热门
                break;
            case R.id.btn_fl://分类
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.gallery://游戏
                BrowserUtils.startBrower(self, "http://localhost:8080/youxi/story_html5.html");
                break;
        }
    }
}
