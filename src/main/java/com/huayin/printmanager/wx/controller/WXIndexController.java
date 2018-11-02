/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.util.HttpRequestProxy;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.entity.sys.UserShare;
import com.huayin.printmanager.persist.enumerate.WXSumQueryType;
import com.huayin.printmanager.utils.CacheUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.wx.util.CheckUtil;
import com.huayin.printmanager.wx.util.MessageUtil;
import com.huayin.printmanager.wx.util.WxUtil;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.UserVo;
import com.huayin.printmanager.wx.vo.WXShareVo;

/**
 * <pre>
 * 微信 - 模块功能
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/wx/menu")
public class WXIndexController extends BaseController
{
	/**
	 * <pre>
	 * 微信入口 目前业务还没用到此类，后面微信操作跟后台交互时需要用到
	 * </pre>
	 * @param request
	 * @param response
	 * @throws IOException
	 * @since 1.0, 2018年2月26日 上午9:22:10, think
	 */
	@RequestMapping(value = "index")
	public void main(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String requestMethod = request.getMethod();
		PrintWriter out = response.getWriter();
		if ("GET".equals(requestMethod))
		{
			String signature = request.getParameter("signature");
			String timestamp = request.getParameter("timestamp");
			String nonce = request.getParameter("nonce");
			String echostr = request.getParameter("echostr");
			if (CheckUtil.CheckSignature(signature, timestamp, nonce))
			{
				out.print(echostr);
			}
		}
		else
		{
			try
			{
				Map<String, String> map = MessageUtil.xmlTomap(request);
				String ToUserName = map.get("ToUserName");
				String FromUserName = map.get("FromUserName");
				String MsgType = map.get("MsgType");
				String Content = map.get("Content");
				System.out.println(ToUserName);
				System.out.println(FromUserName);
				System.out.println(MsgType);
				System.out.println(Content);
				String message = null;
				if (MessageUtil.MESSAGE_TEXT.equals(MsgType))
				{
					message = MessageUtil.initText(ToUserName, FromUserName, MessageUtil.firstMenu());
				}
				out.print(message);
			}
			catch (DocumentException e)
			{
				e.printStackTrace();
			}

		}
	}

	/**
	 * <pre>
	 * 微信测试1
	 * </pre>
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @since 1.0, 2018年2月26日 上午9:22:23, think
	 */
	@RequestMapping(value = "test")
	public String test(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		Map<String, Object> map = HttpRequestProxy.doGetStr("https://api.weixin.qq.com/cgi-bin/user/info?access_token=mcEnuHb7ZUNoO4038eXnjriy4FinaD1lZPGx0ZoAMddxggrxTYrqyLL9ayDAB_2ZcEIy1j1o7F4t6-II8QGDMu7o8JkJ7cPdPambBbfmKoT-JbBhzu-vDoVskGQeRozTUHVgACAYLZ&openid=oS-I_xL-y3W1h8V1lEy0knpzkNzo&lang=zh_CN");
		System.out.println(map.get("nickname"));
		return "wx/test";
	}

	/**
	 * <pre>
	 * 页面 - OAuth2.0网页授权_未清
	 * </pre>
	 * @param request
	 * @param response
	 * @param code
	 * @param map
	 * @return
	 * @throws IOException
	 * @since 1.0, 2018年2月26日 上午9:22:32, think
	 */
	@RequestMapping(value = "oAuth_warn")
	public String oAuth_warn(HttpServletRequest request, HttpServletResponse response, String code, ModelMap map) throws IOException
	{
		if (WxUtil.getUserId(request) != null)
		{
			return "wx/warn/index";
		}
		String openid = WxUtil.getOpenId(request);
		if (openid == null)
		{
			openid = WxUtil.getOpenIdByCode(code);
			if (openid == null)
			{
				return "wx/invalid";
			}
			WxUtil.setOpenId(request, openid);
		}
		WXShareVo wxVo = null;
		if (CacheUtils.get(openid) == null)
		{
			UserShare userShare = serviceFactory.getWXBasicService().getUserShareByOpenId(openid);
			if (userShare == null)
			{
				String url = request.getRequestURL().toString();
				request.getSession().setAttribute("WXTurnUrl", url);
				return "wx/bind";
			}
			wxVo = new WXShareVo(openid, userShare.getUserId(), userShare.getCompanyId());
			CacheUtils.put(openid, wxVo);
		}
		else
		{
			wxVo = (WXShareVo) CacheUtils.get(openid);
		}

		WxUtil.setUserId(request, wxVo.getUserId());
		WxUtil.setCompanyId(request, wxVo.getCompanyId());
		return "wx/warn/index";
	}

	/**
	 * <pre>
	 * 页面 - OAuth2.0网页授权_审核
	 * </pre>
	 * @param request
	 * @param response
	 * @param code
	 * @param map
	 * @return
	 * @throws IOException
	 * @since 1.0, 2018年2月26日 上午9:22:47, think
	 */
	@RequestMapping(value = "oAuth_check")
	public String oAuth_check(HttpServletRequest request, HttpServletResponse response, String code, ModelMap map) throws IOException
	{
		if (WxUtil.getUserId(request) != null)
		{
			return "wx/check/index";
		}
		String openid = WxUtil.getOpenId(request);
		if (openid == null)
		{
			openid = WxUtil.getOpenIdByCode(code);
			if (openid == null)
			{
				return "wx/invalid";
			}
			WxUtil.setOpenId(request, openid);
		}

		WXShareVo wxVo = null;
		if (CacheUtils.get(openid) == null)
		{
			UserShare userShare = serviceFactory.getWXBasicService().getUserShareByOpenId(openid);
			if (userShare == null)
			{
				String url = request.getRequestURL().toString();
				request.getSession().setAttribute("WXTurnUrl", url);
				return "wx/bind";
			}
			wxVo = new WXShareVo(openid, userShare.getUserId(), userShare.getCompanyId());
			CacheUtils.put(openid, wxVo);
		}
		else
		{
			wxVo = (WXShareVo) CacheUtils.get(openid);
		}
		WxUtil.setUserId(request, wxVo.getUserId());
		WxUtil.setCompanyId(request, wxVo.getCompanyId());
		return "wx/check/index";
	}

	/**
	 * <pre>
	 * 页面 - OAuth2.0网页授权_汇总
	 * </pre>
	 * @param request
	 * @param response
	 * @param code
	 * @param map
	 * @return
	 * @throws IOException
	 * @since 1.0, 2018年2月26日 上午9:22:57, think
	 */
	@RequestMapping(value = "oAuth_sum")
	public String oAuth_sum(HttpServletRequest request, HttpServletResponse response, String code, ModelMap map) throws IOException
	{
		if (WxUtil.getUserId(request) != null)
		{
			return "wx/sum/index";
		}
		String openid = WxUtil.getOpenId(request);
		if (openid == null)
		{
			openid = WxUtil.getOpenIdByCode(code);
			if (openid == null)
			{
				return "wx/invalid";
			}
			WxUtil.setOpenId(request, openid);
		}

		WXShareVo wxVo = null;
		if (CacheUtils.get(openid) == null)
		{
			UserShare userShare = serviceFactory.getWXBasicService().getUserShareByOpenId(openid);
			if (userShare == null)
			{
				String url = request.getRequestURL().toString();
				request.getSession().setAttribute("WXTurnUrl", url);
				return "wx/bind";
			}
			wxVo = new WXShareVo(openid, userShare.getUserId(), userShare.getCompanyId());
			CacheUtils.put(openid, wxVo);
		}
		else
		{
			wxVo = (WXShareVo) CacheUtils.get(openid);
		}
		WxUtil.setUserId(request, wxVo.getUserId());
		WxUtil.setCompanyId(request, wxVo.getCompanyId());
		return "wx/sum/index";
	}

	/**
	 * <pre>
	 * 页面 - OAuth2.0网页授权_进度
	 * </pre>
	 * @param request
	 * @param response
	 * @param code
	 * @param map
	 * @return
	 * @throws IOException
	 * @since 1.0, 2018年2月26日 上午9:23:10, think
	 */
	@RequestMapping(value = "oAuth_schedule")
	public String oAuth_schedule(HttpServletRequest request, HttpServletResponse response, String code, ModelMap map) throws IOException
	{
		if (WxUtil.getUserId(request) != null)
		{
			return "wx/schedule/index";
		}
		String openid = WxUtil.getOpenId(request);
		if (openid == null)
		{
			openid = WxUtil.getOpenIdByCode(code);
			if (openid == null)
			{
				return "wx/invalid";
			}
			WxUtil.setOpenId(request, openid);
		}

		WXShareVo wxVo = null;
		if (CacheUtils.get(openid) == null)
		{
			UserShare userShare = serviceFactory.getWXBasicService().getUserShareByOpenId(openid);
			if (userShare == null)
			{
				String url = request.getRequestURL().toString();
				request.getSession().setAttribute("WXTurnUrl", url);
				return "wx/bind";
			}
			wxVo = new WXShareVo(openid, userShare.getUserId(), userShare.getCompanyId());
			CacheUtils.put(openid, wxVo);
		}
		else
		{
			wxVo = (WXShareVo) CacheUtils.get(openid);
		}
		WxUtil.setUserId(request, wxVo.getUserId());
		WxUtil.setCompanyId(request, wxVo.getCompanyId());
		return "wx/schedule/index";
	}

	/**
	 * <pre>
	 * 页面 - OAuth2.0网页授权_报价
	 * </pre>
	 * @param request
	 * @param response
	 * @param code
	 * @param map
	 * @return
	 * @throws IOException
	 * @since 1.0, 2018年2月26日 上午9:23:20, think
	 */
	@RequestMapping(value = "oAuth_offer")
	public String oAuth_offer(HttpServletRequest request, HttpServletResponse response, String code, ModelMap map) throws IOException
	{
		if (WxUtil.getUserId(request) != null)
		{
			if (!WxUtil.getUserHasMenu("offer:order:list"))
			{
				return "wx/notPermission";
			}
			else
			{
				return "wx/offer/index";
			}
		}
		String openid = WxUtil.getOpenId(request);
		if (openid == null)
		{
			openid = WxUtil.getOpenIdByCode(code);
			if (openid == null)
			{
				return "wx/invalid";
			}
			WxUtil.setOpenId(request, openid);
		}

		WXShareVo wxVo = null;
		if (CacheUtils.get(openid) == null)
		{
			UserShare userShare = serviceFactory.getWXBasicService().getUserShareByOpenId(openid);
			if (userShare == null)
			{
				String url = request.getRequestURL().toString();
				request.getSession().setAttribute("WXTurnUrl", url);
				return "wx/bind";
			}
			wxVo = new WXShareVo(openid, userShare.getUserId(), userShare.getCompanyId());
			CacheUtils.put(openid, wxVo);
		}
		else
		{
			wxVo = (WXShareVo) CacheUtils.get(openid);
		}
		WxUtil.setUserId(request, wxVo.getUserId());
		WxUtil.setCompanyId(request, wxVo.getCompanyId());
		if (!WxUtil.getUserHasMenu("offer:order:list"))
		{
			return "wx/notPermission";
		}
		else
		{
			return "wx/offer/index";
		}
	}

	/**
	 * <pre>
	 * 页面 - OAuth2.0网页授权_列表
	 * </pre>
	 * @param request
	 * @param response
	 * @param code
	 * @param map
	 * @return
	 * @throws IOException
	 * @since 1.0, 2018年2月26日 上午9:23:30, think
	 */
	@RequestMapping(value = "oAuth_offerList")
	public String oAuth_offerList(HttpServletRequest request, HttpServletResponse response, String code, ModelMap map) throws IOException
	{
		if (WxUtil.getUserId(request) != null)
		{
			if (!WxUtil.getUserHasMenu("offer:order:list"))
			{
				return "wx/notPermission";
			}
			else
			{
				return "wx/offer/list";
			}
		}
		String openid = WxUtil.getOpenId(request);
		if (openid == null)
		{
			openid = WxUtil.getOpenIdByCode(code);
			if (openid == null)
			{
				return "wx/invalid";
			}
			WxUtil.setOpenId(request, openid);
		}

		WXShareVo wxVo = null;
		if (CacheUtils.get(openid) == null)
		{
			UserShare userShare = serviceFactory.getWXBasicService().getUserShareByOpenId(openid);
			if (userShare == null)
			{
				String url = request.getRequestURL().toString();
				request.getSession().setAttribute("WXTurnUrl", url);
				return "wx/bind";
			}
			wxVo = new WXShareVo(openid, userShare.getUserId(), userShare.getCompanyId());
			CacheUtils.put(openid, wxVo);
		}
		else
		{
			wxVo = (WXShareVo) CacheUtils.get(openid);
		}
		WxUtil.setUserId(request, wxVo.getUserId());
		WxUtil.setCompanyId(request, wxVo.getCompanyId());
		if (!WxUtil.getUserHasMenu("offer:order:list"))
		{
			return "wx/notPermission";
		}
		else
		{
			return "wx/offer/list";
		}
	}

	/**
	 * <pre>
	 * 页面 - 重新拉取授权
	 * </pre>
	 * @param request
	 * @param response
	 * @param code
	 * @param map
	 * @return
	 * @throws IOException
	 * @since 1.0, 2018年2月26日 上午9:23:40, think
	 */
	@RequestMapping(value = "againAuthorization")
	public String againAuthorization(HttpServletRequest request, HttpServletResponse response, String code, ModelMap map) throws IOException
	{
		String refresh_token = (String) HttpRequestProxy.doGetStr("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + SystemConfigUtil.getConfig(SysConstants.WX_APPID) + "&secret=" + SystemConfigUtil.getConfig(SysConstants.WX_APPSECRET) + "&code=" + code + "&grant_type=authorization_code").get("refresh_token");
		Map<String, Object> mapToken = HttpRequestProxy.doGetStr("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + SystemConfigUtil.getConfig(SysConstants.WX_APPID) + "&grant_type=refresh_token&refresh_token=" + refresh_token);
		String openid = (String) mapToken.get("openid");
		WxUtil.setOpenId(request, openid);
		WXShareVo wxVo = null;
		if (CacheUtils.get(openid) == null)
		{
			UserShare userShare = serviceFactory.getWXBasicService().getUserShareByOpenId(openid);
			if (userShare == null)
			{
				return "wx/bind";
			}
			wxVo = new WXShareVo(openid, userShare.getUserId(), userShare.getCompanyId());
			CacheUtils.put(openid, wxVo);
		}
		else
		{
			wxVo = (WXShareVo) CacheUtils.get(openid);
		}
		WxUtil.setUserId(request, wxVo.getUserId());
		WxUtil.setCompanyId(request, wxVo.getCompanyId());
		return "wx/jumping";
	}

	/**
	 * <pre>
	 * 页面 - 页面跳转中转
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:23:54, think
	 */
	@RequestMapping(value = "jumping")
	public String jumping(ModelMap map)
	{
		return "wx/jumping";
	}

	/**
	 * <pre>
	 * 页面 - 跳转绑定
	 * </pre>
	 * @param request
	 * @param code
	 * @return
	 * @throws IOException
	 * @since 1.0, 2018年2月26日 上午9:24:03, think
	 */
	@RequestMapping(value = "toBind")
	public String toBind(HttpServletRequest request, String code) throws IOException
	{
		return "wx/bind";
	}

	/**
	 * <pre>
	 * 功能 - 绑定
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:24:11, think
	 */
	@RequestMapping(value = "bind")
	@ResponseBody
	public AjaxResponseBody bind(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserName(), queryParam.getPassWord());
		if (user == null)
		{
			return returnErrorBody("账号或密码不正确");
		}
//		UserShare userShare = serviceFactory.getWXBasicService().getUserShare(user.getId());
//		if (userShare != null)
//		{
//			return returnErrorBody("该账号已被绑定");
//		}
		String openid = WxUtil.getOpenId(request);
		if (StringUtils.isBlank(openid))
		{
			return returnErrorBody("微信安全效验失效，请重新打开本页面");
		}
		Boolean isSuccess = serviceFactory.getWXBasicService().bind(user, openid);
		if (isSuccess == false)
		{
			return returnErrorBody("绑定失败，请重新打开本页面后尝试");
		}
		request.getSession().setAttribute("userId", user.getId());
		request.getSession().setAttribute("companyId", user.getCompanyId());
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 解除绑定
	 * </pre>
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:24:22, think
	 */
	@RequestMapping(value = "ajaxDelBind")
	@ResponseBody
	public AjaxResponseBody ajaxDelBind(HttpServletRequest request)
	{
		String openid = WxUtil.getOpenId(request);
		try
		{
			UserShare userShare = serviceFactory.getWXBasicService().getUserShareByOpenId(openid);
			request.getSession().removeAttribute("userId");
			request.getSession().removeAttribute("companyId");
			CacheUtils.remove(openid);
			if (userShare != null)
			{
				if (serviceFactory.getWXBasicService().delBind(userShare))
				{
					if (request.getSession().getAttribute("type") != null)
					{
						request.getSession().removeAttribute("type");
					}
				}
				else
				{
					return returnErrorBody("解绑失败，请重新打开本页面后尝试");
				}
			}
			else
			{
				return returnErrorBody("解绑失败，请重新打开本页面后尝试");
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return returnErrorBody("解绑失败，请重新打开本页面后尝试");
		}
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @return
	 */
	/**
	 * <pre>
	 * 页面 - 关于印管家
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:24:31, think
	 */
	@RequestMapping(value = "view/about")
	public String indexView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		return "wx/about";
	}

	/**
	 * <pre>
	 * 页面 - 体验用户进入
	 * </pre>
	 * @param request
	 * @param response
	 * @param map
	 * @param code
	 * @return
	 * @throws IOException
	 * @since 1.0, 2018年2月26日 上午9:24:43, think
	 */
	@RequestMapping(value = "experienceCenter")
	public String experienceCenter(HttpServletRequest request, HttpServletResponse response, ModelMap map, String code) throws IOException
	{
	  //String a = (String) request.getSession().getAttribute("openid");
		String openid = WxUtil.getOpenId(request);
	  //String openid = "oS-I_xGDeCoGMQMw6gZzAbE9usFY";
		if (openid == null)
		{
			openid = WxUtil.getOpenIdByCode(code);
			if (openid == null)
			{
				return "wx/invalid";
			}
			WxUtil.setOpenId(request, openid);
		}
		WXShareVo wxVo = null;

		UserShare userShare = serviceFactory.getWXBasicService().getUserShareByOpenId(openid);
		if (userShare == null)
		{
			User user = serviceFactory.getWXBasicService().getUser(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER), SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_PWD));
			serviceFactory.getWXBasicService().bind(user, openid);
			userShare = new UserShare();
			userShare.setUserId(user.getId());
			userShare.setCompanyId(user.getCompanyId());
			request.getSession().setAttribute("type", "experience");
		}
		wxVo = new WXShareVo(openid, userShare.getUserId(), userShare.getCompanyId());
		WxUtil.setUserId(request, wxVo.getUserId());
		WxUtil.setCompanyId(request, wxVo.getCompanyId());
		UserVo userVo = serviceFactory.getWXBasicService().getUser(wxVo.getUserId(), wxVo.getCompanyId());
		map.put("userVo", userVo);
		return "wx/center";
	}
}
