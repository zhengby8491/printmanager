/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月26日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Lists;
import com.huayin.common.cis.ComponentContextLoader;
import com.huayin.common.util.ConverterUtils;
import com.huayin.common.util.NumberUtil;
import com.huayin.common.util.Reflections;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.helper.service.OfferHelper;
import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Warehouse;
import com.huayin.printmanager.persist.entity.offer.OfferBflute;
import com.huayin.printmanager.persist.entity.offer.OfferPaper;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.persist.entity.sys.Role;
import com.huayin.printmanager.persist.entity.sys.SystemNotice;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.OfferSettingType;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.ResetCycle;
import com.huayin.printmanager.security.Digests;
import com.huayin.printmanager.service.ServiceFactory;
import com.huayin.printmanager.service.sys.impl.SequenceServiceImpl;

/**
 * <pre>
 * 公共 - 用户工具
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月26日, 整理规范
 */
public class UserUtils
{

	private static ServiceFactory serviceFactory = ComponentContextLoader.getBean(ServiceFactory.class);

	/**
	 * PUBLIC_CACHE_SYS缓存的key -> value
	 */
	public static final String CACHE_SUPER_WASTESETTING = "super_waste_setting";

	// ------------------权限缓存存入第三方缓存服务（ehcache、redis等）--------------------------start
	// 公司cache域
	public static final String CACHE_COMPANY = "cache_company_";

	// public static final String CACHE_COMPANY_BASIC = "cache_company_basic_";
	// public static final String CACHE_COMPANY_BASIC_MAP = "cache_company_map_";

	// 公司权限
	public static final String CACHE_COMPANY_PERMISSION = "company_permission";

	// ------------------权限缓存存入SESSION--------------------------end

	// ------------------权限缓存存入SESSION--------------------------start

	// 用户权限session
	public static final String CACHE_USER_PERMISSION = "user_permission";

	// 用户权限标志session
	public static final String CACHE_USER_PERMISSION_IDENTIFIER = "user_permission_identifier";

	// 用户角色session
	public static final String CACHE_USER_ROLE_LIST = "user_role_list";

	// 用户菜单导航栏session
	public static final String CACHE_USER_NAVIGATION = "user_navigation";

	// 所有权限缓存（超级管理员使用：admin）
	// public static final String CACHE_MENU_ALL_LIST = "all_permission_list";
	// 超级管理员用户名
	// public static final String ADMIN_NAME = SystemConfigUtil.getConfig(SysConstants.ADMIN_USERNAME);//
	// ------------------权限缓存---------------------------------------end

	/**
	 * <pre>
	 * 获取当前用户权限
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月26日 上午10:46:50, think
	 */
	public static List<Menu> getMenuList()
	{
		@SuppressWarnings("unchecked")
		List<Menu> menus = (Vector<Menu>) getSessionCache(CACHE_USER_PERMISSION);
		if (menus == null || menus.size() == 0)
		{
			User user = getUser();
			String companyId = user.getCompanyId();
			if (user != null)
			{
				menus = new Vector<Menu>();
				if (isSystemAdmin())
				{
					menus.addAll(serviceFactory.getMenuService().findAll());
					// menus=serviceFactory.getMenuService().findAll();
				}
				else
				{
					if (isSystemAdmin())
					{
						menus.addAll(serviceFactory.getMenuService().findAllPermissionByCompanyId(companyId));
					}
					else
					{
						menus.addAll(serviceFactory.getMenuService().findAllPermissionByUserId(user.getId()));
					}
					// menus = serviceFactory.getMenuService().findAllPermissionByUserId(user.getId());
				}
				putSessionCache(CACHE_USER_PERMISSION, menus);
			}
		}
		return menus;
	}

	/**
	 * 获取当前用户授权导航
	 * @return
	 */
	public static List<Menu> getNavigationMenu()
	{
		@SuppressWarnings("unchecked")
		List<Menu> menus = (Vector<Menu>) getSessionCache(CACHE_USER_NAVIGATION);
		if (menus == null)
		{
			User user = getUser();
			String companyId = user.getCompanyId();
			if (user != null)
			{
				menus = new Vector<Menu>();
				if (isSystemAdmin())
				{
					menus.addAll(serviceFactory.getMenuService().findAllNavigationMenu());
					// menus = serviceFactory.getMenuService().findAllNavigationMenu();
				}
				else
				{
					if (isCompanyAdmin())
					{
						menus.addAll(serviceFactory.getMenuService().findAllNavigationMenu(companyId));
					}
					else
					{
						menus.addAll(serviceFactory.getMenuService().findNavigationMenuByUserId(user.getId()));
					}
					// menus = serviceFactory.getMenuService().findNavigationMenuByUserId(user.getId());
				}
				putSessionCache(CACHE_USER_NAVIGATION, menus);
			}
		}
		return menus;
	}

	/**
	 * 获取当前用户授权角色
	 * @return
	 */
	public static List<Role> getRoleList()
	{
		@SuppressWarnings("unchecked")
		List<Role> roles = (Vector<Role>) getSessionCache(CACHE_USER_ROLE_LIST);
		if (roles == null)
		{
			User user = getUser();
			if (user != null)
			{
				roles = new Vector<Role>();
				roles.addAll(serviceFactory.getRoleService().findByUserId(getUser().getId()));
				// roles = serviceFactory.getRoleService().findByUserId(getUser().getId());
				putSessionCache(CACHE_USER_ROLE_LIST, roles);
			}
		}
		return roles;
	}

	/**
	 * 获取当前公司的授权菜单
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Menu> getMenuListByCompanyId(String companyId)
	{
		List<Menu> menus = null;
		if (StringUtils.isNotBlank(companyId))
		{
			menus = (Vector<Menu>) getCompanyCache(companyId, CACHE_COMPANY_PERMISSION);
			if (menus == null)
			{
				User user = getUser();
				if (user != null)
				{
					menus = new Vector<Menu>();
					/*
					 * if (isSystemAdmin()) { menus.addAll(serviceFactory.getMenuService().findAll()); } else {
					 * menus.addAll(serviceFactory.getMenuService().findAllPermissionByCompanyId(companyId)); }
					 */
					// 取公司权限
					menus.addAll(serviceFactory.getMenuService().findAllPermissionByCompanyId(companyId));
					putCompanyCacheMap(companyId, CACHE_COMPANY_PERMISSION, menus);
				}
			}
		}
		return menus;
	}

	@SuppressWarnings("unchecked")
	public static Set<String> getPermissions()
	{
		return (Set<String>) (getSession().getAttribute(CACHE_USER_PERMISSION_IDENTIFIER));
	}

	public static boolean hasPermission(String permission)
	{
		if (StringUtils.isNotBlank(permission))
		{
			Set<String> permissions = getPermissions();
			if (permissions != null)
			{
				return permissions.contains(permission);
			}
		}
		return false;
	}

	/**
	 * <pre>
	 * 验证报价系统的权限
	 * </pre>
	 * @param offerType
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月11日 上午11:42:28, think
	 */
	public static boolean hasOfferPermission(String offerType, String name)
	{
		OfferType type = OfferType.valueOf(offerType);
		return hasOfferPermission(type, name);
	}

	/**
	 * <pre>
	 * 验证报价系统的权限
	 * </pre>
	 * @param offerType
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月11日 上午11:43:17, think
	 */
	public static boolean hasOfferPermission(OfferType offerType, String name)
	{
		String permission = "offer:" + offerType.getMapping().toLowerCase() + ":" + name;
		if (hasPermission(permission))
		{
			return true;
		}
		return false;
	}

	/**
	 * <pre>
	 *  获取基础类列表
	 * </pre>
	 * @param type
	 * @param isPublic 是否公共
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<?> getBasicList(boolean isPublic, String type)
	{
		List<Object> list = null;
		BasicType _type = BasicType.valueOf(type.toUpperCase());
		if (_type != null)
		{
			String companyId = isPublic ? SystemConfigUtil.getInitCompanyId() : getCompanyId();
			list = (Vector<Object>) (getCompanyCache(companyId, _type.getCacheName()));
			if (list == null)
			{
				list = new Vector<Object>();
				if (isPublic)
				{
					list.addAll(serviceFactory.getCommonService().getCommBasicInfoList(_type));
					// list = serviceFactory.getCommonService().getCommBasicInfoList(_type);
				}
				else
				{
					list.addAll(serviceFactory.getCommonService().getBasicInfoList(_type));
					// list = serviceFactory.getCommonService().getBasicInfoList(_type);
				}

				putCompanyCacheMap(companyId, _type.getCacheName(), list);
			}
		}
		return list;

	}

	/**
	 * 获取公共基础类列表
	 * @return
	 */
	public static List<?> getPublicBasicList(String type)
	{
		return getBasicList(true, type);
	}

	/**
	 * 获取当前基础类列表
	 * @return
	 */
	public static List<?> getBasicList(String type)
	{
		return getBasicList(false, type);
	}

	/**
	 * 获取下拉列表的选项
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<?> getBasicListCustom(String type)
	{
		List<Object> list = null;
		List<Object> listReturn = null;
		BasicType _type = BasicType.valueOf(type.toUpperCase());
		if (_type != null)
		{
			list = (Vector<Object>) (getCompanyCache(getCompanyId(), _type.getCacheName()));
			if (list == null)
			{
				list = new Vector<Object>();
				list.addAll(serviceFactory.getCommonService().getBasicInfoList(_type));
				// list = serviceFactory.getCommonService().getBasicInfoList(_type);
				putCompanyCacheMap(getCompanyId(), _type.getCacheName(), list);
			}
			List<Object> listClone = ObjectUtils.clone(list); 
			// 是否自定义创建基础数据
			if (_type.isQuickCreate())
			{
				try
				{
					if (CollectionUtils.isNotEmpty(listClone))
					{
						// 一个页面可能会有多个相同的枚举，造成重复的自定义选择项
						List<Object> _list = Lists.newArrayList();
						for (Object _obj : listClone)
						{
							BaseBasicTableEntity o =(BaseBasicTableEntity)_obj;
							if (o.getId() == -1 || o.getId() == -99)
							{
								_list.add(o);
							}
						}
						for (Object _obj : _list)
						{
							listClone.remove(_obj);
						}
					}
					BaseBasicTableEntity obj = _type.getCla().newInstance();
					obj.setId(-1l);
					String pro = "name";
					if (_type == BasicType.ACCOUNT)
					{
						pro = "bankNo";
					}
					Reflections.invokeSetter(obj, pro, "自定义");
					// 如果记录为空添加空白项													
					if (CollectionUtils.isEmpty(listClone) && !type.equals("WAREHOUSE"))
					{
						BaseBasicTableEntity obj2 = _type.getCla().newInstance();
						obj2.setId(-99l);
						Reflections.invokeSetter(obj2, pro, "&nbsp;");
						listClone.add(obj2);
					}
					listClone.add(obj);
					listReturn = listClone;
				}
				catch (InstantiationException | IllegalAccessException e)
				{
					// 这里不捕捉异常
				}
			}
		}
		return listReturn;
	}

	/**
	 * <pre>
	 *  获取下拉列表的选项(有过滤条件)
	 * </pre>
	 * @param type
	 * @param filed
	 * @param values
	 * @return
	 * @since 1.0, 2018年1月31日 下午7:14:33, zhengby
	 */
	public static List<?> getBasicListParamCustom(String type, String filed, String values)
	{
		
			List<?> old_list = getBasicListCustom(type);
			if (StringUtils.isBlank(values))
			{
				return old_list;
			}
			List<String> valuList = Arrays.asList(values.split(","));
			List<Object> new_list = new ArrayList<Object>();
			for (Object o : old_list)
			{
				if ((Reflections.getFieldValue(o, "id").toString()).equals("-1") || valuList.contains(Reflections.getFieldValue(o, filed).toString()))
				{
					new_list.add(o);
				}
			}
		try
		{
			if (type.equals("WAREHOUSE") && new_list.size() == 1)
			{
				Warehouse n = (Warehouse) old_list.get(0).getClass().newInstance();
				n.setId(-99l);
				n.setName("&nbsp;");
				new_list.add(0, n);
			}
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return new_list;
	}

	/**
	 * 获取当前基础类列表(有过滤条件)
	 * @return
	 */
	public static List<?> getBasicListParam(boolean isPublic, String type, String filed, String values)
	{
		List<?> old_list = getBasicList(isPublic, type);
		if (StringUtils.isBlank(values))
		{
			return old_list;
		}
		List<String> valuList = Arrays.asList(values.split(","));
		List<Object> new_list = new ArrayList<Object>();
		for (Object o : old_list)
		{
			if (valuList.contains(Reflections.getFieldValue(o, filed).toString()))
			{
				new_list.add(o);
			}
		}

		return new_list;

	}

	public static List<?> getBasicListParams(boolean isPublic, String type, String filed1, String filed2, String values1, String values2)
	{
		List<?> old_list = getBasicList(isPublic, type);
		if (StringUtils.isBlank(values1) && StringUtils.isBlank(values2))
		{
			return old_list;
		}
		List<String> valuList1 = Arrays.asList(values1.split(","));
		List<String> valuList2 = Arrays.asList(values2.split(","));
		List<Object> new_list = new ArrayList<Object>();
		for (Object o : old_list)
		{
			boolean b1 = valuList1.contains(Reflections.getFieldValue(o, filed1).toString());
			boolean b2 = valuList2.contains(Reflections.getFieldValue(o, filed2).toString());
			if (b1 && b2)
			{
				new_list.add(o);
			}
		}
		return new_list;
	}

	/**
	 * 获取公共基础类列表(有过滤条件)
	 * @return
	 */
	public static List<?> getPublicBasicListParam(String type, String filed, String values)
	{
		return getBasicListParam(true, type, filed, values);
	}

	/**
	 * 获取当前基础类列表(有过滤条件)
	 * @return
	 */
	public static List<?> getBasicListParam(String type, String filed, String values)
	{
		return getBasicListParam(false, type, filed, values);
	}

	/**
	 * 获取当前基础类列表(有多个过滤条件)
	 * @return
	 */
	public static List<?> getBasicListParams(String type, String filed1, String filed2, String values1, String values2)
	{
		return getBasicListParams(false, type, filed1, filed2, values1, values2);
	}

	/**
	 * 获取基础类MAP
	 */
	@SuppressWarnings("unchecked")
	public static Map<Long, ?> getBasicMap(boolean isPublic, String type)
	{
		Map<Long, ?> map = null;
		BasicType _type = BasicType.valueOf(type.toUpperCase());
		if (_type != null)
		{
			String companyId = isPublic ? SystemConfigUtil.getInitCompanyId() : getCompanyId();
			String cacheMapKey = _type.getCacheName() + "_map";
			map = (Map<Long, Object>) (getCompanyCache(companyId, cacheMapKey));
			if (map == null)
			{
				map = ConverterUtils.list2Map(getBasicList(isPublic, type), "id");
				putCompanyCacheMap(companyId, cacheMapKey, map);
			}
		}
		return map;
	}

	/**
	 * 获取公共基础类MAP
	 */
	public static Map<Long, ?> getPublicBasicMap(String type)
	{
		return getBasicMap(true, type);
	}

	/**
	 * 获取当前基础类MAP
	 */
	public static Map<Long, ?> getBasicMap(String type)
	{
		return getBasicMap(false, type);
	}

	/**
	 * 获取基础类对象
	 */
	public static Object getBasicInfo(boolean isPublic, String type, Long id)
	{
		return getBasicMap(isPublic, type).get(id);
	}

	/**
	 * 获取公共基础类对象
	 */
	public static Object getPublicBasicInfo(String type, Long id)
	{
		return getBasicInfo(true, type, id);
	}

	/**
	 * 获取基础类对象
	 */
	public static Object getBasicInfo(String type, Long id)
	{
		return getBasicInfo(false, type, id);
	}

	/**
	 * 获取基础类对象属性
	 */
	public static Object getBasicInfoFiledValue(boolean isPublic, String type, Long id, String filedName)
	{
		if (getBasicMap(isPublic, type).get(id) != null)
		{
			return Reflections.getFieldValue(getBasicMap(isPublic, type).get(id), filedName);
		}
		else
		{
			return "";
		}
	}

	/**
	 * 获取当前基础类对象属性
	 */
	public static Object getPublicBasicInfoFiledValue(String type, Long id, String filedName)
	{
		return getBasicInfoFiledValue(true, type, id, filedName);
	}

	/**
	 * 获取当前基础类对象属性
	 */
	public static Object getBasicInfoFiledValue(String type, Long id, String filedName)
	{
		return getBasicInfoFiledValue(false, type, id, filedName);
	}

	public static Long getUserId()
	{
		return getUser() != null ? getUser().getId() : null;
	}

	public static Long getEmployeeId()
	{
		return getUser() != null ? getUser().getEmployeeId() : null;
	}

	public static String getUserName()
	{
		return getUser() != null ? getUser().getUserName() : null;
	}

	public static Company getCompany()
	{
		User u = getUser();
		if (u != null)
		{
			if (u.getCompany() != null)
			{
				return u.getCompany();
			}
			else
			{
				Company company = serviceFactory.getCompanyService().get(u.getCompanyId());
				u.setCompany(company);
				UserUtils.updateSessionUser();
				return company;
			}
		}
		else
		{
			return null;
		}
	}

	public static String getCompanyId()
	{
		User u = getUser();
		if (u != null)
		{
			return u.getCompanyId();
		}
		else
		{
			Object companyId = ServletUtils.getRequest().getSession().getAttribute("companyId");
			if (companyId != null)
			{
				return companyId.toString();
			}
			throw new RuntimeException("公司ID为NULL");
		}
	}

	/**
	 * <pre>
	 * 判断是否系统超级管理员
	 * </pre>
	 * @return
	 */
	public static boolean isSystemAdmin()
	{
		String userName = getUserName();
		if (userName != null)
		{
			return userName.equals(SystemConfigUtil.getConfig(SysConstants.ADMIN_USERNAME));
		}
		return false;
	}

	/**
	 * <pre>
	 * 判断是否公司管理员
	 * 注册用户即为该公司的超级管理员
	 * </pre>
	 * @return
	 */
	public static boolean isCompanyAdmin()
	{
		User u = getUser();
		if (u != null)
		{
			return u.getId().equals(getCompany().getRegisterUserId());
		}
		return false;
	}

	/**
	 * 
	 * <pre>
	 * 获取超级账号的公司ID
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月18日 下午7:18:57, think
	 */
	public static String getSystemCompanyId()
	{
		return SystemConfigUtil.getConfig(SysConstants.ADMIN_COMPANYID);
	}

	/**
	 * <pre>
	 * 是否系统管理员
	 * </pre>
	 * @return
	 */
	public static boolean isSystemCompany()
	{
		return getCompanyId().equals(getSystemCompanyId());

	}

	/**
	 * 用户登录
	 * @return
	 */
	public static void login(User user)
	{
		putSessionCache(SysConstants.SESSION_KEY_LOGIN_USER, user);
		putSessionCache(SysConstants.SESSION_KEY_LOGIN_USERID, user.getId());
	}

	/**
	 * 用户退出
	 * @return
	 */
	public static void loginout()
	{
		// UserUtils.removeSessionCache(SysConstants.SESSION_KEY_LOGIN_USER);
		// UserUtils.removeSessionCache(SysConstants.SESSION_KEY_LOGIN_USERID);
		// UserUtils.removeSessionCache(SysConstants.SESSION_KEY_LOGIN_USER);
		serviceFactory.getDaoFactory().getSessionDao().delete(getSession().getId());// 删除redis session key
		UserUtils.getSession().invalidate();// 删除cookie session key
	}

	/**
	 * 判断用户是否登录
	 * @return
	 */
	public static boolean isLogined()
	{
		User user = (User) UserUtils.getSession().getAttribute(SysConstants.SESSION_KEY_LOGIN_USER);
		return user != null ? true : false;
	}

	/**
	 * 获取当前用户
	 * @return
	 */
	public static User getUser()
	{
		User user = (User) getSessionCache(SysConstants.SESSION_KEY_LOGIN_USER);
		if (user == null)
		{
			Object userId = ServletUtils.getRequest().getSession().getAttribute("userId");
			if (userId != null)
			{
				user = serviceFactory.getUserService().get((Long) userId);
				if (user != null)
				{
					user.setCompany(serviceFactory.getDaoFactory().getCommonDao().getEntity(Company.class, user.getCompanyId()));
				}
				return user;
			}
		}
		return user;
	}

	public static void updateUserPermissionIdentifier()
	{
		Set<String> permissionSet = new HashSet<String>();
		if (getUser() != null)
		{
			List<Menu> mlist = getMenuList();
			for (Menu m : mlist)
			{
				if (StringUtils.isNotBlank(m.getIdentifier()))
				{
					// 添加基于Permission的权限信息
					for (String permission : StringUtils.split(m.getIdentifier(), ","))
					{
						permissionSet.add(permission);
					}
				}
			}
			getSession().removeAttribute(CACHE_USER_PERMISSION_IDENTIFIER);
			getSession().setAttribute(CACHE_USER_PERMISSION_IDENTIFIER, permissionSet);
		}
	}

	public static User updateSessionUser()
	{
		User user = (User) getSessionCache(SysConstants.SESSION_KEY_LOGIN_USER);
		if (user != null)
		{
			user = serviceFactory.getDaoFactory().getUserDao().get(user.getId());
			user.setCompany(serviceFactory.getDaoFactory().getCommonDao().getEntity(Company.class, user.getCompanyId()));
			removeSessionCache(SysConstants.SESSION_KEY_LOGIN_USER);
			putSessionCache(SysConstants.SESSION_KEY_LOGIN_USER, user);
		}

		return user;
	}

	/**
	 * <pre>
	 * 获取当前用户对象员工姓名没有则返回账号名
	 * </pre>
	 * @return
	 */
	public static String getUserEmployeeName()
	{
		Employee e = (Employee) UserUtils.getBasicInfo(BasicType.EMPLOYEE.name(), UserUtils.getEmployeeId());
		if (e != null)
		{
			return e.getName();
		}
		return UserUtils.getUserName();
	}

	/**
	 * 获取当前用户业务数据授权的用户
	 * @return
	 */
	public static Long[] getBusinessDataAuthorizationUser()
	{
		Long[] userIds = serviceFactory.getUserService().findSharedEmployeeIds(UserUtils.getUserId());
		return userIds;
	}

	public static HttpSession getSession()
	{
		HttpServletRequest request = ServletUtils.getRequest();
		return request != null ? request.getSession() : null;
	}

	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword)
	{
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Digests.generateSalt(SysConstants.SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, SysConstants.HASH_INTERATIONS);
		return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
	}

	/**
	 * 验证原始密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password)
	{
		String plain = Encodes.unescapeHtml(plainPassword);
		byte[] salt = Encodes.decodeHex(password.substring(0, 16));
		byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, SysConstants.HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
	}

	/**
	 * <pre>
	 * 校验手机验证码
	 * </pre>
	 * @param validCode
	 * @return
	 */
	public static boolean validateSmsValidCode(String mobile, String validCode)
	{
		// 校验短信手机
		Object session_sms_mobile = getSessionCache(SysConstants.SESSION_KEY_SMS_MOBILE);
		if (session_sms_mobile == null)
		{
			return false;
		}
		else if (!session_sms_mobile.equals(mobile))
		{
			return false;
		}
		// 校验短信验证码
		Object session_sms_validCode = getSessionCache(SysConstants.SESSION_KEY_SMS_VALIDATECODE);
		String wanneng = SystemConfigUtil.getWanNengCode();

		boolean isValidate = false;
		if (session_sms_validCode != null)
		{
			if (wanneng == null)
			{
				isValidate = validCode.equals(session_sms_validCode);
			}
			else
			{
				isValidate = validCode.equals(session_sms_validCode) || validCode.equals(wanneng);
			}
		}

		return isValidate;
	}

	/**
	 * <pre>
	 * 清除当前session手机验证码
	 * </pre>
	 */
	public static void clearSmsValidCode()
	{
		removeSessionCache(SysConstants.SESSION_KEY_SMS_MOBILE);
		removeSessionCache(SysConstants.SESSION_KEY_SMS_VALIDATECODE);
	}

	// ============== User Session Cache (session 可以用第三方缓存代替：redis、ehcache等)==============

	/**
	 * <pre>
	 * 获取公司内的缓存MAP
	 * </pre>
	 * @param companyId
	 */
	public static Object getCompanyCache(String companyId, String cacheMapKey)
	{
		String companyCacheKey = CACHE_COMPANY + companyId;
		return CacheUtils.get(companyCacheKey, cacheMapKey);

	}

	/**
	 * <pre>
	 * 添加公司缓存
	 * </pre>
	 * @param companyId
	 */
	public static void putCompanyCacheMap(String companyId, String mapKey, Object mapValue)
	{
		String companyCacheKey = CACHE_COMPANY + companyId;
		CacheUtils.put(companyCacheKey, mapKey, mapValue);
	}

	/**
	 * <pre>
	 * 清楚公司缓存
	 * </pre>
	 * @param companyId
	 */
	public static void clearCompanyCache(String companyId, String cacheMapKey)
	{
		String companyCacheKey = CACHE_COMPANY + companyId;
		CacheUtils.remove(companyCacheKey, cacheMapKey);
	}

	public static Object getSessionCache(String key)
	{
		return getSessionCache(key, null);
	}

	public static Object getSessionCache(String key, Object defaultValue)
	{
		Object obj = getSession().getAttribute(key);
		return obj == null ? defaultValue : obj;
	}

	public static void putSessionCache(String key, Object value)
	{
		HttpSession session = getSession();
		if (session != null)
		{
			session.setAttribute(key, value);
			if (session.getAttribute("host") == null)
			{
				session.setAttribute("host", ServletUtils.getRequest().getRemoteHost());
			}
		}
		getSession().setAttribute(key, value);
	}

	public static void removeSessionCache(String key)
	{
		getSession().removeAttribute(key);
	}

	/**
	 * <pre>
	 * 创建公司编号7位(YY+5位序号)
	 * </pre>
	 * @return
	 */
	public static String createCompanyId()
	{
		return DateUtils.formatDate(new Date(), "yy") + NumberUtil.formatToStr(SequenceServiceImpl.getInstance().getNoCacheSequence("company.id"), "00000");
	}

	/**
	 * <pre>
	 * 创建用户编号20位(C+8位公司ID+6位日期+5位序号)
	 * </pre>
	 * @return
	 */
	public static String createUserNo(String companyId)
	{
		return "U" + companyId + DateUtils.formatDate(new Date(), "yyMMdd") + NumberUtil.formatToStr(SequenceServiceImpl.getInstance().getNoCacheSequence("user.no"), "00000");
	}

	/**
	 * <pre>
	 * 创建单据编号12位(6位日期+6位序号)
	 * </pre>
	 * @return
	 */
	public static String createBillNo(BillType billType)
	{
		return billType.getCode() + DateUtils.formatDate(new Date(), "yyMMdd") + NumberUtil.formatToStr(SequenceServiceImpl.getInstance().getNoCacheSequence(getCompanyId() + ".order.no." + billType.getCode(), ResetCycle.DAY), "000");
	}

	/**
	 * <pre>
	 * 创建编号12位(6位日期+6位序号)
	 * </pre>
	 * @return
	 */
	public static String createNo(String pre)
	{
		return pre + DateUtils.formatDate(new Date(), "yyMMdd") + NumberUtil.formatToStr(SequenceServiceImpl.getInstance().getNoCacheSequence(getCompanyId() + ".order.no." + pre, ResetCycle.DAY), "000");
	}

	/**
	 * <pre>
	 * 创建基础资料代码
	 * </pre>
	 * @return
	 */
	public static String createBasicCode(BasicType basicType)
	{
		return basicType.getCodePrefix() + NumberUtil.formatToStr(SequenceServiceImpl.getInstance().getNoCacheSequence(getCompanyId() + ".basic." + basicType.getCodePrefix()), "00000");
	}

	/**
	 * 获取所有显示的报价类型
	 */
	public static List<OfferType> getOfferTypeList()
	{
		List<OfferType> offerTypeList = new ArrayList<OfferType>();
		for (OfferType offerType : OfferType.values())
		{
			String permission = "offer:auto:" + offerType.getMapping().toLowerCase();
			if (UserUtils.hasPermission(permission))
			{
				offerTypeList.add(offerType);
			}
		}
		return offerTypeList;
	}

	public static String getLastNotice()
	{
		SystemNotice systemNotice = serviceFactory.getSystemNoticeService().findLastPublish();
		if (systemNotice != null && systemNotice.getPublish().getValue())
		{
			return systemNotice.getContent();
		}
		return "";
	}

	/**
	 * <pre>
	 *  判断是否拥有公司菜单权限（用于业务流程拆分）
	 * </pre>
	 * @param permission
	 * @return
	 */
	public static boolean hasCompanyPermission(String permission)
	{
		boolean hasPermission = false;
		List<Menu> hasMenuList = serviceFactory.getMenuService().findAll(UserUtils.getCompanyId());
		for (Menu m : hasMenuList)
		{
			if (permission.equals(m.getIdentifier()))
			{
				hasPermission = true;
				break;
			}
		}
		return hasPermission;
	}

	/**
	 * <pre>
	 * 清除公司基础资料缓存
	 * </pre>
	 */
	public static void clearCacheBasic(BasicType type)
	{
		CacheUtils.remove(CACHE_COMPANY + getCompanyId(), type.getCacheName());
		CacheUtils.remove(CACHE_COMPANY + getCompanyId(), type.getCacheName() + "_map");
	}

	/**
	 * <pre>
	 * 清除指定公司基础资料缓存
	 * </pre>
	 * @param type
	 * @param companyId
	 * @since 1.0, 2018年9月22日 下午3:36:48, zhengby
	 */
	public static void clearCacheBasic(BasicType type, String companyId)
	{
		CacheUtils.remove(CACHE_COMPANY + companyId, type.getCacheName());
		CacheUtils.remove(CACHE_COMPANY + companyId, type.getCacheName() + "_map");
	}
	
	/**
	 * <pre>
	 * 清除公司权限
	 * </pre>
	 */
	public static void clearCachePermission(String companyId)
	{
		clearCompanyCache(companyId, CACHE_COMPANY_PERMISSION);
	}

	// ==================== 报价系统 ====================

	/**
	 * 
	 * <pre>
	 * 获取报价设置的枚举集合
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月17日 下午5:17:03, think
	 */
	public static List<OfferSettingType> getOfferSettingTypeList(String offerType)
	{
		OfferType type = OfferType.valueOf(offerType);
		// 这里需要过滤权限
		List<OfferSettingType> offerSettingTypeFinalList = Lists.newArrayList();
		List<OfferSettingType> offerSettingTypeList = OfferHelper.findOfferSettingTypeList();
		for (OfferSettingType settingType : offerSettingTypeList)
		{
			// 动态权限，如offer:single:machine
			String permission = "offer:" + type.getMapping().toLowerCase() + ":" + settingType.getMapping().toLowerCase();
			if (UserUtils.hasPermission(permission))
			{
				offerSettingTypeFinalList.add(settingType);
			}
		}

		return offerSettingTypeFinalList;
	}

	/**
	 * <pre>
	 * 报价系统 - 获取基础设置缓存
	 * </pre>
	 * @param type
	 * @param settingType
	 * @return
	 * @since 1.0, 2017年11月3日 下午3:39:16, think
	 */
	public static List<?> getOfferBasicList(String type, String settingType)
	{
		return OfferHelper.getBasicList(OfferType.valueOf(type), OfferSettingType.valueOf(settingType));
	}

	/**
	 * 
	 * <pre>
	 * 自动报价 - 获取印刷纸张的可选类型
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月8日 下午2:39:15, zhengby
	 */
	public static List<OfferPaper> getOfferPaperList(String type)
	{
		return OfferHelper.getOfferPaperList(OfferType.valueOf(type), null);
	}

	/**
	 * 
	 * <pre>
	 * 自动报价 - 获取印刷纸张的可选类型
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月8日 下午2:39:15, zhengby
	 */
	public static List<OfferBflute> getOfferBfluteList()
	{
		return OfferHelper.getOfferBfluteList(null);
	}

}
