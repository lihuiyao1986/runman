package com.clt.runman.model;

import net.tsz.afinal.annotation.sqlite.Id;

/**
 * 
* @ClassName: VersionInfomation 
* @Description: TODO(版本信息) 
* @author fuxianwei 
* @date 2015-4-8 下午2:13:49 
*
 */
public class VersionInfomation {

    @Id
    private long   id;
    /**
     * 版本号
     */
    private int    versionCode;
    /**
     * 版本名
     */
    private String versionName;
    /**
     * 更新日志
     */
    private String versionContent;
    /**
     * apk下载地址
     */
    private String versionUrl;
    /**
     * 是否强制更新0不强制,1强制
     */
    private int    versionForce;

    /*
     * 更新时间
     */
    private long   createTime;

    public int getVersionCode(){
        return versionCode;
    }

    public void setVersionCode(int versionCode){
        this.versionCode = versionCode;
    }

    public String getVersionName(){
        return versionName;
    }

    public void setVersionName(String versionName){
        this.versionName = versionName;
    }

    public String getVersionContent(){
        return versionContent;
    }

    public void setVersionContent(String versionContent){
        this.versionContent = versionContent;
    }

    public String getVersionUrl(){
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl){
        this.versionUrl = versionUrl;
    }

    public int getVersionForce(){
        return versionForce;
    }

    public void setVersionForce(int versionForce){
        this.versionForce = versionForce;
    }

    public long getCreateTime(){
        return createTime;
    }

    public void setCreateTime(long createTime){
        this.createTime = createTime;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

}
