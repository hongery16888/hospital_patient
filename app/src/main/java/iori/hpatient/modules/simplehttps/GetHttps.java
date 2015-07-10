package iori.hpatient.modules.simplehttps;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GetHttps {

    private String url = "";

    private HttpResponse httpResponse = null;// 响应对象
    private HttpEntity httpEntity = null;// 取出响应内容的消息对象
    private InputStream inputStream = null;// 输入流对象

    public GetHttps(String url) {
        this.url = url;
    }

    public String getHttpsData() {

        StringBuilder result = new StringBuilder();

        // 生成一个请求对象
        HttpGet httpGet = new HttpGet(url);
        // 生成一个http客户端对象
        HttpClient httpClient = new DefaultHttpClient();
        // 发送请求
        try {
            httpResponse = httpClient.execute(httpGet);// 接收响应
            httpEntity = httpResponse.getEntity();// 取出响应
            // 客户端收到响应的信息流
            inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));

            String line = "";
            while ((line = reader.readLine()) != null) {
                result = result.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {// 关闭输入流
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //返回的数据
        return result.toString();
    }
}
