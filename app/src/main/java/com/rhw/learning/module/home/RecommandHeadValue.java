package com.rhw.learning.module.home;

import com.rhw.learning.module.BaseModel;

import java.util.ArrayList;

/**
 * Date:2017/11/26 on 22:41
 * @author Simon
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
