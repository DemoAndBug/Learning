package com.rhw.learning.video;

import com.rhw.learning.constant.Constant;

/**
 * Date:2017/11/30 on 14:38
 * @author Simon
 */
public final class VideoSettings {

    //用来记录可自动播放的条件 //默认都可以自动播放
    private static Constant.AutoPlaySetting currentSetting = Constant.AutoPlaySetting.AUTO_PLAY_3G_4G_WIFI;

    public static void setCurrentSetting(Constant.AutoPlaySetting setting) {
        currentSetting = setting;
    }

    public static Constant.AutoPlaySetting getCurrentSetting() {
        return currentSetting;
    }

}
