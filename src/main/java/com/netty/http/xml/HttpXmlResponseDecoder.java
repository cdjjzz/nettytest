package com.netty.http.xml;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.util.List;

public class HttpXmlResponseDecoder extends AbstractXmlDecoder<DefaultFullHttpResponse> {
	
	public HttpXmlResponseDecoder(Class<?> cls) {
		super.cls=cls;
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx,
			DefaultFullHttpResponse msg, List<Object> out) throws Exception {
	      HttpXmlResponse resHttpXmlResponse = new HttpXmlResponse(msg, decode0(ctx, msg.content()));  
		  out.add(resHttpXmlResponse); 
	}

	
	
	

}
