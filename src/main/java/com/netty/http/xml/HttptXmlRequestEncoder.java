package com.netty.http.xml;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.net.InetAddress;
import java.util.List;
public  class HttptXmlRequestEncoder extends AbstractXmlEncoder<HttpXmlRequest>{

	@Override
	protected void encode(ChannelHandlerContext ctx, HttpXmlRequest msg,
			List<Object> out) throws Exception {
        // ���ø����encode0������Order����ת��Ϊxml�ַ������������װΪByteBuf  
        ByteBuf body = encode0(ctx, msg.getMsg());  
        FullHttpRequest request = msg.getRequest();  
        // ��requestΪ�գ����½�һ��FullHttpRequest���󣬲���������Ϣͷ  
        if (request == null) {  
            // �ڹ��췽���У���body����Ϊ������Ϣ��  
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/do", body);  
            HttpHeaders headers = request.headers();  
            // ��ʾ����ķ�������ַ  
            headers.set(HttpHeaderNames.HOST, InetAddress.getLocalHost().getHostAddress());  
            // Connection��ʾ�ͻ���������������ͣ�Keep-Alive��ʾ�����ӣ�CLOSE��ʾ������  
            // header�а�����ֵΪclose��connection����������ǰ����ʹ�õ�tcp��������������Ϻ�ᱻ�ϵ���  
            // �Ժ�client�ٽ����µ�����ʱ�ͱ��봴���µ�tcp�����ˡ�  
            headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);  
            // �����֧�ֵ�ѹ�������� gzip �� deflate  
            headers.set(HttpHeaderNames.ACCEPT_ENCODING,  
                    HttpHeaderValues.GZIP.toString() + ',' + HttpHeaderValues.DEFLATE.toString());  
            // �����֧�ֵĽ��뼯  
            headers.set(HttpHeaderNames.ACCEPT_CHARSET, "ISO-8859-1,utf-8;q=0.7,*;q=0.7");  
            // �����֧�ֵ�����  
            headers.set(HttpHeaderNames.ACCEPT_LANGUAGE, "zh");  
            // ʹ�õ��û������� Netty xml Http Client side  
            headers.set(HttpHeaderNames.USER_AGENT, "Netty xml Http Client side");  
            // �����֧�ֵ� MIME����,����˳��Ϊ������  
            headers.set(HttpHeaderNames.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");  
        }  
        // ���ڴ˴�û��ʹ��chunk��ʽ������Ҫ������Ϣͷ��������Ϣ���CONTENT_LENGTH  
        request.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, body.readableBytes());  
        // ��������Ϣ��ӽ�out�У�������ı���������Ϣ���б���  
        out.add(request);  
    }  
}
