/**
 * @Title: SlideMenuItemAdapter.java
 * @Package com.clt.runman.adapter
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com
 * @date 2015年4月8日 下午3:40:38
 * @Copyright:Copyright (c)
 * @Company:whty李焱生
 * @version V1.0
 */
package com.clt.runman.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.model.SlideMenuItemBean;
import com.clt.runman.utils.StringUtils;

/**
 *@Description 左侧菜单列表adapter
 *@Author:李焱生
 *@Since:2015年4月8日下午3:40:38  
 */
public class SlideMenuItemAdapter extends RunManBaseAdapter<SlideMenuItemBean> {

    /** 
     * @param context
     * @param data
     * @param resource 
     */
    public SlideMenuItemAdapter(Context context, List<SlideMenuItemBean> data, int resource) {
        super (context, data, resource);
    }

    /**
     *@Description: getView方法 
     *@Author: 李焱生
     *@Since: 2015年4月8日下午3:45:08
     *@param position
     *@param convertView
     *@param parent
     *@return
     */
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = listContainer.inflate (itemViewResource, null);
            holder = new ViewHolder ();
            holder.icon = (ImageView)convertView.findViewById (R.id.slidemenu_icon);
            holder.menu_name = (TextView)convertView.findViewById (R.id.slidemenu_name);
            holder.extra_info = (TextView)convertView.findViewById (R.id.slidemenu_extra_info);
            convertView.setTag (holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag ();
        }
        displayView(listData.get (position),position,holder);
        if(position !=0)
        {
            convertView.setBackgroundResource(R.drawable.slide_menu_item_bg_selector);
        }
        else
        {
            convertView.setBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));
        }
        return convertView;
    }

    /**
     *@Description: 展示视图
     *@Author: 李焱生
     *@Since: 2015年4月8日下午4:04:31
     *@param slideMenuItemBean
     *@param position
     *@param holder
     */
    private void displayView(SlideMenuItemBean slideMenuItemBean,int position,ViewHolder holder)
    {
        holder.icon.setImageDrawable (context.getResources ().getDrawable (slideMenuItemBean.getIcon ()));
        holder.menu_name.setText (StringUtils.trimNull (slideMenuItemBean.getMenuName ()));
        if(position == 0)
        {
            holder.extra_info.setText (StringUtils.trimNull (slideMenuItemBean.getExtraInfo(),"0") + "元"); 
            holder.extra_info.setVisibility (View.VISIBLE);      
        }
        else
        {
            holder.extra_info.setVisibility (View.GONE);
            holder.extra_info.setText("");
        }
    }

    /**
     *@Description:ViewHolder
     *@Author:李焱生
     *@Since:2015年4月8日下午3:57:18
     */
    static class ViewHolder {

        public ImageView icon;

        public TextView  menu_name;

        public TextView  extra_info;
    }

}
