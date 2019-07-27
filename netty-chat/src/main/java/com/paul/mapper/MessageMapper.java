package com.paul.mapper;

import com.paul.entity.Message;
import com.paul.framework.BaseMapper;

import java.util.List;

public interface MessageMapper extends BaseMapper<Message> {

    void updateRead(List<String> msgIds);
    void updateSend(List<String> msgIds);
}
