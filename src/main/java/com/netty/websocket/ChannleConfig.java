package com.netty.websocket;

import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.traffic.GlobalChannelTrafficShapingHandler;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * ȫ������
 * �洢�ͻ������ӵ�chanle
 * @author pet-lsf
 *
 */
public class ChannleConfig {
	
	public  static ChannelGroup group=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
}
