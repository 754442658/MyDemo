package com.mydemo.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;

public class EtUtils {

	/**
	 * 判断传递过来的edittext 内容是否为空
	 *
	 * @param et
	 *            需要判断的et
	 * @return 返回值
	 */
	public static boolean isEmpty(EditText et) {
		if (et.getText() == null || et.getText().toString().trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 给et添加过滤器，字符限制 最大输入18位、禁止输入中文
	 *
	 * @param et
	 */
	public static void addFilter(EditText et) {
		et.setFilters(new InputFilter[] { new InputFilter.LengthFilter(18),
				new InputFilter() {

					@Override
					public CharSequence filter(CharSequence source, int start,
											   int end, Spanned dest, int dstart, int dend) {
						if (isCN(source.toString())) {
							return "";
						} else {
							if (dstart >= 6) {
								return source;
							}
						}
						return source;
					}
				} });
	}

	/**
	 * 判断输入的是不是中文
	 *
	 * @param str
	 * @return
	 */
	private static boolean isCN(String str) {
		try {
			byte[] bytes = str.getBytes("UTF-8");
			if (bytes.length == str.length()) {
				return false;
			} else {
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
