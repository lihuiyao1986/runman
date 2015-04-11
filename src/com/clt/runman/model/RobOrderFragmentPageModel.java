/**
 * @Title: RobOrderFragmentPageModel.java
 * @Package com.clt.runman.model
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com
 * @date 2015年4月10日 下午2:35:28
 * @Copyright:Copyright (c)
 * @Company:whty李焱生
 * @version V1.0
 */
package com.clt.runman.model;

import java.io.Serializable;

/**
 *@Description:RobOrderFragmentPageModel
 *@Author:李焱生
 *@Since:2015年4月10日下午2:35:28  
 */
public class RobOrderFragmentPageModel implements Serializable {

    private static final long serialVersionUID = 7708625228809394518L;

    // 类型
    private String            type;

    // 标题
    private String            title;

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }
}
