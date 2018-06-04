package com.netty.encoder;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MsgPackDecoder  extends MessageToMessageDecoder<ByteBuf>{

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf,
			List<Object> out) throws Exception {
		byte[] array=new byte[buf.readableBytes()];
		//��buf��ȡ�ֽ�
		buf.getBytes(buf.readableBytes(), array,0,array.length);
		//ʹ��MessagePack ������ byte-��object
		MessagePack messagePack=new MessagePack();
		out.add(messagePack.read(array));
	}

}
