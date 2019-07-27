package com.paul.entity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "t_message")
public class Message {

    @Id
    @Column(name = "msgId")
    private String msgId; // 消息 id

    @Column(name = "messageType")
    private int messageType;  //消息类型，私聊或者群聊，对应 MessageEnum

    @Column(name = "senderId")
    private String senderId; //发送人的 ID

    @Column(name = "receiverId")
    private String receiverId; // 接收人的 id，或者群组 id

    @Column(name = "msg")
    private String msg; //消息内容

    @Column(name = "isRead")
    private Boolean isRead; //是否已读

    @Column(name = "isSend")
    private Boolean isSend; //是否发送给对方

    @Column(name = "createDate")
    private Date createDate; //创建时间



    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Boolean getSend() {
        return isSend;
    }

    public void setSend(Boolean send) {
        isSend = send;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
