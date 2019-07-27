package com.paul.spring;

import com.paul.netty.NettyWebSocketServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class NettyBootStrap implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if(contextRefreshedEvent.getApplicationContext().getParent() == null){
            try {
                System.out.println("********************************开始启动聊天服务器******************************");
                NettyWebSocketServer.getInstance().start();
            } catch (InterruptedException e) {
                System.out.println("聊天服务器启动失败");
                e.printStackTrace();
            }
        }
    }
}
