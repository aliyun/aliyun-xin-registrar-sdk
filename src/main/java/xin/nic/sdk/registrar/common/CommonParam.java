/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.common;

import java.io.Serializable;

/**
 * 类 CommonParam.java的实现描述：接口公共参数
 *
 * @author shaopeng.wei 2015-04-17 14:08
 */
public class CommonParam implements Serializable {
    private static final long  serialVersionUID     = -169944912739849107L;

    public static final String FIELD_APPKEY         = "appKey";
    public static final String FIELD_TIMESTAMP      = "timestamp";
    public static final String FIELD_SIGNATURENONCE = "signatureNonce";
    public static final String FIELD_SIGNATURE      = "signature";

    private String             appKey;
    private long               timestamp;
    private String             signature;
    private String             signatureNonce;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getSignatureNonce() {
        return signatureNonce;
    }

    public void setSignatureNonce(String signatureNonce) {
        this.signatureNonce = signatureNonce;
    }

}
