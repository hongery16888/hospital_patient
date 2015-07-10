/*
 * Copyright © 2009-2012 Chengdu Tianfu Software Park Co., Ltd.</br>
 * Company: Chengdu Tianfu Software Park Co., Ltd.</br>
 * Project: CHLogistics
 * Package: com.tfsp.mobile.chlogistics.api
 * Version: 1.0</br>
 * Android SDK: min sdk level: 7</br>
 * Create Time: 2012-12-20</br>
 */
package iori.hpatient.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * DataTransfer summary:解析json数据并封装成model对象；
 * 
 * @author Wang hongyun</br> Description: 用于解析json数据并封装成model对象，需要自己实现解析部分</br>
 *         Create Time: 2012-12-20 下午3:32:43</br> </br>History： </br> Editor
 *         ****Time **** Mantis No **** Operation **** Description ***
 *** 
 *** 
 *** 
 *** 
 */
public class DataTransfer implements HttpRequest {

	private static final String TAG = "DataTransfer";
	// ↓登录信息↓
	private static String userID;
	
	public ResponseFilter handleResponse(String jsonString, String requestAction, int requestMark, Class<?> cls, NetworkConnectListener listener) throws JSONException {

		ResponseFilter filter = new ResponseFilter(
				ERROR_DATA_TRANSFORM, ERRORMSG_DATA_TRANSFORM, 
				requestAction, requestMark, listener);
		
		if (null == jsonString) {
			return filter;
		}
		JSONObject jsonObject = new JSONObject(jsonString);
		// System.out.println("jsonString : " + jsonString);
		Boolean result = jsonObject.optBoolean(KEY_RESULT);
		if (result) {
			JSONObject dataJson = jsonObject.optJSONObject(KEY_DATA);
			Object data = null;
			if(dataJson != null) {
				String dataJsonStr = dataJson.toString();
				if(cls != null) {
					data = decode(dataJsonStr, cls);
				}else {
					data = dataJsonStr;
				}
			}
			filter.setError(ERROR_NONE);
			filter.setData(data);
		} else {
			// 如果获取错误码为ERROR_NONE 强制更改为ERROR_OTHER
			filter.setError(jsonObject.optInt(KEY_ERROR_ID));
			if (filter.getError() == ERROR_NONE) filter.setError(ERROR_OTHER);
			filter.setData(jsonObject.optString(KEY_ERROR_MSG));
		}
		return filter;
	}

	public static Object decode(String strJson, Class<?> cls) {
		GsonBuilder gsong = new GsonBuilder();
		Gson gson = gsong.setPrettyPrinting().disableHtmlEscaping().create();
		return gson.fromJson(strJson, cls);
	}
	
	public static void setUserId(String userId) {
		userID = userId;
	}
	
	public static String getUserId() {
		return userID;
	}
	
}
