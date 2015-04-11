/**
 *@Title: MessageListAdapter.java 
 *@Package com.clt.runman.adapter 
 *@Description: TODO(用一句话描述该文件做什么) 
 *@author A18ccms A18ccms_gmail_com   
 *@date 2015年4月9日 上午10:51:41 
 *@Copyright:Copyright (c) 
 *@Company:whty李焱生
 *@version V1.0 
 */
package com.clt.runman.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.db.model.MessageDaoModel;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.StringUtils;

/**
 *@Description:MessageListAdapter
 *@Author:李焱生
 *@Since:2015年4月9日上午10:51:41  
 */
@SuppressLint("SimpleDateFormat")
public class MessageListAdapter extends RunManBaseAdapter<MessageDaoModel>
{
   
    /** 
     * @param context
     * @param data
     * @param resource 
     */
    public MessageListAdapter(Context context, List<MessageDaoModel> data, int resource) {
        super (context, data, resource);
    }

   
    /**
     *@Description: getView方法 
     *@Author: 李焱生
     *@Since: 2015年4月9日上午11:01:31
     *@param position
     *@param convertView
     *@param parent
     *@return
     */
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        ViewHolder viewHolder = null;
        if(convertView == null)
        {
            convertView = listContainer.inflate (itemViewResource, null);
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView)convertView.findViewById (R.id.msg_list_item_icon);
            viewHolder.title = (TextView)convertView.findViewById (R.id.msg_list_item_title);
            viewHolder.time = (TextView)convertView.findViewById (R.id.msg_list_item_time);
            viewHolder.content = (TextView)convertView.findViewById (R.id.msg_list_item_content);
            convertView.setTag (viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag (); 
        }
        display(listData.get (position),viewHolder);
        return convertView;
    }
    
   
    /**
     *@Description: 展示视图 
     *@Author: 李焱生
     *@Since: 2015年4月9日下午1:23:42
     *@param messageDaoModel
     *@param viewHolder
     */
    private void display(MessageDaoModel messageDaoModel,ViewHolder viewHolder)
    {
        if(messageDaoModel.getType () == AppConstant.remind_type_book_due)
        {
            viewHolder.icon.setImageResource (R.drawable.msg_type_1_icon);
            viewHolder.title.setTextColor (context.getResources ().getColor (R.color.color_43));
            viewHolder.title.setText ("预约单提醒");
        }
        else if(messageDaoModel.getType () == AppConstant.remind_type_money_in)
        {
            viewHolder.icon.setImageResource (R.drawable.msg_type_2_icon);
            viewHolder.title.setTextColor (context.getResources ().getColor (R.color.color_42));
            viewHolder.title.setText ("已到账");
        }
        viewHolder.content.setText (StringUtils.trimNull (messageDaoModel.getDescribe ()));
        viewHolder.time.setText (convertTime(StringUtils.trimNull (messageDaoModel.getTime ())));
    }


    /**
     *@Description:转换时间格式
     *@Author: 李焱生
     *@Since: 2015年4月9日下午1:27:38
     *@param trimNull
     *@return
     */
    private String convertTime(String time)
    {
        if(StringUtils.isEmpty (time))
        {
            return "";
        }
        DateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return format2.format (format1.parse (time));
        } catch (ParseException e) {
        }
        return "";
    }


    /**
     *@Description:ViewHolder类
     *@Author:李焱生
     *@Since:2015年4月9日下午12:38:09
     */
    static class ViewHolder
    {
        public ImageView icon;
        
        public TextView title;
        
        public TextView time;
        
        public TextView content;
        
    }

}
