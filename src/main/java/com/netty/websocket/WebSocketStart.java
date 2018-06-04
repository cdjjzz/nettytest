package com.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WebSocketStart {
	public static void main(String[] args) throws Exception{
		EventLoopGroup boss=new NioEventLoopGroup();
		EventLoopGroup work=new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap=new ServerBootstrap();
			ChannelFuture channelFuture=bootstrap.group(boss, work)
					.channel(NioServerSocketChannel.class)
					.childHandler(new WebScoketInit())
			.bind(15000).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			boss.shutdownGracefully();
			work.shutdownGracefully();
			System.out.println(e.getMessage());
		}finally{
			
		}
	}
}
