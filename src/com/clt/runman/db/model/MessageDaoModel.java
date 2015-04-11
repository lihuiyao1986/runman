/**
 * @Title: MessageBean.java
 * @Package com.clt.runman.model
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com
 * @date 2015年4月9日 上午10:52:45
 * @Copyright:Copyright (c)
 * @Company:whty李焱生
 * @version V1.0
 */
package com.clt.runman.db.model;

import net.tsz.afinal.annotation.sqlite.Id;

/**
 *@Description:消息dao bean
 *@Author:李焱生
 *@Since:2015年4月9日上午10:52:45  
 */
public class MessageDaoModel extends BaseDaoModel {

    private static final long serialVersionUID = -9097231814148157011L;

    @Id
    protected long            id;

    /** 类型 **/
    private int               type;

    /** 描述 **/
    private String            describe;

    /** 时间 **/
    private String            time;

    public int getType(){
        return type;
    }

    public void setType(int type){
        this.type = type;
    }

    public String getDescribe(){
        return describe;
    }

    public void setDescribe(String describe){
        this.describe = describe;
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

}
