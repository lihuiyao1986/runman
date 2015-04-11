package com.clt.runman.model;

/**
 * 
* @ClassName: VersionInfoRespBean 
* @Description: TODO(获取版本信息响应) 
* @author fuxianwei 
* @date 2015-4-8 下午2:16:32 
*
 */
public class VersionInfoRespBean extends BaseRespBean {

    /**
     * 版本信息
     */
    private VersionInfomation softVersion;

    public VersionInfomation getSoftVersion(){
        return softVersion;
    }

    public void setSoftVersion(VersionInfomation softVersion){
        this.softVersion = softVersion;
    }

}
