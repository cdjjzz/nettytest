package com.netty.nioSocket;

import java.net.InetSocketAddress;
import java.nio.channels.AcceptPendingException;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * 使用aio实现通信
 * @author pet-lsf
 *
 */
public class AsycTimeServerHandler implements Runnable {
	
	CountDownLatch countDownLatch;
	AsynchronousServerSocketChannel asynchronousServerSocketChannel;
	//初始化异步通道，绑定端口
	public AsycTimeServerHandler(int port) throws Exception{
		asynchronousServerSocketChannel=AsynchronousServerSocketChannel.open();
		asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
	}
	

	public void run() {
		//异步 主线程等待异步线程执行完成
		countDownLatch=new CountDownLatch(1);
		doAccept();
	}
	//
	private void doAccept(){
		asynchronousServerSocketChannel.accept();
	}
	

}
