package iori.hpatient.modules.update.manager;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iori.hpatient.modules.common.BaseManager;
import iori.hpatient.modules.common.RequestException;
import iori.hpatient.modules.common.ServerCode;
import iori.hpatient.modules.update.entity.UpdateEntity;

public class UpdateManager extends BaseManager {

    String url = "";

    public UpdateManager(String url) {
        this.url = url;
    }

    public UpdateEntity getSomething() {

        return new UpdateEntity();
    }

    /*如果有新版本会返回新版本信息，客户端无需比较版本*/
    public UpdateEntity checkUpdate(int versionCode) {
        HashMap<String, String> map = null;

        UpdateEntity result = new UpdateEntity();
        HttpPost req = new HttpPost(url);//将要请求的升级地址通过构造方法传入HttpPost对象

        //Post运作传送变数必须用NameValuePair[]阵列储存     ,传参数 服务端获取的方法为request.getParameter("name")
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("action", "USER.UPDATE"));
        param.add(new BasicNameValuePair("versionCode", String.valueOf(versionCode)));

        HttpEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(param, HTTP.UTF_8);//发送http请求
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //发出HTTP request
        req.setEntity(entity);
        String res = null;
        try {
            res = postRequest(req);
        } catch (IOException e) {
            result.setResult(ServerCode.FAILURE);
            e.printStackTrace();
        } catch (RequestException e) {
            result.setResult(e.getStatusCode());
            e.printStackTrace();
        }
	        
	        /* JSON格式
        	{"response":{"data":{"versionContent":null,"versionDesc":" 更新内容的描述  ",
        						"versionCode":"100", 最新的版本号
        						"versionId":"ff8080813f60c0b5013f644da05d0002", 更新的ID
        						"versionState":null, 更新的状态
        						"versionType":"App", 程序的格式
        						"versionName":"XXX_160.apk", 更新APK的名字
        						"forceUpdate":1,     是否强行更新
        						"versionPath":"http:\/\/61.50.159.196:2501\/MTS\/ver\/app\/XXX_160.apk"   更新的路径
        						}
        				}
        				,"result":100,
        				"errorInfo":null
        	}

	        */
        if (res != null) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(res);
                result.setResult(jsonObject.getJSONObject("response").getInt("result"));
                if (result.getResult() == ServerCode.SUCCESS) {
                    String data = jsonObject.getJSONObject("response").getString(
                            "data");
                    JSONObject jsonRoot = new JSONObject(data);
                    map = new HashMap<String, String>();
                    map.put("versionCode", jsonRoot.getString("versionCode"));
                    map.put("versionPath", jsonRoot.getString("versionPath"));
                    map.put("versionName", jsonRoot.getString("versionName"));
                    map.put("forceUpdate", jsonRoot.getString("forceUpdate"));
                    map.put("versionDesc", jsonRoot.getString("versionDesc"));


                    result.setMap(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                result.setResult(ServerCode.FAILURE);
            }
        }
        return result;
    }

}
