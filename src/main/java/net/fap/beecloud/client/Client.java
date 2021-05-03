package net.fap.beecloud.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Synapse Entry
 *
 * BeeCloud 客户端
 *
 * @author catrainbow
 */

public class Client {

    private int port;
    private String ip;

    public Client(int port) {
        this.ip = "127.0.0.1";
        this.port = port;
    }

    public void run() throws IOException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ClientIniterHandler());
        try {
            Channel channel = bootstrap.connect(ip, port).sync().channel();
            while (true) {
                /**
                 * @pk 数据包
                 * @sendDataPacket 发送数据包的方法
                 * @channel 远程连接
                 */
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String pk = reader.readLine();
                if (StringUtils.isNotEmpty(pk)) {
                        this.sendDataPacket(channel,pk);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 向服务端发送数据包
     *
     * @param packet 字符串数据包(BeeCloudPacket)
     */
    private void sendDataPacket(Channel channel,String packet)
    {
        channel.writeAndFlush(packet);
    }


    /**
     * 测试方法
     */
    /*

    public static void main(String[] args) throws Exception {
        new Client(8888).run();
    }

     */


}