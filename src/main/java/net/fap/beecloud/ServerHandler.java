package net.fap.beecloud;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 连接的子服务器
     */
    public static final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 数据数据包
     *
     * @param channelHandlerContext 连接
     * @param pk 数据包
     * @throws Exception
     */
    protected void handleDataPacket(ChannelHandlerContext channelHandlerContext, String pk)
            throws Exception {
        Channel channel = channelHandlerContext.channel();
        for (Channel ch : group) {
            if (ch == channel) {
                ch.writeAndFlush("[you]: " + pk + "\n");
            } else {
                ch.writeAndFlush("[" + channel.remoteAddress() + "]: " + pk + "\n");
            }
        }
        System.out.println("[" + channel.remoteAddress() + "]: " + pk + "\n");
    }


    /**
     * 收到新的子服务器连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        boolean active = channel.isActive();
        if (active) {
            System.out.print(channel.remoteAddress()+" 连接了到了 BeeCloud Server");
        } else {
            System.out.print(channel.remoteAddress()+" 断开了连接");
        }
    }

    /**
     * 断开连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        if (!channel.isActive()) {
            System.out.print(channel.remoteAddress()+" 断开了连接");
        } else {
            System.out.print(channel.remoteAddress()+" 连接了到了 BeeCloud Server");
        }
    }

    /**
     * 数据包接收方法
     *
     * @param channelHandlerContext 连接
     * @param packet 数据包
     * @throws Exception
     */
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String packet) throws Exception {
        this.handleDataPacket(channelHandlerContext,packet);
    }

}