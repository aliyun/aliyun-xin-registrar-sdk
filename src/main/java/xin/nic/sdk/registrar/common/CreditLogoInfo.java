/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.common;

import java.io.Serializable;

/**
 * 类CreditLogoInfo.java的实现描述：TODO 诚信标识信息
 * 
 * @author zhulinhu 2016年9月4日 下午10:08:15
 */
public class CreditLogoInfo implements Serializable {
    private static final long serialVersionUID = 6297115080734360587L;

    /**
     * 域名
     */
    private String            domainName;

    /**
     * 详情页
     */
    private String            detailUrl;

    /**
     * 图片地址
     */
    private String            imgUrl;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
