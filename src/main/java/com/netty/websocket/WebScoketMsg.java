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
		//使用http请求连接WebSocket
		if(msg instanceof FullHttpRequest){
			handHttpRequest(ctx,(FullHttpRequest)msg);
		}else if(msg instanceof WebSocketFrame){
			//连接成功后。WebSocket开始通信
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
			throw new Exception("不支持二进制消息");
		}
		//处理文本消息，返回应答消息
		if(msg instanceof TextWebSocketFrame){
			TextWebSocketFrame frame=(TextWebSocketFrame)msg;
			System.out.println("客户端发送消息："+frame.text());
			TextWebSocketFrame frame2=new TextWebSocketFrame("服务端发送数据：你好"+frame.text());
			//群发 
			ChannleConfig.group.writeAndFlush(frame2);
		}
		
	}
	/**
	 * 处理http	请求
	 * @param ctx
	 * @param req
	 */
	private void handHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
		//处理http请求
		/**
		 * 使用http建立websocket 连接
		 * 
		 * 在http头部协议添加
		 * Upgrade:websocket;
		 * Connection:Upgrade;
		 * (告诉http服务器，我使用http请求，但是使用websocket协议)
		 * 
		 * Sec-WebSocket-Key: x3JJHMbDL1EzLkh9GBhXDw==
		 * Sec-WebSocket-Protocol: chat, superchat
		 * Sec-WebSocket-Version: 13
		 * 
		 * webscoket 版本协议，验证
		 * 
		 * 服务器返回头
		 * 
		 * HTTP/1.1 101 Switching Protocols
		 * 返回101 响应码 http-》websocket
         * Upgrade: websocket
         * Connection: Upgrade
         * Sec-WebSocket-Accept: HSmrc0sMlYUkAGmm5OPpG2HaGWk=
         * 能
         * Sec-WebSocket-Protocol: chat
		 * 
		 */
		if(!req.decoderResult().isSuccess()||!("websocket").equals(req.headers().get("Upgrade"))){
			sendWebSocketMsg(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.BAD_REQUEST));
			return;
		}
		//处理websocket请求
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
	 * 客户端与服务端连接，添加连接channel到全局配置
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端连接服务端成功");
		ChannleConfig.group.add(ctx.channel());
	}
	/**
	 * 客户端与服务端连接关闭
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端断开服务端成功");
		ChannleConfig.group.remove(ctx.channel());
	}
	/**
	 * 服务端接受客户端发送的数据 将数据发送出去
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	/**
	 * 出现异常
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.out.println("客户端与服务端出现异常");
		cause.printStackTrace();
		ctx.close();
	}
	

}
