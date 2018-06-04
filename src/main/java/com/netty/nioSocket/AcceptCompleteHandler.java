package com.netty.nioSocket;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public class AcceptCompleteHandler implements CompletionHandler<Integer, ByteBuffer> {

	public void completed(Integer result, ByteBuffer attachment) {
		// TODO Auto-generated method stub
		
	}

	public void failed(Throwable exc, ByteBuffer attachment) {
		// TODO Auto-generated method stub
		
	}

}
