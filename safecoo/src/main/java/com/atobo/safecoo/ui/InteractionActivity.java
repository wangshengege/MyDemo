package com.atobo.safecoo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.atobo.safecoo.R;
import com.atobo.safecoo.ui.biz.BackActivity;

/**
 * Created by ws on 2016/3/30.
 * 直播互动
 */
public class InteractionActivity extends BackActivity {
    public static void startAction(Context ctx){
        Intent intent=new Intent(ctx,InteractionActivity.class);
        ctx.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_interaction);
    }
}
