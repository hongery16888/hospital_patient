package iori.hpatient.modules.simplehttps;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PostHttps {

    private HttpResponse httpResponse = null;// 响应对象
    private HttpEntity httpEntity = null;// 取出响应内容的消息对象
    private InputStream inputStream = null;// 输入流对象

    private String url = "";

    public PostHttps(String url){
        this.url = url;
    }

    public String postHttpsData(){

        StringBuilder result = new StringBuilder();

        NameValuePair nameValuePair = new BasicNameValuePair("content", "test");//键值对
        //然后将键值对放到列表里(类似于形成数组)
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(nameValuePair);//将键值对放入到列表中
        try {
            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(nameValuePairs);//对参数进行编码操作
            //生成一个post请求对象
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(requestHttpEntity);
            //生成一个http客户端对象
            HttpClient httpClient = new DefaultHttpClient();//发送请求
            try {
                httpResponse = httpClient.execute(httpPost);//接收响应
                httpEntity = httpResponse.getEntity();//取出响应
                //客户端收到响应的信息流
                inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";
                while((line = reader.readLine()) != null){
                    result = result.append(line);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally{//关闭输入流
                try{
                    inputStream.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //返回的数据
        return result.toString();
    }
}
