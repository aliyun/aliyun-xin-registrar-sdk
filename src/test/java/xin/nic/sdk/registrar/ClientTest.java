/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

import xin.nic.sdk.registrar.common.CreditLogoInfo;
import xin.nic.sdk.registrar.module.AuthType;
import xin.nic.sdk.registrar.module.CreditInfo;
import xin.nic.sdk.registrar.module.CreditRatingType;
import xin.nic.sdk.registrar.module.CreditType;
import xin.nic.sdk.registrar.module.DomainInfo;
import xin.nic.sdk.registrar.module.ResultDTO;

/**
 * 类 ClientTest.java的实现描述：ClientTest
 * 
 * @author shaopeng.wei 2015-06-02 14:02
 */
public class ClientTest {

    private final XinClient xinClient = new XinClient("https://verify.nic.xin", "aliyun_test", "test");

    @Test
    public void testGenerateVerifyUrl() {
        String url = xinClient.generateVerifyUrl("xxx.xin");
        System.out.println(url);
    }

    @Test
    public void testGenerateVerifyMainUrl() {
        String url = xinClient.generateVerifyMainUrl("xxx.xin");
        System.out.println(url);
    }

    @Test
    public void testQueryCreditInfo() {
        ResultDTO<CreditInfo> result = xinClient.queryCreditInfo("xxx.xin", CreditType.CREDIT_OWNER);
        System.out.println(result.toJsonString());
    }

    @Test
    public void testDeleteCreditInfo() {
        String domain = "xxx.xin";

        long start = System.currentTimeMillis();
        ResultDTO resultDTO = xinClient.deleteCreditInfo(domain, AuthType.AUTH_PERSON, CreditRatingType.CREADIT_ZHIMA,
                CreditType.CREDIT_WEBSITE);
        System.out.println(
                String.format("cost:%s, resp:%s", System.currentTimeMillis() - start, JSON.toJSONString(resultDTO)));

        Assert.assertEquals(true, resultDTO.isSuccess());
    }

    @Test
    public void testQueryCreditLogo() {
        String domain = "xxx.xin";

        long start = System.currentTimeMillis();
        ResultDTO<CreditLogoInfo> resultDTO = xinClient.queryCreditLogo(domain);
        System.out.println(
                String.format("cost:%s, resp:%s", System.currentTimeMillis() - start, JSON.toJSONString(resultDTO)));

        Assert.assertEquals(true, resultDTO.isSuccess());
    }

    @Test
    public void testQueryBindDomains() {
        String domain = "xxx.xin";

        long start = System.currentTimeMillis();
        ResultDTO<DomainInfo> resultDTO = xinClient.queryBindDomains(domain);
        System.out.println(
                String.format("cost:%s, resp:%s", System.currentTimeMillis() - start, JSON.toJSONString(resultDTO)));

        Assert.assertEquals(true, resultDTO.isSuccess());
    }

    @Test
    public void testBindXinDomain() {
        String domain = "xxx.xin";

        long start = System.currentTimeMillis();
        ResultDTO resultDTO = xinClient.bindXinDomain(domain, "xxx.xin", 2);
        System.out.println(
                String.format("cost:%s, resp:%s", System.currentTimeMillis() - start, JSON.toJSONString(resultDTO)));

        Assert.assertEquals(true, resultDTO.isSuccess());
    }

}
