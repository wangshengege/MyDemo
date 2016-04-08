package com.atobo.safecoo.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.atobo.safecoo.R;
import com.atobo.safecoo.ui.biz.BackActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

import arg.mylibrary.utils.Tools;

/**
 * Created by ws on 2016/3/29.
 * 演播室
 */
public class DemonstrationActivity extends BackActivity {
    @ViewInject(R.id.gv_videolist)
    private GridView gv_videolist;
    private static final int[][] TAB_RES={
            {R.id.ll_tab1,R.string.tab_item1},
            {R.id.ll_tab2,R.string.tab_item2},
            {R.id.ll_tab3,R.string.tab_item3}
    };
    //选择的tab
    private View selectTab;
    public static void startAction(Context ctx){
        Tools.toActivity(ctx,DemonstrationActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_demonstration);
        initTab();
    }

    private void initTab() {
        for (int i = 0; i < TAB_RES.length; i++) {
            View v=findViewById(TAB_RES[i][0]);
            TextView tv_tab= (TextView) v.findViewById(R.id.tv_tab);
            tv_tab.setText(getResources().getText(TAB_RES[i][1]));
            v.setTag(i);
            v.setOnClickListener(listener);
            if(i==0){
                selectTab=v;
                selectTab.setSelected(true);
            }
        }
    }
    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v==selectTab){
                return;
            }
            selectTab.setSelected(false);
            selectTab=v;
            selectTab.setSelected(true);
            int index= (int) v.getTag();
            Tools.showToast(self,getResources().getString(TAB_RES[index][1]));
            switch (index){
                case 0:

                    break;
                case 1:

                    break;
                case 2:

                    break;
            }

        }
    };
}
