/**
 * @Title: BaseFragmentStatePagerAdapter.java
 * @Package com.clt.runman.adapter
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com
 * @date 2015年4月10日 下午4:01:30
 * @Copyright:Copyright (c)
 * @Company:whty李焱生
 * @version V1.0
 */
package com.clt.runman.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 *@Description:BaseFragmentStatePagerAdapter
 *@Author:李焱生
 *@Since:2015年4月10日下午4:01:30  
 */
public abstract class BaseFragmentStatePagerAdapter<T> extends FragmentStatePagerAdapter {

    protected List<T> dataList;

    public BaseFragmentStatePagerAdapter(FragmentManager fm, List<T> dataList) {
        super (fm);
        this.dataList = dataList;
    }

    @Override
    public Fragment getItem(int position){
        return obtainItem(position);
    }
    
    public abstract Fragment obtainItem(int position);

    @Override
    public int getCount(){
        return dataList.size ();
    }

}
