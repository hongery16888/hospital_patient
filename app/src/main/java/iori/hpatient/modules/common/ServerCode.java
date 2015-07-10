package iori.hpatient.modules.common;

/**
 * User: jiangfayou
 * Date: 11-11-30
 * Time: 下午2:30
 */
public class ServerCode {
    public static final int SUCCESS = 100;
    public static final int FAILURE = 101;

    public static final int ERR_PARAMS = 102;   //缺少参数
    public static final int ERR_TOKEN = 103;   //token无效
    public static final int NO_DATA = 104;   //没有匹配结果

    public static final int NO_NET = 111;   //无网络

    public static final int ERR_ACCOUNT = 201;   //账号或密码错误
    public static final int ACCOUNT_STOP = 202;   //账号停用
    public static final int ERR_OLDPWD = 203;    //原密码错误
    public static final int ERR_ACCOUNT_EQUIP = 204;    //账号设备匹配错误

    public static final int ERR_FILE = 301;   //文件格式错误
    public static final int ERR_ZIP = 302;   //ZIP格式错误
    public static final int ORDER_EXISTS = 303;   //订单重复
}
