/**
 * @Title: SlideMenuItemBean.java
 * @Package com.clt.runman.model
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com
 * @date 2015年4月8日 下午3:42:08
 * @Copyright:Copyright (c)
 * @Company:whty李焱生
 * @version V1.0
 */
package com.clt.runman.model;

import java.io.Serializable;

/**
 *@Description:侧栏菜单选项
 *@Author:李焱生
 *@Since:2015年4月8日下午3:42:08  
 */
public class SlideMenuItemBean implements Serializable {

    private static final long serialVersionUID = -7087440338668511603L;

    /** 图标 **/
    private int               icon;

    /** 菜单名称 **/
    private String            menuName;

    /** 收入 **/
    private String            extraInfo;

    public int getIcon(){
        return icon;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public String getMenuName(){
        return menuName;
    }

    public void setMenuName(String menuName){
        this.menuName = menuName;
    }

    public String getExtraInfo(){
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo){
        this.extraInfo = extraInfo;
    }

}
