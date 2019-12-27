package com.scaler7.common;

import org.springframework.stereotype.Component;

@Component(value = "webcommons")
public class WebCommons {
	
	/**
	 * 
	 * @Description: 去除html标签
	 * @param content
	 * @return
	 */
	public static String removeHtml(String content) {
		content = content.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "");
		return content;
	}
	
	/**
	 * 
	 * @Description: 切割字符串
	 * @param str
	 * @param len
	 * @return
	 */
	public static String subStr(String str, Integer len) {
		if (null == len) {
			len = 100;
		}
		String tempStr = null;
		if (str.length() > len) {
			tempStr = str.substring(0, len);
			return tempStr + "...";
		}
		return str;
	}
	
}
