package com.rhw.learning.jpush;

import com.rhw.learning.module.BaseModel;

/**
 * Author:renhongwei
 * Date:2017/12/6 on 11:42
 */
public class PushMessage extends BaseModel {
    // 消息类型
    public String messageType = null;
    // 连接
    public String messageUrl = null;
    // 详情内容
    public String messageContent = null;
}
