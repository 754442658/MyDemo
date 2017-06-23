package com.mydemo.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mydemo.state.Constant;

/**
 * Created by ShiShow_xk on 2017/1/7.
 */
public class GlideUtils {

    /**
     * @param context
     * @param url
     * @param img
     */
    public static void showPhoto(Context context, String url, ImageView img) {
        Glide.with(context)
                .load(url).asBitmap()
                .dontAnimate()
                .placeholder(Constant.PHOTO)
                .error(Constant.ERROR_PHOTO)
                .into(img);
    }

    /**
     * @param context
     * @param url
     * @param img
     */
    public static void showPic(Context context, String url, ImageView img) {
        Glide.with(context)
                .load(url).asBitmap()
                .dontAnimate()
                .placeholder(Constant.BAC)
                .error(Constant.ERROR_BAC)
                .into(img);
    }
}
