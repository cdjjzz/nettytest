package com.netty.http.xml.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * 使用jaxb序列化
 * @author pet-lsf
 *
 */
@SuppressWarnings("restriction")
@XmlRootElement
public class ClassI {
	private Set<Student> student;
	private String class_id;
	private String description;
	private Map<String, String> map;
	private Set<String> s;
	private List<Student> list; 
 	public Set<Student> getStudent() {
		return student;
	}
	public void setStudent(Set<Student> student) {
		this.student = student;
	}
	public String getClass_id() {
		return class_id;
	}
	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}
	
	public Set<String> getS() {
		return s;
	}
	public void setS(Set<String> s) {
		this.s = s;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
	public List<Student> getList() {
		return list;
	}
	public void setList(List<Student> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "ClassI [student=" + student + ", class_id=" + class_id
				+ ", description=" + description + ", map=" + map + ", s=" + s
				+ ", list=" + list + "]";
	}
	
	
	
	
}
