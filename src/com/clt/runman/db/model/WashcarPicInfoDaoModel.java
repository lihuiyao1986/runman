package com.clt.runman.db.model;

import net.tsz.afinal.annotation.sqlite.Id;

import com.clt.runman.utils.StringUtils;

/**
 * 洗车照片信息DAO实体类
 * 
 * @author yanshengli
 * @since 2015-3-19
 */
public class WashcarPicInfoDaoModel extends BaseDaoModel {

    private static final long serialVersionUID = 3665265289225851192L;

    @Id
    private long              id;

    /** 子类型
     *  0--整体照片
     *  1--正前照片
     *  2--左侧照片
     *  3--正后照片
     *  4--右侧照片
     *  5--顶部照片
     *  **/
    private int               picSubtype;

    /** 类型 0--洗车前 1-－洗车后 **/
    private int               picType;

    /** 图片链接地址 **/
    private String            picUrl;

    /** 拍摄日期 **/
    private String            picDate;

    /** 跑男id **/
    private String            runmanid;

    /** 订单号 **/
    private String            orderid;

    /** 序号 **/
    private int               picIndex;

    /** Thumbnail路径 **/
    private String            thumbnailPath;

    /** 是否已上传 **/
    private int               isUpload         = 0;

    public String getRunmanid(){
        return runmanid;
    }

    public void setRunmanid(String runmanid){
        this.runmanid = runmanid;
    }

    public String getOrderid(){
        return orderid;
    }

    public void setOrderid(String orderid){
        this.orderid = orderid;
    }

    public int getPicSubtype(){
        return picSubtype;
    }

    public void setPicSubtype(int picSubtype){
        this.picSubtype = picSubtype;
    }

    public int getPicType(){
        return picType;
    }

    public void setPicType(int picType){
        this.picType = picType;
    }

    public String getPicUrl(){
        return picUrl;
    }

    public void setPicUrl(String picUrl){
        this.picUrl = picUrl;
    }

    public String getPicDate(){
        return picDate;
    }

    public void setPicDate(String picDate){
        this.picDate = picDate;
    }

    public int getPicIndex(){
        return picIndex;
    }

    public void setPicIndex(int picIndex){
        this.picIndex = picIndex;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getFilename(){
        return StringUtils.trimNull (this.orderid) + "_" + StringUtils.trimNull (this.picDate) + "_" + StringUtils.trimNull (this.runmanid) + "_"
                + this.picType + "_" + this.picSubtype + "_" + this.picIndex + ".jpg";
    }

    public String getThumbnailPath(){
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath){
        this.thumbnailPath = thumbnailPath;
    }

    public int getIsUpload(){
        return isUpload;
    }

    public void setIsUpload(int isUpload){
        this.isUpload = isUpload;
    }

    public String getUploadPhotoType(){
        return "" + getPicType () + getPicSubtype () + getPicIndex ();
    }
}
