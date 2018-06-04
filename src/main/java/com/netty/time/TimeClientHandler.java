package com.netty.time;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TimeClientHandler extends SimpleChannelInboundHandler<String>  {
	private int counter;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		System.out.println("now date is :"+msg+" and this count="+counter++);
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 100; i++) {
			ByteBuf buf=Unpooled.copiedBuffer("你好，当前时间是多少?\r\n".getBytes(Charset.forName("utf-8")));
			ctx.writeAndFlush(buf);
		}
	}


}
