package com.atobo.safecoo.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.atobo.safecoo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.net.URL;

import arg.mylibrary.ui.base.AbstractBaseActivity;
import arg.mylibrary.utils.Tools;

/**
 * Created by ZL on 2016/3/29.
 */
public class TextHtmlActivity extends AbstractBaseActivity {
    @ViewInject(R.id.tv_html)
    private TextView  tv_html;
    public static void startAction(Context ctx,String str){
        Intent intent=new Intent(ctx,TextHtmlActivity.class);
        intent.putExtra("html",str);
        ctx.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewId(R.layout.activity_texthtml);
        Intent intent=getIntent();
        String html=intent.getStringExtra("html");
        if(Tools.isEmpty(html)){
            Tools.showToast(self,"数据出错");
            finish();
        }
        tv_html.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
        tv_html.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
        tv_html.setText(Html.fromHtml(html, imgGetter, null));
    }
    //这里面的resource就是fromhtml函数的第一个参数里面的含有的url
    Html.ImageGetter imgGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {
            Log.i("RG", "source---?>>>" + source);
            Drawable drawable = null;
            URL url;
            try {
                url = new URL(source);
                Log.i("RG", "url---?>>>" + url);
                drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            Log.i("RG", "url---?>>>" + url);
            return drawable;
        }
    };
}
