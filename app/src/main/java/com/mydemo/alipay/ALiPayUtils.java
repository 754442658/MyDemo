package com.mydemo.alipay;

import android.app.Activity;
import android.os.Message;
import android.support.annotation.Nullable;

import com.alipay.sdk.app.PayTask;
import com.lzy.okhttputils.OkHttpUtils;
import com.mydemo.TApplication;
import com.mydemo.callback.JsonCallback;
import com.mydemo.state.AppStore;
import com.mydemo.state.Constant;
import com.mydemo.utils.SPUtils;
import com.mydemo.utils.T;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ShiShow_xk on 2017/6/23.
 */
public class ALiPayUtils {

    /**
     * 后台签名、生成订单
     *
     * @param context
     */
    public static void pay(final Activity context) {
//        http://zb.shixiutv.com/data/payhandler.ashx?paytarget=alipayapp&type=depositPay&productid=582&uid=13912345678
        // 联网获取orderInfo
        OkHttpUtils.post("http://zb.shixiutv.com/data/payhandler.ashx")
                .tag(context)
                .params("paytarget", "alipayapp")
                .params("type", "depositPay")

                .params("productid", "582")  // 订单号
                .params("uid", (String) SPUtils.get(TApplication.context, "uid", ""))  // 用户id
                .execute(new JsonCallback<String>(String.class) {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        try {
                            if (!s.startsWith("app_id")) {
                                T.showShort(TApplication.context, s);
                                return;
                            }
                            final String orderInfo = URLDecoder.decode(s, "utf-8");
                            Runnable payRunnable = new Runnable() {
                                @Override
                                public void run() {
                                    PayTask alipay = new PayTask(context);
                                    Map<String, String> result = alipay.payV2(orderInfo, true);

                                    Message msg = new Message();
                                    msg.what = Constant.ALI_PAY;
                                    msg.obj = result;
                                    AppStore.wxPayHandler.sendMessage(msg);
                                }
                            };
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
