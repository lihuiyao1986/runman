package com.clt.runman.db.dao;

import java.util.List;

import android.content.Context;

import com.clt.runman.db.RunManDataBase;
import com.clt.runman.db.model.WashcarPicInfoDaoModel;

/**
 * 洗车照片相关的dao
 * 
 * @author yanshengli
 * @since 2015-3-19
 */
public class WashcarPicInfoDao extends BaseDao {

    /** 上下文对象 **/
    private Context                  context;

    /** WashcarBeforePicDao对象 **/
    private static WashcarPicInfoDao dao;

    private WashcarPicInfoDao(Context context) {
        this.context = context;
    }

    public static WashcarPicInfoDao newInstance(Context ctx){
        if (dao == null) {
            dao = new WashcarPicInfoDao (ctx);
        }
        return dao;
    }

    /**
     * 判断对应的照片是否存在
     * 
     * @param picInfo
     * @return
     */
    public List<WashcarPicInfoDaoModel> isPicExists(WashcarPicInfoDaoModel picInfo){

        String where = "picSubtype =" + picInfo.getPicSubtype () + " and picType = " + picInfo.getPicType () + " and picDate = '" + picInfo.getPicDate () + "'"
                + " and runmanid = '" + picInfo.getRunmanid () + "'" + " and orderid = '" + picInfo.getOrderid () + "'" + " and picIndex = "
                + picInfo.getPicIndex ();
        List<WashcarPicInfoDaoModel> list = RunManDataBase.newInstance (context).database ().findAllByWhere (WashcarPicInfoDaoModel.class, where);
        return list;
    }

    /**
     * 保存或者更新图片信息
     * 
     * @param picInfo
     */
    public void saveOrUpdatePicInfo(WashcarPicInfoDaoModel picInfo){
        List<WashcarPicInfoDaoModel> list = isPicExists (picInfo);
        if (list == null || list.isEmpty ()) {
            RunManDataBase.newInstance (context).database ().save (picInfo);
        } else {
            RunManDataBase.newInstance (context).database ().update (picInfo, "id = " + list.get (0).getId ());
        }
    }

    /**
     * 查询所有的记录
     * @return
     */
    public List<WashcarPicInfoDaoModel> queryPicInfoList(WashcarPicInfoDaoModel picInfo){
        String where = "picType =" + picInfo.getPicType () + " and picDate = '" + picInfo.getPicDate () + "'" + " and runmanid = '" + picInfo.getRunmanid ()
                + "'" + " and orderid = '" + picInfo.getOrderid () + "'";
        List<WashcarPicInfoDaoModel> list = RunManDataBase.newInstance (context).database ().findAllByWhere (WashcarPicInfoDaoModel.class, where);
        return list;
    }

    /**
     * 查询某个日期之前的所有照片信息
     * @param Date
     * @return
     */
    public List<WashcarPicInfoDaoModel> queryPicInfoListBeforeDate(String date){
        String where = "picDate <= '" + date + "'";
        List<WashcarPicInfoDaoModel> list = RunManDataBase.newInstance (context).database ().findAllByWhere (WashcarPicInfoDaoModel.class, where);
        return list;
    }

    /**
     * 删除对应的照片信息
     * @param picInfo
     */
    public void deletePicInfo(WashcarPicInfoDaoModel picInfo){
        RunManDataBase.newInstance (context).database ().delete (picInfo);
    }

    /**
     *@Description: 更新照片的信息
     *@Author: 李焱生
     *@Since: 2015年4月8日上午10:15:11
     *@param picInfo
     */
    public void updatePicInfo(WashcarPicInfoDaoModel picInfo){
        RunManDataBase.newInstance (context).database ().update (picInfo);
    }

    /**
     * 
    * @Title: queryPicInfoByFileName 
    * @Description: TODO(查询洗车后拍照的整体照片) 
    * @param @param model
    * @param @return    
    * @return List<WashcarPicInfoDaoModel>    
    * @throws
     */
    public List<WashcarPicInfoDaoModel> queryPicInfoByFileName(WashcarPicInfoDaoModel model){
        String where = "runmanid='" + model.getRunmanid () + "' and picType=" + model.getPicType () + " and orderid='" + model.getOrderid ()
                + "' and picSubtype=" + model.getPicSubtype () + " and picIndex=" + model.getPicIndex ();
        return RunManDataBase.newInstance (context).database ().findAllByWhere (WashcarPicInfoDaoModel.class, where);
    }

    /**
     * 
    * @Title: queryAllWashCarAfterListByRunManId 
    * @Description: TODO(查询所有未上传的洗车后整体照片) 
    * @param @param runManId
    * @param @return    
    * @return List<WashcarPicInfoDaoModel>    
    * @throws
     */
    public List<WashcarPicInfoDaoModel> queryAllWashCarAfterListByRunManId(String runManId){
        String where = "runmanid='" + runManId + "' and picSubtype=0 and picType=1 and isUpload=0";
        return RunManDataBase.newInstance (context).database ().findAllByWhere (WashcarPicInfoDaoModel.class, where);
    }
}
