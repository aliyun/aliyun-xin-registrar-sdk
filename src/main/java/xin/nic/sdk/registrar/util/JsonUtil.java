/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.util;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

import xin.nic.sdk.registrar.common.IntEnumDeserializer;
import xin.nic.sdk.registrar.module.AuthType;
import xin.nic.sdk.registrar.module.CreditRatingType;
import xin.nic.sdk.registrar.module.CreditType;

/**
 * 类 JsonUtil.java的实现描述：FastJson的调用封装
 *
 * @author shaopeng.wei 2015-06-02 14:42
 */
public class JsonUtil {

    private static final ParserConfig parserConfig = new ParserConfig();

    static {
        // 指定IntEnum的反序列化
        parserConfig.putDeserializer(AuthType.class, new IntEnumDeserializer(AuthType.class));
        parserConfig.putDeserializer(CreditRatingType.class, new IntEnumDeserializer(CreditRatingType.class));
        parserConfig.putDeserializer(CreditType.class, new IntEnumDeserializer(CreditType.class));
    }

    public static <T> T parseObject(String input, Class<T> clz) {
        if (StringUtils.isBlank(input)) {
            return null;
        }

        return JSON.parseObject(input, clz, parserConfig, JSON.DEFAULT_PARSER_FEATURE);
    }
}
