package com.mydemo.wxapi;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.PostRequest;
import com.mydemo.callback.DialogCallback;
import com.mydemo.entity.PayEntity;
import com.mydemo.entity.WxPayInfo;
import com.mydemo.state.Constant;
import com.mydemo.utils.AppUtils;
import com.mydemo.utils.L;
import com.mydemo.utils.T;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ShiShow_xk on 2017/3/20.
 */
public class WXPayUtils {
    private static IWXAPI msgApi;

    /**
     * 生成微信预支付订单，如果在本地签名的话，就掉这个接口，获取 订单号 out_trade_no、回调地址 notify_url、商品金额 total_fee
     * 然后调用微信的预支付订单接口
     *
     * @param orderID 订单ID  type为0的时候需要携带订单ID
     * @param type    0表示商品订单支付，1表示充值
     * @param money   type为1的时候  需要携带充值金额
     *                UID         用户账号
     */
    public static void wxPay(final Context context, String orderID, String type, String money) {
        // url 是获取上面数据的接口地址
        try {
            if (msgApi == null) {
                msgApi = WXAPIFactory.createWXAPI(context, Constant.WxAppID, true);
                msgApi.registerApp(Constant.WxAppID);
            }
            // 首先判断是否安装微信客户端
            if (!AppUtils.isInstallWx(context, msgApi)) {
                // 没有安装微信
                T.showShort(context, "未安装微信");
                return;
            }

            String url = "";
            PostRequest post = new PostRequest(url);
            post.tag(context);
            post.params("type", type);
            post.params("UID", "18773141706");

            if (type.equals("0")) {
                // 订单支付
                post.params("orderid", orderID);
            } else if (type.equals("1")) {
                // 微信充值
                post.params("Money", money);
            }
            post.execute(new DialogCallback<PayEntity>((Activity) context, PayEntity.class) {
                @Override
                public void onResponse(boolean isFromCache, PayEntity payEntity, Request request, @Nullable Response response) {
                    pay(context, payEntity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 微信支付 信息直接生成数据
     *
     * @param context
     * @param payEntity
     */
    public static void pay(Context context, PayEntity payEntity) {
        try {
            String xml = WXPayUtils.getXml((Activity) context, Constant.KEY,
                    "圈圈优选商品支付", payEntity.getNotify_url(), payEntity.getOut_trade_no
                            (), payEntity.getTotal_fee());
            toPay(context, xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void toPay(final Context context, String xml) {
        OkHttpUtils.post("https://api.mch.weixin.qq.com/pay/unifiedorder")
                .tag(context)
                .postString(xml)
                .execute(new DialogCallback<String>((Activity) context, String.class) {
                    @Override
                    public void onResponse(boolean isFromCache, String s, Request request, @Nullable Response response) {
                        Map<String, String> map = WXPayUtils.xml2Map(s);
                        L.e(map.toString());
                        if (map.get("return_code").equals("SUCCESS")) {
                            // 二次签名

                            String time = String.valueOf(System.currentTimeMillis() / 1000);
                            String str = getStr(32);
                            // 获取sign签名
                            SortedMap<Object, Object> map1 = new TreeMap<>();
                            map1.put("appid", map.get("appid"));
                            map1.put("partnerid", map.get("mch_id"));
                            map1.put("prepayid", map.get("prepay_id"));
                            map1.put("package", Constant.PACKAGE_VALUE);
                            map1.put("noncestr", str);
                            map1.put("timestamp", time);

                            String sign = createSign(Constant.KEY, map1);


                            // 生成预支付订单成功  吊起支付接口
                            WxPayInfo wxPayInfo = new WxPayInfo();
                            wxPayInfo.setAppid(map.get("appid"));
                            wxPayInfo.setPartnerid(map.get("mch_id"));
                            wxPayInfo.setPrepayid(map.get("prepay_id"));
                            wxPayInfo.setNoncestr(str);
                            wxPayInfo.setSign(sign);
                            wxPayInfo.setTimestamp(time);

                            L.e(wxPayInfo.toString());
                            pay(context, wxPayInfo);
                        } else if (map.get("return_code").equals("FAIL")) {
                            // 生成预支付订单失败
                        }
                    }
                });
    }

    /**
     * 通过微信支付实体进行微信支付
     * 如果是后台进行签名的话，可以掉接口获取WxPayInfo然后调用这个接口就行
     *
     * @param context
     * @param wxPayInfo
     */
    private static void pay(Context context, WxPayInfo wxPayInfo) {
        try {
            if (msgApi == null) {
                msgApi = WXAPIFactory.createWXAPI(context, wxPayInfo.getAppid(), true);
                msgApi.registerApp(wxPayInfo.getAppid());
            }
            // 首先判断是否安装微信客户端
            if (!AppUtils.isInstallWx(context, msgApi)) {
                // 没有安装微信
                T.showShort(context, "未安装微信");
                return;
            }

            PayReq request = new PayReq();
            request.appId = wxPayInfo.getAppid();
            request.partnerId = wxPayInfo.getPartnerid();
            request.packageValue = Constant.PACKAGE_VALUE;

            request.prepayId = wxPayInfo.getPrepayid();
            request.nonceStr = wxPayInfo.getNoncestr();
            request.timeStamp = wxPayInfo.getTimestamp();
            request.sign = wxPayInfo.getSign();
            msgApi.sendReq(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成微信Xml参数
     *
     * @param activity
     * @param key      秘钥
     * @param body     商品描述
     * @param url      回调地址
     * @param orderID  订单号
     * @param money    订单金额
     * @return
     */
    public static String getXml(Activity activity, String key, String body, String url,
                                String orderID, String money) {
        try {
            StringBuffer xml = new StringBuffer();
            String ip = getWifiIp(activity);
            if (ip == "" && ip == "") {
                ip = getLocalIpAddress();
            }
            String str = getStr(32);

            // 获取sign签名
            SortedMap<Object, Object> map = new TreeMap<>();
            map.put("appid", Constant.WxAppID);
            map.put("body", body);
            map.put("mch_id", Constant.MCH_ID);
            map.put("nonce_str", str);
            map.put("notify_url", url);
            map.put("out_trade_no", orderID);
            map.put("spbill_create_ip", ip);
            map.put("total_fee", money);
            map.put("trade_type", "APP");
            String sign = createSign(key, map);

            Log.e("TAG", "IP = " + ip);
            Log.e("TAG", "str = " + str);
            Log.e("TAG", "sing = " + sign);
            try {
                xml.append("</xml>");
                List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
                packageParams.add(new BasicNameValuePair("appid", Constant.WxAppID));
                packageParams.add(new BasicNameValuePair("mch_id", Constant.MCH_ID));
                packageParams.add(new BasicNameValuePair("nonce_str", str));
                packageParams.add(new BasicNameValuePair("body", body));
                packageParams.add(new BasicNameValuePair("out_trade_no", orderID));
                packageParams.add(new BasicNameValuePair("total_fee", money));
                packageParams.add(new BasicNameValuePair("spbill_create_ip", ip));
                packageParams.add(new BasicNameValuePair("notify_url", url));
                packageParams.add(new BasicNameValuePair("trade_type", "APP"));
                packageParams.add(new BasicNameValuePair("sign", sign));
                String xmlstring = toXml(packageParams);
                return xmlstring;
            } catch (Exception e) {
                Log.e("TAG", "fail, ex = " + e.getMessage());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @param xml
     * @return Map
     * @description 将xml字符串转换成map
     */
    public static Map<String, String> xml2Map(String xml) {
        Map<String, String> map = new HashMap<>();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点
            List<Element> list = rootElt.elements();//获取根节点下所有节点
            for (Element element : list) {  //遍历节点
                map.put(element.getName(), element.getText()); //节点的name为map的key，text为map的value
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    private static String toXml(List<NameValuePair> params) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<xml>");
            for (int i = 0; i < params.size(); i++) {
                sb.append("<" + params.get(i).getName() + ">");


                sb.append(params.get(i).getValue());
                sb.append("</" + params.get(i).getName() + ">");
            }
            sb.append("</xml>");

            Log.e("orion", sb.toString());
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成签名
     *
     * @param key
     * @param parameters
     * @return
     */

    private static String createSign(String key, SortedMap<Object, Object> parameters) {
        try {
            StringBuffer sb = new StringBuffer();
            Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
            Iterator it = es.iterator();
            while (it.hasNext()) {
                @SuppressWarnings("rawtypes")
                Map.Entry entry = (Map.Entry) it.next();
                String k = (String) entry.getKey();
                Object v = entry.getValue();
                if (null != v && !"".equals(v) && !"sign".equals(k)
                        && !"key".equals(k)) {
                    sb.append(k + "=" + v + "&");
                }
            }
            sb.append("key=" + key); //KEY是商户秘钥
            String sign = MD5.MD5Encode(sb.toString()).toUpperCase();
            return sign;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取随机32为字符
     *
     * @return
     */
    private static String getStr(int length) {
        try {
            String base = "abcdefghijklmnopqrstuvwxyz0123456789";
            Random random = new Random();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < length; i++) {
                int number = random.nextInt(base.length());
                sb.append(base.charAt(number));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取手机本地IP
     *
     * @return
     */
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    /**
     * 获取手机wifi Ip地址
     *
     * @param activity
     * @return
     */
    private static String getWifiIp(Activity activity) {
        try {
            //获取wifi服务
            WifiManager wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ip = intToIp(ipAddress);
            return ip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }
}
