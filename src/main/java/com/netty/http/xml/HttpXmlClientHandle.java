package com.netty.http.xml;

import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.netty.http.xml.entity.ClassI;

/**
 * 客户端最后一个通道，最先执行
 * @author pet-lsf
 *
 * @param <T>
 */
public class HttpXmlClientHandle<T> extends ExtentsChannelHandler<ClassI,HttpXmlRequest> {
	
	private Class<T> entity;
	
	@SuppressWarnings("unchecked")
	public HttpXmlClientHandle() {
		super();
	  ParameterizedType parameterizedType=(ParameterizedType)getClass().getGenericSuperclass();
	  Type tt=parameterizedType.getActualTypeArguments()[0];
	  entity=(Class<T>)tt;
	}
	
	/**
	 * 客户端与服务端连接事件触发
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//构造 t
		HttpXmlRequest request=new HttpXmlRequest(null,null);
		T t=init();
		request.setMsg(t);
		ctx.writeAndFlush(request);
	}
	/**
	 * 与服务器连接出现错误
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		//ctx.close 会关闭 所有通道
		ctx.close();
	}
	@SuppressWarnings("unchecked")
	private T init(){
		try {
			T t=entity.newInstance();
			Field[] f=entity.getDeclaredFields();
			for (Field field:f) {
				field.setAccessible(true);
				if(field.getType()==String.class){
					StringBuffer sb=new StringBuffer();
					for (int i = 0; i<6; i++) {
						sb.append(Helper.getRandomChar());
					}
					field.set(t, sb.toString());
				}
				if(field.getType()==char.class||field.getType()==Character.class){
					field.set(t, Helper.getRandomChar());
				}
				if(field.getType()==byte.class||field.getType()==Byte.class){
					field.set(t, Helper.getRandom());
				}
				if(field.getType()==int.class||
						field.getType()==Integer.class||
						field.getType()==long.class||
						field.getType()==Long.class||
						field.getType()==short.class||
						field.getType()==Short.class){
					field.set(t, Helper.getRandom());
				}
				if(field.getType()==float.class||
						field.getType()==Float.class||
						field.getType()==double.class||
						field.getType()==Double.class){
					field.set(t, Helper.getRandomIEEE());
				}
				if(field.getType()==BigDecimal.class){
					field.set(t, new BigDecimal(Helper.getRandom()));
				}
				if(field.getType()==Date.class){
					field.set(t, Helper.currtDate());
				}
				if(field.getType()==boolean.class||field.getType()==Boolean.class){
					field.set(t, true);
				}
				if(field.getType()==List.class){
					Class<?> cls=(Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
					Object object=initChT(cls);
					Object o=field.get(t);
					List<Object> list=null;
					if(o!=null){
						if(o instanceof Set)
							list=(List<Object>)o;
						else
							continue;
					}else{
						list=new ArrayList<Object>();
					}
					list.add(object);
					field.set(t, list);
				}
				if(field.getType()==Set.class){
					Class<?> cls=(Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
					Object object=initChT(cls);
					Object o=field.get(t);
					Set<Object> set=null;
					if(o!=null){
						if(o instanceof Set)
						set=(Set<Object>)o;
						else
							continue;
					}else{
						set=new HashSet<Object>();
					}
					set.add(object);
					field.set(t, set);
				}
				if(field.getType()==Map.class){
					Class<?> key=(Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
					Class<?> val=(Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[1];
					Object o=field.get(t);
					Map<Object,Object> map=null;
					if(o!=null){
						if(o instanceof Map)
						map=(Map<Object,Object>)o;
						else
							continue;
					}else{
						map=new HashMap<Object,Object>();
					}
					Object k=initChT(key);
					Object v=initChT(val);
					map.put(k, v);
					field.set(t, map);
				}
			}
			return t;
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}
	private Object initChT(Class<?> pacls){
		try {
			if(pacls==String.class){
				StringBuffer sb=new StringBuffer();
				for (int i = 0; i<6; i++) {
					sb.append(Helper.getRandomChar());
				}
				return sb.toString();
			}
			Object t=pacls.newInstance();
			Field[] f=pacls.getDeclaredFields();
			for (Field field:f) {
				field.setAccessible(true);
				if(field.getType()==String.class){
					StringBuffer sb=new StringBuffer();
					for (int i = 0; i<6; i++) {
						sb.append(Helper.getRandomChar());
					}
					field.set(t, sb.toString());
				}
				if(field.getType()==char.class||field.getType()==Character.class){
					field.set(t, Helper.getRandomChar());
				}
				if(field.getType()==byte.class||field.getType()==Byte.class){
					field.set(t, Helper.getRandom());
				}
				if(field.getType()==int.class||
						field.getType()==Integer.class||
						field.getType()==long.class||
						field.getType()==Long.class||
						field.getType()==short.class||
						field.getType()==Short.class){
					field.set(t, Helper.getRandom());
				}
				if(field.getType()==float.class||
						field.getType()==Float.class||
						field.getType()==double.class||
						field.getType()==Double.class){
					field.set(t, Helper.getRandomIEEE());
				}
				if(field.getType()==BigDecimal.class){
					field.set(t, new BigDecimal(Helper.getRandom()));
				}
				if(field.getType()==Date.class){
					field.set(t, Helper.currtDate());
				}
				if(field.getType()==boolean.class||field.getType()==Boolean.class){
					field.set(t, true);
				}
				if(field.getType()==List.class){
					Class<?> cls=(Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
					Object object=initChT(cls);
					Object o=field.get(t);
					List<Object> list=null;
					if(o!=null){
						if(o instanceof Set)
							list=(List<Object>)o;
						else
							continue;
					}else{
						list=new ArrayList<Object>();
					}
					list.add(object);
					field.set(t, list);
				}
				if(field.getType()==Set.class){
					Class<?> cls=(Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
					Object object=initChT(cls);
					Object o=field.get(t);
					Set<Object> set=null;
					if(o!=null){
						if(o instanceof Set)
						set=(Set<Object>)o;
						else
							continue;
					}else{
						set=new HashSet<Object>();
					}
					set.add(object);
					field.set(t, set);
				}
				if(field.getType()==Map.class){
					Class<?> key=(Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
					Class<?> val=(Class<?>)((ParameterizedType)field.getGenericType()).getActualTypeArguments()[1];
					Object o=field.get(t);
					Map<Object,Object> map=null;
					if(o!=null){
						if(o instanceof Map)
						map=(Map<Object,Object>)o;
						else
							continue;
					}else{
						map=new HashMap<Object,Object>();
					}
					Object k=initChT(key);
					Object v=initChT(val);
					map.put(k, v);
					field.set(t, map);
				}
			}
			return t;
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpXmlRequest msg)
			throws Exception {
		System.out.println("The client receive response of http header is : " 
				+ msg.getRequest().headers().names());  
		System.out.println("The client receive response of http body is : " 
				+ msg.getMsg());  
		
	}

}
