package com.paul.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳检测
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;		// 强制类型转换

            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("读空闲...");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                System.out.println("写空闲...");
            } else if (event.state() == IdleState.ALL_IDLE) {
                // 读写空闲一定时间后关闭那个对应的 Channel
                Channel channel = ctx.channel();
                // 关闭无用的channel，以防资源浪费
                channel.close();

            }
        }
    }
}
