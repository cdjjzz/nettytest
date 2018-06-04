package com.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
/**
 * 初始化 ChannelHandler 组件
 * @author pet-lsf
 *
 */
public class WebScoketInit extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast("http-codec",new HttpServerCodec())
		.addLast("gggregator",new HttpObjectAggregator(65536))
		.addLast("http-chunked",new ChunkedWriteHandler())
		.addLast(new WebScoketMsg());
	}

}
