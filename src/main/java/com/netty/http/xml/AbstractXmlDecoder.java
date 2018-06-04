package com.netty.http.xml;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

@SuppressWarnings("restriction")
public abstract class AbstractXmlDecoder<T> extends MessageToMessageDecoder<T> {
	
	public Class<?> cls;
	
    final static Charset GBK = Charset.forName("GBK");  
    
    protected T decode0(ChannelHandlerContext ctx, ByteBuf  buf) throws Exception { 
    	String content = buf.toString(GBK);  
    	ByteArrayInputStream inputStream=new ByteArrayInputStream(content.getBytes());
		JAXBContext context=JAXBContext.newInstance(cls);    
		Unmarshaller unmarshaller=context.createUnmarshaller();
	    @SuppressWarnings("unchecked")
		T t=(T) unmarshaller.unmarshal(inputStream);
        return t;  
    }  
}
