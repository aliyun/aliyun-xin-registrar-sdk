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

import xin.nic.sdk.registrar.module.AuthType;
import xin.nic.sdk.registrar.module.CreditInfo;
import xin.nic.sdk.registrar.module.CreditRatingType;
import xin.nic.sdk.registrar.util.JsonUtil;

/**
 * 类 JsonUtilTest.java的实现描述：JsonUtilTest
 *
 * @author shaopeng.wei 2015-06-02 14:45
 */
public class JsonUtilTest {

    @Test
    public void testParseCreditInfo() {
        String json = "{\"authType\":1,\"creditDesc\":\"无信用数据\",\"creditRatingType\":1}";

        CreditInfo creditInfo = JsonUtil.parseObject(json, CreditInfo.class);

        Assert.assertNotNull(creditInfo);
        Assert.assertEquals(AuthType.AUTH_PERSON, creditInfo.getAuthType());
        Assert.assertEquals(CreditRatingType.CREADIT_ZHIMA, creditInfo.getCreditRatingType());
    }

    @Test
    public void testParseCreditInfo2() {
        String json = "{\"authType\":\"AUTH_PERSON\",\"creditDesc\":\"优秀\",\"creditRatingType\":\"CREADIT_ZHIMA\"}";

        CreditInfo creditInfo = JsonUtil.parseObject(json, CreditInfo.class);

        Assert.assertNotNull(creditInfo);
        Assert.assertEquals(AuthType.AUTH_PERSON, creditInfo.getAuthType());
        Assert.assertEquals(CreditRatingType.CREADIT_ZHIMA, creditInfo.getCreditRatingType());
    }
}
