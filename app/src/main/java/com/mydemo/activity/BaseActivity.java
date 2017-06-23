package com.mydemo.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mydemo.R;
import com.mydemo.TApplication;
import com.mydemo.utils.L;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;


public class BaseActivity extends Activity implements OnClickListener, AdapterView
        .OnItemClickListener {
    // 整个Bar,title布局
    private AutoRelativeLayout rl_root, rl_title;
    // title上面的状态栏高度的bar
    private TextView tv_bar;
    // 左侧text 标题 右侧text
    private TextView tv_left, tv_mid, tv_right;
    // 左侧布局 右侧布局
    private AutoLinearLayout ll_left, ll_right;
    // 右侧img
    private ImageView img_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.title);
        try {
            addActivity();
            findView();
            initView();
            addListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findView() {
        rl_root = (AutoRelativeLayout) findViewById(R.id.rl_root);
        rl_title = (AutoRelativeLayout) findViewById(R.id.rl_title);
        tv_bar = (TextView) findViewById(R.id.tv_bar);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_mid = (TextView) findViewById(R.id.tv_mid);
        tv_right = (TextView) findViewById(R.id.tv_right);
        ll_left = (AutoLinearLayout) findViewById(R.id.ll_left);
        ll_right = (AutoLinearLayout) findViewById(R.id.ll_right);
        img_right = (ImageView) findViewById(R.id.img_right);
    }

    /**
     * 透明状态栏
     */
    private void initView() {
        tv_left.setVisibility(View.GONE);
        tv_right.setVisibility(View.GONE);
        img_right.setVisibility(View.GONE);
        L.e("SDK_INT = " + Build.VERSION.SDK_INT);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(0x80000000);
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        |View.SYSTEM_UI_LAYOUT_FLAGS|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            getWindow().getDecorView().setFitsSystemWindows(true);
            showRoot(true);
        } else {
            tv_bar.setVisibility(View.GONE);
            rl_title.setVisibility(View.VISIBLE);
        }
    }

    private void addListener() {
    }
    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void setContentView(int layoutResID) {
        // 子类的activity的布局view
        L.e("setContentView");
        View view = getLayoutInflater().inflate(layoutResID, null);
        AutoRelativeLayout.LayoutParams lp = new AutoRelativeLayout.LayoutParams(AutoRelativeLayout.LayoutParams.MATCH_PARENT, AutoRelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.BELOW, R.id.rl_title);
        if (rl_root != null)
            rl_root.addView(view, lp);
    }

    /**
     * 隐藏整个title和状态bar
     */
    public void showRoot(boolean isShow) {
        if (isShow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                tv_bar.setVisibility(View.VISIBLE);
                rl_title.setVisibility(View.VISIBLE);
            } else {
                tv_bar.setVisibility(View.GONE);
                rl_title.setVisibility(View.VISIBLE);
            }
        } else {
            tv_bar.setVisibility(View.GONE);
            rl_title.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏标题栏，显示状态bar并且设置状态栏颜色
     * * @param color
     */
    public void hintTitleSetStateColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            tv_bar.setVisibility(View.VISIBLE);
            rl_title.setVisibility(View.GONE);

            tv_bar.setBackgroundColor(color);
        } else {
            tv_bar.setVisibility(View.GONE);
            rl_title.setVisibility(View.GONE);

            tv_bar.setBackgroundColor(color);
        }
    }

    /**
     * 是否显示左侧的Text
     *
     * @param isShow
     */
    public void showLeftText(boolean isShow) {
        if (isShow) {
            tv_left.setVisibility(View.VISIBLE);
        } else {
            tv_left.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示右侧的Text
     *
     * @param isShow
     */
    public void showRightText(boolean isShow) {
        if (isShow) {
            tv_right.setVisibility(View.VISIBLE);
        } else {
            tv_right.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示右侧的ImageView
     *
     * @param isShow
     */
    public void showRightImg(boolean isShow) {
        if (isShow) {
            img_right.setVisibility(View.VISIBLE);
        } else {
            img_right.setVisibility(View.GONE);
        }
    }

    /**
     * 设置左侧text文本
     *
     * @param text
     */
    public void setLeftText(String text) {
        tv_left.setText(text);
    }

    /**
     * 设置右侧text文本
     *
     * @param text
     */
    public void setRightText(String text) {
        tv_right.setText(text);
    }

    /**
     * 设置标题text文本
     *
     * @param text
     */
    public void setTitle(String text) {
        tv_mid.setText(text);
    }

    /**
     * 设置右侧img图片
     *
     * @param resId
     */
    public void setRightImg(int resId) {
        img_right.setImageResource(resId);
    }

    /**
     * 获取左侧布局以设置点击事件
     */
    public AutoLinearLayout getLeft() {
        return ll_left;
    }

    /**
     * 获取右侧布局已设置点击事件
     */
    public AutoLinearLayout getRight() {
        return ll_right;
    }


    public void addActivity() {
        try {
            TApplication.context.addActivity(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeActivity() {
        try {
            TApplication.context.removeActivity(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity();
    }
}
