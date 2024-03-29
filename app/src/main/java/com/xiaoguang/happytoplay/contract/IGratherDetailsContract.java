package com.xiaoguang.happytoplay.contract;

import android.widget.GridView;

import com.xiaoguang.happytoplay.activity.IBaseView;
import com.xiaoguang.happytoplay.bean.Grather;
import com.xiaoguang.happytoplay.bean.User;
import com.xiaoguang.happytoplay.presenter.IBasePresenter;
import com.xiaoguang.happytoplay.view.XListView;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;

/**
 * 契约接口，表明 View和Presenter之间的方法
 * Created by 11655 on 2016/9/26.
 */

public interface IGratherDetailsContract {
    interface IGratherDetailsView extends IBaseView<IGratherDetailsPresenter> {
        /**
         * Toast形式提示
         */
        void showMsg(String msg);

        /**
         * 加载提示框
         */

        void showLoading(String title, String msg, boolean flag);

        /**
         * 隐藏提示框
         */
        void hiddenLoading();

        /**
         * 页面跳转
         */
        void jumpActivity();

        /**
         * 返回
         *
         * @return
         */
        boolean back();

        /**
         * 设置数据
         *
         * @param object
         */
        void setData(Object object);

        /**
         * 获取控件中的数据
         */
        void getData();

        /**
         * 改变按钮的状态
         * @param str
         * @param i
         * @param state 按钮的状态
         */
        void setViewState(String str, int i,boolean state);
    }

    interface IGratherDetailsPresenter extends IBasePresenter {
        /**
         * 加载数据
         *
         * @param o
         */
        void loadingData(Object o);

        /**
         * 设置图片
         *
         * @param imgFiles             数据源
         * @param mActGratherDetialsGv GridView  控件
         */
        void setImages(List<BmobFile> imgFiles, GridView mActGratherDetialsGv);

        /**
         * 获取评论信息，并显示到控件上
         *
         * @param objectId              当前活动的评论id
         * @param mActGratherDetialsXlv 要显示的控件
         */
        void showDiscuss(String objectId, XListView mActGratherDetialsXlv);

        /**
         * 发送评论
         *
         * @param GratherObjectId 当前选中的活动id
         * @param text            评论内容
         * @param userObjecId     评论人的用户id
         */
        void sendDiscuss(String GratherObjectId, String text, String userObjecId);

        /**
         * 查询失败返回的方法
         *
         * @param e
         */
        void queryUseError(BmobException e);

        /**
         * 查询信息成功
         */
        void queryUserSuccess();

        /**
         * 参加活动
         *
         * @param grather 当前的活动
         * @param user    当前的用户，参加活动的人
         * @param orderId 付款的订单编号
         */
        void joinGrather(Grather grather, User user,String orderId);
    }
}
