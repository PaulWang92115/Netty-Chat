package com.paul.service;

import com.paul.entity.Message;
import com.paul.mapper.MessageMapper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;


    @Override
    public List<Message> notSend(String receiverId) {
        Example e = new Example(Message.class);
        e.createCriteria().andEqualTo("receiverId", receiverId).andEqualTo("isSend",false);
        List<Message> list = messageMapper.selectByExample(e);
        return list;
    }

    @Override
    public int insert(Message t) {
        int res = messageMapper.insert(t);
        return  res;
    }

    @Override
    public void setHaveRead(List<String> msgIds) {
        messageMapper.updateRead(msgIds);
    }

    @Override
    public void setHaveSend(List<String> msgIds) {
        messageMapper.updateSend(msgIds);
    }

}
