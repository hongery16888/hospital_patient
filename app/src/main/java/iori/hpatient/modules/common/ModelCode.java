package iori.hpatient.modules.common;

public class ModelCode {

	/** 网络连接错误 **/
	public static final int ERROR_OTHER = -1;
	public static final String ERRORMSG_OTHER = "其他错误";
	/** 网络连接错误 **/
	public static final int ERROR_CONNECT = 101;
	public static final String ERRORMSG_CONNECT = "网络连接错误";
	/** 网络连接超时 **/
	public static final int ERROR_CONNECT_TIMEOUT = 102;
	public static final String ERRORMSG_CONNECT_TIMEOUT = "网络连接超时";
	/** 数据解析错误 **/
	public static final int ERROR_DATA_TRANSFORM = 103;
	public static final String ERRORMSG_DATA_TRANSFORM = "数据解析错误";
	/** 未登陆 **/
	public static final int ERROR_LOGOUT = 105;
	public static final String ERRORMSG_LOGOUT = "您当前尚未登录";
	/** 上传附件错误 **/
	public static final int ERROR_UPLOAD = 106;
	public static final String ERRORMSG_UPLOAD = "上传附件失败";
	
}
