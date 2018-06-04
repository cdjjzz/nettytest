package com.netty.common;

public class NeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String parames;
	private Throwable parentException;
	
	public NeException() {
	}
	public NeException(String id,String parames){
		this.id=id;
		this.parames=parames;
	}
	
	public NeException(String id,String parames,Throwable exception){
		this.id=id;
		this.parames=parames;
		this.parentException=exception;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParames() {
		return parames;
	}
	public void setParames(String parames) {
		this.parames = parames;
	}
	public Throwable getParentException() {
		return parentException;
	}
	public void setParentException(Throwable parentException) {
		this.parentException = parentException;
	}
	@Override
	public String getMessage() {
		return structMessage();
	}
	
	private String structMessage(){
		StringBuffer sb=new StringBuffer();
		Throwable ex=this;
		while(ex!=null){
			if(ex instanceof NeException){
				NeException e=(NeException)ex;
				sb.append(e.getParames());
				ex=((NeException)ex).parentException;
			}else{
				sb.append(ex.getMessage());
			}
		}
		return sb.toString();
	}
	

}
