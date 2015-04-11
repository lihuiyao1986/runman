package com.clt.runman.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.clt.runman.R;
import com.clt.runman.model.RobOrderDetailBean;
import com.clt.runman.utils.AppConstant;
import com.clt.runman.utils.DateUtils;
import com.clt.runman.utils.StringUtils;

/**
 *@Description:抢单列表适配器
 *@Author:张聪
 *@Since:2015年3月19日下午2:25:48
 */
public class RobOrderAdapter extends RunManBaseAdapter<RobOrderDetailBean> {

    public interface RobOrderAdpaterCallBack {
        public void robBtnClicked(Button btn,RobOrderDetailBean orderInfo);
    }

    private String                  type;

    private RobOrderAdpaterCallBack callBack;

    public RobOrderAdapter(Context context, List<RobOrderDetailBean> data, int resource, String type, RobOrderAdpaterCallBack callBack) {
        super (context, data, resource);
        this.type = type;
        this.callBack = callBack;
    }

    public RobOrderAdapter(Context context, List<RobOrderDetailBean> data, int resource, String type) {
        super (context, data, resource);
        this.type = type;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        if ("0".equals (type)) {
            return handleUnRobbed (position, convertView, parent);
        } else {
            return handleRobbed (position, convertView, parent);
        }
    }

    /**
     * 处理已抢单
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    private View handleRobbed(int position,View convertView,ViewGroup parent){
        RobbedOrderViewHolder robbedOrderViewHolder = null;
        RobOrderDetailBean orderQueryDetailBean = listData.get (position);
        if (null == convertView) {
            convertView = listContainer.inflate (itemViewResource, null);
            robbedOrderViewHolder = new RobbedOrderViewHolder ();
            robbedOrderViewHolder.robbed_order_ongoing_img = (ImageView)convertView.findViewById (R.id.robbed_order_ongoing_img);
            robbedOrderViewHolder.robbed_button = (ImageView) convertView.findViewById (R.id.robbed_order_btn);
            robbedOrderViewHolder.robbed_carNum = (TextView) convertView.findViewById (R.id.robbed_carNum);
            robbedOrderViewHolder.robbed_carPosition = (TextView) convertView.findViewById (R.id.robbed_carPosition);
            robbedOrderViewHolder.robbed_subscribeTime = (TextView) convertView.findViewById (R.id.robbed_subscribeTime);
            convertView.setTag (robbedOrderViewHolder);
        } else {
            robbedOrderViewHolder = (RobbedOrderViewHolder) convertView.getTag ();
        }
        displayRobbedOrderView (orderQueryDetailBean, robbedOrderViewHolder);
        convertView.setBackgroundResource(R.drawable.robbed_orderlist_query_bg_selector);
        return convertView;
    }

    /**
     * 处理未抢单
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    private View handleUnRobbed(int position,View convertView,ViewGroup parent){
        RobOrderViewHolder robOrderViewHolder = null;
        RobOrderDetailBean orderQueryDetailBean = listData.get (position);
        if (null == convertView) {
            convertView = listContainer.inflate (itemViewResource, null);
            robOrderViewHolder = new RobOrderViewHolder ();
            robOrderViewHolder.rob_button = (Button) convertView.findViewById (R.id.rob_order_btn);
            robOrderViewHolder.rob_carNum = (TextView) convertView.findViewById (R.id.rob_carNum);
            robOrderViewHolder.rob_carPosition = (TextView) convertView.findViewById (R.id.rob_carPosition);
            robOrderViewHolder.rob_distance = (TextView) convertView.findViewById (R.id.rob_distance);
            robOrderViewHolder.rob_subscribeTime = (TextView) convertView.findViewById (R.id.rob_subscribeTime);
            convertView.setTag (robOrderViewHolder);
        } else {
            robOrderViewHolder = (RobOrderViewHolder) convertView.getTag ();
        }
        displayRobOrderView (orderQueryDetailBean, robOrderViewHolder);
        convertView.setBackgroundResource(R.drawable.robbed_orderlist_query_bg_selector);
        return convertView;
    }

    /**
     *@Description: 展示抢单列表
     *@Author: 张聪
     *@Since: 2015年3月19日下午4:59:19
     *@param orderQueryDetailBean
     *@param viewHolder
     */
    private void displayRobOrderView(final RobOrderDetailBean orderQueryDetailBean,final RobOrderViewHolder viewHolder){
        viewHolder.rob_carNum.setText (StringUtils.trimNull(orderQueryDetailBean.getCarPlateNum()));
        viewHolder.rob_carPosition.setText (StringUtils.trimNull(orderQueryDetailBean.getAddress()));
        viewHolder.rob_distance.setText (StringUtils.trimNull(orderQueryDetailBean.getDistance ())+"公里");
        String hopeTime = DateUtils.convertHopeTime(orderQueryDetailBean.getServiceBeginTime(),orderQueryDetailBean.getServiceEndTime());
        viewHolder.rob_subscribeTime.setText (hopeTime);
        viewHolder.rob_button.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v)
            {
                if (callBack != null) 
                {
                    callBack.robBtnClicked (viewHolder.rob_button, orderQueryDetailBean);
                }
            }
        });
    }

    /**
     *@Description: 展示已抢单列表
     *@Author: 张聪
     *@Since: 2015年3月19日下午4:59:19
     *@param orderQueryDetailBean
     *@param viewHolder
     */
    private void displayRobbedOrderView(final RobOrderDetailBean orderQueryDetailBean,final RobbedOrderViewHolder viewHolder){
        viewHolder.robbed_carNum.setText (orderQueryDetailBean.getCarPlateNum());
        viewHolder.robbed_carPosition.setText (orderQueryDetailBean.getAddress());
        String hopeTime = DateUtils.convertHopeTime(orderQueryDetailBean.getServiceBeginTime(),orderQueryDetailBean.getServiceEndTime());
        viewHolder.robbed_subscribeTime.setText (hopeTime);
        int status = orderQueryDetailBean.getStatus ();
        if(status > AppConstant.ORDER_RECEIVED)
        {
            viewHolder.robbed_order_ongoing_img.setVisibility (View.VISIBLE);
        }
        else
        {
            viewHolder.robbed_order_ongoing_img.setVisibility (View.GONE);
        }
    }

    /**
     *@Description:接单
     *@Author:张聪
     *@Since:2015年3月26日下午4:50:01
     */
    static class RobOrderViewHolder {

        public Button   rob_button;
        public TextView rob_carNum;
        public TextView rob_carPosition;
        public TextView rob_distance;
        public TextView rob_subscribeTime;
    }

    /**
     *@Description:已抢单
     *@Author:张聪
     *@Since:2015年3月26日下午4:50:22
     */
    static class RobbedOrderViewHolder {
        public ImageView robbed_button;
        public ImageView robbed_order_ongoing_img;
        public TextView robbed_carNum;
        public TextView robbed_carPosition;
        public TextView robbed_subscribeTime;
    }

}
