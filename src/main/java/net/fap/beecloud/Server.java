package net.fap.beecloud;

/**
 * BeeCloud Server 服务端
 *
 * 基于Netty框架
 *
 * @author catrainbow
 */

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

    /**
     * 服务端端口
     */
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        new Server(8888).run();
    }


    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ServerIniterHandler());
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            Channel channel = bootstrap.bind(port).sync().channel();
            System.out.println("- Start BeeCloud Server on: "+port+" -");
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
