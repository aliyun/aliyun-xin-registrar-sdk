/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 类 UrlUtil.java的实现描述：UrlUtil
 * 
 * @author shaopeng.wei 2015-06-02 11:17
 */
public class UrlUtil {

    private static final String EMPTY           = "";
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 以UTF-8字符集进行URL编码
     * 
     * @param value 编码前
     * @return 编码后
     */
    public static String urlEncode(String value) {
        return urlEncode(value, DEFAULT_CHARSET);
    }

    /**
     * 以指定字符集进行URL编码
     * 
     * @param value 编码前
     * @param charset 字符集
     * @return 编码后
     */
    public static String urlEncode(String value, String charset) {
        try {
            return value == null ? EMPTY : URLEncoder.encode(value, charset);
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    /**
     * 生成GET请求的URL
     * 
     * @param baseUrl 请求地址，不带任何参数
     * @param params 请求参数
     * @return 完整URL
     */
    public static String generateGetUrl(String baseUrl, Map<String, String> params) {
        String paramStr = paramsToQueryString(params);
        return baseUrl + "?" + paramStr;
    }

    private static String paramsToQueryString(Map<String, String> params) {
        if (params == null || params.size() == 0) {
            return null;
        }

        StringBuilder paramString = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> p : params.entrySet()) {
            String key = p.getKey();
            String val = p.getValue();

            if (!first) {
                paramString.append("&");
            }

            paramString.append(urlEncode(key));

            if (val != null) {
                paramString.append("=").append(urlEncode(val));
            }

            first = false;
        }
        return paramString.toString();
    }

    /**
     * 去除最后的/
     * 
     * @param url URL
     * @return 去除/后的URL
     */
    private static String removeLastSlash(String url) {
        return StringUtils.removeEndIgnoreCase(url, "/");
    }

    /**
     * 生成GET请求的URL
     * 
     * @param host 请求Host，scheme + domain
     * @param path 请求路径
     * @param params 请求参数
     * @return 完整URL
     */
    public static String generateGetUrl(String host, String path, Map<String, String> params) {
        String baseUrl = UrlUtil.removeLastSlash(host) + path;
        return generateGetUrl(baseUrl, params);
    }

    /**
     * 生成POST请求的URL
     * 
     * @param host
     * @param path
     * @return
     */
    public static String generatePostUrl(String host, String path) {
        return UrlUtil.removeLastSlash(host) + path;
    }
}
