package com.netty.time;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
	public static void main(String[] args) throws Exception {
		EventLoopGroup boss=new NioEventLoopGroup();
		EventLoopGroup work=new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap=new ServerBootstrap();
			bootstrap.group(boss, work)
			.channel(NioServerSocketChannel.class)
			.childOption(ChannelOption.SO_BACKLOG, 1024)
			.childHandler(new ChildChannelHandler());
			 //绑定端口
			 ChannelFuture future=bootstrap.bind(12345).sync();
			 //客户端断开连接
			 future.channel().closeFuture().sync();
		}finally{
			boss.shutdownGracefully();
			work.shutdownGracefully();
		}
	}

}
