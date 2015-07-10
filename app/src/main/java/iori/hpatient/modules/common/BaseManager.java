package iori.hpatient.modules.common;


import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class BaseManager {

    public final static int L_DEBUG = 0;
    public final static int L_INFO = 1;
    public final static int L_WARN = 2;
    public final static int L_ERROR = 3;

    protected HttpClient client = null;


    protected String endpoint = "";

    public static void DEBUG(String tag, String log) {
        logFilter(L_DEBUG, tag, log);
    }

    public static void INFO(String tag, String log) {
        logFilter(L_INFO, tag, log);
    }

    public static void WARN(String tag, String log) {
        logFilter(L_WARN, tag, log);
    }

    public static void ERROR(String tag, String log) {
        logFilter(L_ERROR, tag, log);
    }

    private static void logFilter(int level, String tag, String log) {
        log(level, tag, log);
    }

    private static void log(int level, String tag, String log) {
        if(log==null)
            log = "null";
        switch (level) {
            case L_DEBUG:
                Log.d(tag, log);
                break;
            case L_INFO:
                Log.i(tag, log);
                break;
            case L_WARN:
                Log.w(tag, log);
                break;
            case L_ERROR:
                Log.e(tag, log);
                break;
        }
    }

    public String getUrlContent(String url) {
        HttpGet req = new HttpGet(url);
        INFO("mts sync :","--"+url);
        try {
            HttpResponse response = getServerResponse(req);
            if (response.getStatusLine().getStatusCode() != 200) {
                return "-1";
            } else {
                StringBuilder result = new StringBuilder();
                BufferedReader reader = null;
                String encode = "UTF-8";
                INFO("mts url:",url);
                for(Header header:response.getAllHeaders()){
                    INFO("mts head",header.getName()+"----"+header.getValue());
                }
                if (response.getFirstHeader("Content-Type") != null) {
                    String contentType = response.getFirstHeader("Content-Type").getValue();
                    if(contentType!=null){
                        String[] e = contentType.split(";");
                        if(e.length>0){
                            INFO("mts sync encode:",e[0]);
                            for(String h:e){
                                if(h.indexOf("charset")>-1){
                                    encode = h.split("=")[1];

                                    break;
                                }
                            }
                        }
                    }

                }
                InputStream is = null;
                if (response.getHeaders("Content-Encoding") != null && response.getHeaders("Content-Encoding").length > 0 &&
                        response.getHeaders("Content-Encoding")[0].getValue().equalsIgnoreCase("gzip")) {
                    GZIPInputStream gzip = new GZIPInputStream(response.getEntity().getContent());
                    is = gzip;
                } else {
                    is = response.getEntity().getContent();

                }
                INFO("mts sync encode k:",encode);
                reader = new BufferedReader(new InputStreamReader(is, encode));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\r\n");
                }
                reader.close();

                return result.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "-1";
    }

    public String postRequest(HttpRequestBase method) throws IOException, RequestException {
        return postRequest(method, "UTF-8");
    }

    public String postRequest(HttpRequestBase method, String encode) throws IOException, RequestException {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;

        reader = new BufferedReader(new InputStreamReader(getRequestStream(method), encode));
        String line = "";
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();

        return result.toString();
    }

    public InputStream getRequestStream(HttpRequestBase method) throws IOException, RequestException {


        HttpResponse response = getServerResponse(method);

        if (response.getStatusLine().getStatusCode() != 200)
            throw new RequestException(response.getStatusLine().getStatusCode());
        method = null;
        client = null;
        if (response.getHeaders("Content-Encoding") != null && response.getHeaders("Content-Encoding").length > 0 &&
                response.getHeaders("Content-Encoding")[0].getValue().equalsIgnoreCase("gzip")) {
            GZIPInputStream gzip = new GZIPInputStream(response.getEntity().getContent());
            return gzip;
        } else {
            return response.getEntity().getContent();

        }
    }

    public HttpResponse getServerResponse(HttpRequestBase method) throws IOException {
        method.addHeader("Accept-Encoding", "gzip");
        method.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
        method.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");

        client = createHttpClient(method.getURI().getScheme().startsWith("https"));

        return client.execute(method);


    }

    private HttpClient createHttpClient(boolean ssl) {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 30000);
        HttpConnectionParams.setSoTimeout(params, 30000);


        if (!ssl) {
            DefaultHttpClient client = new DefaultHttpClient(params);
            client.setHttpRequestRetryHandler(myRetryHandler);
            return client;
        }

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8081));
            registry.register(new Scheme("https", sf, 8443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            DefaultHttpClient client = new DefaultHttpClient(ccm, params);
            client.setHttpRequestRetryHandler(myRetryHandler);
            return client;
        } catch (Exception e) {
            return new DefaultHttpClient(params);
        }
    }

    public class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, new SecureRandom());

        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }


    private HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler()

    {


        @Override

        public boolean retryRequest(IOException exception, int executionCount, HttpContext context)

        {

            ERROR("http request", "err count:" + executionCount + "   " + exception.toString());
            if (executionCount >= 3)

                return false;

            if (exception instanceof NoHttpResponseException)

                return true;

            if (exception instanceof SocketTimeoutException)

                return true;

            if (exception instanceof UnknownHostException)

                return true;

            HttpRequest request = (HttpRequest) context.getAttribute(ExecutionContext.HTTP_REQUEST);
            boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);

            if (idempotent)

                return true;


            return false;


        }


    };


    final static char[] digits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P',
            'R', 'S', 'T', 'U', 'W', 'X', 'Y', 'Z'};


    public static String numericToString(long i, int system) {
        long num = 0;
        if (i < 0) {
            num = ((long) 2 * 0x7fffffff) + i + 2;
        } else {
            num = i;
        }
        char[] buf = new char[32];
        int charPos = 32;
        while ((num / system) > 0) {
            buf[--charPos] = digits[(int) (num % system)];
            num /= system;
        }
        buf[--charPos] = digits[(int) (num % system)];
        return new String(buf, charPos, (32 - charPos));
    }

    protected synchronized String getRndId() {
        String nano = String.valueOf(System.nanoTime());
        long id = System.currentTimeMillis() * 1000 + Long.parseLong(nano.substring(nano.length() - 6, nano.length() - 3));
        return Long.toString(id, 32).toUpperCase().replaceAll("I", "X").replaceAll("J", "Y").replaceAll("O", "Z");
    }

}