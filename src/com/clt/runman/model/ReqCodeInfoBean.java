package com.clt.runman.model;

import java.io.Serializable;

/**
 *@Description:消息
 *@Author:张聪
 *@Since:2014年12月27日下午12:33:28
 */
public class ReqCodeInfoBean implements Serializable {

    private static final long serialVersionUID = -7266596596292838390L;
    // 错误码
    private String            code;
    // 错误描述
    private String            message;

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }
}
