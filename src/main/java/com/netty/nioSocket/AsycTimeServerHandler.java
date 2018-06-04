package com.netty.nioSocket;

import java.net.InetSocketAddress;
import java.nio.channels.AcceptPendingException;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * ʹ��aioʵ��ͨ��
 * @author pet-lsf
 *
 */
public class AsycTimeServerHandler implements Runnable {
	
	CountDownLatch countDownLatch;
	AsynchronousServerSocketChannel asynchronousServerSocketChannel;
	//��ʼ���첽ͨ�����󶨶˿�
	public AsycTimeServerHandler(int port) throws Exception{
		asynchronousServerSocketChannel=AsynchronousServerSocketChannel.open();
		asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
	}
	

	public void run() {
		//�첽 ���̵߳ȴ��첽�߳�ִ�����
		countDownLatch=new CountDownLatch(1);
		doAccept();
	}
	//
	private void doAccept(){
		asynchronousServerSocketChannel.accept();
	}
	

}
