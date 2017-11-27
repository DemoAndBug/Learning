package com.rhw.learning.module.recommand;

import com.rhw.learning.module.BaseModel;

import java.util.ArrayList;

/**
 * Author:renhongwei
 * Date:2017/11/26 on 22:39
 */
public class RecommandBodyValue extends BaseModel {
    public int type;
    public String logo;

    @Override
    public String toString() {
        return "RecommandBodyValue{" +
                "type=" + type +
                ", logo='" + logo + '\'' +
                ", title='" + title + '\'' +
                ", info='" + info + '\'' +
                ", price='" + price + '\'' +
                ", text='" + text + '\'' +
                ", site='" + site + '\'' +
                ", from='" + from + '\'' +
                ", zan='" + zan + '\'' +
                ", url=" + url +
                '}';
    }

    public String title;
    public String info;
    public String price;
    public String text;
    public String site;
    public String from;
    public String zan;
    public ArrayList<String> url;


}
