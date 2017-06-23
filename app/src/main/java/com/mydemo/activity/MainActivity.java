package com.mydemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mydemo.R;
import com.mydemo.TApplication;
import com.mydemo.utils.AppUtils;
import com.mydemo.utils.L;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.tv1)
    TextView tv1;
    @InjectView(R.id.tv2)
    TextView tv2;
    @InjectView(R.id.tv3)
    TextView tv3;
    @InjectView(R.id.bt_date)
    Button btDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        try {
            findView();
            initView();
            addListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findView() {
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
    }

    private void initView() {
        tv2.setTypeface(TApplication.context.font_bz);
        tv3.setTypeface(TApplication.context.font_xi);

        showLeftText(true);
        showRightText(true);
        setRightText("测试");
        showRightImg(true);
        setRightImg(R.mipmap.ic_launcher);
        setTitle(AppUtils.getAppName(this));
        getLeft().setOnClickListener(this);
        getRight().setOnClickListener(this);
    }

    private void addListener() {
    }


    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.ll_left:
                    finish();
                    break;
                case R.id.ll_right:
                    L.e("测试");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @OnClick({R.id.bt_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_date:
                // 日期选择器

                break;
        }
    }
}
