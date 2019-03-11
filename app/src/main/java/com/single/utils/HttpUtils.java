package com.single.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;

import android.util.Log;

/**
 * http连接工具�?
 * 
 * @author Jangs
 *
 */
//public class HttpUtils {
//
//	private static final String TAG = "HttpUtils";
//	/**
//	 * post方式
//	 * 
//	 * @param path
//	 * @param params
//	 * @param ecode
//	 * @return
//	 * @throws Exception
//	 */
//	public static String sendPostMethod(String path,
//			Map<String, Object> params, String encode) throws Exception {
//
//		HttpClient httpClient = new DefaultHttpClient();
//		HttpClient httpPost = new HttpPost(path);
//		// 请求超时
//		httpClient.getParams().setParameter(
//				CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
//		// 读取超时
//		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
//				10000);
//
//		String result = "";
//		List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
//		if (params != null && !params.isEmpty()) {
//			String name = "";
//			String value = "";
//			for (Map.Entry<String, Object> entry : params.entrySet()) {
//				name = entry.getKey();
//				value = entry.getValue().toString();
//				BasicNameValuePair valuePair = new BasicNameValuePair(name,
//						value);
//				parameters.add(valuePair);
//			}
//		}
//
//		UrlEncodedFormEntity uFormEntity = new UrlEncodedFormEntity(parameters,
//				encode);
//		httpPost.setEntity(uFormEntity);
//		HttpResponse httpResponse = httpClient.execute(httpPost);
//		if (httpResponse.getStatusLine().getStatusCode() == 200) {
//			result = toString(httpResponse.getEntity(), encode);
//		} else {
//			result = "操作操时！";
//		}
//		return result;
//	}
//	
//	public static String toString(HttpEntity entity, String encode)
//			throws IllegalStateException, IOException {
//		String result = null;
//		StringBuffer sb = new StringBuffer();
//		InputStream is = entity.getContent();
//		BufferedReader br = new BufferedReader(
//				new InputStreamReader(is, encode));
//		String data = "";
//		while ((data = br.readLine()) != null) {
//			sb.append(data);
//		}
//		result = sb.toString();
//		return result;
//	}
//
//}
