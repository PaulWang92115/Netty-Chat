package com.paul.netty;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.paul.entity.Data;
import com.paul.entity.Message;
import com.paul.enums.ActionEnum;
import com.paul.service.MessageService;
import com.paul.util.IdChannelMapping;
import com.paul.util.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


// websocket 协议需要用帧来传递参数
// 客户与服务端的通信采用 josn 的形式
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //用来管理所有的 Channel
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception{


        //获取 service 用来插入数据
        final MessageService messageService = (MessageService) SpringUtil.getBean("messageService");
        System.out.println("2222:"+msg.text());
        final Data receive = JSON.parseObject(msg.text(),Data.class);
        int action = receive.getAction();


        if(action == ActionEnum.CONNECT.getType()){
            System.out.println(111);
            //action 名为连接，但是 channel 的连接并不是通过这个 action，这个
            //action 只是为了我们保存一些 mapping 关系，比如用户 id 和 channel
            final String senderId = receive.getMessage().getSenderId();
            IdChannelMapping.mapping.put(senderId,ctx.channel());


            NettyWebSocketServer.submit(new Runnable() {
                @Override
                public void run() {
                    //读取数据库，将所有 未发送 的消息发送给指定用户,前段自己去根据 id 筛选是和谁的消息
                    List<Message> l = messageService.notSend(senderId);
                    List<String> ids = new ArrayList<>();
                    //2.发送给某个用户或者群
                    Channel rchannel = IdChannelMapping.mapping.get(senderId);
                    if(null != l && l.size() > 0) {
                        if (null == rchannel) {
                            //用户离线，需要在用户上线时扫描在数据库中给他的未发送消息
                            // app 可以使用离线消息推送
                        } else {
                            Channel channel = group.find(rchannel.id());
                            if (channel == null) {
                                for (Message m : l) {
                                    ids.add(m.getMsgId());
                                    Data d = new Data();
                                    d.setMessage(m);
                                    channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(d)));
                                }
                            } else {
                                //用户离线，需要在用户上线时扫描在数据库中给他的未发送消息
                                // app 可以使用离线消息推送
                            }
                        }
                        messageService.setHaveSend(ids);
                    }
                }
            });


        }else if(action == ActionEnum.CHAT.getType()){
            // 用户发送聊天消息

            Data d = new Data();
            d.setMessage(receive.getMessage());

            //2.发送给某个用户或者群
            Channel rchannel = IdChannelMapping.mapping.get(receive.getMessage().getReceiverId());
            if(null == rchannel){
                System.out.println("123");
                //用户离线，需要在用户上线时扫描在数据库中给他的未发送消息
                // app 可以使用离线消息推送
                NettyWebSocketServer.submit(new Runnable() {
                    @Override
                    public void run() {
                        receive.getMessage().setRead(false);
                        receive.getMessage().setSend(false);
                        receive.getMessage().setCreateDate(new Date());
                        messageService.insert(receive.getMessage());
                    }
                });
            }else{
                Channel c = group.find(rchannel.id());
                if(c != null){
                    System.out.println(234);
                    c.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(d)));
                    NettyWebSocketServer.submit(new Runnable() {
                        @Override
                        public void run() {
                            receive.getMessage().setRead(false);
                            receive.getMessage().setSend(true);
                            receive.getMessage().setCreateDate(new Date());
                            messageService.insert(receive.getMessage());
                        }
                    });
                }else{
                    System.out.println("111111111111111:"+group.size());
                    System.out.println(456);
                    //用户离线，需要在用户上线时扫描在数据库中给他的未发送消息
                    // app 可以使用离线消息推送
                    NettyWebSocketServer.submit(new Runnable() {
                        @Override
                        public void run() {
                            receive.getMessage().setRead(false);
                            receive.getMessage().setSend(false);
                            receive.getMessage().setCreateDate(new Date());
                            messageService.insert(receive.getMessage());
                        }
                    });
                }
            }



        }else if(action == ActionEnum.SIGNED.getType()){
            // 客户端查看某条消息后，将这条消息标为已读
            final List<String> read = receive.getRead();
            // 更新数据库,将消息更新为已读
            NettyWebSocketServer.submit(new Runnable() {
                @Override
                public void run() {
                    messageService.setHaveRead(read);
                }
            });

        }else if(action == ActionEnum.KEEPALIVE.type){
            //心跳包,如果没有心跳包，服务端在触发条件后会主动关闭连接
            System.out.println("收到来自channel为[" + ctx.channel() + "]的心跳包...");
        }

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
        System.out.println("handlerAdded" + ctx.channel().id().asLongText());
        group.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
        System.out.println("handlerRemoved" + ctx.channel().id().asLongText());
        group.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
        group.remove(ctx.channel());
    }

}
