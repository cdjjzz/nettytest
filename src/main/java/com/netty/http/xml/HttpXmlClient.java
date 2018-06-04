package com.netty.http.xml;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

import java.net.InetSocketAddress;

import com.netty.http.xml.entity.ClassI;
public class HttpXmlClient {
	
	public void connect(int port) throws Exception{
		EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
		try {
			Bootstrap bootstrap=new Bootstrap();
			  bootstrap.group(eventLoopGroup)
			  .channel(NioSocketChannel.class)
			  .option(ChannelOption.TCP_NODELAY, true)
			  .handler(new ChannelInitializer<SocketChannel>() {
				  protected void initChannel(SocketChannel ch) throws Exception {
					  /**
					   * xml 接受服务器数据 解码器
					   */
					 ch.pipeline().addLast("http-decoder",new HttpResponseDecoder());
					 ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
					 ch.pipeline().addLast("xml-decoder",new HttpXmlResponseDecoder(ClassI.class));
					 /**
					  * 发送
					  * http 编码
					  */
					 ch.pipeline().addLast("http-encoder", new HttpRequestEncoder());
					 /**
					  * xml  编码
					  */
					 ch.pipeline().addLast("xml-encoder",new HttptXmlRequestEncoder());
					 ch.pipeline().addLast("xmlClientHandler",new HttpXmlClientHandle<ClassI>());
				  };
			});
			 //异步通知
			ChannelFuture future=bootstrap.connect(new InetSocketAddress(port)).sync();
			//关闭通道
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			eventLoopGroup.shutdownGracefully();
		}
	}
	public static void main(String[] args) throws Exception{
		new HttpXmlClient().connect(12345);
	}

}
