package com.mydemo.window;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mydemo.R;
import com.mydemo.state.Constant;
import com.mydemo.utils.ScreenUtils;
import com.mydemo.utils.ShareUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.zhy.autolayout.AutoLinearLayout;


/**
 * Created by ShiShow_xk on 2016/8/9.
 */
public class PopShare extends PopupWindow implements View.OnClickListener {
    private View mMenuView;
    private Context context;

    // 分享到朋友圈、微信好友的AutoLinearLayout
    private AutoLinearLayout ll_wxp, ll_wx;
    // 关闭按钮
    private TextView tv_close;
    // 微信 API
    private IWXAPI api;
    // 腾讯实例
    private Tencent tencent;

    public PopShare(Context context) {
        try {
            this.context = context;
//            this.live = live;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mMenuView = inflater.inflate(R.layout.pop_share, null);
            // 设置SelectPicPopupWindow的View
            this.setContentView(mMenuView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight((int) (ScreenUtils.getScreenHeight(context) / 3.3f));
            // 设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            // 设置SelectPicPopupWindow弹出窗体动画效果
            this.setAnimationStyle(R.style.AnimBottom);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            // 触摸外面销毁
            mMenuView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        dismiss();
                    }
                }
            });
            findView();
            initView();
            addListener();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findView() {
        ll_wx = (AutoLinearLayout) mMenuView.findViewById(R.id.ll_wx);
        ll_wxp = (AutoLinearLayout) mMenuView.findViewById(R.id.ll_wxp);

        tv_close = (TextView) mMenuView.findViewById(R.id.tv_close);
    }

    private void initView() {
        // 微信
        api = WXAPIFactory.createWXAPI(context, Constant.WxAppID, true);
        api.registerApp(Constant.WxAppID);
        // 腾讯
        tencent = Tencent.createInstance(Constant.QQAppID, context.getApplicationContext());
    }

    private void addListener() {
        tv_close.setOnClickListener(this);
        ll_wx.setOnClickListener(this);
        ll_wxp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.ll_wx:
                    // 点击了分享给微信好友
                    ShareUtils.ShareTo((Activity) context, api, Constant.SHARE_WX, "title", "content", "imgurl", "titleurl");
                    dismiss();
                    break;
                case R.id.ll_wxp:
                    // 点击了分享给微信朋友圈
                    ShareUtils.ShareTo((Activity) context, api, Constant.SHARE_WXP, "title", "content", "imgurl", "titleurl");
                    dismiss();
                    break;
                case R.id.tv_close:
                    // 点击了关闭按钮
                    dismiss();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
