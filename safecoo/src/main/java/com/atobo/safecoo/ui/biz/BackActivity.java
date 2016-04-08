package com.atobo.safecoo.ui.biz;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.atobo.safecoo.R;
import com.lidroid.xutils.ViewUtils;

import arg.mylibrary.autolayout.AutoFrameLayout;
import arg.mylibrary.ui.base.AbstractBaseActivity;
import arg.mylibrary.utils.PxUtil;

/**
 * Created by ws on 2016/3/30.
 * 带返回键的activity的基类返回键的宽度120px,需要子布局自己控制
 */
public abstract class BackActivity extends BaseActivity {
    private LinearLayout ll_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_back);
        initView();
    }

    private void initView() {
        ll_group = (LinearLayout) findViewById(R.id.ll_group);
        findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /**设置布局*/
    protected void setView(int view) {
        if(view<1){
            return;
        }
        View v = getLayoutInflater().inflate(view, null);
        v.setLayoutParams(new AutoFrameLayout.LayoutParams(PxUtil.getScreenWidth(self),PxUtil.getScreenHeight(self)));
        ll_group.removeAllViews();
        ll_group.addView(v);
        ViewUtils.inject(this);
    }
}
