package com.netty.time;

import java.nio.charset.Charset;
import java.sql.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TimeServerHandler extends SimpleChannelInboundHandler<String>{
	private int counter;
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		System.out.println("this time server receive order " +new String(msg.getBytes(),"utf-8")+" and  this count ="+counter++);
		String currentTime=new Date(System.currentTimeMillis()).toString()+"\r\n";
		ByteBuf resp=Unpooled.copiedBuffer(currentTime.getBytes(Charset.forName("utf-8")));
		ctx.writeAndFlush(resp);
	}
}
