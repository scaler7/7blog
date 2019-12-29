package com.scaler7.util;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSONObject;

public class QQHttpClient {
	
	//获取token
	public static String getAccessToken(String url) throws IOException {
		
		CloseableHttpClient client = HttpClients.createDefault();
		String token = null;
		
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		 
        if(entity != null){
            String result = EntityUtils.toString(entity,"UTF-8");
            if(result.indexOf("access_token") >= 0){
                String[] array = result.split("&");
                for (String str : array){
                    if(str.indexOf("access_token") >= 0){
                        token = str.substring(str.indexOf("=") + 1);
                        break;
                    }
                }
            }
        }
 
        httpGet.releaseConnection();
        return token;
	}
	
	//获取openID 用户在此网站的唯一标识
    public static String getOpenID(String url) throws IOException {
        JSONObject jsonObject = null;
        CloseableHttpClient client = HttpClients.createDefault();
 
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
 
        if(entity != null){
            String result = EntityUtils.toString(entity,"UTF-8");
            jsonObject = parseJSONP(result);
        }
 
        httpGet.releaseConnection();
 
        if(jsonObject != null){
            return jsonObject.getString("openid");
        }else {
            return null;
        }
    }
    
    //获取用户信息
    public static JSONObject getUserInfo(String url) throws IOException {
        JSONObject jsonObject = null;
        CloseableHttpClient client = HttpClients.createDefault();
 
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
 
 
        if(entity != null){
            String result = EntityUtils.toString(entity,"UTF-8");
            jsonObject = JSONObject.parseObject(result);
        }
 
        httpGet.releaseConnection();
 
        return jsonObject;
    }

	private static JSONObject parseJSONP(String jsonResult) {
		int startIndex = jsonResult.indexOf("(");
        int endIndex = jsonResult.lastIndexOf(")");
 
        String json = jsonResult.substring(startIndex + 1,endIndex);
 
        return JSONObject.parseObject(json);
	}
	
}
