package com.paul.entity;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {

    private int action;  //传送数据类型，对应 ActionEnum
    private Message  message; //聊天内容
    private List<String> read; //已读消息集合，用来将某些消息设置为已读

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<String> getRead() {
        return read;
    }

    public void setRead(List<String> read) {
        this.read = read;
    }
}
