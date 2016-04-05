package com.atobo.safecoo.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.atobo.safecoo.R;

/**
 * Created by ZL on 2016/3/30.
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
