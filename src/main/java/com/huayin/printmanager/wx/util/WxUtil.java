/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.util;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.huayin.common.cis.ComponentContextLoader;
import com.huayin.common.util.HttpRequestProxy;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.service.ServiceFactory;
import com.huayin.printmanager.utils.ServletUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架 - 微信工具
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public class WxUtil
{
	private static ServiceFactory serviceFactory = ComponentContextLoader.getBean(ServiceFactory.class);

	private static String COMPANY_ID = "companyId";

	private static String USER_ID = "userId";

	private static String OPEN_ID = "openid";

	public static String getCompanyId(HttpServletRequest request)
	{
		return request.getSession().getAttribute(COMPANY_ID) != null ? request.getSession().getAttribute(COMPANY_ID).toString() : null;
	}

	public static Long getUserId(HttpServletRequest request)
	{
		return request.getSession().getAttribute(USER_ID) != null ? (Long) request.getSession().getAttribute(USER_ID) : null;
	}

	public static String getOpenId(HttpServletRequest request)
	{
		return request.getSession().getAttribute(OPEN_ID) != null ? request.getSession().getAttribute(OPEN_ID).toString() : null;
	}

	public static void setCompanyId(HttpServletRequest request, String companyId)
	{
		request.getSession().setAttribute(COMPANY_ID, companyId);
	}

	public static void setUserId(HttpServletRequest request, Long userId)
	{
		request.getSession().setAttribute(USER_ID, userId);
	}

	public static void setOpenId(HttpServletRequest request, String openid)
	{
		request.getSession().setAttribute(OPEN_ID, openid);
	}

	/**
	 * <pre>
	 * 当前登陆用户 是否存在此权限
	 * </pre>
	 * @param identifier
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:04:43, think
	 */
	public static Boolean getUserHasMenu(String identifier)
	{
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>) ServletUtils.getRequest().getAttribute("menuList");
		if (menuList == null)
		{
			menuList = getMenuList();
			ServletUtils.getRequest().setAttribute("menuList", menuList);
		}

		for (Menu menu : menuList)
		{
			if (identifier.equals(menu.getIdentifier()))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * <pre>
	 * 根据code调用微信api 拉取openId
	 * </pre>
	 * @param code
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:04:29, think
	 */
	public static String getOpenIdByCode(String code)
	{
		try
		{
			String refresh_token = (String) HttpRequestProxy.doGetStr("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + SystemConfigUtil.getConfig(SysConstants.WX_APPID) + "&secret=" + SystemConfigUtil.getConfig(SysConstants.WX_APPSECRET) + "&code=" + code + "&grant_type=authorization_code").get("refresh_token");
			Map<String, Object> mapToken = HttpRequestProxy.doGetStr("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + SystemConfigUtil.getConfig(SysConstants.WX_APPID) + "&grant_type=refresh_token&refresh_token=" + refresh_token);
			return mapToken.get("openid").toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}

	public static List<Menu> getMenuList()
	{
		List<Menu> menus = new Vector<Menu>();
		User user = UserUtils.getUser();
		String companyId = user.getCompanyId();
		if (user != null)
		{
			if (UserUtils.isSystemAdmin())
			{
				menus.addAll(serviceFactory.getMenuService().findAll());
				// menus=serviceFactory.getMenuService().findAll();
			}
			else
			{
				if (UserUtils.isCompanyAdmin())
				{
					menus.addAll(serviceFactory.getMenuService().findAllPermissionByCompanyId(companyId));
				}
				else
				{
					menus.addAll(serviceFactory.getMenuService().findAllPermissionByUserId(user.getId()));
				}
				// menus = serviceFactory.getMenuService().findAllPermissionByUserId(user.getId());
			}
		}

		return menus;
	}

	public static Boolean hasPermission(String permission)
	{
		return getUserHasMenu(permission);
	}
}
