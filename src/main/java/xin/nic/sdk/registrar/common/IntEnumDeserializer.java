/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.common;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import xin.nic.sdk.registrar.module.IntEnum;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;

/**
 * 类 IntEnumDeserializer.java的实现描述：实现IntEnum的反序列化，如果是int值就查找code，其他值交给默认处理器
 *
 * @author shaopeng.wei 2015-06-02 14:30
 */
public class IntEnumDeserializer extends EnumDeserializer {

    private final Map<Integer, IntEnum> maps = new HashMap<Integer, IntEnum>();

    public IntEnumDeserializer(Class<? extends IntEnum> enumClass) {
        super(enumClass);

        IntEnum[] enums = enumClass.getEnumConstants();
        for (IntEnum em : enums) {
            maps.put(em.getCode(), em);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object o) {
        final JSONLexer lexer = parser.getLexer();
        if (lexer.token() == JSONToken.LITERAL_INT) {
            Integer value = lexer.intValue();
            lexer.nextToken(JSONToken.COMMA);

            return (T) maps.get(value);
        }

        return super.deserialze(parser, type, o);
    }
}
