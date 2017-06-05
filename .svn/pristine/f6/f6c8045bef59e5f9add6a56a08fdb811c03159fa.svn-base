package com.lexinsmart.utils;

import static com.lexinsmart.MyApplication.gCookieStore;
import static com.lexinsmart.MyApplication.gHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.protocol.BasicHttpContext;

public class HttpCommunications {

	private HttpCommunications() {

	}

	/**
	 * 通过url从服务器获取应答字符串
	 * 
	 * @param url
	 * @param args
	 * @return
	 */
	public static String getServerResponse(String url, NameValuePair[] args) {
		BasicHttpContext httpContext = new BasicHttpContext();
		
		httpContext.setAttribute(ClientContext.COOKIE_STORE, gCookieStore);

		HttpPost httpPost = new HttpPost(url);
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (int i = 0; i < args.length; i++) {
			pairs.add(args[i]);
		}
		HttpResponse httpResp;
		String response = "";
		try {
			HttpEntity reqHttpEntity = new UrlEncodedFormEntity(pairs, "utf-8");
			httpPost.setEntity(reqHttpEntity);
			httpResp = gHttpClient.execute(httpPost, httpContext);

			int statusCode = httpResp.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity respEntity = httpResp.getEntity();

				// InputStream is = new
				// BufferedInputStream(respEntity.getContent());
				// byte[] buff = new byte[1024];
				// while (is.read(buff) > 0) {
				// response += new String(buff);
				// }
				// response = response.trim();

				InputStream is = new BufferedInputStream(respEntity.getContent());
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(is));
				String inputLine = "";
				while ((inputLine = bufferReader.readLine()) != null) {
					response += inputLine;
				}
				response = response.trim();
				response = new String(response.getBytes(), "UTF-8"); // URLDecoder.decode(response, "UTF-8");

			}
			reqHttpEntity.consumeContent();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 从服务器应答的字符串中查找某个键的值
	 * 
	 * @param response
	 * @param key
	 * @return
	 */
	public static String getResponseKeyValue(String response, String key) {
		response = response.replace("\"", ""); // 去除所有双引号
		if (!response.contains(key)) {
			return null;
		}
		int length = response.length();
		int start = response.indexOf(key) + key.length() + 1; // KV值在response中的起点索引
		if (start <= 0 || start >= length - 1) {
			return null;
		}
		final String[] endChars = new String[] { ",", "]", "}" }; // KV值的结束符，如有需要可以修改此数组
		int[] endIndexs = new int[endChars.length];
		for (int i = 0; i < endIndexs.length; i++) {
			endIndexs[i] = response.substring(start).indexOf(endChars[i]) + start;
			if (endIndexs[i] < 0 + start) {
				endIndexs[i] = Integer.MAX_VALUE;
			}
		}
		int end = endIndexs[0]; // KV值在response中的终点索引
		for (int i = 1; i < endIndexs.length; i++) {
			end = end > endIndexs[i] ? endIndexs[i] : end;
		}
		if (end <= 0 || end > length - 1 || start > end) {
			return null;
		}
		String value = response.substring(start, end);
		return value;
	}

}
