package com.netty.http.privtpro;

import java.util.List;

import com.netty.common.NeException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage>{

	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg,
			List<Object> out) throws Exception {
		if(msg==null||msg.getHeader()==null){
			throw new NeException();
		}
	}

}
