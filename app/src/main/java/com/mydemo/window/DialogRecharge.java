package com.mydemo.window;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mydemo.R;
import com.mydemo.TApplication;
import com.mydemo.alipay.ALiPayUtils;
import com.mydemo.alipay.PayResult;
import com.mydemo.state.AppStore;
import com.mydemo.state.Constant;
import com.mydemo.utils.EtUtils;
import com.mydemo.utils.T;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.Map;

/**
 * Created by ShiShow_xk on 2016/8/17.
 */
public class DialogRecharge extends Dialog implements View.OnClickListener {

    private Context context;
    // 关闭、余额tv
    private TextView tv_close, tv_balance;
    // 金额et
    private EditText et_money;
    // 支付宝支付、微信支付布局
    private AutoLinearLayout ll_zfb, ll_wx;

    // 支付结果
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case Constant.WX_PAY_SUCCESS:
                        // 微信支付成功
                        T.showShort(TApplication.context, "支付成功");
                        break;
                    case Constant.WX_PAY_CANCLE:
                        // 取消支付
                        T.showShort(TApplication.context, "取消支付");
                        break;
                    case Constant.WX_PAY_FAILED:
                        // 支付失败
                        T.showShort(TApplication.context, "网络异常");
                        break;
                    case Constant.ALI_PAY:
                        // 支付宝支付
                        @SuppressWarnings("unchecked")
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        /**
                         * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            Toast.makeText(TApplication.context, "支付成功", Toast.LENGTH_SHORT).show();
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            Toast.makeText(TApplication.context, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public DialogRecharge(Context context) {
        super(context);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);

        // 去除背景
        Window win = getWindow();
        win.setBackgroundDrawable(new BitmapDrawable());
    }

    public DialogRecharge(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogRecharge(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recharge);
        try {
            findView();
            initView();
            addListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findView() {
        tv_close = (TextView) findViewById(R.id.tv_close);
        tv_balance = (TextView) findViewById(R.id.tv_balance);

        et_money = (EditText) findViewById(R.id.et_money);

        ll_zfb = (AutoLinearLayout) findViewById(R.id.ll_zfb);
        ll_wx = (AutoLinearLayout) findViewById(R.id.ll_wx);
    }

    private void initView() {

    }

    private void addListener() {
        tv_close.setOnClickListener(this);
        ll_zfb.setOnClickListener(this);
        ll_wx.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.tv_close:
                    // 点击了关闭
                    dismiss();
                    break;
                case R.id.ll_zfb:
                    // 点击了支付宝支付
                    if (EtUtils.isEmpty(et_money)) {
                        T.showShort(TApplication.context, "请填写充值金额");
                        return;
                    }
                    if (Integer.parseInt(et_money.getText().toString().trim()) < 5) {
                        T.showShort(TApplication.context, "充值金额必须大于5");
                        return;
                    }

                    AppStore.wxPayHandler = handler;

                    ALiPayUtils.pay(getOwnerActivity());
                    break;
                case R.id.ll_wx:
                    // 点击了微信支付
                    if (EtUtils.isEmpty(et_money)) {
                        T.showShort(TApplication.context, "请填写充值金额");
                        return;
                    }
                    if (Integer.parseInt(et_money.getText().toString().trim()) < 5) {
                        T.showShort(TApplication.context, "充值金额必须大于5");
                        return;
                    }
                    // 微信充值
                    AppStore.wxPayHandler = handler;
//                    WXPayUtils.pay();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
