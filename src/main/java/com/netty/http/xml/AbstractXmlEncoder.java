package com.netty.http.xml;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

@SuppressWarnings("restriction")
public abstract class AbstractXmlEncoder<T> extends MessageToMessageEncoder<T> {
	final static String CHARSET_NAME = "UTF-8";  
    final static Charset UTF_8 = Charset.forName(CHARSET_NAME);  
    
    protected ByteBuf encode0(ChannelHandlerContext ctx, Object body) throws Exception { 
    	StringWriter writer=new StringWriter();
		JAXBContext context=JAXBContext.newInstance(body.getClass());    
		Marshaller marshaller=context.createMarshaller();
		marshaller.marshal(body, writer);
		System.out.println(writer.toString());
        ByteBuf encodeBuf = Unpooled.copiedBuffer(writer.toString(), UTF_8);  
        return encodeBuf;  
    }  
}
