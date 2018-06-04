package com.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.util.regex.Pattern;


public class FileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	private String url;
	private static final Pattern ALLOWED_FILE_NAME = Pattern
            .compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

	public FileServerHandler(String url) {
		this.url=url;
	}
   @Override
   protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request)
			throws Exception {
		if(!request.decoderResult().isSuccess()){//����ʧ��
			sendError(ctx,HttpResponseStatus.BAD_REQUEST);
			return;
		}
		if(request.method()!=HttpMethod.GET){
			sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
		}
		final String uri=request.uri();
		final String path=revoser(uri);
		if(path==null){
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
		    return;
		}
		
		//���ļ�
		File file=new File(path);
		if(file.isHidden()||!file.exists()){
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}
        if (file.isDirectory()) {
            //1. ��/��β���г������ļ�
            if (uri.endsWith("/")) {
                sendListing(ctx, file);
            } else {
                //2. �����Զ�+/
                sendRedirect(ctx, uri + '/');
            }
            return;
        }
		if(!file.isFile()){
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
			return;
		}
		RandomAccessFile accessFile=null;
		try {
			accessFile=new RandomAccessFile(file, "r");
		} catch (Exception e) {
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}
		long fileLength=accessFile.length();
		HttpResponse response=new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		response.headers().add(HttpHeaderNames.CONTENT_LENGTH,fileLength);
		response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/plain");
		//���֧�ֳ����ӣ����ص�ͷ����Ϣ
		if(request.headers().get(HttpHeaderNames.CONNECTION).equals("Keep-Alive")){
			response.headers().set(HttpHeaderNames.CONNECTION,"Keep-Alive");
		}
		ctx.writeAndFlush(response);
		ChannelFuture sendFuture=ctx.writeAndFlush(new ChunkedFile(accessFile,0,fileLength,8192),ctx.newProgressivePromise());
		//�����ֿش���
		sendFuture.addListener(new ChannelProgressiveFutureListener() {
			
			public void operationComplete(ChannelProgressiveFuture future)
					throws Exception {
				 System.err.println("Transfer progress: complete");
				
			}
			public void operationProgressed(ChannelProgressiveFuture future,
					long progress, long total) throws Exception {
				 System.err.println("Transfer progress: " + progress);
			}
		});
		//�����֧�֣����ļ�������ϣ��ر���ͻ�������
		if(!request.headers().get(HttpHeaderNames.CONNECTION).equals("Keep-Alive")){
			//����һ���տ鲢�ر�����
			ctx.write(LastHttpContent.EMPTY_LAST_CONTENT).addListener(ChannelFutureListener.CLOSE);
		}
   }	
   private static void sendListing(ChannelHandlerContext ctx, File dir) {
       FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
       response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
       StringBuilder buf = new StringBuilder();
       String dirPath = dir.getPath();
       buf.append("<!DOCTYPE html>\r\n");
       buf.append("<html><head><title>");
       buf.append(dirPath);
       buf.append(" Ŀ¼��");
       buf.append("</title></head><body>\r\n");
       buf.append("<h3>");
       buf.append(dirPath).append(" Ŀ¼��");
       buf.append("</h3>\r\n");
       buf.append("<ul>");
       buf.append("<li>���ӣ�<a href=\"../\">..</a></li>\r\n");
       for (File f : dir.listFiles()) {
           if (f.isHidden() || !f.canRead()) {
               continue;
           }
           String name = f.getName();
           if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
               continue;
           }
           buf.append("<li>���ӣ�<a href=\"");
           buf.append(name);
           buf.append("\">");
           buf.append(name);
           buf.append("</a></li>\r\n");
       }
       buf.append("</ul></body></html>\r\n");
       
       ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
       response.content().writeBytes(buffer);
       buffer.release();
       ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
   }
   	    //�����Ŀ¼ ������¶�λ��ָ���ļ���
	   private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
	       FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.FOUND);
	       response.headers().set(HttpHeaderNames.LOCATION, newUri);
	       ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	   }
	private String revoser(String uri) {
		 try {
			uri=URLDecoder.decode(uri, "utf-8");
		} catch (Exception e) {
			try {
				uri=URLDecoder.decode(uri, "ISO-8859-1");
			} catch (Exception e2) {
			}
		}
		 if (!uri.startsWith(url)) {
	            return null;
	     }
		 if (!uri.startsWith("/")) {
	            return null;
	      }
		 uri=uri.replace("/", File.separator);
		 if(uri.contains(File.separator+".")||url.contains("."+File.separator)
				 ||uri.startsWith(".")||uri.endsWith(".")
				 ||ALLOWED_FILE_NAME.matcher(uri).matches()){
			 return null;
			 
		 }
		 return System.getProperty("user.dir")+File.separator+uri;
	   }
	    private static void sendError(ChannelHandlerContext ctx,
               HttpResponseStatus status) {
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
		     	status, Unpooled.copiedBuffer("Failure: " + status.toString()
			+ "\r\n", CharsetUtil.UTF_8));
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
			ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	    }
	    @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	            throws Exception {
	        cause.printStackTrace();
	        if (ctx.channel().isActive()) {
	            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
}
