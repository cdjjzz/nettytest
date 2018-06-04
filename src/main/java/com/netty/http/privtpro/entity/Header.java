package com.netty.http.privtpro.entity;

import java.util.HashMap;
import java.util.Map;

public class Header {
	private int code=0xabef0101;
	private long length;//��Ϣ���峤��
	private long sessionId;//�Ựid;
	private byte opcode;//��������
	private byte priority;//���ȼ�
	private Map<String,Object> attchment=new HashMap<String, Object>();//����չ��Ϣͷ
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public long getLength() {
		return length;
	}
	public void setLength(long length) {
		this.length = length;
	}
	public byte getOpcode() {
		return opcode;
	}
	public void setOpcode(byte opcode) {
		this.opcode = opcode;
	}
	public byte getPriority() {
		return priority;
	}
	public void setPriority(byte priority) {
		this.priority = priority;
	}
	public Map<String, Object> getAttchment() {
		return attchment;
	}
	public void setAttchment(Map<String, Object> attchment) {
		this.attchment = attchment;
	}
	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	@Override
	public String toString() {
		return "Header [code=" + code + ", length=" + length + ", sessionId="
				+ sessionId + ", opcode=" + opcode + ", priority=" + priority
				+ ", attchment=" + attchment + "]";
	}
	

}
