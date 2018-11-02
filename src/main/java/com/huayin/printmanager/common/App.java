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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.session.ExpiringSession;

import com.huayin.common.cis.ComponentContextLoader;
import com.huayin.common.util.DateTimeUtil;
import com.huayin.printmanager.common.vo.OnlineUserVo;
import com.huayin.printmanager.common.vo.OnlineVo;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.service.ServiceFactory;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 公共 - 统计在线人数
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public class App
{
	private static ServiceFactory serviceFactory = ComponentContextLoader.getBean(ServiceFactory.class);

	public static Log logger = LogFactory.getLog(App.class);

	/**
	 * <pre>
	 * 在线人数
	 *  1.每周的周一至周六  08：01-18：00  （基数）3000+随机数（1以下）*4000+真实在线人数
	                  18：01-20：30  （基数）1000+随机数（1以下）*2000+真实在线人数
	                  20：31-08：00   随机数（1以下）*1000+真实的在线人数。
		2.每周天            随机数（1以下）*1000+真实的在线人数。
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:40:41, think
	 */
	public static Integer sessionCount()
	{
		String[] baseCountArr = SystemConfigUtil.getConfig(SysConstants.SITE_ONLINE_COUNT_BASE).split("\\|");

		int count = 0;
		int baseCount = 0;
		int randomCount = 0;
		Date currTime = new Date();
		int index = 0;
		if (DateTimeUtil.getDayForWeek(currTime) == 1)
		{// 周日
			index = 3;
		}
		else
		{// 周一到周六

			if (DateUtils.isInTime("08:01-18:00", DateTimeUtil.formatToStr(currTime, "HH:mm")))
			{
				index = 0;
			}
			else if (DateUtils.isInTime("18:00-20:30", DateTimeUtil.formatToStr(currTime, "HH:mm")))
			{
				index = 1;
			}
			else if (DateUtils.isInTime("20:30-18:00", DateTimeUtil.formatToStr(currTime, "HH:mm")))
			{
				index = 2;
			}

		}
		String[] arr = baseCountArr[index].split("\\,");
		baseCount = Integer.parseInt(arr[0]);
		randomCount = RandomUtils.nextInt(0, Integer.parseInt(arr[1]));
		count = baseCount + randomCount + serviceFactory.getDaoFactory().getSessionDao().getSessionsCount();
		return count;
		// return Integer.parseInt(SystemConfigUtil.getConfig(SysConstants.SITE_ONLINE_COUNT_BASE))+
		// serviceFactory.getDaoFactory().getSessionDao().getSessionsCount();

	}

	/**
	 * <pre>
	 * 获取所有在线用户，按公司分类
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:40:51, think
	 */
	public static OnlineVo onlineUsers()
	{
		if(UserUtils.isSystemAdmin() || UserUtils.isSystemCompany())
		{
			return systemOnlineUsers();
		}
		else
		{
			return companyOnlineUsers();
		}
	}
	
	private static OnlineVo systemOnlineUsers()
	{
		OnlineVo vo = new OnlineVo();
		logger.info("开发统计在线人数...");
		try
		{
			List<OnlineUserVo> userList = new ArrayList<OnlineUserVo>();
			Map<String, Collection<OnlineUserVo>> userMap = new LinkedHashMap<String, Collection<OnlineUserVo>>();
			Collection<ExpiringSession> sessions = serviceFactory.getDaoFactory().getSessionDao().getActiveSessions();
			
			for (ExpiringSession s : sessions)
			{
				User u = (User) (s.getAttribute("sessionAttr:" + SysConstants.SESSION_KEY_LOGIN_USER));
				
				if (u != null)
				{
					OnlineUserVo uVo = new OnlineUserVo();

					uVo.setSession(s);
					uVo.setUser(u);
					uVo.setHost((String) s.getAttribute("sessionAttr:host"));
					uVo.setLastAccessTime(new Date(s.getLastAccessedTime()));
					uVo.setStartTimestamp(new Date(s.getCreationTime()));
					uVo.setTimeout(s.getMaxInactiveIntervalInSeconds() * 1000);
					userList.add(uVo);
					vo.setLoginedUserCount(vo.getLoginedUserCount() + 1);
				}
				else
				{
					vo.setUnLoginUserCount(vo.getUnLoginUserCount() + 1);
				}
			}

			Collections.sort(userList);
			for (OnlineUserVo u : userList)
			{
				try
				{
					String company_key = u.getUser().getCompanyId() + "_" + u.getUser().getCompany().getName();
					if (userMap.containsKey(company_key))
					{
						userMap.get(company_key).add(u);
					}
					else
					{
						Collection<OnlineUserVo> c_userList = new ArrayList<OnlineUserVo>();
						c_userList.add(u);
						userMap.put(company_key, c_userList);
					}
				}
				catch (Exception e)
				{
					logger.error("统计异常", e);
				}
			}
			vo.setCompanyUsersMap(userMap);
		}
		catch (Exception e)
		{
			logger.error("统计异常", e);
		}

		logger.info("完成统计在线人数，未登录[" + vo.getUnLoginUserCount() + "]，已登录[" + vo.getLoginedUserCount() + "]");
		// logger.info(JsonUtils.toJson(vo));
		return vo;
	}
	
	private static OnlineVo companyOnlineUsers()
	{
		OnlineVo vo = new OnlineVo();
		logger.info("开发统计在线人数...");
		try
		{
			List<OnlineUserVo> userList = new ArrayList<OnlineUserVo>();
			Map<String, Collection<OnlineUserVo>> userMap = new LinkedHashMap<String, Collection<OnlineUserVo>>();
			Collection<ExpiringSession> sessions = serviceFactory.getDaoFactory().getSessionDao().getActiveSessions();
			// 公司管理员查看当前在线用户
			String companyId = UserUtils.getCompanyId();
			List<User> allUserList = serviceFactory.getUserService().getAll();
			
			for (ExpiringSession s : sessions)
			{
				User u = (User) (s.getAttribute("sessionAttr:" + SysConstants.SESSION_KEY_LOGIN_USER));
				
				if (u != null)
				{
					// 过滤非当前公司用户
					if(!u.getCompanyId().equals(companyId))
					{
						continue;
					}
					
					OnlineUserVo uVo = new OnlineUserVo();
					uVo.setSession(s);
					uVo.setUser(u);
					uVo.setHost((String) s.getAttribute("sessionAttr:host"));
					uVo.setLastAccessTime(new Date(s.getLastAccessedTime()));
					uVo.setStartTimestamp(new Date(s.getCreationTime()));
					uVo.setTimeout(s.getMaxInactiveIntervalInSeconds() * 1000);
					userList.add(uVo);
					vo.setLoginedUserCount(vo.getLoginedUserCount() + 1);
				}
			}
			
			// 设置不在线人数
			vo.setUnLoginUserCount(allUserList.size() - vo.getLoginedUserCount());

			Collections.sort(userList);
			for (OnlineUserVo u : userList)
			{
				try
				{
					String company_key = u.getUser().getCompanyId() + "_" + u.getUser().getCompany().getName();
					if (userMap.containsKey(company_key))
					{
						userMap.get(company_key).add(u);
					}
					else
					{
						Collection<OnlineUserVo> c_userList = new ArrayList<OnlineUserVo>();
						c_userList.add(u);
						userMap.put(company_key, c_userList);
					}
				}
				catch (Exception e)
				{
					logger.error("统计异常", e);
				}
			}
			vo.setCompanyUsersMap(userMap);
		}
		catch (Exception e)
		{
			logger.error("统计异常", e);
		}

		logger.info("完成统计在线人数，未登录[" + vo.getUnLoginUserCount() + "]，已登录[" + vo.getLoginedUserCount() + "]");
		// logger.info(JsonUtils.toJson(vo));
		return vo;
	}

	/**
	 * <pre>
	 * 获取税率对象
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:40:59, think
	 */
	public static TaxRate getTaxRate(Long id)
	{
		return serviceFactory.getDaoFactory().getCommonDao().getEntity(TaxRate.class, id);
	}

	/**
	 * <pre>
	 * 获取单位对象
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:41:06, think
	 */
	public static Unit getUnit(Long id)
	{
		return serviceFactory.getDaoFactory().getCommonDao().getEntity(Unit.class, id);
	}

	/**
	 * <pre>
	 * 得到下一个排序
	 * </pre>
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:41:13, think
	 */
	public static int getNextSort(String type)
	{
		return serviceFactory.getCommonService().getNextSort(BasicType.valueOf(type.toUpperCase()));
	}

}
