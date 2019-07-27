package com.paul.util;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户 id 和 它与服务器的 channel 的 mapping
 */
public class IdChannelMapping {

    public static Map<String, Channel> mapping = new ConcurrentHashMap<>();

}
