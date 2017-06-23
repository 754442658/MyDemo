package com.mydemo.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.mydemo.state.Constant;
import com.mydemo.utils.T;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信客户端回调activity示例
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.WxAppID, false);
        api.registerApp(Constant.WxAppID);
        api.handleIntent(getIntent(), this);
    }

    /**
     * 处理微信发出的向app的请求
     */
    @Override
    public void onReq(BaseReq arg0) {
        switch (arg0.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
        finish();
    }

    /**
     * 处理app向微信发送请求，微信返回的结果
     */
    @Override
    public void onResp(BaseResp arg0) {
        String result = "";
        switch (arg0.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "取消分享";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "认证失败";
                break;
            default:
                result = "errcode_unknown";
                break;
        }
        T.showShort(this, result);
        finish();
    }

    // /**
    // * 处理微信发出的向第三方应用请求app message
    // * 在微信客户端中的聊天页面有添加工具，可以将本应用的图标添加到其中然后点击图标，下面的代码会被执行。demo仅仅只是打开自己而已，但你可以
    // * 做点其他的事情，包括跟本不打开任何页面
    // */
    // public void onGetMessageFromWXReq(WXMediaMessage msg) {
    // Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(
    // getPackageName());
    // startActivity(iLaunchMyself);
    // }
    //
    // /**
    // * 处理微信向第三方应用发起的消息 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
    // * 应用是可以不分享应用文件，分享应用的自定义信息。接收方的微信 客户端会通过这个方法，将这个信息发送回接收放手机上的本demo中，当做回调方法
    // * bendemo只是将信息展示出来但你可做点其他事，不仅仅只是toast
    // */
    // public void onShowMessageFromWXReq(WXMediaMessage msg) {
    // if (msg != null && msg.mediaObject != null
    // && (msg.mediaObject instanceof WXAppExtendObject)) {
    // WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
    // Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
    // }
    // }

}
