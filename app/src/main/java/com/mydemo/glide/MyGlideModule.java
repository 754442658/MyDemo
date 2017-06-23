package com.mydemo.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.mydemo.state.Constant;

public class MyGlideModule implements GlideModule {

	@Override
	public void applyOptions(Context arg0, GlideBuilder builder) {
		// TODO Auto-generated method stub
		// 应用缓存文件地址
		String cachePath = Constant.CACHE_FILE.getAbsolutePath() + "/图片缓存";
		// 缓存区大小100M
		int cacheSize100MegaBytes = 104857600;
		builder.setDiskCache(new DiskLruCacheFactory(cachePath,
				cacheSize100MegaBytes));
		// 设置图片解码格式，默认格式RGB_565，使用内存是ARGB_8888的一半，但是图片质量就没那么高了，而且不支持透明度
		// builder.setDecodeFormat(DecodeFormat.ALWAYS_ARGB_8888);

	}

	@Override
	public void registerComponents(Context arg0, Glide arg1) {
		// TODO Auto-generated method stub

	}

}
