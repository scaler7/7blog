package com.scaler7.common;

/**
 * 
* Title: WebConstant
* Description: web相关常量类  
* @author scaler7
* @date 2019年12月29日
 */
public class WebConstant {
	
	public static final String GRANT_TYPE = "code";
	
	public static final String APP_ID = "101835851";
	
	public static final String APP_KEY = "3609f27f080426ed21c30429c90be908";
	/**
	 * qq回调域
	 */
	public static final String CALLBACKURL = "http://scaler7.online/qq/callback";
	/**
	 * 当前登陆用户key值
	 */
	public static final String CURRENT_USER = "currUser";
	/**
	 * 当前登陆访客key值
	 */
	public static final String CURRENT_VISITOR = "currVisitor";
	/**
	 * qq登陆访客唯一标识
	 */
	public static final String OPEN_ID = "openid";
	/**
	 * 昵称
	 */
	public static final String NICK_NAME = "nickname";
	/**
	 * qq登陆访客头像
	 */
	public static final String PROFILE_PHOTO = "profilePhoto";
	/**
	 * 判断访客登陆状态的标志
	 */
	public static final String IS_LOGIN = "isLogin";
	
}
