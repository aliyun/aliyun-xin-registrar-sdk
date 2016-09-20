/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.module;

/**
 * 类CreditType.java的实现描述：TODO 信用类型
 * 
 * @author zhulinhu 2016年8月17日 上午9:47:55
 */
public enum CreditType implements IntEnum<CreditType> {

    CREDIT_OWNER(1, "所有者"),
    CREDIT_WEBSITE(2, "网站主体");

    private int    code;

    private String desc;

    private CreditType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CreditType valuesOf(int code) {
        for (CreditType messageType : CreditType.values()) {
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
