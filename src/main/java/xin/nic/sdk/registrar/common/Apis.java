/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.common;

/**
 * 类 Apis.java的实现描述：XIN域名注册局支持的接口
 * 
 * @author shaopeng.wei 2015-06-02 13:56
 */
public class Apis {

    /**
     * 域名所有者信用验证页面
     */
    public static final String VERIFY_PAGE           = "/verify.htm";

    /**
     * 网站主体信用验证页面
     */
    public static final String VERIFY_MAIN_PAGE      = "/verifyMain.htm";

    /**
     * 获取信用信息
     */
    public static final String QUERY_CREDIT_INFO     = "/api/domain/queryCreditInfo.json";

    /**
     * 删除域名信用信息
     */
    public static final String DELETE_CREDIT_INFO    = "/api/domain/deleteCreditInfo.json";

    /**
     * 查询联系人实名制审核结果
     */
    public static final String QUERY_AUDIT_CONTACT   = "/api/domain/queryContactAudit.json";

    /**
     * 查询域名实名制审核结果
     */
    public static final String QUERY_AUDIT_DOMAIN    = "/api/domain/queryDomainAudit.json";

    /**
     * 提交审核资料
     */
    public static final String SUBMIT_AUDIT_MATERIAL = "/api/domain/uploadAuditMaterial.json";

    /**
     * 绑定其他域名到xin
     */
    public static final String BIND_XIN_DOMAIN       = "/api/domain/bindXinDomain.json";

    /**
     * 查询xin域名绑定的域名
     */
    public static final String QUERY_BIND_DOMAINS    = "/api/domain/queryBindDomains.json";

    /**
     * 查询诚信认证标识
     */
    public static final String QUERY_CREDIT_LOGO     = "/api/domain/queryCreditLogo.json";
}
