package com.paul.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NettyWebSocketServer {

    private static NettyWebSocketServer instance = new NettyWebSocketServer();

    private NettyWebSocketServer(){};

    public static NettyWebSocketServer getInstance(){
        return instance;
    }

    private static Executor executor = Executors.newCachedThreadPool();

    public static void submit(Runnable t){
        executor.execute(t);
    }

    public void start() throws InterruptedException {
        //定义两个线程组，事件循环组,可以类比与 Tomcat 就是死循环，不断接收客户端的连接
        // boss 线程组不断从客户端接受连接，但不处理，由 worker 线程组对连接进行真正的处理
        // 一个线程组其实也能完成，推荐使用两个
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 服务端启动器
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //group 方法有两个，一个接收一个参数，另一个接收两个参数
            // childhandler 是我们自己写的请求处理器
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new WebSocketInitializer());
            //绑定端口
            ChannelFuture future = serverBootstrap.bind(8013).sync();
            System.out.println("聊天服务器启动成功，监听端口：8013");
            //channel 关闭的监听
            future.channel().closeFuture().sync();
        }finally {
            //优雅的关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
