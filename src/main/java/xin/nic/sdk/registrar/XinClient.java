/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import xin.nic.sdk.registrar.common.Apis;
import xin.nic.sdk.registrar.common.CommonParam;
import xin.nic.sdk.registrar.common.CreditLogoInfo;
import xin.nic.sdk.registrar.exception.XinException;
import xin.nic.sdk.registrar.module.AuditContactInfo;
import xin.nic.sdk.registrar.module.AuditDomainInfo;
import xin.nic.sdk.registrar.module.AuthType;
import xin.nic.sdk.registrar.module.CreditInfo;
import xin.nic.sdk.registrar.module.CreditRatingType;
import xin.nic.sdk.registrar.module.CreditType;
import xin.nic.sdk.registrar.module.DomainInfo;
import xin.nic.sdk.registrar.module.Material;
import xin.nic.sdk.registrar.module.ResultDTO;
import xin.nic.sdk.registrar.util.CheckUtil;
import xin.nic.sdk.registrar.util.HttpUtil;
import xin.nic.sdk.registrar.util.JsonUtil;
import xin.nic.sdk.registrar.util.SignUtil;
import xin.nic.sdk.registrar.util.UrlUtil;

/**
 * 类 XinClient.java的实现描述：封装了注册局的接口调用
 * 
 * @author shaopeng.wei 2015-06-02 09:49
 */
public class XinClient {
    private static final String FIELD_DOMAIN             = "domain";
    private static final String FIELD_CONTACTID          = "contactId";
    private static final String FIELD_AUTH_TYPE          = "authType";
    private static final String FIELD_CREDIT_RATING_TYPE = "creditRatingType";
    private static final String FIELD_ATTACHMENT         = "attachment";
    private static final String FIELD_CREDIT_TYPE        = "creditType";
    private static final String FIELD_BIND_TYPE          = "bindType";
    private static final String FIELD_OTHER_DOMAIN       = "otherDomain";

    /**
     * 默认编码
     */
    private static final String DEFAULT_CHARSET          = "UTF-8";

    /**
     * 注册局旁路系统服务地址
     */
    private final String        apiEndpoint;

    /**
     * 注册局旁路系统颁发给注册商的AppId
     */
    private final String        appKey;

    /**
     * 注册局旁路系统颁发给注册商的AppSecret
     */
    private final String        appSecret;

    /**
     * 创建Xin客户端
     * 
     * @param apiEndpoint 服务地址
     * @param appKey AppId
     * @param appSecret AppSecret
     */
    public XinClient(String apiEndpoint, String appKey, String appSecret) {
        this(apiEndpoint, appKey, appSecret, 5000, 60000);
    }

    /**
     * 创建Xin客户端
     * 
     * @param apiEndpoint 服务地址
     * @param appKey AppId
     * @param appSecret AppSecret
     * @param connTimeout 创建连接超时时间
     * @param socketTimeout 响应超时时间
     */
    public XinClient(String apiEndpoint, String appKey, String appSecret, int connTimeout, int socketTimeout) {
        CheckUtil.checkNotBlank("apiEndpoint", apiEndpoint);
        CheckUtil.checkNotBlank("appKey", appKey);
        CheckUtil.checkNotBlank("appSecret", appSecret);

        this.apiEndpoint = apiEndpoint;
        this.appKey = appKey;
        this.appSecret = appSecret;

        HttpUtil.setConnTimeout(connTimeout);
        HttpUtil.setReadTimeout(socketTimeout);
        HttpUtil.setCharset(DEFAULT_CHARSET);
    }

    /**
     * 生成发起信用验证的URL
     * 
     * @param domain 域名
     * @return 信用验证的跳转URL
     */
    public String generateVerifyUrl(String domain) {
        CheckUtil.checkNotBlank(FIELD_DOMAIN, domain);

        // 时间戳和随机码
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = UUID.randomUUID().toString();

        // 签名
        String sign = SignUtil.signVerifyUrl(appKey, appSecret, timestamp, nonce, domain);

        // 准备URL参数
        Map<String, String> params = new HashMap<String, String>();
        params.put(CommonParam.FIELD_APPKEY, appKey);
        params.put(CommonParam.FIELD_TIMESTAMP, String.valueOf(timestamp));
        params.put(CommonParam.FIELD_SIGNATURENONCE, nonce);
        params.put(CommonParam.FIELD_SIGNATURE, sign);
        params.put(FIELD_DOMAIN, domain);

        // 生成完成URL
        return UrlUtil.generateGetUrl(apiEndpoint, Apis.VERIFY_PAGE, params);
    }

    /**
     * 生成发起主体信用验证的URL
     * 
     * @param domain 域名
     * @return 信用验证的跳转URL
     */
    public String generateVerifyMainUrl(String domain) {
        CheckUtil.checkNotBlank(FIELD_DOMAIN, domain);

        // 时间戳和随机码
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = UUID.randomUUID().toString();

        // 签名
        String sign = SignUtil.signVerifyUrl(appKey, appSecret, timestamp, nonce, domain);

        // 准备URL参数
        Map<String, String> params = new HashMap<String, String>();
        params.put(CommonParam.FIELD_APPKEY, appKey);
        params.put(CommonParam.FIELD_TIMESTAMP, String.valueOf(timestamp));
        params.put(CommonParam.FIELD_SIGNATURENONCE, nonce);
        params.put(CommonParam.FIELD_SIGNATURE, sign);
        params.put(FIELD_DOMAIN, domain);

        // 生成完成URL
        return UrlUtil.generateGetUrl(apiEndpoint, Apis.VERIFY_MAIN_PAGE, params);
    }

    /**
     * 查询信用信息
     * 
     * @param domain
     * @param creditType
     * @return
     */
    public ResultDTO<CreditInfo> queryCreditInfo(String domain, CreditType creditType) {
        CheckUtil.checkNotBlank(FIELD_DOMAIN, domain);
        CheckUtil.checkNotNull(FIELD_CREDIT_TYPE, creditType);

        // 时间戳和随机码
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = UUID.randomUUID().toString();

        // 签名参数
        Map<String, String> params = new HashMap<String, String>();
        params.put(FIELD_DOMAIN, domain);
        params.put(FIELD_CREDIT_TYPE, String.valueOf(creditType.getCode()));

        // 签名
        String sign = SignUtil.sign(appKey, appSecret, timestamp, nonce, params);

        // 准备URL参数
        params.put(CommonParam.FIELD_APPKEY, appKey);
        params.put(CommonParam.FIELD_TIMESTAMP, String.valueOf(timestamp));
        params.put(CommonParam.FIELD_SIGNATURENONCE, nonce);
        params.put(CommonParam.FIELD_SIGNATURE, sign);

        // 返回请求结果
        return doGetRequest(Apis.QUERY_CREDIT_INFO, params, CreditInfo.class);
    }

    /**
     * 查询联系人实名制审核状态
     * 
     * @param contactId 域名的联系人ID
     * @return 审核信息
     */
    public ResultDTO<AuditContactInfo> queryContactAudit(String contactId) {
        CheckUtil.checkNotBlank(FIELD_CONTACTID, contactId);

        // 时间戳和随机码
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = UUID.randomUUID().toString();

        // 签名参数
        Map<String, String> params = new HashMap<String, String>();
        params.put(FIELD_CONTACTID, contactId);

        // 签名
        String sign = SignUtil.sign(appKey, appSecret, timestamp, nonce, params);

        // 准备URL参数
        params.put(CommonParam.FIELD_APPKEY, appKey);
        params.put(CommonParam.FIELD_TIMESTAMP, String.valueOf(timestamp));
        params.put(CommonParam.FIELD_SIGNATURENONCE, nonce);
        params.put(CommonParam.FIELD_SIGNATURE, sign);

        // 返回请求结果
        return doGetRequest(Apis.QUERY_AUDIT_CONTACT, params, AuditContactInfo.class);
    }

    /**
     * 查询联系人实名制审核状态
     * 
     * @param domain 域名名字
     * @return 审核信息
     */
    public ResultDTO<AuditDomainInfo> queryDomainAudit(String domain) {
        CheckUtil.checkNotBlank(FIELD_DOMAIN, domain);

        // 时间戳和随机码
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = UUID.randomUUID().toString();

        // 签名参数
        Map<String, String> params = new HashMap<String, String>();
        params.put(FIELD_DOMAIN, domain);

        // 签名
        String sign = SignUtil.sign(appKey, appSecret, timestamp, nonce, params);

        // 准备URL参数
        params.put(CommonParam.FIELD_APPKEY, appKey);
        params.put(CommonParam.FIELD_TIMESTAMP, String.valueOf(timestamp));
        params.put(CommonParam.FIELD_SIGNATURENONCE, nonce);
        params.put(CommonParam.FIELD_SIGNATURE, sign);

        // 返回请求结果
        return doGetRequest(Apis.QUERY_AUDIT_DOMAIN, params, AuditDomainInfo.class);
    }

    /**
     * 注册商上传审核资料
     * 
     * @param contactId
     * @param is
     * @return
     * @throws IOException
     */
    public ResultDTO<Boolean> uploadAuditMaterial(String contactId, Material[] materials) throws IOException {

        // 时间戳和随机码
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = UUID.randomUUID().toString();

        // 附件
        Map<String, InputStream> attachments = new HashMap<String, InputStream>();

        // 签名参数
        Map<String, String> params = new HashMap<String, String>();
        params.put(FIELD_CONTACTID, contactId);
        StringBuilder fileNames = new StringBuilder();
        for (Material material : materials) {
            fileNames.append(material.getFilename());
            attachments.put(material.getFilename(), material.getIss());
        }
        params.put(FIELD_ATTACHMENT, fileNames.toString());
        // 签名
        String sign = SignUtil.sign(appKey, appSecret, timestamp, nonce, params);
        params.remove(FIELD_ATTACHMENT);

        // 准备URL参数
        params.put(CommonParam.FIELD_APPKEY, appKey);
        params.put(CommonParam.FIELD_TIMESTAMP, String.valueOf(timestamp));
        params.put(CommonParam.FIELD_SIGNATURENONCE, nonce);
        params.put(CommonParam.FIELD_SIGNATURE, sign);

        return doPostRequest(Apis.SUBMIT_AUDIT_MATERIAL, params, attachments, Boolean.class);
    }

    /**
     * 删除域名信用信息
     * 
     * @param domain 域名
     * @param authType 认证类型
     * @param creditRatingType 认证机构
     * @return 删除结果
     */
    public ResultDTO deleteCreditInfo(String domain, AuthType authType, CreditRatingType creditRatingType,
                                      CreditType creditType) {
        CheckUtil.checkNotBlank(FIELD_DOMAIN, domain);
        CheckUtil.checkNotNull(FIELD_AUTH_TYPE, authType);
        CheckUtil.checkNotNull(FIELD_CREDIT_RATING_TYPE, creditRatingType);
        CheckUtil.checkNotNull(FIELD_CREDIT_TYPE, creditType);

        // 时间戳和随机码
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = UUID.randomUUID().toString();

        // 签名参数
        Map<String, String> params = new HashMap<String, String>();
        params.put(FIELD_DOMAIN, domain);
        params.put(FIELD_AUTH_TYPE, String.valueOf(authType.getCode()));
        params.put(FIELD_CREDIT_RATING_TYPE, String.valueOf(creditRatingType.getCode()));
        params.put(FIELD_CREDIT_TYPE, String.valueOf(creditType.getCode()));

        // 签名
        String sign = SignUtil.sign(appKey, appSecret, timestamp, nonce, params);

        // 准备URL参数
        params.put(CommonParam.FIELD_APPKEY, appKey);
        params.put(CommonParam.FIELD_TIMESTAMP, String.valueOf(timestamp));
        params.put(CommonParam.FIELD_SIGNATURENONCE, nonce);
        params.put(CommonParam.FIELD_SIGNATURE, sign);

        // 返回请求结果
        return doGetRequest(Apis.DELETE_CREDIT_INFO, params, null);
    }

    /**
     * xin域名绑定解绑
     * 
     * @param domainName
     * @param otherDomain
     * @param bindType
     * @return
     */
    public ResultDTO bindXinDomain(String domainName, String otherDomain, Integer bindType) {
        CheckUtil.checkNotBlank(FIELD_DOMAIN, domainName);
        CheckUtil.checkNotNull(FIELD_OTHER_DOMAIN, otherDomain);
        CheckUtil.checkNotNull(FIELD_BIND_TYPE, bindType);

        // 时间戳和随机码
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = UUID.randomUUID().toString();

        // 签名参数
        Map<String, String> params = new HashMap<String, String>();
        params.put(FIELD_DOMAIN, domainName);
        params.put(FIELD_OTHER_DOMAIN, otherDomain);
        params.put(FIELD_BIND_TYPE, String.valueOf(bindType));

        // 签名
        String sign = SignUtil.sign(appKey, appSecret, timestamp, nonce, params);

        // 准备URL参数
        params.put(CommonParam.FIELD_APPKEY, appKey);
        params.put(CommonParam.FIELD_TIMESTAMP, String.valueOf(timestamp));
        params.put(CommonParam.FIELD_SIGNATURENONCE, nonce);
        params.put(CommonParam.FIELD_SIGNATURE, sign);

        // 返回请求结果
        return doGetRequest(Apis.BIND_XIN_DOMAIN, params, null);
    }

    /**
     * 查询绑定的域名列表
     * 
     * @param domain
     * @return
     */
    public ResultDTO<DomainInfo> queryBindDomains(String domain) {
        CheckUtil.checkNotBlank(FIELD_DOMAIN, domain);

        // 时间戳和随机码
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = UUID.randomUUID().toString();

        // 签名参数
        Map<String, String> params = new HashMap<String, String>();
        params.put(FIELD_DOMAIN, domain);

        // 签名
        String sign = SignUtil.sign(appKey, appSecret, timestamp, nonce, params);

        // 准备URL参数
        params.put(CommonParam.FIELD_APPKEY, appKey);
        params.put(CommonParam.FIELD_TIMESTAMP, String.valueOf(timestamp));
        params.put(CommonParam.FIELD_SIGNATURENONCE, nonce);
        params.put(CommonParam.FIELD_SIGNATURE, sign);

        // 返回请求结果
        return doGetRequest(Apis.QUERY_BIND_DOMAINS, params, DomainInfo.class);
    }

    /**
     * 查询诚信认证标识
     * 
     * @param domain
     * @return
     */
    public ResultDTO<CreditLogoInfo> queryCreditLogo(String domain) {
        CheckUtil.checkNotBlank(FIELD_DOMAIN, domain);

        // 时间戳和随机码
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = UUID.randomUUID().toString();

        // 签名参数
        Map<String, String> params = new HashMap<String, String>();
        params.put(FIELD_DOMAIN, domain);

        // 签名
        String sign = SignUtil.sign(appKey, appSecret, timestamp, nonce, params);

        // 准备URL参数
        params.put(CommonParam.FIELD_APPKEY, appKey);
        params.put(CommonParam.FIELD_TIMESTAMP, String.valueOf(timestamp));
        params.put(CommonParam.FIELD_SIGNATURENONCE, nonce);
        params.put(CommonParam.FIELD_SIGNATURE, sign);

        // 返回请求结果
        return doGetRequest(Apis.QUERY_CREDIT_LOGO, params, CreditLogoInfo.class);
    }

    @SuppressWarnings("unchecked")
    private <T extends Serializable> ResultDTO<T> doGetRequest(String apiPath, Map<String, String> params,
                                                               Class<T> moduleClz) {
        // 生成请求URL
        String getUrl = UrlUtil.generateGetUrl(apiEndpoint, apiPath, params);

        // 请求接口
        HttpUtil.HttpResp httpResp = HttpUtil.doGet(getUrl);

        // 状态码
        int statusCode = httpResp.getStatusCode();
        if (statusCode != 200) {
            throw new XinException("xin.response.error", "HttpStatusCode:" + statusCode);
        }

        // 读取返回文本
        String jsonText = httpResp.getContent();

        // 转换公共结果
        ResultDTO<T> resultDTO = JsonUtil.parseObject(jsonText, ResultDTO.class);
        if (resultDTO == null) {
            throw new XinException("xin.response.error", jsonText);
        }

        // 转换module字段
        if (moduleClz != null && !moduleClz.isAssignableFrom(Void.class)) {
            JSONObject jsonObject = JSON.parseObject(jsonText);
            resultDTO.setModule(JsonUtil.parseObject(jsonObject.getString("module"), moduleClz));
        }

        // 返回结果
        return resultDTO;
    }

    @SuppressWarnings("unchecked")
    private <T extends Serializable> ResultDTO<T> doPostRequest(String apiPath, Map<String, String> params,
                                                                Map<String, InputStream> attachments,
                                                                Class<T> moduleClz) {
        // 生成请求URL
        String getUrl = UrlUtil.generatePostUrl(apiEndpoint, apiPath);

        // 请求接口
        try {
            String jsonText = HttpUtil.doPost(getUrl, params, attachments);
            // 转换公共结果
            ResultDTO<T> resultDTO = JsonUtil.parseObject(jsonText, ResultDTO.class);
            if (resultDTO == null) {
                throw new XinException("xin.response.error", jsonText);
            }
            // 转换module字段
            if (moduleClz != null && !moduleClz.isAssignableFrom(Void.class)) {
                JSONObject jsonObject = JSON.parseObject(jsonText);
                resultDTO.setModule(JsonUtil.parseObject(jsonObject.getString("module"), moduleClz));
            }

            // 返回结果
            return resultDTO;
        } catch (Exception e) {
            throw new XinException("xin.submitAuditMaterial.error", e);
        }
    }
}
