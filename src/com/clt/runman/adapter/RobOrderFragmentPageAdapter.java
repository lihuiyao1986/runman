/**
 * @Title: RobOrderFragmentAdapter.java
 * @Package com.clt.runman.adapter
 * @Description: TODO(用一句话描述该文件做什么)
 * @author A18ccms A18ccms_gmail_com
 * @date 2015年4月10日 下午2:27:37
 * @Copyright:Copyright (c)
 * @Company:whty李焱生
 * @version V1.0
 */
package com.clt.runman.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.clt.runman.fragment.RobOrderFragment;

/**
 *@Description:TODO(这里用一句话描述这个类的作用)
 *@Author:李焱生
 *@Since:2015年4月10日下午2:27:37  
 */
public class RobOrderFragmentPageAdapter extends BaseFragmentPagerAdapter<RobOrderFragment> {

    public RobOrderFragmentPageAdapter(FragmentManager fm, List<RobOrderFragment> dataList) {
        super (fm, dataList);
    }

    @Override
    public Fragment obtainItem(int position){
        return dataList.get (position);
    }

    @Override
    public CharSequence getPageTitle(int position){
        RobOrderFragment fragment = dataList.get (position);
        return fragment.getTitle ();
    }
}
