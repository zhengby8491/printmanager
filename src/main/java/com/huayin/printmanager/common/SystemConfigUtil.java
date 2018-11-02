/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.huayin.common.cis.ComponentContextLoader;
import com.huayin.common.persist.CommonDao;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.entity.sys.SystemConfig;

/**
 * <pre>
 * 公共 - 读取系统参数
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public class SystemConfigUtil
{
	private final static Log logger = LogFactory.getLog(SystemConfigUtil.class);

	private static CommonDao commDao = ComponentContextLoader.getBean(CommonDao.class);

	public static String getConfig(String key)
	{
		SystemConfig sc = commDao.getEntity(SystemConfig.class, key);
		if (sc == null)
		{
			logger.error("系统参数{" + key + "}不存在!");
		}
		return sc == null ? "" : sc.getValue();
	}

	/**
	 * <pre>
	 * 万能校验码（TODO 需要去除）
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:43:11, think
	 */
	public static String getWanNengCode()
	{
		return getConfig(SysConstants.DEFAULT_WANNENG_CODE);
	}

	/**
	 * <pre>
	 * 获取管理端根路径
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:43:19, think
	 */
	public static String getBasePath()
	{
		return getConfig(SysConstants.SITE_BASE_PATH);
	}
	
	/**
	 * <pre>
	 * 获取默认国际化语言
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月26日 上午9:04:03, think
	 */
	public static String getDefaultI18n()
	{
		return getConfig(SysConstants.SITE_DEFAULT_I18N);
	}

	/**
	 * <pre>
	 * 获取初始化公司模板ID
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:43:25, think
	 */
	public static String getInitCompanyId()
	{
		return getConfig(SysConstants.REGISTER_INIT_TEMPLATE_COMPANY_ID);
	}

	/**
	 * <pre>
	 * 获取初始化公司菜单（普及版）
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:43:41, think
	 */
	public static String getInitCompanyMenuRoleId()
	{
		return getConfig(SysConstants.REGISTER_INIT_TEMPLATE_ROLE_ID);
	}

	/**
	 * <pre>
	 * 获取初始化公司菜单（简易版）
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:43:47, think
	 */
	public static String getInitCompanyMenuRoleId2()
	{
		return getConfig(SysConstants.REGISTER_INIT_TEMPLATE_ROLE_ID2);
	}

	/**
	 * <pre>
	 * 获取初始化公司角色（普及版）
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:43:54, think
	 */
	public static String getInitCompanyRoleIds()
	{
		return getConfig(SysConstants.REGISTER_INIT_TEMPLATE_ROLE_IDS);
	}

	/**
	 * <pre>
	 * 获取初始化公司角色（简易版）
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:44:00, think
	 */
	public static String getInitCompanyRoleIds2()
	{
		return getConfig(SysConstants.REGISTER_INIT_TEMPLATE_ROLE_IDS2);
	}

	/**
	 * <pre>
	 * 获取QQ快捷登录 APP ID
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:44:06, think
	 */
	public static String getExpressLoginTencentAppId()
	{
		return getConfig(SysConstants.EXPRESSLOGIN_TENCENT_APPID);
	}

	/**
	 * <pre>
	 * 获取QQ快捷登录 APP KEY
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:44:13, think
	 */
	public static String getExpressLoginTencentAppKey()
	{
		return getConfig(SysConstants.EXPRESSLOGIN_TENCENT_APPKEY);
	}

	/**
	 * <pre>
	 * 获取QQ快捷登录回调路径
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:44:19, think
	 */
	public static String getExpressLoginTencentReturnUrl()
	{
		return getConfig(SysConstants.EXPRESSLOGIN_TENCENT_RETURNURL);
	}

	/**
	 * <pre>
	 * 获取微信快捷登录 APP ID
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:44:25, think
	 */
	public static String getExpressLoginWechatAppId()
	{
		return getConfig(SysConstants.EXPRESSLOGIN_WECHAT_APPID);
	}

	/**
	 * <pre>
	 * 获取微信快捷登录 APP KEY
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:44:32, think
	 */
	public static String getExpressLoginWechatAppKey()
	{
		return getConfig(SysConstants.EXPRESSLOGIN_WECHAT_APPKEY);
	}

	/**
	 * <pre>
	 * 获取微信快捷登录回调路径
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:44:39, think
	 */
	public static String getExpressLoginWechatReturnUrl()
	{
		return getConfig(SysConstants.EXPRESSLOGIN_WECHAT_RETURNURL);
	}
}
