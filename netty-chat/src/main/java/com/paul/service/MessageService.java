package com.paul.service;

import com.paul.entity.Message;

import java.util.List;

public interface MessageService {

    List<Message> notSend(String receiverId);
    int insert(Message t);
    void setHaveRead(List<String> msgIds);
    void setHaveSend(List<String> msgIds);
}
