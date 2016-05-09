package com.atobo.safecoo.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.atobo.safecoo.R;
import com.atobo.safecoo.adapter.GameAdapter;
import com.atobo.safecoo.common.SafeCooConfig;
import com.atobo.safecoo.entity.GameEntity;
import com.atobo.safecoo.net.AppDao;
import com.atobo.safecoo.ui.biz.BackActivity;
import com.atobo.safecoo.utils.BrowserUtils;
import com.atobo.safecoo.view.coverflow.CoverFlowView;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import arg.mylibrary.net.NetworkUtil;
import arg.mylibrary.net.RequestCall;
import arg.mylibrary.utils.JSONUtils;
import arg.mylibrary.utils.LogTools;
import arg.mylibrary.utils.Tools;

/**
 * Created by ws on 2016/3/30.
 * 安全游戏
 */
public class GameActivity extends BackActivity implements View.OnClickListener, CoverFlowView.CoverFlowListener<GameAdapter> {
    private static final int[] GRES = {R.drawable.game1, R.drawable.game2,
            R.drawable.game3, R.drawable.game4,
            R.drawable.game5, R.drawable.game6,
            R.drawable.game7, R.drawable.game8,
            R.drawable.game9, R.drawable.game10,
            R.drawable.game11};

    @ViewInject(R.id.btn_ph)
    private Button btn_ph;
    @ViewInject(R.id.btn_rm)
    private Button btn_rm;
    @ViewInject(R.id.btn_fl)
    private Button btn_fl;
    @ViewInject(R.id.ll_back)
    private View ll_back;
    @ViewInject(R.id.flowview)
    private CoverFlowView<GameAdapter> flowView;
    private GameAdapter mAdapter;
    private ArrayList<GameEntity> items = new ArrayList<>();

    public static void startAction(Context ctx) {
        Intent intent = new Intent(ctx, GameActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_game);
        ll_back.setOnClickListener(this);
        initData();
        flowView.setCoverFlowListener(this);
    }

    private void initData() {
        if (SafeCooConfig.PREVIEW) {//预览版的数据
            for (int i = 0; i < GRES.length; i++) {
                GameEntity entity = new GameEntity();
                entity.setBitmap(BitmapFactory.decodeResource(getResources(), GRES[i]));
                items.add(entity);
            }
            mAdapter = new GameAdapter(self);
            mAdapter.setData(items);
            mAdapter.setCoverFlowView(flowView);
            flowView.setAdapter(mAdapter);
        } else {//正式版的数据
            AppDao.getGames(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ph://排行
                break;
            case R.id.btn_rm://热门
                break;
            case R.id.btn_fl://分类
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    public void imageOnTop(CoverFlowView<GameAdapter> coverFlowView, int position, float left, float top, float right, float bottom) {

    }

    @Override
    public void topImageClicked(CoverFlowView<GameAdapter> coverFlowView, int position) {
        if (!SafeCooConfig.PREVIEW) {
            GameEntity entity=items.get(position);
            if(!Tools.isEmpty(entity.getGameUrl())){//打开游戏
                BrowserUtils.startB(self,entity.getGameUrl());
            }
        } else {
            //   String uri =String.format( "http://%s:8080/youxi%d/story_html5.html", NetworkUtil.getIP(self),position+1);
            String uri = String.format("http://%s:8080/youxi%d/story_html5.html", "localhost", position + 1);
            LogTools.logi(self, uri);
            BrowserUtils.startB(self, uri);
        }
    }

    @Override
    public void invalidationCompleted() {

    }

    @Override
    public void onResponseSuccess(RequestCall call) {
        super.onResponseSuccess(call);
        if (call.getState() == 200) {
            JSONArray jsonArray = JSONUtils.getJJsonArr(call.getJson(), "games");
            getData(jsonArray);
        } else {
            Tools.showToast(self, call.getMsg());
        }
    }

    //解析数据
    private void getData(JSONArray jsonArray) {
        int num = jsonArray.length() < 3 ? 3 : jsonArray.length();
        try {
            for (int i = 0; i < num; i++) {
                if (i < jsonArray.length()) {
                    items.add(new GameEntity(jsonArray.getJSONObject(i)));
                } else {
                    GameEntity entity = new GameEntity();
                    entity.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.geme_load));
                    items.add(entity);
                }

            }
            mAdapter = new GameAdapter(self);
            mAdapter.setData(items);
            mAdapter.setCoverFlowView(flowView);
            flowView.setAdapter(mAdapter);
            flowView.invalidate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
