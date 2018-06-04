package com.netty.http.xml;

import io.netty.handler.codec.http.FullHttpRequest;
/**
 * «Î«Û∂‘œÛ
 * @author pet-lsf
 *
 * @param <T>
 */
public class HttpXmlRequest {
	
	
	
	public HttpXmlRequest(FullHttpRequest request, Object msg) {
		super();
		this.request = request;
		this.msg = msg;
	}

	private FullHttpRequest request;
	
	private Object msg;

	public FullHttpRequest getRequest() {
		return request;
	}

	public void setRequest(FullHttpRequest request) {
		this.request = request;
	}

	public Object getMsg() {
		return msg;
	}

	public void setMsg(Object msg) {
		this.msg = msg;
	}
	
	

}
