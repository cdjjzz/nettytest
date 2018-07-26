package com.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ResetReaderIndexTest {
	public static void main(String[] args) {
		ByteBuf buf=Unpooled.buffer(10, 16);
		buf.writeInt(1);
	    System.out.println(buf.readableBytes());
	    System.out.println(buf.readInt());
	    //buf.resetReaderIndex();
	    System.out.println(buf.readableBytes());
		
	}

}
