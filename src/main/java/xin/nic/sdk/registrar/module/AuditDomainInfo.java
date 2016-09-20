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
 * 类 AuditDomainInfo.java的实现描述：域名审核结果信息
 * 
 * @author menlc 2015-10-09 14:51
 */
public class AuditDomainInfo implements Serializable {
    private static final long serialVersionUID = 361951234009099341L;

    /**
     * domain
     */
    private String            domain;

    /**
     * 审核当前状态
     */
    private AuditEnum         auditStatus;

    /**
     * 审核失败原因
     */
    private String            errorMsg;

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public AuditEnum getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(AuditEnum auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

}
