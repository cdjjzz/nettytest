package com.netty.echo;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EchoServerHandler extends SimpleChannelInboundHandler<String>
		implements ChannelHandler {
	
	private int counter;
	/**
	 * 读取
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		System.out.println("this is count:"+ ++counter+" times receive data:"+msg);
		msg+="\r\n";
		ByteBuf rsp=Unpooled.copiedBuffer(msg.getBytes(Charset.forName("utf-8")));
		ctx.writeAndFlush(rsp);
	}
	/**
	 * 出现异常
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
