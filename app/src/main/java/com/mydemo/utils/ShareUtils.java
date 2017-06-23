package com.mydemo.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mydemo.R;
import com.mydemo.TApplication;
import com.mydemo.state.Constant;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.Tencent;

import java.io.ByteArrayOutputStream;

public class ShareUtils {


    /**
     * 分享到指定平台并且
     *
     * @param shareTo  分享到的平台
     * @param title    太累了 标题
     * @param content  我行我秀 摘要
     * @param imgUrl   http://zb.shixiutv.com/ 图片路径
     * @param titleUrl http://zb.shixiutv.com/ZhiBo.aspx?pid=5wh615 点击后跳转路径
     */
    public static void ShareTo(Activity context, Object api, int shareTo,
                               String title, String content, String imgUrl, String titleUrl) {
        // TODO Auto-generated method stub
        switch (shareTo) {
            case Constant.SHARE_SINA:
                // 分享给新浪微博
                break;
            case Constant.SHARE_QQ:
                // 分享给qq好友
                Bundle bundle = new Bundle();
                // 分享的消息类型
                bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                //这条分享消息被好友点击后的跳转URL。
                bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, titleUrl);
                //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
                bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
                //分享的消息摘要，最长50个字
                bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
                //分享的图片URL
                bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
//                //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
//                bundle.putString(Constants.PARAM_APPNAME, "??我在测试");
//                //标识该消息的来源应用，值为应用名称+AppId。
//                bundle.putString(Constants.PARAM_APP_SOURCE, "星期几" + AppId);

                // 其中APP_ID是分配给第三方应用的appid，类型为String。
                api = Tencent.createInstance(Constant.QQAppID, TApplication.context);
                ((Tencent) api).shareToQQ(context, bundle, Constant.listener);
                break;
            case Constant.SHARE_QZ:
                // 分享到qq空间
                Bundle qz_bundle = new Bundle();
                // 分享到qq空间要加上这句
                qz_bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                // 分享的消息类型
                qz_bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                //这条分享消息被好友点击后的跳转URL。
                qz_bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, titleUrl);
                //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
                qz_bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
                //分享的消息摘要，最长50个字
                qz_bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
                //分享的图片URL
                qz_bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
//                //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
//                bundle.putString(Constants.PARAM_APPNAME, "??我在测试");
//                //标识该消息的来源应用，值为应用名称+AppId。
//                bundle.putString(Constants.PARAM_APP_SOURCE, "星期几" + AppId);

                // 其中APP_ID是分配给第三方应用的appid，类型为String。
                api = Tencent.createInstance(Constant.QQAppID, TApplication.context);
                ((Tencent) api).shareToQQ(context, qz_bundle, Constant.listener);
                break;
            case Constant.SHARE_WX:
                // 分享给微信好友
                WXWebpageObject webpage = new WXWebpageObject();
                // 网页地址
                webpage.webpageUrl = titleUrl;
                final WXMediaMessage msg = new WXMediaMessage(webpage);
                // 网页标题
                msg.title = title;
                // 网页描述
                msg.description = content;

                if (imgUrl == null || imgUrl.equals("")) {
                    Bitmap thumb = BitmapFactory.decodeResource(
                            context.getResources(), R.mipmap.ic_launcher);
                    L.e("lenght=" + thumb.getByteCount());
                    msg.setThumbImage(thumb);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;
                    // 表示发送场景为好友对话，这个代表分享给好友
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                    // 表示发送场景为朋友圈，这个代表分享到朋友圈
                    // req.scene =
                    // SendMessageToWX.Req.WXSceneTimeline;
                    ((IWXAPI) api).sendReq(req);
                } else {
                    getBitmap(context, imgUrl, shareTo, msg, (IWXAPI) api);
                }
                break;
            case Constant.SHARE_WXP:
                // 分享到微信朋友圈
                L.e("AppId = " + Constant.WxAppID);
                WXWebpageObject webpageP = new WXWebpageObject();
                // 网页地址
                webpageP.webpageUrl = titleUrl;
                final WXMediaMessage msgP = new WXMediaMessage(webpageP);
                // 网页标题
                msgP.title = title;
                // 网页描述
                msgP.description = content;

                if (imgUrl == null || imgUrl.equals("")) {
                    msgP.setThumbImage(BitmapFactory.decodeResource(
                            context.getResources(), R.mipmap.ic_launcher));
                    SendMessageToWX.Req reqP = new SendMessageToWX.Req();
                    reqP.transaction = buildTransaction("webpage");
                    reqP.message = msgP;
                    // 表示发送场景为好友对话，这个代表分享给好友
                    // reqP.scene = SendMessageToWX.Req.WXSceneSession;
                    // 表示发送场景为朋友圈，这个代表分享到朋友圈
                    reqP.scene = SendMessageToWX.Req.WXSceneTimeline;
                    ((IWXAPI) api).sendReq(reqP);
                } else {
                    getBitmap(context, imgUrl, shareTo, msgP, (IWXAPI) api);
                }
                break;
        }
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    /**
     * 如果有图片的话就把图片压缩处理再分享到微信
     *
     * @param activity
     * @param imgUrl
     * @param shareTo
     * @param msgP
     * @param api
     */
    private static void getBitmap(Activity activity, String imgUrl, final int shareTo,
                                  final WXMediaMessage msgP, final IWXAPI api) {
        Glide.with(activity)
                .load(imgUrl).asBitmap()
                .into(new SimpleTarget<Bitmap>(100, 100) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        // 获取图片 判断图片进行压缩至32k
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 85, out);
                        float zoom = (float) Math.sqrt(32 * 1024 / (float) out.toByteArray().length);

                        Matrix matrix = new Matrix();
                        matrix.setScale(zoom, zoom);

                        Bitmap result = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true);

                        out.reset();
                        result.compress(Bitmap.CompressFormat.JPEG, 85, out);
                        while (out.toByteArray().length > 32 * 1024) {
                            System.out.println(out.toByteArray().length);
                            matrix.setScale(0.9f, 0.9f);
                            result = Bitmap.createBitmap(result, 0, 0, result.getWidth(), result.getHeight(), matrix, true);
                            out.reset();
                            result.compress(Bitmap.CompressFormat.JPEG, 85, out);
                        }

                        msgP.setThumbImage(resource);
                        SendMessageToWX.Req reqP = new SendMessageToWX.Req();
                        reqP.transaction = buildTransaction("webpage");
                        reqP.message = msgP;
                        if (shareTo == Constant.SHARE_WX) {
                            // 表示发送场景为好友对话，这个代表分享给好友
                            reqP.scene = SendMessageToWX.Req.WXSceneSession;
                        } else if (shareTo == Constant.SHARE_WXP) {
                            // 表示发送场景为朋友圈，这个代表分享到朋友圈
                            reqP.scene = SendMessageToWX.Req.WXSceneTimeline;
                        }
                        api.sendReq(reqP);
                    }
                });
    }
}
