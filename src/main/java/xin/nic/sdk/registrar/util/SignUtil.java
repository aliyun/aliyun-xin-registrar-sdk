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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import xin.nic.sdk.registrar.common.CommonParam;

/**
 * 类 SignUtil.java的实现描述：计算签名
 *
 * @author shaopeng.wei 2015-04-17 14:08
 */
public class SignUtil {

    private static final String FIELD_DOMAIN = "domain";
    private static final String encode       = "UTF-8";
    private static final String ALGORITHM    = "HmacSHA1";
    private static final String EMPTY        = "";

    /**
     * 对授权URL参数进行签名
     * 
     * @param appKey appKey
     * @param appSecret appSecret
     * @param timestamp 时间戳
     * @param signatureNonce 随机码，建议采用UUID
     * @param domain 域名
     * @return 签名
     */
    public static String signVerifyUrl(String appKey, String appSecret, long timestamp, String signatureNonce,
                                       String domain) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(FIELD_DOMAIN, domain);
        return sign(appKey, appSecret, timestamp, signatureNonce, map);
    }

    /**
     * 计算签名
     * 
     * @param appKey appKey
     * @param appSecret appSecret
     * @param timestamp timestamp
     * @param signatureNonce signatureNonce
     * @param param param
     * @return 签名
     */
    public static String sign(String appKey, String appSecret, long timestamp, String signatureNonce,
                              Map<String, String> param) {
        // 准备参数
        param = new HashMap<String, String>(param);

        // 加入公共参数
        param.put(CommonParam.FIELD_APPKEY, appKey);
        param.put(CommonParam.FIELD_TIMESTAMP, String.valueOf(timestamp));
        param.put(CommonParam.FIELD_SIGNATURENONCE, signatureNonce);

        // 计算签名
        try {
            return computeSignature(appSecret, param);
        } catch (Exception e) {
            // logger.error("sign error.", e);
            return EMPTY;
        }
    }

    private static String computeSignature(String appSecret, Map<String, String> parameters) throws Exception {
        // 将参数Key按字典顺序排序
        Set<String> keys = parameters.keySet();
        String[] sortedKeys = keys.toArray(new String[keys.size()]);
        Arrays.sort(sortedKeys);

        // 生成请求字符串
        StringBuilder queryString = new StringBuilder();
        for (String key : sortedKeys) {
            queryString.append(String.format("&%s=%s", key, percentEncode(parameters.get(key))));
        }

        // 计算签名
        return calculateSignature(appSecret, queryString.toString().substring(1));
    }

    private static String percentEncode(String value) throws UnsupportedEncodingException {
        return value == null ? EMPTY : URLEncoder.encode(value, encode);
    }

    private static String calculateSignature(String key, String stringToSign) throws Exception {
        // 使用HmacSHA1算法计算HMAC值
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(new SecretKeySpec(key.getBytes(encode), ALGORITHM));
        byte[] signData = mac.doFinal(stringToSign.getBytes(encode));
        return new String(Base64Util.encode(signData));
    }
}
