package com.clt.runman.model;

public class RunMan {

    private String  runManId;      // 跑男编号
    private String  runManPhone;   // 手机号
    private String  runManPassword; // 密码
    private String  runManName;    // 姓名
    private Integer runManStatus;  // 工作状态
    private Integer runManSex;     // 性别
    private String  runManIDCard;  // 身份证
    private String  runManAvatar;  // 头像
    private String  runManArea;    // 驻点
    private String  runManUuid;    // token
    private Integer orderCount;    // 订单数量
    private String  runManEncrypt; // 验证密串
    private Integer equipmentId;   // 设备ID
    private String  equipmentKey;  // 交互设备密码
    private String  equipmentMac;  // 交互设备MAC地址

    public String getRunManId(){
        return runManId;
    }

    public void setRunManId(String runManId){
        this.runManId = runManId;
    }

    public String getRunManPhone(){
        return runManPhone;
    }

    public void setRunManPhone(String runManPhone){
        this.runManPhone = runManPhone;
    }

    public String getRunManPassword(){
        return runManPassword;
    }

    public void setRunManPassword(String runManPassword){
        this.runManPassword = runManPassword;
    }

    public String getRunManName(){
        return runManName;
    }

    public void setRunManName(String runManName){
        this.runManName = runManName;
    }

    public Integer getRunManStatus(){
        return runManStatus;
    }

    public void setRunManStatus(Integer runManStatus){
        this.runManStatus = runManStatus;
    }

    public Integer getRunManSex(){
        return runManSex;
    }

    public void setRunManSex(Integer runManSex){
        this.runManSex = runManSex;
    }

    public String getRunManIDCard(){
        return runManIDCard;
    }

    public void setRunManIDCard(String runManIDCard){
        this.runManIDCard = runManIDCard;
    }

    public String getRunManAvatar(){
        return runManAvatar;
    }

    public void setRunManAvatar(String runManAvatar){
        this.runManAvatar = runManAvatar;
    }

    public String getRunManArea(){
        return runManArea;
    }

    public void setRunManArea(String runManArea){
        this.runManArea = runManArea;
    }

    public String getRunManUuid(){
        return runManUuid;
    }

    public void setRunManUuid(String runManUuid){
        this.runManUuid = runManUuid;
    }

    public Integer getOrderCount(){
        return orderCount;
    }

    public void setOrderCount(Integer orderCount){
        this.orderCount = orderCount;
    }

    public String getRunManEncrypt(){
        return runManEncrypt;
    }

    public void setRunManEncrypt(String runManEncrypt){
        this.runManEncrypt = runManEncrypt;
    }

    public Integer getEquipmentId(){
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId){
        this.equipmentId = equipmentId;
    }

    public String getEquipmentKey(){
        return equipmentKey;
    }

    public void setEquipmentKey(String equipmentKey){
        this.equipmentKey = equipmentKey;
    }

    public String getEquipmentMac(){
        return equipmentMac;
    }

    public void setEquipmentMac(String equipmentMac){
        this.equipmentMac = equipmentMac;
    }

}
