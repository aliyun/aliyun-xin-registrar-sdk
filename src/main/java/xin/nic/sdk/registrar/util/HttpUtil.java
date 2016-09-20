/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import xin.nic.sdk.registrar.exception.XinException;

/**
 * 类 HttpUtil.java的实现描述：使用HttpURLConnection封装了http调用，因业务场景，暂时只封装了GET
 * 
 * @author shaopeng.wei 2015-06-10 17:25
 */
public class HttpUtil {
    private static final String PROTOCOL_HTTP  = "http";
    private static final String PROTOCOL_HTTPS = "https";

    /** 创建连接超时时间，默认5s. */
    private static int          connTimeout    = 5000;
    /** 数据读取超时时间，默认60s. */
    private static int          readTimeout    = 60000;
    /** 编码格式，默认编码：UTF-8 . */
    private static String       charset        = "UTF-8";

    /**
     * 发起get请求，做了http与https的差异处理
     * 
     * @param url
     * @param params
     * @return
     */
    public static HttpResp doGet(String url, Map<String, String> params) {

        // 如果有参数，就拼接参数
        if (params != null) {
            url = UrlUtil.generateGetUrl(url, params);
        }

        try {
            // 分析URL
            URL urlParse = new URL(url);

            // 判断协议是否为http或https
            String protocol = urlParse.getProtocol();
            if (!PROTOCOL_HTTP.equalsIgnoreCase(protocol) && !PROTOCOL_HTTPS.equals(protocol)) {
                throw new XinException("xin.error.url", "仅支持http和https请求");
            }

            if (PROTOCOL_HTTP.equalsIgnoreCase(protocol)) {
                return doHttpGet(urlParse);
            }

            if (PROTOCOL_HTTPS.equals(protocol)) {
                return doHttpsGet(urlParse);
            }
        } catch (MalformedURLException e) {
            throw new XinException("xin.error.url", "url:" + url + "错误, 请检查url是否正确");
        }

        return null;

    }

    /**
     * 发送HTTP GET请求
     * 
     * @param url URL
     * @return 请求结果
     */
    public static HttpResp doHttpGet(URL url) {

        HttpURLConnection conn = null;
        InputStream inputStream = null;
        Reader reader = null;

        try {
            // 判断协议是否为http或https
            String protocol = url.getProtocol();
            if (!PROTOCOL_HTTP.equals(protocol)) {
                throw new XinException("xin.error.url", "仅支持http请求");
            }

            // 打开连接
            conn = (HttpURLConnection) url.openConnection();

            // 设置连接参数
            conn.setConnectTimeout(connTimeout);
            conn.setReadTimeout(readTimeout);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // UserAgent
            conn.setRequestProperty("User-Agent", "java-sdk");

            // 连接服务器
            conn.connect();

            // 读取
            inputStream = conn.getInputStream();
            reader = new InputStreamReader(inputStream, charset);
            BufferedReader bufferReader = new BufferedReader(reader);
            StringBuilder stringBuilder = new StringBuilder();
            String inputLine = "";
            while ((inputLine = bufferReader.readLine()) != null) {
                stringBuilder.append(inputLine);
                stringBuilder.append("\n");
            }

            // 创建返回值
            HttpResp resp = new HttpResp();
            resp.setStatusCode(conn.getResponseCode());
            resp.setStatusPhrase(conn.getResponseMessage());
            resp.setContent(stringBuilder.toString());

            // 返回
            return resp;
        } catch (MalformedURLException e) {
            throw new XinException("xin.error.url", "url:" + url + "错误, 请检查url是否正确");
        } catch (IOException e) {
            throw new XinException("xin.error.http", String.format("IOException:%s", e.getMessage()));
        } finally {

            if (reader != null) {
                try {
                    reader.close();

                } catch (IOException e) {
                    throw new XinException("xin.error.url", "url:" + url + "错误, 关闭reader错误");
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new XinException("xin.error.url", "url:" + url + "错误, 关闭输入流错误");
                }
            }

            // 关闭连接
            quietClose(conn);
        }
    }

    /**
     * 发送HTTPS GET请求
     * 
     * @param url URL
     * @return 请求结果
     */
    public static HttpResp doHttpsGet(URL url) {

        HttpsURLConnection conn = null;
        InputStream inputStream = null;
        Reader reader = null;

        try {
            // 判断协议是否为http或https
            String protocol = url.getProtocol();
            if (!PROTOCOL_HTTPS.equals(protocol)) {
                throw new XinException("xin.error.url", "仅支持https请求");
            }

            // 打开连接
            conn = (HttpsURLConnection) url.openConnection();

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, tmArr, new SecureRandom());

            conn.setSSLSocketFactory(sc.getSocketFactory());

            // 设置连接参数
            conn.setConnectTimeout(connTimeout);
            conn.setReadTimeout(readTimeout);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // UserAgent
            conn.setRequestProperty("User-Agent", "java-sdk");

            // 连接服务器
            conn.connect();

            // 读取
            inputStream = conn.getInputStream();
            reader = new InputStreamReader(inputStream, charset);
            BufferedReader bufferReader = new BufferedReader(reader);
            StringBuilder stringBuilder = new StringBuilder();
            String inputLine = "";
            while ((inputLine = bufferReader.readLine()) != null) {
                stringBuilder.append(inputLine);
                stringBuilder.append("\n");
            }

            // 创建返回值
            HttpResp resp = new HttpResp();
            resp.setStatusCode(conn.getResponseCode());
            resp.setStatusPhrase(conn.getResponseMessage());
            resp.setContent(stringBuilder.toString());

            // 返回
            return resp;
        } catch (MalformedURLException e) {
            throw new XinException("xin.error.url", "url:" + url + "错误, 请检查url是否正确");
        } catch (IOException e) {
            throw new XinException("xin.error.http", String.format("IOException:%s", e.getMessage()));
        } catch (KeyManagementException e) {
            throw new XinException("xin.error.url", "url:" + url + "错误, 请检查url是否正确");
        } catch (NoSuchAlgorithmException e) {
            throw new XinException("xin.error.url", "url:" + url + "错误, 请检查url是否正确");
        } finally {

            if (reader != null) {
                try {
                    reader.close();

                } catch (IOException e) {
                    throw new XinException("xin.error.url", "url:" + url + "错误, 关闭reader错误");
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new XinException("xin.error.url", "url:" + url + "错误, 关闭输入流错误");
                }
            }

            // 关闭连接
            quietClose(conn);
        }
    }

    private static PoolingHttpClientConnectionManager cm         = new PoolingHttpClientConnectionManager();

    private static CloseableHttpClient                httpClient = null;

    private static TrustManager[]                     tmArr      = { new X509TrustManager() {
                                                             @Override
                                                             public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate,
                                                                                            String paramString)
                                                                     throws CertificateException {
                                                             }

                                                             @Override
                                                             public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate,
                                                                                            String paramString)
                                                                     throws CertificateException {
                                                             }

                                                             @Override
                                                             public X509Certificate[] getAcceptedIssuers() {
                                                                 return null;
                                                             }
                                                         } };

    static {
        cm.setMaxTotal(500);
        cm.setDefaultMaxPerRoute(200);
        httpClient = HttpClients.custom().setConnectionManager(cm).build();

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    synchronized (this) {
                        try {
                            wait(5000);
                        } catch (InterruptedException e) {
                        }
                        cm.closeExpiredConnections();
                        cm.closeIdleConnections(60, TimeUnit.SECONDS);
                    }
                }
            }
        }).start();
    }

    public static String doPost(String requestUrl, Map<String, String> paramsMap, Map<String, InputStream> files)
            throws Exception {

        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(500).setSocketTimeout(20000)
                .setConnectTimeout(20000).build();

        // 构造请求参数
        MultipartEntityBuilder mbuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .setCharset(Charset.forName(charset));

        if (paramsMap != null) {
            Set<Entry<String, String>> paramsSet = paramsMap.entrySet();
            for (Entry<String, String> entry : paramsSet) {
                mbuilder.addTextBody(entry.getKey(), entry.getValue(), ContentType.create("text/plain", charset));
            }
        }

        if (files != null) {
            Set<Entry<String, InputStream>> filesSet = files.entrySet();
            for (Entry<String, InputStream> entry : filesSet) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                InputStream is = entry.getValue();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) > 0) {
                    os.write(buffer, 0, len);
                }
                os.close();
                is.close();
                mbuilder.addBinaryBody("attachment", os.toByteArray(), ContentType.APPLICATION_OCTET_STREAM,
                        entry.getKey());
            }
        }

        HttpPost httpPost = new HttpPost(requestUrl);
        httpPost.setConfig(requestConfig);

        HttpEntity httpEntity = mbuilder.build();

        httpPost.setEntity(httpEntity);

        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            @Override
            public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= HttpStatus.SC_OK && status < HttpStatus.SC_MULTIPLE_CHOICES) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }

        };

        return httpClient.execute(httpPost, responseHandler);
    }

    /**
     * 获取Entity中数据
     * 
     * @param httpEntity
     * @return
     * @throws Exception
     */
    public static byte[] getData(HttpEntity httpEntity) throws Exception {
        BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(httpEntity);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bufferedHttpEntity.writeTo(byteArrayOutputStream);
        byte[] responseBytes = byteArrayOutputStream.toByteArray();
        return responseBytes;
    }

    /**
     * 将字节数组转换成字符串
     * 
     * @param bytes
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String bytesToString(byte[] bytes) throws UnsupportedEncodingException {
        if (bytes != null) {
            String returnStr = new String(bytes, "utf-8");
            returnStr = StringUtils.trim(returnStr);
            return returnStr;
        }
        return null;
    }

    /**
     * 静默关闭连接
     * 
     * @param conn 连接
     */
    private static void quietClose(HttpURLConnection conn) {
        if (conn != null) {
            try {
                conn.disconnect();
            } catch (Throwable ignore) {

            }
        }
    }

    /**
     * 连接超时时间
     * 
     * @return connTimeout
     */
    public static int getConnTimeout() {
        return connTimeout;
    }

    /**
     * 设置连接超时时间，单位毫秒
     * 
     * @param connTimeout connTimeout
     */
    public static void setConnTimeout(int connTimeout) {
        if (connTimeout <= 0) {
            throw new IllegalArgumentException("timeouts can\'t be negative or zero");
        }

        HttpUtil.connTimeout = connTimeout;
    }

    /**
     * 读取数据超时时间，单位毫秒
     * 
     * @return readTimeout
     */
    public static int getReadTimeout() {
        return readTimeout;
    }

    /**
     * 设置读取超时时间
     * 
     * @param readTimeout readTimeout
     */
    public static void setReadTimeout(int readTimeout) {
        if (readTimeout <= 0) {
            throw new IllegalArgumentException("timeouts can\'t be negative or zero");
        }

        HttpUtil.readTimeout = readTimeout;
    }

    /**
     * 发送GET请求
     * 
     * @param url URL
     * @return 请求结果
     */
    public static HttpResp doGet(String url) {
        return doGet(url, null);
    }

    /**
     * 获取编码
     * 
     * @return charset
     */
    public static String getCharset() {
        return charset;
    }

    /**
     * 设置默认编码
     * 
     * @param charset charset
     */
    public static void setCharset(String charset) {
        if (charset == null || charset.trim().length() == 0) {
            throw new IllegalArgumentException("charset cannot be empty");
        }

        HttpUtil.charset = charset;
    }

    public static class HttpResp implements Serializable {
        private static final long serialVersionUID = 5805287033074616466L;

        private int               statusCode;
        private String            statusPhrase;
        private String            content;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public String getStatusPhrase() {
            return statusPhrase;
        }

        public void setStatusPhrase(String statusPhrase) {
            this.statusPhrase = statusPhrase;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
