package com.huayin.printmanager.pay.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author mys
 *
 */
public class HttpUtil {
	
	/**
	 * 获取客户端IP
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for"); 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		   ip = request.getHeader("Proxy-Client-IP"); 
		} 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		   ip = request.getHeader("WL-Proxy-Client-IP"); 
		} 
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
		   ip = request.getRemoteAddr(); 
		}
		return ip;
	}
	

	/**
	 * 
	 * <pre>
	 * 获取当前主机Ip
	 * </pre>
	 * @return
	 */
	public static String getHostIp(){  
		
		InetAddress netAddress;
		try
		{
			netAddress = InetAddress.getLocalHost();
			if(null == netAddress){  
	            return null;  
	        }  
	        String ip = netAddress.getHostAddress(); //get the ip address  
	        return ip;  
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			System.out.println("unknown host!");
		}  
        return null;
    }  
}
