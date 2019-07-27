package com.paul.enums;

public enum MessageEnum {

    ONETOONE(1,"1对1聊天"),
    ONETOALL(2,"群聊");

    public final int type;
    public final String desc;

    MessageEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
