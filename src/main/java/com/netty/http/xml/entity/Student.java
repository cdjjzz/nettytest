package com.netty.http.xml.entity;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * 使用jaxb序列化
 * @author pet-lsf
 *
 */
@SuppressWarnings("restriction")
@XmlRootElement
public class Student {
	
	private String name;
	private String address;
	private int age;
	private String phone;
	private List<String> childList;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public List<String> getChildList() {
		return childList;
	}
	public void setChildList(List<String> childList) {
		this.childList = childList;
	}
	@Override
	public String toString() {
		return "Student [name=" + name + ", address=" + address + ", age="
				+ age + ", phone=" + phone + ", childList=" + childList + "]";
	}
	

}
