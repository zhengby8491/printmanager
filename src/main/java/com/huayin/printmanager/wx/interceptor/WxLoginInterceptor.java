/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.huayin.common.util.JsonUtils;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.wx.util.WxUtil;

/**
 * <pre>
 * 微信框架 - 微信登陆状态拦截器（拦截跳转）
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class WxLoginInterceptor extends HandlerInterceptorAdapter
{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		// request.getSession().setAttribute("userId",new Long("835"));
		// request.getSession().setAttribute("companyId","1600016");
		// request.getSession().setAttribute("openid","oS-I_xKlpPMFDJenP5WnzDvT4X3U");

		// request.getSession().setAttribute("userId",new Long("18902"));
		// request.getSession().setAttribute("companyId","1601003");
		// request.getSession().setAttribute("openid","oS-I_xOMr2IQklgf7WUl0V9WjWwY");

		/*request.getSession().setAttribute("userId",new Long("404"));
		request.getSession().setAttribute("companyId","1600010");
	  request.getSession().setAttribute("openid","oS-I_xGDeCoGMQMw6gZzAbE9usFY");*/

		// 拦截url，用于扫一扫（个人中心也必须支持）
		request.getSession().getAttribute("openid");
		String url = request.getRequestURL().toString();
		if (url.indexOf("/view/") != -1 || url.indexOf("/wx/homepage/center") != -1)
		{
			request.getSession().setAttribute("WXSCANQR", url);
			request.getSession().setAttribute("WXSHARE", url);
		}
		//System.out.print(WxUtil.getOpenId(request));
		if (WxUtil.getOpenId(request) == null || WxUtil.getUserId(request) == null)
		{
			ResponseBody resBody = ((HandlerMethod) handler).getMethodAnnotation(ResponseBody.class);
			if (resBody != null)
			{
				// String url = request.getRequestURL().toString();
				url = url.substring(0, url.lastIndexOf("/"));
				String hrefStr = url.substring(url.lastIndexOf("/") + 1, url.length());
				if (hrefStr.equals("check"))
				{
					url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + SystemConfigUtil.getConfig(SysConstants.WX_APPID) + "&redirect_uri=" + SystemConfigUtil.getConfig(SysConstants.WX_URL) + "/print/wx/menu/oAuth_check&response_type=code&scope=snsapi_userinfo&state=STATE";
				}
				else if (hrefStr.equals("warn"))
				{
					url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + SystemConfigUtil.getConfig(SysConstants.WX_APPID) + "&redirect_uri=" + SystemConfigUtil.getConfig(SysConstants.WX_URL) + "/print/wx/menu/oAuth_warn&response_type=code&scope=snsapi_userinfo&state=STATE";
				}
				else if (hrefStr.equals("sum"))
				{
					url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + SystemConfigUtil.getConfig(SysConstants.WX_APPID) + "&redirect_uri=" + SystemConfigUtil.getConfig(SysConstants.WX_URL) + "/print/wx/menu/oAuth_sum&response_type=code&scope=snsapi_userinfo&state=STATE";
				}
				else if (hrefStr.equals("offer"))
				{
					url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + SystemConfigUtil.getConfig(SysConstants.WX_APPID) + "&redirect_uri=" + SystemConfigUtil.getConfig(SysConstants.WX_URL) + "/print/wx/menu/oAuth_offer&response_type=code&scope=snsapi_userinfo&state=STATE";
				}
				else
				{
					url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + SystemConfigUtil.getConfig(SysConstants.WX_APPID) + "&redirect_uri=" + SystemConfigUtil.getConfig(SysConstants.WX_URL) + "/print/wx/menu/oAuth_schedule&response_type=code&scope=snsapi_userinfo&state=STATE";
				}
				request.getSession().setAttribute("WXTurnUrl", url);
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json; charset=utf-8");
				PrintWriter out = response.getWriter();
				out.print(JsonUtils.formatJSON("turn1"));
				return false;
			}
			else
			{
				// String url = request.getRequestURL().toString();
				request.getSession().setAttribute("WXTurnUrl", url);
				response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + SystemConfigUtil.getConfig(SysConstants.WX_APPID) + "&redirect_uri=" + SystemConfigUtil.getConfig(SysConstants.WX_URL) + "/print/wx/menu/againAuthorization&response_type=code&scope=snsapi_userinfo&state=STATE");
				return false;
			}

		}
		if (request.getSession().getAttribute("WXTurnUrl") != null)
		{
			request.getSession().removeAttribute("WXTurnUrl");
		}
		return true;
	}

}
