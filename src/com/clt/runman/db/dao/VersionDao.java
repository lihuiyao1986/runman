package com.clt.runman.db.dao;

import java.util.List;

import android.content.Context;

import com.clt.runman.db.RunManDataBase;
import com.clt.runman.model.VersionInfomation;

/**
 * 
* @ClassName: VersionDao 
* @Description: TODO(版本信息数据库管理) 
* @author fuxianwei 
* @date 2015-4-10 下午3:48:25 
*
 */
public class VersionDao extends BaseDao {

    private static VersionDao instance;

    private Context           mContext;

    public static VersionDao newInstance(Context context){
        if (instance == null) instance = new VersionDao (context);
        return instance;
    }

    private VersionDao() {

    }

    private VersionDao(Context context) {
        this.mContext = context;
    }

    /*
     * 保存版本信息
     */
    public void saveVersion(VersionInfomation version){
        deleteVersion ();
        RunManDataBase.newInstance (mContext).database ().save (version);
    }

    /*
     * 清空数据库版本信息
     */
    public void deleteVersion(){
        RunManDataBase.newInstance (mContext).database ().deleteAll (VersionInfomation.class);
    }

    /*
     * 获取最新版本信息
     */
    public VersionInfomation getVersion(){
        List<VersionInfomation> list = RunManDataBase.newInstance (mContext).database ().findAll (VersionInfomation.class);
        if (list == null || list.isEmpty ()) return null;
        return list.get (0);
    }
}
