package com.mydemo.entity;

import java.io.Serializable;

/**
 * Created by ShiShow_xk on 2017/3/20.
 */
public class PayEntity implements Serializable {
    public String Msg;  //提示信息
    public String State;//状态    1成功，0失败
    public String out_trade_no;//订单号
    public String notify_url;//回调地址
    public String total_fee;//商品金额

    @Override
    public String toString() {
        return "PayEntity{" +
                "Msg='" + Msg + '\'' +
                ", State='" + State + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", notify_url='" + notify_url + '\'' +
                ", total_fee='" + total_fee + '\'' +
                '}';
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }
}
