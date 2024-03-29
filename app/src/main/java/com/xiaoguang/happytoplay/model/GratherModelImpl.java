package com.xiaoguang.happytoplay.model;

import com.xiaoguang.happytoplay.bean.Grather;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * 活动操作的model
 * Created by 11655 on 2016/10/8.
 */

public class GratherModelImpl {
    /**
     * 数据源，用于个人中中心更多界面的数据
     */
    private List<String> datas = new ArrayList<>();
    /**
     * 文件批量上传
     *
     * @param filePaths      文件的路径的数组
     * @param uploadCallBack 回调方法
     */
    public void Upload(String[] filePaths, final UploadCallBack uploadCallBack) {
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                uploadCallBack.onSuccess(list, list1);
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
                uploadCallBack.onProcess(i, i1, i2, i3);
            }

            @Override
            public void onError(int i, String s) {
                uploadCallBack.onError(i, s);
            }
        });
    }

    /**
     * 保存数据到服务器
     *
     * @param grather      活动bean对象
     * @param saveCallBack 回调方法
     */
    public void Save(Grather grather, final SaveCallBack saveCallBack) {
        grather.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                saveCallBack.done(objectId, e);
            }
        });

    }

    /**
     * 查询收藏的活动
     *　@param datas  S数据源
     * @param queryCallBack 回调接口
     *
     */
    public void queryGrather(List datas,final QueryCallBack<Grather> queryCallBack) {
        //初始化数据源
        this.datas = datas;
        //调用查询800
        queryGrather(800,null,0,0,null,queryCallBack);
    }
    /**
     * 查询活动的
     *
     * @param queryType     查询类型，值为 100时，为默认查询10条数据；值为200时 ，值为300，值为400时 为类型查询<br/>
     *                      </> 500:距离查询  600:点赞数查询  700:为免费活动查询
     * @param dataType
     * @param skip
     * @param counts
     * @param queryCallBack 查询的回调方法
     * @param bmobGeoPoint 距离查询，配合500使用
     */
    public void queryGrather(final int queryType, String dataType, int skip, int counts,BmobGeoPoint bmobGeoPoint, final QueryCallBack<Grather> queryCallBack) {
        final BmobQuery<Grather> query = new BmobQuery<>();
        switch (queryType) {
            case 100:
                //设置默认查询10条
                query.setLimit(counts);
                break;
            case 200:
                query.setLimit(counts);
                //设置跳过skip条数据
                query.setSkip(skip);
                break;
            case 300:
                //添加查询条件，相当于where objectId  = dataType
                query.addWhereEqualTo("objectId", dataType);
                break;
            case 400:
                //添加查询条件，相当于where gratherType = dataType
                query.addWhereEqualTo("gratherType", dataType);
                break;
            case 500://距离查询
                query.addWhereNear("gratherGeoPoint",bmobGeoPoint);
                query.setLimit(10);    //获取最接近用户地点的10条数据
                break;
            case 600://根据根据点赞数量较多的数据进行查询，默认点赞数量大于5
                break;
            case 700://查询免费活动
                query.addWhereEqualTo("gratherMoney",0);
                break;
            case 800://查询收藏
                query.addWhereContainedIn("objectId",datas);
                break;
            case 900://发起活动查询
                query.addWhereEqualTo("gratherOriginator",dataType);
                break;
            default:return;
        }
        //查询数据
        query.findObjects(new FindListener<Grather>() {
            @Override
            public void done(List<Grather> list, BmobException e) {
                if (queryType == 600){//点赞查询默认显示几条数据
                    List<Grather> lists = new ArrayList<Grather>();
                    for (Grather grather:
                            list) {
                        if (grather.getLoveUserIds().size()>5){//点赞数大于5则显示数据
                            lists.add(grather);
                        }
                    }
                    queryCallBack.done(lists,e);
                }else {
                    queryCallBack.done(list, e);
                }
            }
        });
    }

    /**
     * 更新活动的方法
     * @param objectId  活动ID
     * @param grather   新的活动对象
     * @param callBack  回调方法
     */
    public void Update(String objectId, Grather grather, final UpdateCallBack callBack){
        grather.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                callBack.done(e);
            }
        });
    }
    /**
     * 批量上传的回调接口
     */
    public interface UploadCallBack {
        void onSuccess(List<BmobFile> list, List<String> list1);

        void onProcess(int i, int i1, int i2, int i3);

        void onError(int i, String s);
    }

    /**
     * 保存用户数据的回调接口
     */
    public interface SaveCallBack {
        void done(String objectId, BmobException e);
    }

    /**
     * 查询的回调接口
     *
     * @param <T>
     */
    public interface QueryCallBack<T> {
        void done(List<T> result, BmobException e);
    }

    public interface UpdateCallBack{
        void done(BmobException e);
    }
}
