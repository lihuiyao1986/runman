package com.clt.runman.db.dao;

import java.util.List;

import android.content.Context;

import com.clt.runman.db.RunManDataBase;
import com.clt.runman.db.model.OrderDaoModel;
import com.clt.runman.utils.StringUtils;

/**
 * 操作订单数据的数据库dao
 * @author yanshengli
 *
 */
public class OrderDao extends BaseDao {

    private static OrderDao orderDao;

    private Context         context;

    private OrderDao(Context context) {
        this.context = context;
    }

    public static OrderDao newInstance(Context ctx){
        if (orderDao == null) {
            orderDao = new OrderDao (ctx);
        }
        return orderDao;
    }

    /**
     * 保存订单
     * @param order
     * @return
     */
    public void saveOrder(OrderDaoModel order){
        RunManDataBase.newInstance (context).database ().save (order);
    }

    /**
     * 查询所有的订单数据
     */
    public List<OrderDaoModel> queryAllOrders(){
        return RunManDataBase.newInstance (context).database ().findAll (OrderDaoModel.class);
    }

    /**
     * 根据订单号查询订单
     * @param orderid
     * @return
     */
    public OrderDaoModel queryOrderByOrderId(String orderid){
        if (StringUtils.isEmpty (orderid)) { return null; }
        String whereStr = "orderId = '" + StringUtils.trimNull (orderid) + "'";
        List<OrderDaoModel> result = RunManDataBase.newInstance (context).database ().findAllByWhere (OrderDaoModel.class, whereStr);
        return result == null || result.size () == 0 ? null : result.get (0);
    }

    /**
     * 删除所有的订单
     */
    public void deleteAllOrder(){
        RunManDataBase.newInstance (context).database ().deleteAll (OrderDaoModel.class);
    }

}
