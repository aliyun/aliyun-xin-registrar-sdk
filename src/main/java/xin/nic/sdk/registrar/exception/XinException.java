/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.exception;

/**
 * 类 XinException.java的实现描述：XinException
 *
 * @author shaopeng.wei 2015-06-02 09:51
 */
public class XinException extends RuntimeException {
    private static final long serialVersionUID = 4672270715795106951L;

    private String            errCode;

    public XinException(String errMsg) {
        super(errMsg);
    }

    public XinException(String errMsg, Throwable throwable) {
        super(errMsg, throwable);
    }

    public XinException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
    }

    public String getErrCode() {
        return errCode;
    }
}
