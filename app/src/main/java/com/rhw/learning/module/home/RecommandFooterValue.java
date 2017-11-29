package com.rhw.learning.module.home;

import com.rhw.learning.module.BaseModel;

/**
 * Author:renhongwei
 * Date:2017/11/26 on 22:41
 */
public class RecommandFooterValue extends BaseModel {

    public String title;
    public String info;
    public String from;
    public String imageOne;
    public String imageTwo;
    public String destationUrl;

    @Override
    public String toString() {
        return "RecommandFooterValue{" +
                "title='" + title + '\'' +
                ", info='" + info + '\'' +
                ", from='" + from + '\'' +
                ", imageOne='" + imageOne + '\'' +
                ", imageTwo='" + imageTwo + '\'' +
                ", destationUrl='" + destationUrl + '\'' +
                '}';
    }
}
