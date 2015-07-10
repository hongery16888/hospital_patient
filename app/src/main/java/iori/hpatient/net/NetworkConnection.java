package iori.hpatient.net;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import iori.hpatient.net.request.BaseRequest;
import iori.hpatient.view.RequestProgressDialog;
import iori.hpatient.view.RequestProgressDialog.OnCancelClickListenr;

public class NetworkConnection implements HttpRequest {

	private static class CustomHandler extends Handler {

		WeakReference<NetworkConnection> weakConnection;

		public CustomHandler(NetworkConnection connection) {

			this.weakConnection = new WeakReference<NetworkConnection>(connection);
		}

		@Override
		public void handleMessage(Message msg) {

			if (weakConnection.get() == null) {
				this.weakConnection = new WeakReference<NetworkConnection>(NetworkConnection.getNetworkConnection());
			}
			NetworkConnection connection = weakConnection.get();
			connection.postResponse((ResponseFilter) msg.obj);
		}

	}
	
	public static final String TAG = "NetworkAPI";
	private static NetworkConnection mNetworkConnection;

	/**
	 * HTTP連結対象
	 */
	private HttpClient customerHttpClient;

	/**
	 * Main thread handler
	 */
	private final CustomHandler mForgroundHandler;

	/**
	 * thread pool
	 */
	private final ExecutorService mThreadPool;

	/**
	 * Response解析対象
	 */
	private DataTransfer mDataTransfer;

	public NetworkConnection() {

		super();
		this.customerHttpClient = this.getHttpClient();
//		this.mThreadPool = Executors.newSingleThreadExecutor();
		this.mThreadPool = Executors.newCachedThreadPool();
		this.mForgroundHandler = new CustomHandler(this);
		this.mDataTransfer = new DataTransfer();
		
	}

	public static NetworkConnection getNetworkConnection() {

		if (null == mNetworkConnection) {
			mNetworkConnection = new NetworkConnection();
		}
		return mNetworkConnection;
	}

	public static void recyleConnection() {
		mNetworkConnection = null;
	}
	
	private synchronized HttpClient getHttpClient() {

		if (null == customerHttpClient) {
			HttpParams params = new BasicHttpParams();
			// 设置一些基本参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams.setHttpElementCharset(params, CHARSET);
			HttpProtocolParams.setUserAgent(params, "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
					+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			// 超时设置
			/* 从连接池中取连接的超时时间 */
			ConnManagerParams.setTimeout(params, TIMEOUT);
			/* 连接超时 */
			HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(params, TIMEOUT);

			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			// schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

			// 使用线程安全的连接管理来创建HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
			customerHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return customerHttpClient;
	}

	private synchronized DataTransfer getDataTransfer() {

		if (null == mDataTransfer) {
			mDataTransfer = new DataTransfer();
		}
		return mDataTransfer;
	}
	
	public Future sendRequestByPost(final BaseRequest baseRequest, final RequestProgressDialog diag, Class<?> cls, NetworkConnectListener listener){
		
		// 发送请求
		RequestThread thread = new RequestThread(baseRequest, diag, cls, listener);
		// mThreadPool.execute(thread);
		return mThreadPool.submit(thread);
	}
	
	class RequestThread extends Thread {
		final BaseRequest baseRequest;
		final Class<?> cls;
		final NetworkConnectListener listener;
		
		boolean isCancel = false;
		public RequestThread(BaseRequest baseRequest, RequestProgressDialog diag, Class<?> cls, NetworkConnectListener listener) {
			this.baseRequest = baseRequest;
			this.cls = cls;
			this.listener = listener;
			if(diag != null) {
				diag.setOnCancelClickListenr(new OnCancelClickListenr() {
					
					@Override
					public void onCancelClick() {
						isCancel = true;
					}
				});
			}
		}
		
		@Override
		public void run() {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			// 返回数据过滤
			ResponseFilter filter = new ResponseFilter(
					ERROR_OTHER, ERRORMSG_OTHER, 
					baseRequest.getRequestAction(),
					baseRequest.getRequestMark(),
					listener);
			HttpResponse res = null;
			try {
				// check&charge userId
				if(baseRequest.postUserId() == BaseRequest.FORCE_POST) {
					String userId= DataTransfer.getUserId();
					if(TextUtils.isEmpty(userId)) {
						filter.setError(ERROR_LOGOUT);
						filter.setData(ERRORMSG_LOGOUT);
						return;
					}else {
						baseRequest.setUserId(userId);
					}
				}else if(baseRequest.postUserId() == BaseRequest.ENABLE_POST) {
					baseRequest.setUserId(DataTransfer.getUserId());
				}
				
				// 请求地址
				HttpPost postMethod = new HttpPost(baseRequest.getRequestUrl());
				// 设置请求数据
				if(baseRequest.postFile()) {
					MultipartEntity entity = baseRequest.getMultiEntity(false);
					postMethod.setEntity(entity);
				}else {
					List<NameValuePair> para = baseRequest.postEncode();
					postMethod.setEntity(new UrlEncodedFormEntity(para, CHARSET));
					// 打印request
					printRequest(baseRequest.getRequestUrl(), para);
				}
				

				HttpEntity resEntity = null;
				String result = null;
				// 执行网络访问
				res = getHttpClient().execute(postMethod);
				if(!isCancel){
					// 获取返回结果
					resEntity = res.getEntity();
					// 转码
					result = (resEntity == null) ? "" : EntityUtils.toString(resEntity, CHARSET);
//					try{
//						logger.i(TAG, "Response: " + result);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
					// 设置返回数据过滤
					filter = getDataTransfer().handleResponse(result, baseRequest.getRequestAction(),
									baseRequest.getRequestMark(), cls, listener);
				}
			
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				filter.setError(ERROR_DATA_TRANSFORM);
				filter.setData(ERRORMSG_DATA_TRANSFORM);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				filter.setError(ERROR_DATA_TRANSFORM);
				filter.setData(ERRORMSG_DATA_TRANSFORM);
			}catch(ConnectTimeoutException e) {
				e.printStackTrace();
				filter.setError(ERROR_CONNECT_TIMEOUT);
				filter.setData(ERRORMSG_CONNECT_TIMEOUT);
			}catch (IllegalAccessException e) {
				e.printStackTrace();
				filter.setError(ERROR_DATA_TRANSFORM);
				filter.setData(ERRORMSG_DATA_TRANSFORM);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				filter.setError(ERROR_CONNECT);
				filter.setData(ERRORMSG_CONNECT);
			} catch (IOException e) {
				e.printStackTrace();
				filter.setError(ERROR_CONNECT);
				filter.setData(ERRORMSG_CONNECT);
			} catch (JSONException e) {
				e.printStackTrace();
				filter.setError(ERROR_DATA_TRANSFORM);
				filter.setData(ERRORMSG_DATA_TRANSFORM);
			}finally {
				if(!isCancel) {
					// 若该请求没有被撤销
					// 回调
					mForgroundHandler.sendMessage(mForgroundHandler.obtainMessage(0, filter));
				}
				// getHttpClient().getConnectionManager().shutdown();
				// try {
				// if (res != null) {
				// // res.getEntity().getContent().close();
				// }
				// } catch (IllegalStateException e) {
				// e.printStackTrace();
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
		}
	}
	
	private void printRequest(String uri, List<NameValuePair> parameters) {
		
		String str = "Request URI:[" + uri +"], params:[";
		for(NameValuePair para:parameters) {
			str += para.getName() +":"+para.getValue()+", ";
		}
		str += "]";
        Log.i(TAG, "printRequest : " + str);
	}

	private void postResponse(ResponseFilter filter) {

		try {
			NetworkConnectListener listener = filter.getListener();
			if(listener != null) {
				if (filter.getError() == ERROR_NONE) {
					listener.onRequestSucceed(filter.getData(), filter.getRequestType(), filter.getRequestMark());
				} else {
					listener.onRequestFailure(filter.getError(), (String) filter.getData(), filter.getRequestType(),
							filter.getRequestMark());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
}
