package com.clt.runman.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * Created by yanshengli on 15-1-22.
 */
public abstract class RunManBaseAdapter<T> extends BaseAdapter {

    protected Context context;//运行上下文
    protected List<T> listData;//数据集合
    protected LayoutInflater listContainer;//视图容器
    protected int itemViewResource;//自定义项视图源id

    public RunManBaseAdapter(Context context, List<T> data,int resource)
    {
        this.context = context;
        this.listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
        this.itemViewResource = resource;
        this.listData = data;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public T getItem(int arg0) {
        return listData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }
}
