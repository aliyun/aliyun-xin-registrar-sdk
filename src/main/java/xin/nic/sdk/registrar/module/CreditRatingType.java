/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.module;

/**
 * 类CreditRatingType.java的实现描述：
 * 
 * <pre>
 * 信用认证机构类型
 * </pre>
 * 
 * @author fanyong.kfy 2015年4月14日 上午9:32:19
 */
public enum CreditRatingType implements IntEnum<CreditRatingType> {

    CREADIT_ZHIMA(1, "芝麻信用"),
    CREADIT_CHENGXINTONG(2, "1688诚信通"),
    CREADIT_B2B(3, "B2B");

    private int    code;

    private String desc;

    private CreditRatingType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static CreditRatingType valuesOf(int code) {
        for (CreditRatingType creditRatingType : CreditRatingType.values()) {
            if (creditRatingType.code == code) {
                return creditRatingType;
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
