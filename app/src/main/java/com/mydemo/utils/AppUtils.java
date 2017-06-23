package com.mydemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.mydemo.TApplication;
import com.tencent.mm.sdk.openapi.IWXAPI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//跟App相关的辅助类
public class AppUtils {

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本号信息]
     *
     * @param context
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 匹配是不是手机号
     *
     * @param mobiles 手机号
     * @return
     */
    public static boolean isPhoneNum(String mobiles) {
        Pattern p = Pattern
//				^(1)\d{10}$
                .compile("^[1][0-9]{10}$");
//				.compile("^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断sp是否有 用户的账号密码
     *
     * @return 有返回true 没有返回false
     */
    public static boolean isOnLine() {
        String uid = (String) SPUtils.get(TApplication.context, "uid", "");
        String pwd = (String) SPUtils.get(TApplication.context, "pwd", "");
        if (uid == null || uid.equals("") || pwd == null || pwd.equals("")) {
            // 没有
            return false;
        }
        return true;
    }

    /**
     * 客户端是否安装有微信
     *
     * @param api 微信接口
     * @return
     */
    public static boolean isInstallWx(Context context, IWXAPI api) {
        // LogOutput.d(TAG, "isWXAppInstalledAndSupported");
        boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled()
                && api.isWXAppSupportAPI();
        if (!sIsWXAppInstalledAndSupported) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断用户是否给权限，没有给的话索要权限
     *
     * @param context      跳转的activity
     * @param permission   权限数组
     * @param request_code 请求权限请求码
     * @return true 有权限，false 无权限
     */
    public static boolean getPermission(Activity context, String[] permission, int request_code) {
        for (int i = 0; i < permission.length; i++) {
            int permissionState = ContextCompat.checkSelfPermission(context, permission[i]);
            if (permissionState == PackageManager.PERMISSION_DENIED) {
                // 用户未授予权限，索要权限
                ActivityCompat.requestPermissions(context, permission, request_code);
                return false;
            }
        }
        return true;
    }
}
