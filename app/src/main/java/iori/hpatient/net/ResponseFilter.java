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


/**
 * ResponseFilter summary: 网络请求返回数据过滤类；
 * 
 * @author Wang hongyun</br>
 * Description: 对数据返回值进行处理，如请求成功、失败信息等；</br>
 * Create Time: 2012-12-20 下午3:35:15</br>
 * </br>History：</br>
 * Editor **** Time **** Mantis No **** Operation **** Description ***
 *** 
 *** 
 *** 
 *** 
 */
public class ResponseFilter {

	private int error;

	private final String requestType;

	private final int requestMark;
	
	private NetworkConnectListener listener;

	private Object data;

	public ResponseFilter(int error, Object data, String requestType, int requestMark, NetworkConnectListener listener) {

		this.requestType = requestType;
		this.requestMark = requestMark;
		this.error = error;
		this.data = data;
		this.listener = listener;
	}

	public int getError() {

		return error;
	}

	public String getRequestType() {

		return requestType;
	}

	public int getRequestMark() {

		return requestMark;
	}

	public Object getData() {

		return data;
	}

	public void setError(int error) {

		this.error = error;
	}

	public void setData(Object data) {

		this.data = data;
	}

	public NetworkConnectListener getListener() {
		return listener;
	}

	public void setListener(NetworkConnectListener listener) {
		this.listener = listener;
	}

	@Override
	public String toString() {
		return "ResponseFilter [error=" + error + ", requestType="
				+ requestType + ", requestMark=" + requestMark + ", data="
				+ data + "]";
	}
	
	

}
