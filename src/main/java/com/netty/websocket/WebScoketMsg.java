package com.netty.websocket;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

public class WebScoketMsg extends SimpleChannelInboundHandler<Object> {
	
	private WebSocketServerHandshaker handshaker;
	
	private String url="ws://localhost:15000/websocket";
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		//ʹ��http��������WebSocket
		if(msg instanceof FullHttpRequest){
			handHttpRequest(ctx,(FullHttpRequest)msg);
		}else if(msg instanceof WebSocketFrame){
			//���ӳɹ���WebSocket��ʼͨ��
			handWebSocketFrame(ctx,(WebSocketFrame)msg);
		}

	}
	private void handWebSocketFrame(ChannelHandlerContext ctx,
			WebSocketFrame msg) throws Exception{
		if(msg instanceof CloseWebSocketFrame){
			handshaker.close(ctx.channel(),(CloseWebSocketFrame)msg.retain());
		}
		if(msg instanceof PingWebSocketFrame){
			ctx.channel().write(new PongWebSocketFrame(msg.content().retain()));
			return;
		}
		if(msg instanceof BinaryWebSocketFrame){
			throw new Exception("��֧�ֶ�������Ϣ");
		}
		//�����ı���Ϣ������Ӧ����Ϣ
		if(msg instanceof TextWebSocketFrame){
			TextWebSocketFrame frame=(TextWebSocketFrame)msg;
			System.out.println("�ͻ��˷�����Ϣ��"+frame.text());
			TextWebSocketFrame frame2=new TextWebSocketFrame("����˷������ݣ����"+frame.text());
			//Ⱥ�� 
			ChannleConfig.group.writeAndFlush(frame2);
		}
		
	}
	/**
	 * ����http	����
	 * @param ctx
	 * @param req
	 */
	private void handHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		//����http����
		/**
		 * ʹ��http����websocket ����
		 * 
		 * ��httpͷ��Э�����
		 * Upgrade:websocket;
		 * Connection:Upgrade;
		 * (����http����������ʹ��http���󣬵���ʹ��websocketЭ��)
		 * 
		 * Sec-WebSocket-Key: x3JJHMbDL1EzLkh9GBhXDw==
		 * Sec-WebSocket-Protocol: chat, superchat
		 * Sec-WebSocket-Version: 13
		 * 
		 * webscoket �汾Э�飬��֤
		 * 
		 * ����������ͷ
		 * 
		 * HTTP/1.1 101 Switching Protocols
		 * ����101 ��Ӧ�� http-��websocket
         * Upgrade: websocket
         * Connection: Upgrade
         * Sec-WebSocket-Accept: HSmrc0sMlYUkAGmm5OPpG2HaGWk=
         * ��
         * Sec-WebSocket-Protocol: chat
		 * 
		 */
		if(!req.decoderResult().isSuccess()||!("websocket").equals(req.headers().get("Upgrade"))){
			sendWebSocketMsg(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.BAD_REQUEST));
			return;
		}
		//����websocket����
		WebSocketServerHandshakerFactory factory=new WebSocketServerHandshakerFactory(url,null, false);
		handshaker=factory.newHandshaker(req);
		if(handshaker==null){
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		}else{
			handshaker.handshake(ctx.channel(), req);	
		}
	}
	private void sendWebSocketMsg(ChannelHandlerContext ctx,FullHttpRequest req,DefaultFullHttpResponse resp){
		if(resp.status().code()!=200){
			ByteBuf buf=Unpooled.copiedBuffer(resp.status().toString(),Charset.forName("utf-8"));
			resp.content().writeBytes(buf);
			buf.release();
		}
		ChannelFuture channelFuture=ctx.channel().writeAndFlush(resp);
		if(resp.status().code()!=200){
			channelFuture.addListener(ChannelFutureListener.CLOSE);
		}
	}
	/**
	 * �ͻ������������ӣ��������channel��ȫ������
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("�ͻ������ӷ���˳ɹ�");
		ChannleConfig.group.add(ctx.channel());
	}
	/**
	 * �ͻ������������ӹر�
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("�ͻ��˶Ͽ�����˳ɹ�");
		ChannleConfig.group.remove(ctx.channel());
	}
	/**
	 * ����˽��ܿͻ��˷��͵����� �����ݷ��ͳ�ȥ
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	/**
	 * �����쳣
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.out.println("�ͻ��������˳����쳣");
		cause.printStackTrace();
		ctx.close();
	}
	

}
