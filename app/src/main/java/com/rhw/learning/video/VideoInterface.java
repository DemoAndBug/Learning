package com.rhw.learning.video;

/**
 * Author:renhongwei
 * Date:2017/11/30 on 14:53
 * Function: 最终通知应用层视频是否成功
 */
public interface VideoInterface {

    void onVideoSuccess();

    void onVideoFailed();

    void onClickVideo(String url);
}
