package com.netty.http.privtpro;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.marshalling.MarshallingDecoder;

import java.awt.event.InputEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.jboss.marshalling.ByteInputStream;
/**
 * byte-》nettymessage 消息解码
 * @author pet-lsf
 *
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder{
	/**
	 * 使用jboss marshalling 序列化  反序列化
	 */
	final MarshallingDecoder marshallingDecoder;
	public NettyMessageDecoder(int maxFrameLength,int lengthFiledOffset,int lengthFiledlength) throws Exception {
		super(maxFrameLength,lengthFiledOffset,lengthFiledlength);
		marshallingDecoder=MarshallingCodeCFactory.buildMarshallingDecoder();
	}
	
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in)
			throws Exception {
		//按照长度先解码
		ByteBuf byf=(ByteBuf)super.decode(ctx, in);
		marshallingDecoder.setSingleDecode(true);
		marshallingDecoder.channelRegistered(ctx);
		byte[] b=in.array();
		//输出到指定文件夹
		File file=new File("d:/decode.txt");
		OutputStream outputStream=new FileOutputStream(file);
		outputStream.write(b);
		outputStream.flush();
		outputStream.close();
		return super.decode(ctx, in);
	}
	

}
