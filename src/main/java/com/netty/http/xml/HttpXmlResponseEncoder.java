package com.netty.http.xml;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.util.List;

public class HttpXmlResponseEncoder extends AbstractXmlEncoder<HttpXmlResponse> {

	@Override
	protected void encode(ChannelHandlerContext ctx, HttpXmlResponse msg,
			List<Object> out) throws Exception {
			System.out.println(msg.getData());
		    ByteBuf body = encode0(ctx, msg.getData());  
		    FullHttpResponse response = msg.getFullHttpResponse();  
	        if (response == null) {  
	          response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK, body);  
	        } else {  
	          response = new DefaultFullHttpResponse(msg.getFullHttpResponse()  
	                .protocolVersion(), msg.getFullHttpResponse().status(),  
	                 body);  
	         }  
	       response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/xml");  
	       response.headers().set(HttpHeaderNames.CONTENT_LENGTH,body.readableBytes());
	       out.add(response);  
	}

}
