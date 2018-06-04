package com.netty.http.xml;


import com.netty.http.xml.entity.ClassI;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
/**
 * http xml server·þÎñÆ÷
 * @author pet-lsf
 *
 */
public class HttpXmlServer {
	public void start(int port){
		EventLoopGroup boss=new NioEventLoopGroup();
		EventLoopGroup work=new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap=new ServerBootstrap();
			bootstrap.group(boss, work).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast("request-decoder",new HttpRequestDecoder());
						ch.pipeline().addLast("http",new HttpObjectAggregator(65536));
						ch.pipeline().addLast("xml-decoder",new HttpXmlRequestDecoder(ClassI.class));
						ch.pipeline().addLast("http-response",new HttpResponseEncoder());
						ch.pipeline().addLast("xml-encode",new HttpXmlResponseEncoder());
						ch.pipeline().addLast("xx",new HttpXmlServerHandler<ClassI>());
					}
					
				});
		   ChannelFuture future=bootstrap.bind(port).sync();
		   future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			boss.shutdownGracefully();
			work.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		new HttpXmlServer().start(12345);
	}

}
