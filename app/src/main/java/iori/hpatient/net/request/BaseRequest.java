/*
 * Copyright © 2009-2013 Chengdu Tianfu Software Park Co., Ltd.</br>
 * Company: Chengdu Tianfu Software Park Co., Ltd.</br>
 * Project: CHLogisticsMobile
 * Package: com.toscm.chlogistics.phone.view
 * Version: 1.0</br>
 * Android SDK: min sdk level: 7</br>
 * Create Time: 2013-1-25</br>
 */
package iori.hpatient.net.request;


import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import iori.hpatient.net.HttpRequest;

/**
 * 基本请求
 */
public abstract class BaseRequest implements HttpRequest {

	private static final Charset CHARSET = Charset.forName(HttpRequest.CHARSET);
	
	private int mRequestMark;
	protected String userId;
	private static final String KEY_USER_ID = "userId";

	public List<NameValuePair> postEncode() throws IllegalArgumentException, IllegalAccessException {

		Class<? extends BaseRequest> cl = this.getClass();
		Field[] field = cl.getDeclaredFields();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

//		// set ActionVerify
//		nameValuePairs.add(new BasicNameValuePair("verify", MD5.getMd5(getRequestAction().concat("qzys"))));
//		// set Action
//		nameValuePairs.add(new BasicNameValuePair("action", getRequestAction()));

		Gson gson = new Gson();
		String data = gson.toJson(this);
		// data
		JSONObject dataObj = null;
		try {
			dataObj = new JSONObject(data);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// for (Field f : field) {
		// Object obj;
		// if ((f.getModifiers() == Modifier.PROTECTED) && (obj = f.get(this))
		// != null) {
		// try {
		// if (f.get(this) instanceof List) {
		// List temp = (List) f.get(this);
		// JSONArray array = new JSONArray(temp);
		// dataObj.put(f.getName(),
		// handleStringNull(array.toString()));
		// } else {
		// dataObj.put(f.getName(),
		// handleStringNull(obj.toString()));
		// }
		//
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// 需要传递userId

		if(postUserId() != UNABLE_POST) {
			try {
				dataObj.put(KEY_USER_ID, userId != null ? userId : "");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		nameValuePairs.add(new BasicNameValuePair("data", dataObj.toString()));
		
		return nameValuePairs;
	}

	public static String handleStringNull(String str) {
		return str == null ? "" : str;
	}

	public MultipartEntity getMultiEntity(boolean printRequst) throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		MultipartEntity entity = new MultipartEntity();
		Class<? extends BaseRequest> cl = this.getClass();
		Field[] field = cl.getDeclaredFields();
		
		String printStr = "";
		if(printRequst) {
			printStr = "Request URI:[" + getRequestUrl() 
						+ "], action:[" + getRequestAction()
						+ "], data params:[";
		}
		
//		// set ActionVerify
//		entity.addPart("verify", new StringBody(MD5.getMd5(getRequestAction() + "qzys"), CHARSET));
//		// set Action
//		entity.addPart("action", new StringBody(getRequestAction(), CHARSET));
		// data
		Gson gson = new Gson();
		String data = gson.toJson(this);
		// data
		if (printRequst) {
			printStr += data;
		}
		JSONObject dataObj = null;
		try {
			dataObj = new JSONObject(data);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 需要传递userId
		if(postUserId() != UNABLE_POST) {
			try {
				String value = userId!=null?userId:"";
				dataObj.put(KEY_USER_ID, value);
				if(printRequst) printStr += KEY_USER_ID + ":" + value + ", ";
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		entity.addPart("data", new StringBody(dataObj.toString(), CHARSET));
		
		// 附件
		HashMap<String, String> fileEncode = getFileEncode();
		if (fileEncode != null) {
			Iterator<String> iterator = fileEncode.keySet().iterator();
			int size = 0;
			while (iterator.hasNext()) {
				String key = iterator.next();
				String file_path = fileEncode.get(key);
				FileBody fileBody = new FileBody(new File(file_path));
				entity.addPart(key, fileBody);
				if(printRequst) printStr += "], " + key + ":[" + file_path;
				size++;
			}
			if(size > 1) {
				entity.addPart("num", new StringBody(String.valueOf(size), CHARSET));
				if(printRequst) printStr +=  "], num:[" + size;
			}
		}
//		if (Logger.logLevel == Log.VERBOSE) {
//		Log.i(TAG, "response:" + result);
//		}
		
		return entity;
	}
	
	/** 不需要加入post **/
	public static final int UNABLE_POST = 0;
	/** 必须加入post **/
	public static final int FORCE_POST = 1;
	/** 可加可不加入post **/
	public static final int ENABLE_POST = 2;
	
	/** 用户ID传送标志</br> 见UNABLE_POST, FORCE_POST, ENABLE_POST **/
	public abstract int postUserId();

	/** 是否有附件上传 **/
	public abstract boolean postFile();

	/** 对附件进行编码 **/
	public abstract HashMap<String, String> getFileEncode();
	
	public int getRequestMark() {
		return mRequestMark;
	}

	public void setRequestMark(int requestMark) {
		this.mRequestMark = requestMark;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Summary : BaseRequest的子类需实现此方法，返回与http接口对应的Request type</br> Method
	 * expatiate: 返回请求类型
	 */
	public abstract String getRequestUrl();

	/** 请求的action，作为action验证需要 **/
	public abstract String getRequestAction();
	
}
