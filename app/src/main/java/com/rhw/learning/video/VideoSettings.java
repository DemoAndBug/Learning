package com.rhw.learning.video;

import com.rhw.learning.constant.Constant;

/**
 * Author:renhongwei
 * Date:2017/11/30 on 14:38
 */
public final class VideoSettings {

    //用来记录可自动播放的条件
    private static Constant.AutoPlaySetting currentSetting = Constant.AutoPlaySetting.AUTO_PLAY_3G_4G_WIFI; //默认都可以自动播放

    public static void setCurrentSetting(Constant.AutoPlaySetting setting) {
        currentSetting = setting;
    }

    public static Constant.AutoPlaySetting getCurrentSetting() {
        return currentSetting;
    }

}
