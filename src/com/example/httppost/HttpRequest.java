package com.example.httppost;

import java.io.*;
import java.net.*;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpRequest {

	 public static String HttpPostRequest(List<NameValuePair> params, String url)
			 {
         String result = null;
         // 获取HttpClient对象
         HttpClient httpClient = new DefaultHttpClient();
         // 新建HttpPost对象
         HttpPost httpPost = new HttpPost(url);
         if (params != null) {
             // 设置字符集
             HttpEntity entity;
			try {
				entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				httpPost.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             // 设置参数实体
             
         }

         /*// 连接超时
         httpClient.getParams().setParameter(
                 CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
         // 请求超时
         httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                 3000);*/
         // 获取HttpResponse实例
         HttpResponse httpResp;
		try {
			httpResp = httpClient.execute(httpPost);
			// 判断是够请求成功
         if (httpResp.getStatusLine().getStatusCode() == 200) {
             // 获取返回的数据
             result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
         } else {
             Log.i("HttpPost", "HttpPost方式请求失败");
         }
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         

         return result;
     }
}
