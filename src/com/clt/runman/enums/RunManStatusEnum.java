package com.clt.runman.enums;

import com.clt.runman.utils.AppConstant;

/**
 *@Description:跑男工作状态枚举类
 *@Author:张聪
 *@Since:2015年1月26日下午2:41:49
 */
public enum RunManStatusEnum {
    RUNMAN_FREE (AppConstant.RUNMAN_STATUS_FREE, "空闲"), RUNMAN_BUSY (AppConstant.RUNMAN_STATUS_BUSY, "忙碌"), RUNMAN_OFFDUTY (AppConstant.RUNMAN_STATUS_OFFDUTY,
            "休息中");

    private RunManStatusEnum(int statusCode, String statusName) {
        this.statusCode = statusCode;
        this.statusName = statusName;
    }

    private Integer statusCode;

    private String  statusName;

    public Integer getStatusCode(){
        return statusCode;
    }

    public void setStatusCode(Integer statusCode){
        this.statusCode = statusCode;
    }

    public String getStatusName(){
        return statusName;
    }

    public void setStatusName(String statusName){
        this.statusName = statusName;
    }

    public static String getStatusNameByCode(Integer code){
        for ( RunManStatusEnum runManStatus : RunManStatusEnum.values () ) {
            if (runManStatus.statusCode == code) { return runManStatus.statusName; }
        }
        return null;
    }

}
