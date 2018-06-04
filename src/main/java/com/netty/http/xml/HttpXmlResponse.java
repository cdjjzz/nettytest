package com.netty.http.xml;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * ���ض���  FullHttpResponse(http����)+t(xml������Ķ���)
 * @author pet-lsf
 *
 * @param <T>
 */
public class HttpXmlResponse{
	private FullHttpResponse fullHttpResponse;
	
	private Object data;
	

	public HttpXmlResponse(FullHttpResponse fullHttpResponse, Object data) {
		super();
		this.fullHttpResponse = fullHttpResponse;
		this.data = data;
	}

	public FullHttpResponse getFullHttpResponse() {
		return fullHttpResponse;
	}

	public void setFullHttpResponse(FullHttpResponse fullHttpResponse) {
		this.fullHttpResponse = fullHttpResponse;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
