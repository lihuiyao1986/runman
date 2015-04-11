package com.clt.runman.model;

public class UserLoginRespBean extends BaseRespBean {

    // 登录用户信息
    private RunMan  runMan;
    // 工作状态
    private Integer status;

    public Integer getStatus(){
        return status;
    }

    public void setStatus(Integer status){
        this.status = status;
    }

    public RunMan getRunMan(){
        return runMan;
    }

    public void setRunMan(RunMan runMan){
        this.runMan = runMan;
    }

    @Override
    public String toString(){
        return super.toString () + "," + "UserLoginRespBean{" + "token='" + runMan.getRunManUuid () + '\'' + ", runMan=" + runMan + '}';
    }

}
