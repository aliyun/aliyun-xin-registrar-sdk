/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.module;

/**
 * 类AuthType.java的实现描述：
 * 
 * <pre>
 * 认证类型
 * </pre>
 * 
 * @author fanyong.kfy 2015年4月14日 上午9:32:19
 */
public enum AuthType implements IntEnum<AuthType> {

    AUTH_PERSON(1, "个人"),
    AUTH_COMPANY(2, "公司");

    private int    code;

    private String desc;

    private AuthType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AuthType valuesOf(int code) {
        for (AuthType messageType : AuthType.values()) {
            if (messageType.code == code) {
                return messageType;
            }
        }

        return null;
    }

    @Override
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
