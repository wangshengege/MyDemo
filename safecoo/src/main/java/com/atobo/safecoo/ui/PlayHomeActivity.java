package com.atobo.safecoo.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelXorXfermode;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.adapter.PalyAdapter;
import com.atobo.safecoo.common.SafeCooConfig;
import com.atobo.safecoo.entity.VideoEntiity;
import com.atobo.safecoo.entity.VideoTypeEntity;
import com.atobo.safecoo.net.AppDao;
import com.atobo.safecoo.ui.biz.BackActivity;
import com.atobo.safecoo.ui.video.IjkVideoActicity;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import arg.mylibrary.autolayout.AutoLinearLayout;
import arg.mylibrary.common.CommonFunction;
import arg.mylibrary.common.FileAccessor;
import arg.mylibrary.net.RequestCall;
import arg.mylibrary.utils.JSONUtils;
import arg.mylibrary.utils.LogTools;
import arg.mylibrary.utils.PxUtil;
import arg.mylibrary.utils.Tools;

/**
 * Created by ws on 2016/3/30.
 * 安酷演播室
 * 显示类型，0是播放室，1是安全视频，2是精品推荐
 */
public class PlayHomeActivity extends BackActivity {
    //标题资源
    private static final int[][] TITLE_RES = {
            {R.string.menu_menu2, R.drawable.ic_ybs, R.drawable.ic_ybs_tb},
            {R.string.menu_menu5, R.drawable.ic_safe, R.drawable.ic_safe_tab},
            {R.string.menu_menu6, R.drawable.ic_tj, R.drawable.ic_jp_tb}
    };
    @ViewInject(R.id.gv_videolist)
    private GridView gv_videolist;
    @ViewInject(R.id.iv_title)
    private ImageView iv_title;//图标
    @ViewInject(R.id.tv_title)
    private TextView tv_title;//标题
    @ViewInject(R.id.ll_tab_group)
    private LinearLayout ll_tab_group;//视频类型的导航栏
    private PalyAdapter mAdapter;
    //数据源
    private ArrayList<VideoEntiity> items = new ArrayList<>();
    /**
     * 显示类型，0是播放室，1是安全视频，2是精品推荐
     */
    private int type;
    //选择的视频类型
    private TextView selectTab;
    private ProgressDialog pd;
    private ArrayList<VideoTypeEntity> videoType;
    private int[][] res = {{R.drawable.ic_ybs1, R.drawable.ic_ybs2,
            R.drawable.ic_ybs3, R.drawable.ic_ybs4,
            R.drawable.ic_ybs5, R.drawable.ic_ybs6,
            R.drawable.ic_ybs7, R.drawable.ic_ybs8,
            R.drawable.ic_ybs9, R.drawable.ic_ybs10,
            R.drawable.ic_ybs11, R.drawable.ic_ybs12},
            {R.drawable.ic_aq1, R.drawable.ic_aq2,
                    R.drawable.ic_aq3, R.drawable.ic_aq4,
                    R.drawable.ic_aq5, R.drawable.ic_aq6,
                    R.drawable.ic_aq7, R.drawable.ic_aq8},
            {R.drawable.ic_jp1, R.drawable.ic_jp2,
                    R.drawable.ic_jp3, R.drawable.ic_jp4,
                    R.drawable.ic_jp5, R.drawable.ic_jp6,
                    R.drawable.ic_jp7, R.drawable.ic_jp8,
                    R.drawable.ic_jp9, R.drawable.ic_jp10,
                    R.drawable.ic_jp11, R.drawable.ic_jp12,}};

    /**
     * @param ctx
     * @param type 显示类型，0是播放室，1是安全视频，2是精品推荐
     */
    public static void startAction(Context ctx, int type) {
        if (type > 2 || type < 0) {
            return;
        }
        Intent intent = new Intent(ctx, PlayHomeActivity.class);
        intent.putExtra("type", type);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_playhome);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);

        if (!SafeCooConfig.PREVIEW) {
           getData(-1);
        } else {
            ImageView iv = new ImageView(self);
            iv.setLayoutParams(new AutoLinearLayout.LayoutParams(AutoLinearLayout.LayoutParams.WRAP_CONTENT, AutoLinearLayout.LayoutParams.WRAP_CONTENT));
            iv.setImageResource(TITLE_RES[type][2]);
            ll_tab_group.addView(iv);
            for (int i = 0; i < res[type].length; i++) {
                VideoEntiity en = new VideoEntiity();
                en.setViodeoImg("drawable://" + res[type][i]);
                items.add(en);
            }
        }
        mAdapter = new PalyAdapter(self, items);
        initView();
    }

    //初始化界面
    private void initView() {
        iv_title.setImageResource(TITLE_RES[type][1]);
        tv_title.setText(TITLE_RES[type][0]);
        gv_videolist.setAdapter(mAdapter);
        gv_videolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!SafeCooConfig.PREVIEW) {
                    IjkVideoActicity.intentTo(self, IjkVideoActicity.PlayMode.defaults, IjkVideoActicity.PlayType.vid, items.get(position).getPath(), true);
                } else {
                    if (position < 0 || position > 2) {
                        return;
                    }
                    onClikOfline(position);
                }
            }
        });
    }

    //离线版本的点击
    private void onClikOfline(int position) {
        if (position > 2) {
            return;
        }
        if (!FileAccessor.isExistExternalStore()) {
            Tools.showToast(self, "SD卡不可用");
            return;
        }
        String path;
        if (type == 2) {
            path = String.format("%s/anku/xyj%d.mp4", FileAccessor.getExternalStorePath(), position + 1);
        } else {
            path = String.format("%s/anku/ak%d.mp4", FileAccessor.getExternalStorePath(), position + 1);
        }
       // VideoPlayerActivity.startAction(self, path);
        VideoPlayActivity.startAction(self, path);
    }

    @Override
    public void onResponseSuccess(RequestCall call) {
        super.onResponseSuccess(call);
        if(call.getState()==200){
        if (videoType == null) {
            praseTab(JSONUtils.getJJsonArr(call.getJson(), "vedioCate"));
        }
        JSONArray jsarr = JSONUtils.getJJsonArr(call.getJson(), "vedio");
        praseList(jsarr);}else{
            Tools.showToast(self,call.getMsg());
        }
    }

    //解析数据类型
    private void praseTab(JSONArray json) {
        videoType = new ArrayList<>();
        try {
            for (int i = 0; i < json.length(); i++) {
                videoType.add(new VideoTypeEntity(json.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setTab();
    }

    private void setTab() {
        for (int i = 0; i < videoType.size(); i++) {
            TextView tv = new TextView(self);
            tv.setText(videoType.get(i).getTitle());
            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            tv.setLayoutParams(params);
            if (i == 0) {
                tv = getSelText(tv);
                selectTab = tv;
            } else {
                tv = getNorText(tv);
            }
            tv.setTag(videoType.get(i).getType());
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == selectTab) {
                        return;
                    }
                    int type = (int) v.getTag();
                    getData(type);
                    getNorText(selectTab);
                    selectTab = getSelText((TextView) v);
                }
            });
            ll_tab_group.addView(tv);
        }
    }
    //视频小类型
    private void getData(int t){
        String pageType;
       // 1是安酷tv，2是精品推荐
        if(type==2){//精品推荐
            pageType="1";
        }else{
            pageType="0";
        }
        if(pd==null){
            pd=new ProgressDialog(self);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setIndeterminate(true);
            pd.setMessage("正在加载。。。");
            pd.setCanceledOnTouchOutside(false);
        }
        if (!pd.isShowing()){
            pd.show();
        }
        AppDao.getVedio(pageType, t == -1 ? "" : String.valueOf(t), self);
    }
    //未选中的选项卡
    private TextView getNorText(TextView tv) {
        tv.setTextColor(getResources().getColor(R.color.gray));
        tv.setTextSize(getTextSize());
        tv.setBackgroundResource(0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tv.getLayoutParams();
        layoutParams.setMargins(PxUtil.dip2px(self, 10), 0, 0, 0);
        return tv;
    }
    //根据屏幕分辨率获取文字大小
    private int getTextSize(){
       /*int w= PxUtil.getScreenWidth(self);
        LogTools.logi(PlayHomeActivity.this,"w:"+w);
        if (w<=1290){//低清
            return w/100;
        }else if(w>1290 && w< 1930){//高清
            return w/150;
        }else {//超清
            return w/200;
        }*/
        int textSize=PxUtil.sp2px(self,6);
        LogTools.logi(self,"testSize:"+textSize);
        if (textSize<15){
            textSize=17;
        }else if(textSize > 25){
            textSize=15;
        }
        return textSize;
    }
    //选中的选项卡
    private TextView getSelText(TextView tv) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tv.getLayoutParams();
        layoutParams.setMargins(PxUtil.dip2px(self, 10), 0, 0, 0);
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setTextSize(getTextSize());
        tv.setBackgroundResource(R.drawable.bg_tab_text);
        return tv;
    }
    //解析数组
    private void praseList(JSONArray jsarr) {
        items.clear();
        if (Tools.isEmpty(jsarr) || jsarr.length() < 1) {
            Tools.showToast(self, "数据为空");
        } else {
            try {
                for (int i = 0; i < jsarr.length(); i++) {
                    items.add(new VideoEntiity(jsarr.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFinishRequest(RequestCall call) {
        super.onFinishRequest(call);
        if(pd!=null && pd.isShowing()){
            pd.dismiss();
        }
    }
}
