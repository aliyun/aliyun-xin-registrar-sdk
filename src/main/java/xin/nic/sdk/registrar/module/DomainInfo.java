/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类 DomainInfo.java的实现描述：DomainInfo
 *
 * @author shaopeng.wei 2015-05-08 14:51
 */
public class DomainInfo implements Serializable {
    private static final long serialVersionUID = 561952868009099341L;

    private String            domainName;

    private List<CreditInfo>  creditInfoList   = new ArrayList<CreditInfo>();

    private List<String>      bindDomainList   = new ArrayList<String>();

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public List<CreditInfo> getCreditInfoList() {
        return creditInfoList;
    }

    public void setCreditInfoList(List<CreditInfo> creditInfoList) {
        this.creditInfoList = creditInfoList;
    }

    public List<String> getBindDomainList() {
        return bindDomainList;
    }

    public void setBindDomainList(List<String> bindDomainList) {
        this.bindDomainList = bindDomainList;
    }

    private void addCreditInfo(CreditInfo creditInfo) {
        if (creditInfo == null) {
            return;
        }

        if (creditInfoList == null) {
            creditInfoList = new ArrayList<CreditInfo>();
        }

        creditInfoList.add(creditInfo);
    }

}
