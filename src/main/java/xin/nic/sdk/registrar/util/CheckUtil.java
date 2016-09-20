/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.util;

import org.apache.commons.lang3.StringUtils;

import xin.nic.sdk.registrar.exception.XinException;

/**
 * 类 CheckUtil.java的实现描述：CheckUtil
 *
 * @author shaopeng.wei 2015-06-03 10:49
 */
public class CheckUtil {
    public static void checkNotBlank(String name, String value) {
        if (StringUtils.isBlank(value)) {
            throw new XinException("xin.param.error", String.format("%s不能为空", name));
        }
    }

    public static void checkNotNull(String name, Object value) {
        if (value == null) {
            throw new XinException("xin.param.error", String.format("%s不能为NULL", name));
        }
    }
}
