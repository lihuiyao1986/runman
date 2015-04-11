/**
 * @Title: BaseFragmentPagerAdapter.java
 * @Package com.clt.runman.adapter
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com
 * @date 2015年4月10日 下午2:28:26
 * @Copyright:Copyright (c)
 * @Company:whty李焱生
 * @version V1.0
 */
package com.clt.runman.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 *@Description:基础的fragmentPagerAdapter
 *@Author:李焱生
 *@Since:2015年4月10日下午2:28:26  
 */
public abstract class BaseFragmentPagerAdapter<T> extends FragmentPagerAdapter {

    protected List<T> dataList;

    public BaseFragmentPagerAdapter(FragmentManager fm, List<T> dataList) {
        super (fm);
        this.dataList = dataList;
    }

    /**
     *@Author: 李焱生
     *@Since: 2015年4月10日下午2:28:51
     *@param arg0
     *@return
     */
    @Override
    public Fragment getItem(int position){
        return obtainItem(position);
    }

    /**
     *@Author: 李焱生
     *@Since: 2015年4月10日下午2:32:32
     *@param position
     *@return
     */
    public abstract Fragment obtainItem(int position);

    /**
     *@Author: 李焱生
     *@Since: 2015年4月10日下午2:28:51
     *@return
     */
    @Override
    public int getCount(){
        return dataList.size ();
    }

}
