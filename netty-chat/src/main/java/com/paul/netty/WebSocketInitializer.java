package com.paul.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class WebSocketInitializer extends ChannelInitializer<SocketChannel> {
    //需要支持 websocket，我们在 initChannel 是做一点改动
    @Override
    protected void initChannel(SocketChannel ch) throws Exception{
        ChannelPipeline pipeline = ch.pipeline();
        //因为 websocket 是基于 http 的，所以要加入 http 相应的编解码器
        pipeline.addLast(new HttpServerCodec());
        //以块的方式进行写的处理器
        pipeline.addLast(new ChunkedWriteHandler());
        // 进行 http 聚合的处理器，将 HttpMessage 和 HttpContent 聚合到 FullHttpRequest 或者
        // FullHttpResponse
        //HttpObjectAggregator 在基于 netty 的 http 编程使用的非常多，粘包拆包。
        pipeline.addLast(new HttpObjectAggregator(8192));
        //对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());

        /**********************************对于 http 的支持*********************************************/


        // 针对客户端，如果在1分钟时没有向服务端发送读写心跳(ALL)，则主动断开
        // 如果是读空闲或者写空闲，不处理
        pipeline.addLast(new IdleStateHandler(8, 10, 12));
        // 自定义的空闲状态检测
        pipeline.addLast(new HeartBeatHandler());

        // 针对 websocket 的类,完成 websocket 构建的所有繁重工作，负责握手，以及心跳（close，ping，
        // pong）的处理， websocket 通过 frame 帧来传递数据。
        // BinaryWebSocketFrame，CloseWebSocketFrame，ContinuationWebSocketFrame，
        // PingWebSocketFrame，PongWebSocketFrame，TextWebSocketFrame。
        // /ws 是 context_path，websocket 协议标准，ws://server:port/context_path
        pipeline.addLast(new WebSocketServerProtocolHandler("/netty/ws"));
        pipeline.addLast(new WebSocketHandler());
    }
}
