package com.netty.encoder;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
 * 使用MessagePack 序列化
 * MessageToByteEncoder object -> byte
 * @author pet-lsf
 *
 */
public class MsgPackEncoder extends MessageToByteEncoder<Object> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out)
			throws Exception {
		MessagePack messagePack=new MessagePack();
		//messagePack.write(msg) 序列化-》 将字节写入buf
		out.writeBytes(messagePack.write(msg));
	}

}
