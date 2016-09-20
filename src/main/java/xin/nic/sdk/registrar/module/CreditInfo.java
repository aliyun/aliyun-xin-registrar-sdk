/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.module;

import java.io.Serializable;

/**
 * 类 CreditInfo.java的实现描述：信用信息
 *
 * @author shaopeng.wei 2015-05-13 11:41
 */
public class CreditInfo implements Serializable {
    private static final long serialVersionUID = 6297115080734360587L;

    /** 信用类型 */
    private CreditType        creditType;
    /** 认证类型 */
    private AuthType          authType;
    /** 认证机构 */
    private CreditRatingType  creditRatingType;
    /** 信用描述 */
    private String            creditDesc;

    public CreditType getCreditType() {
        return creditType;
    }

    public void setCreditType(CreditType creditType) {
        this.creditType = creditType;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public CreditRatingType getCreditRatingType() {
        return creditRatingType;
    }

    public void setCreditRatingType(CreditRatingType creditRatingType) {
        this.creditRatingType = creditRatingType;
    }

    public String getCreditDesc() {
        return creditDesc;
    }

    public void setCreditDesc(String creditDesc) {
        this.creditDesc = creditDesc;
    }
}
