package com.jktd.ipms.control.thread;


import javax.annotation.PreDestroy;

import com.jktd.ipms.custom.properties.NetProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

@Component
public class BeatServer extends Thread implements InitializingBean {
    // 缓冲区长度
    @Autowired
    private BeatServerHandler beatServerHandler;

    @Autowired
    private NetProperties netProperties;

    private EventLoopGroup group;
    private Bootstrap b;
    private Channel channel;
    /**
     * 定位引擎信息
     */
    private int port;

    private boolean callListen = false;

    public BeatServer() {

    }

    public void startListen() {
        this.callListen = true;
    }
    /**
     * 开始监听
     */
    private void listen() {
        try {
            group = new NioEventLoopGroup();
            b = new Bootstrap();
            b.group(group).channel(NioDatagramChannel.class).handler(beatServerHandler);
            channel = b.bind(port).sync().channel();
            channel.closeFuture().await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    @Override
    public void run() {

        while (true) {
            if (callListen) {
                this.callListen = false;
                this.listen();
            } else {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void unbind() {
        group.shutdownGracefully();
    }

    /**
     * 销毁UDP链接
     */
    @PreDestroy
    public void finish() {
        group.shutdownGracefully();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub
        this.port = Integer.parseInt(netProperties.getBeatport());
        this.startEngine();
        try {
            this.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public boolean startEngine() {
        if ((this.getChannel() == null) || (!this.getChannel().isActive())) {
            this.startListen();
            return true;
        } else {
            return false;
        }
    }
}
