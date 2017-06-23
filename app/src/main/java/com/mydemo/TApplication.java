package com.mydemo;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;

import com.lzy.okhttputils.OkHttpUtils;
import com.mydemo.utils.L;

import java.util.ArrayList;

/**
 * Created by ShiShow_xk on 2017/3/28.
 */
public class TApplication extends Application {
    private ArrayList<Activity> activityList = new ArrayList<Activity>();
    public static TApplication context;
    // 是否是测试环境
    public static boolean isDebug = true;

    public Typeface font_bz, font_xi;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        // 初始化Okhttps
        OkHttpUtils.init(this);
        // 初始化字体
        font_bz = Typeface.createFromAsset(getAssets(), "fonts/bz.ttf");
        font_xi = Typeface.createFromAsset(getAssets(), "fonts/xi.ttf");
    }

    /**
     * 把所有的activity注册 到集合
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 移除某个adtivity
     */
    public void removeActivity(Activity ac) {
        L.e("size = " + activityList.size());
        activityList.remove(ac);
    }

    /**
     * 完全退出
     */
    public void exit() {
        try {
            for (Activity activity : activityList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
