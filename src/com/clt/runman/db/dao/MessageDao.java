/**
 * @Title: MessageDao.java
 * @Package com.clt.runman.db.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com
 * @date 2015年4月9日 下午10:13:05
 * @Copyright:Copyright (c)
 * @Company:whty李焱生
 * @version V1.0
 */
package com.clt.runman.db.dao;

import java.util.List;

import android.content.Context;

import com.clt.runman.db.RunManDataBase;
import com.clt.runman.db.model.MessageDaoModel;

/**
 *@Description:消息列表对应的dao
 *@Author:李焱生
 *@Since:2015年4月9日下午10:13:05  
 */
public class MessageDao extends BaseDao {

    private static MessageDao dao;

    private Context           context;

    private MessageDao(Context context) {
        this.context = context;
    }

    public static MessageDao newInstance(Context ctx){
        if (dao == null) {
            dao = new MessageDao (ctx);
        }
        return dao;
    }
    
    /**
     *@Description: 保存消息列表
     *@Author: 李焱生
     *@Since: 2015年4月9日下午10:18:44
     *@param model
     */
    public void saveMessage(MessageDaoModel model)
    {
        RunManDataBase.newInstance (context).database ().save (model);
    }
    
    /**
     *@Description: 查询所有的订单数据 
     *@Author: 李焱生
     *@Since: 2015年4月9日下午10:21:59
     *@return
     */
    public List<MessageDaoModel> queryAllMsgs()
    {
        return RunManDataBase.newInstance (context).database ().findAll (MessageDaoModel.class);
    }
    
    /**
     *@Description: 分页查询消息列表
     *@Author: 李焱生
     *@Since: 2015年4月9日下午10:21:20
     *@param pageNo
     *@param pageSize
     *@return
     */
    public List<MessageDaoModel> queryMsgByPage(String where,int pageNo,int pageSize)
    {
        return RunManDataBase.newInstance (context).database ().findAllByWhere(MessageDaoModel.class,where,"time",1,pageNo,pageSize);
    }


}
