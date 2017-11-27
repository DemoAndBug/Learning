package com.rhw.learning.module.recommand;

import com.rhw.learning.module.BaseModel;

import java.util.ArrayList;

/**
 * Author:renhongwei
 * Date:2017/11/26 on 22:41
 */
public class RecommandHeadValue extends BaseModel {
    public ArrayList<String> ads;
    public ArrayList<String> middle;
    public ArrayList<RecommandFooterValue> footer;

    @Override
    public String toString() {
        return "RecommandHeadValue{" +
                "ads=" + ads +
                ", middle=" + middle +
                ", footer=" + footer +
                '}';
    }
}
