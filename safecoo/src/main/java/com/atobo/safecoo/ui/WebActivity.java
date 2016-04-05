package com.atobo.safecoo.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.widget.Toast;

import com.atobo.safecoo.R;
import com.baidu.mobstat.StatService;
import com.lidroid.xutils.view.annotation.ViewInject;

import arg.mylibrary.ui.base.AbstractBaseActivity;
import arg.mylibrary.utils.Tools;
import arg.mylibrary.view.ProgressWebView;

/**
 * Created by ws on 2015/11/5.
 */
public class WebActivity extends AbstractBaseActivity {
    @ViewInject(R.id.webWiew)
    private ProgressWebView webView;
    private String url;
    @ViewInject(R.id.ll_back)
    private View ll_back;
    /***/
    public static void startAction(Context ctx,String url){
        Intent intent=new Intent(ctx,WebActivity.class);
        intent.putExtra("url",url);
        ctx.startActivity(intent);
    }
    /**初始化*/
    @SuppressLint("JavascriptInterface")
    private void init(){
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        // 设置不缓存：
       // webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // 阻止图片
       // webSettings.setBlockNetworkImage(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setCacheMode(webSettings.LOAD_DEFAULT);
        // 相关配置：
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webView.setSaveEnabled(false);
        // 允许使用javascript
       // webView.addJavascriptInterface(getHtmlObject(), "jsObj");
        webView.loadUrl(url);
        StatService.bindJSInterface(this, webView);
    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setViewId(R.layout.activit_web);
        Intent intent=getIntent();
        url=intent.getStringExtra("url");
        if(Tools.isEmpty(url)){
            url="http://www.safecoo.com/";
            //url="file:///android_asset/test.html";
        }
        init();
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private Object getHtmlObject(){
        Object insertObj = new Object(){
            @JavascriptInterface
            public String HtmlcallJava(){
                Toast.makeText(WebActivity.this, "Html call Java", Toast.LENGTH_SHORT).show();
                return "Html call Java";
            }
            @JavascriptInterface
            public String HtmlcallJava2(final String param){
                Toast.makeText(WebActivity.this, "Html call Java", Toast.LENGTH_SHORT).show();
                return "Html call Java : " + param;
            }

            public void JavacallHtml(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript: showFromHtml()");
                        Toast.makeText(WebActivity.this, "clickBtn", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            public void JavacallHtml2(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript: showFromHtml2('IT-homer blog')");
                        Toast.makeText(WebActivity.this, "clickBtn2", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        return insertObj;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
        if(webView.canGoBack()){
            webView.goBack();
            return true;
        }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
        webView=null;
    }
}
