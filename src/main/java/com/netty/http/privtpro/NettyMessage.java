package com.netty.http.privtpro;

import com.netty.http.privtpro.entity.Header;

/**
 * 定义私有协议消息体 参考http
 * @author pet-lsf
 *
 */
public class NettyMessage {
	private Header header;
	private Object body;
	
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	public int hashCode() {
		return body.hashCode();
	}
	public boolean equals(Object obj) {
		return body.equals(obj);
	}
	public String toString() {
		return body.toString();
	}
	
}
