package com.netty.http.xml;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.util.List;
public class HttpXmlRequestDecoder extends AbstractXmlDecoder<FullHttpRequest>{
	
	public HttpXmlRequestDecoder(Class<?> cls) {
		super.cls=cls;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg,
			List<Object> out) throws Exception {
		if(!msg.decoderResult().isSuccess()){
			sendError(ctx,HttpResponseStatus.BAD_REQUEST);
			return;
		}
		HttpXmlRequest resHttpXmlResponse = new HttpXmlRequest(msg, decode0(ctx, msg.content())); 
		out.add(resHttpXmlResponse); 
	}
	private void sendError(ChannelHandlerContext ctx,HttpResponseStatus status){
		 FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,  
	                Unpooled.copiedBuffer("Ê§°Ü: " + status.toString() + "\r\n", CharsetUtil.UTF_8));  
	        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");  
	        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);  
	}


}
