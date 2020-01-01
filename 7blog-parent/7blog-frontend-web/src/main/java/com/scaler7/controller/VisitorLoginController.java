package com.scaler7.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scaler7.common.CodeMsg;
import com.scaler7.common.Result;
import com.scaler7.common.WebConstant;
import com.scaler7.entity.BlogVisitor;
import com.scaler7.service.BlogVisitorService;
import com.scaler7.util.QQHttpClient;
import cn.hutool.core.date.DateUtil;


@RequestMapping("")
@Controller
public class VisitorLoginController {
	
	@Autowired
	BlogVisitorService blogVisitorService;
	
	@GetMapping("checkLogin")
	@ResponseBody
	public Object checkLogin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Boolean isLogin = (Boolean) session.getAttribute(WebConstant.IS_LOGIN);
		BlogVisitor visitor = (BlogVisitor) session.getAttribute(WebConstant.CURRENT_VISITOR);
		
		if(isLogin!=null && isLogin == true && visitor != null) {
			return new Result(visitor.getVisitorId());
		}
		return new Result(CodeMsg.ERROR);
	}
	
	@GetMapping("loginOut")
	@ResponseBody
	public Object loginOut(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute(WebConstant.CURRENT_VISITOR); // 移出session中的当前访客
		session.setAttribute(WebConstant.IS_LOGIN, false);
		return new Result();
	}
	
	@GetMapping("currVisitorinfo")
	@ResponseBody
	public Object getCurrVisitor(HttpServletRequest request) {
		HttpSession session = request.getSession();
		BlogVisitor currVisitor = (BlogVisitor) session.getAttribute(WebConstant.CURRENT_VISITOR);
		BlogVisitor visitor = new BlogVisitor();
		
		visitor.setVisitorId(currVisitor.getVisitorId());
		visitor.setEmail(currVisitor.getEmail());
		visitor.setPersonalWebsite(currVisitor.getPersonalWebsite());
		visitor.setQqAccount(currVisitor.getQqAccount());
		visitor.setWechatAccount(currVisitor.getWechatAccount());
		visitor.setAllowInform(currVisitor.getAllowInform()); // 按需给予数据，防止敏感数据被抓取
		
		return new Result(visitor);
		
	}
	
	@PutMapping("currVisitorinfo")
	@ResponseBody
	public Object updateById(@RequestBody BlogVisitor visitor,HttpServletRequest request) {
		blogVisitorService.updateById(visitor);
		HttpSession session = request.getSession();
		BlogVisitor currVisitor = (BlogVisitor) session.getAttribute(WebConstant.CURRENT_VISITOR);
		
		currVisitor.setEmail(visitor.getEmail());
		currVisitor.setPersonalWebsite(visitor.getPersonalWebsite());
		currVisitor.setQqAccount(visitor.getQqAccount());
		currVisitor.setWechatAccount(visitor.getWechatAccount());
		currVisitor.setAllowInform(visitor.getAllowInform());
		
		session.setAttribute(WebConstant.CURRENT_VISITOR, currVisitor); // 更新session
		return new Result();
	}
	
	@GetMapping("qq/oauth")
	public String qqOauth(HttpServletRequest request) {
		HttpSession session = request.getSession();
		//申请时填写的回调地址
		String callBackUrl = WebConstant.CALLBACKURL;
		//第三方应用防止CSRF攻击
		String uuid = UUID.randomUUID().toString().replace("-", "");
		session.setAttribute("state", uuid);
		@SuppressWarnings("deprecation")
		String url = "https://graph.qq.com/oauth2.0/authorize?response_type=code"+
	                 "&client_id=" + WebConstant.APP_ID +
	                 "&redirect_uri=" + URLEncoder.encode(callBackUrl) +
	                 "&state=" + uuid;
		return "redirect:"+url;
	}

	@GetMapping("qq/callback")
	public Object qqCallback(HttpServletRequest request) {
		HttpSession session = request.getSession();
		//HttpSession session = request.getSession();
		String code = request.getParameter("code");
		//String state = request.getParameter("state");
		//String uuid = (String) session.getAttribute("state");
		
		String callBackUrl = WebConstant.CALLBACKURL;
		
		String access_token = null;
		String openid = null;
		JSONObject jsonObject = null;
		try {
			
			//通过返回的code获取Access Token
			String url = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code"+
	                "&client_id=" + WebConstant.APP_ID +
	                "&client_secret=" + WebConstant.APP_KEY +
	                "&code=" + code +
	                "&redirect_uri=" + callBackUrl;
			access_token = QQHttpClient.getAccessToken(url);
			
			//通过accesToken 获取oppenId(用户唯一标识)
			url = "https://graph.qq.com/oauth2.0/me?access_token=" + access_token;
	        openid = QQHttpClient.getOpenID(url);
			
	        //获取QQ用户信息
	        url = "https://graph.qq.com/user/get_user_info?access_token=" + access_token +
	                "&oauth_consumer_key="+ WebConstant.APP_ID +
	                "&openid=" + openid;
	        jsonObject = QQHttpClient.getUserInfo(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        boolean isSuccess = login(jsonObject,openid,request);
        
        if(isSuccess) {
        	BlogVisitor visitor = blogVisitorService.findVisitorByOpenId(openid);
        	session.setAttribute(WebConstant.CURRENT_VISITOR, visitor);
        	session.setAttribute(WebConstant.PROFILE_PHOTO, visitor.getProfilePhoto());
        	session.setAttribute(WebConstant.NICK_NAME, visitor.getNickname());
        	session.setAttribute(WebConstant.IS_LOGIN, true);
        }
        
		return "redirect:/";
	}

	private boolean login(JSONObject jsonObject, String openid,HttpServletRequest request) {
		BlogVisitor visitor = blogVisitorService.findVisitorByOpenId(openid);
		if(null == visitor) {
			return addNewVisitor(jsonObject,openid,request);
		}else {
			return updateVisitor(visitor,request);
		}
	}

	/**
	 * 
	 * @Description:添加新访客
	 * @param jsonObject
	 * @param openid
	 * @param request
	 */
	private boolean addNewVisitor(JSONObject jsonObject, String openid,HttpServletRequest request) {
		String nickname = (String) jsonObject.get("nickname"); // 获取昵称
		String profilePhoto = (String) jsonObject.get("figureurl_qq_2"); // 100x100头像
		String gender = (String) jsonObject.get("gender"); // 获取性别
		Integer year = null;
		if(null != jsonObject.get("year")) {
			year = Integer.valueOf((String)jsonObject.get("year"));
		}
		Integer currYear = DateUtil.year(new Date());
		Integer age = currYear-year; // 获取年龄
		
		BlogVisitor visitor = new BlogVisitor();
		visitor.setAge(age);
		visitor.setGender(gender);
		visitor.setProfilePhoto(profilePhoto);
		visitor.setNickname(nickname);
		visitor.setVisitorName(nickname);
		visitor.setRegistTime(LocalDateTime.now());
		visitor.setLastLoginTime(LocalDateTime.now());
		visitor.setOpenId(openid);
		visitor.setIp(request.getRemoteAddr());
		visitor.setIsValid(1);
		
		return blogVisitorService.save(visitor);
	}

	/**
	 * 
	 * @Description:修改原有访客信息
	 * @param visitor
	 * @param request
	 */
	private boolean updateVisitor(BlogVisitor visitor,HttpServletRequest request) {
		visitor.setIp(request.getRemoteAddr());
		visitor.setLastLoginTime(LocalDateTime.now());
		return blogVisitorService.updateById(visitor);
	}
	
	
	
	
}
