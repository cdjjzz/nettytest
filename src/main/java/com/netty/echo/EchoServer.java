package com.netty.echo;

import java.nio.channels.Channel;
import java.nio.charset.Charset;

import com.netty.encoder.MsgPackDecoder;
import com.netty.encoder.MsgPackEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {
	public void bind(int port) throws Exception{
		EventLoopGroup boss=new NioEventLoopGroup();
		EventLoopGroup work=new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap=new ServerBootstrap();
			bootstrap
			.group(boss, work)
			.channel(NioServerSocketChannel.class)
				.childOption(ChannelOption.TCP_NODELAY, true)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						ByteBuf buf=Unpooled.copiedBuffer("\r\n".getBytes());
						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
						ch.pipeline().addLast(new StringDecoder(Charset.forName("utf-8")));
						ch.pipeline().addLast(new MsgPackDecoder());
						ch.pipeline().addLast(new MsgPackEncoder());
						ch.pipeline().addLast(new EchoServerHandler());
						
					}
					
				});
			ChannelFuture future=bootstrap.bind(port).sync();
			future.channel().closeFuture().sync();
		} catch (Exception e) {
		}finally{
			boss.shutdownGracefully();
			work.shutdownGracefully();
		}
		
		
	}
	public static void main(String[] args)  throws Exception{
		new EchoServer().bind(12345);
	}

}
