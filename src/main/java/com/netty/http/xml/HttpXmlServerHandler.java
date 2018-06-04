package com.netty.http.xml;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

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


public class HttpXmlServerHandler<T> extends ExtentsChannelHandler<ClassI, HttpXmlRequest> {
	

	private Class<T> entity;
	
	@SuppressWarnings("unchecked")
	public HttpXmlServerHandler() {
		super();
	  ParameterizedType parameterizedType=(ParameterizedType)getClass().getGenericSuperclass();
	  Type tt=parameterizedType.getActualTypeArguments()[0];
	  entity=(Class<T>)tt;
	}
	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, HttpXmlRequest msg)
			throws Exception {
		    FullHttpRequest request = msg.getRequest();  
	        T t = init();
	        System.out.println("Http server receive request : " + t);  
	        System.out.println(t);  
	        ChannelFuture future = ctx.writeAndFlush(new HttpXmlResponse(null, t));  
	        if (!request.headers().get(HttpHeaderNames.CONNECTION).equals("keep_alive")){  
	        	future.addListener(new GenericFutureListener<Future<? super Void>>() {
					public void operationComplete(Future<? super Void> future)
							throws Exception {
						ctx.close();
					}
				});
	        }  
	}
	/**
	 * 与服务器连接出现错误
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		 // 在链路没有关闭并且出现异常的时候发送给客户端错误信息  
        if (ctx.channel().isActive()) {  
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);  
        }  
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
	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {  
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,  
                Unpooled.copiedBuffer("失败: " + status.toString() + "\r\n", CharsetUtil.UTF_8));  
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");  
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);  
    }  

}
