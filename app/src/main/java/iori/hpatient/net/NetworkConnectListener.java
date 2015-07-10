/*
 * Copyright © 2009-2012 Chengdu Tianfu Software Park Co., Ltd.</br>
 * Company: Chengdu Tianfu Software Park Co., Ltd.</br>
 * Project: CHLogistics
 * Package: com.tfsp.mobile.chlogistics.api
 * Version: 1.0</br>
 * Android SDK: min sdk level: 7</br>
 * Create Time: 2012-12-3</br>
 */
package iori.hpatient.net;

/**
 * NetworkConnectListener summary: 数据请求返回值监听类；
 * 
 * Description: 网络连接回调，实现onRequestSucceed,onRequestFailure两个方法</br>
 */
public interface NetworkConnectListener {

	/**
	 * Summary : 请求成功的回调 </br>
	 * 
	 * @param data 返回数据，具体数据格式见{@link NetworkAPI}各接口的说明
	 * @param requestType 请求类型
	 * @param requestMark 请求次数
	 * @return void </br>
	 * @throws </br>
	 */
	public abstract void onRequestSucceed(Object data, String requestAction, int requestMark);

	/**
	 * Summary : 请求失败的回调</br>
	 * 
	 * @param error 错误类型,具体类型见{@link Constants.HttpRequest}, 以及接口文档的errorId
	 * @param requestType 请求类型
	 * @param requestMark 请求次数
	 * @return void </br>
	 * @throws </br>
	 */
	public abstract void onRequestFailure(int error, String errorMsg, String requestAction, int requestMark);
}

