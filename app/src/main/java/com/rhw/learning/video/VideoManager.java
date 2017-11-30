package com.rhw.learning.video;

import android.content.Intent;
import android.view.ViewGroup;

import com.rhw.learning.okhttp.ResponseEntityToModule;
import com.rhw.learning.video.monitor.VideoValue;
import com.rhw.learning.video.widget.CustomVideoView;

/**
 * Author:renhongwei
 * Date:2017/11/30 on 14:55
 */
public class VideoManager implements VideoContral.VideoContralListener {

    //the ad container
    private ViewGroup mParentView;

    private VideoContral mAdSlot;
    private VideoValue mInstance = null;
    //the listener to the app layer
    private VideoInterface mListener;
    private CustomVideoView.FrameImageLoadListener mFrameLoadListener;

    public VideoManager(ViewGroup parentView, String instance,
                     CustomVideoView.FrameImageLoadListener frameLoadListener) {
        this.mParentView = parentView;
        this.mInstance = (VideoValue) ResponseEntityToModule.
                parseJsonToModule(instance, VideoValue.class);
        this.mFrameLoadListener = frameLoadListener;
        load();
    }


    /**
     * init the ad,不调用则不会创建videoview
     */
    public void load() {
        if (mInstance != null && mInstance.resource != null) {
            mAdSlot = new VideoContral(mInstance, this, mFrameLoadListener);
            //发送解析成功事件
            //sendAnalizeReport(Params.ad_analize, Constant.AD_DATA_SUCCESS);
        } else {
            mAdSlot = new VideoContral(null, this, mFrameLoadListener); //创建空的slot,不响应任何事件
            if (mListener != null) {
                mListener.onVideoFailed();
            }
           // sendAnalizeReport(Params.ad_analize, HttpConstant.AD_DATA_FAILED);
        }
    }

    /**
     * release the ad
     */
    public void destroy() {
        mAdSlot.destroy();
    }

    public void setVideoResultListener(VideoInterface listener) {
        this.mListener = listener;
    }
    /**
     * 根据滑动距离来判断是否可以自动播放, 出现超过50%自动播放，离开超过50%,自动暂停
     */
    public void updateAdInScrollView() {
        if (mAdSlot != null) {
            mAdSlot.updateAdInScrollView();
        }
    }


    @Override
    public ViewGroup getParentGroup() {
        return mParentView;
    }

    @Override
    public void onVideoLoadSuccess() {
        if (mListener != null) {
            mListener.onVideoSuccess();
        }
    }

    @Override
    public void onVideoLoadFailed() {
        if (mListener != null) {
            mListener.onVideoFailed();
        }
    }

    @Override
    public void onVideoLoadComplete() {
    }

    @Override
    public void onClickVideo(String url) {
        if (mListener != null) {
            mListener.onClickVideo(url);
        } else {
            Intent intent = new Intent(mParentView.getContext(), BrowserActivity.class);
            intent.putExtra(BrowserActivity.KEY_URL, url);
            mParentView.getContext().startActivity(intent);
        }
    }
}
