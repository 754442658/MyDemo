package com.mydemo.state;

import android.os.Environment;

import com.mydemo.R;
import com.mydemo.TApplication;
import com.mydemo.utils.AppUtils;
import com.mydemo.utils.T;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.io.File;

/**
 * Created by ShiShow_xk on 2017/3/28.
 */
public class Constant {

    // 应用缓存文件
    public static final File CACHE_FILE = new File(Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/" + AppUtils.getAppName(TApplication.context));

    //  QQAppID
    public static final String QQAppID = "";

    //  微信AppID
    public static final String WxAppID = "";
    // 微信Key
    public static final String KEY = "";
    // 微信AppSecret
    public static final String AppSecret = "";
    // 微信支付商户id
    public static final String MCH_ID = "";

    //  微信固定标识
    public static final String PACKAGE_VALUE = "Sign=WXPay";
    // 微信支付结果
    public static final int WX_PAY_SUCCESS = 1;
    public static final int WX_PAY_CANCLE = 0;
    public static final int WX_PAY_FAILED = -1;
    public static final int ALI_PAY = 2;


    // 默认头像图片
    public static final int PHOTO = R.mipmap.ic_launcher;
    // 默认背景图片
    public static final int BAC = R.mipmap.ic_launcher;
    // 出错头像图片
    public static final int ERROR_PHOTO = R.mipmap.ic_launcher;
    // 出错背景图片
    public static final int ERROR_BAC = R.mipmap.ic_launcher;

    // 刷新
    public static final int REFRESH = 0;
    // 加载更多
    public static final int LOAD = 1;

    // 分享给qq好友
    public static final int SHARE_QQ = 1;
    // 分享到qq空间
    public static final int SHARE_QZ = 2;
    // 分享给微信好友
    public static final int SHARE_WX = 3;
    // 分享到微信朋友圈
    public static final int SHARE_WXP = 4;
    // 分享到新浪微博
    public static final int SHARE_SINA = 5;

    /**
     * 腾讯分享的回调
     */
    public static final IUiListener listener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            T.showShort(TApplication.context, "分享成功");
        }

        @Override
        public void onError(UiError uiError) {
            T.showShort(TApplication.context, "分享失败");
        }

        @Override
        public void onCancel() {
            T.showShort(TApplication.context, "取消分享");
        }
    };
}
