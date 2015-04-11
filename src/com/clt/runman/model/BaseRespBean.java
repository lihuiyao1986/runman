package com.clt.runman.model;


/**
 *@Description:TODO(这里用一句话描述这个类的作用)
 *@Author:张聪
 *@Since:2014年12月27日下午12:16:33
 */
public class BaseRespBean {

    // 响应报文头
    protected ReqCodeInfoBean error;

    
    public ReqCodeInfoBean getError(){
        return error;
    }

    
    public void setError(ReqCodeInfoBean error){
        this.error = error;
    }

    
   
}
