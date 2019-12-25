package com.scaler7.utils;

import java.util.Date;

import cn.hutool.core.date.DateUtil;

public class CommonUtils {
	
	public static String getTimeFormat(String format) {
		return DateUtil.format(new Date(), format);
	}
	
}
