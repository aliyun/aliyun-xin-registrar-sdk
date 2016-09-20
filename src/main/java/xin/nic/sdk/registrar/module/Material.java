/*
 * Copyright 2015 Aliyun.com All right reserved. This software is the
 * confidential and proprietary information of Aliyun.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Aliyun.com .
 */
package xin.nic.sdk.registrar.module;

import java.io.InputStream;

/**
 * 类Material.java的实现描述：上传文件实体类
 * 
 * @author shiming.zhao 2015年10月19日 下午5:23:31
 */
public class Material {

    private String      filename;

    private InputStream iss;

    /**
     * 
     */
    public Material() {
        // TODO Auto-generated constructor stub
    }

    public Material(String filename, InputStream iss) {
        this.filename = filename;
        this.iss = iss;
    }

    /**
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return the iss
     */
    public InputStream getIss() {
        return iss;
    }

    /**
     * @param iss the iss to set
     */
    public void setIss(InputStream iss) {
        this.iss = iss;
    }
}
