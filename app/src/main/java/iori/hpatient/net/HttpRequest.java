package iori.hpatient.net;

public interface HttpRequest {

	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String CHARSET = CHARSET_UTF8;
	public static final int TIMEOUT = 30000;
	// ///////////////////////////////////////////////////////////
	// Request error
	// ///////////////////////////////////////////////////////////
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
	
	/** 请求类型GET **/
	public static final int GET = 0;
	/** 请求类型POST **/
	public static final int POST = 1;

	/** 成功 **/
	public static final int ERROR_NONE = 0;

	// ///////////////////////////////////////////////////////////
	// Request action & URI
	// ///////////////////////////////////////////////////////////

	// 开发环境
//	public static final String BASIC_PATH = "http://www.qingzaoyisheng.com/webservice/";
//	public static final String PATH_NORMAL = BASIC_PATH + "im";

    public static final String BASIC_PATH = "http://120.26.116.12:9090/";
    public static final String PATH_NORMAL =  "etc_web";
    public static final String PATH_CHECK_VERSION = BASIC_PATH + "etc_web/json.html";
    public static final String CHECK_VERSION =  "check_version";


	// ////////////////
	public static final String TEST_PATH = "webserviceTest";
	
	/** 06、检查更新 **/
//	public static final String CHECK_VERSION = "checkVersion";


	// ///////////////////////////////////////////////////////////
	// 取数据需要的key
	// ///////////////////////////////////////////////////////////
	/** result **/
	public static final String KEY_RESULT = "Result";
	/** action **/
	public static final String KEY_ACTION = "Action";
	/** error id **/
	public static final String KEY_ERROR_ID = "ErrorId";
	/** error message **/
	public static final String KEY_ERROR_MSG = "ErrorMsg";
	/** data **/
	public static final String KEY_DATA = "Data";

	// upload
	/** upload result **/
	public static final String KEY_SUCCEED = "Succeed";
	/** upload message id **/
	public static final String KEY_MESSAGE = "Message";
	/** upload return value **/
	public static final String KEY_RETURN_VALUE = "ReturnValue";

}
