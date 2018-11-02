/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.controller.vo.UserVo;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.persist.entity.sys.Role;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.State;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.plugin.PageSupport;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统模块 - 用户管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/user")
public class UserController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 用户管理列表
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param userName
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:12:30, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("sys:user:list")
	public String list(@DateTimeFormat(iso = ISO.DATE) Date dateMin, @DateTimeFormat(iso = ISO.DATE) Date dateMax, String userName, HttpServletRequest request, ModelMap map)
	{
		map.put("dateMin", request.getParameter("dateMin"));
		map.put("dateMax", request.getParameter("dateMax"));
		map.put("userName", userName);
		PageSupport<User> page = new PageSupport<User>(request);
		SearchResult<User> result = serviceFactory.getUserService().findByCondition(dateMin, dateMax, userName, page.getPageNo(), page.getPageSize());
		page.setSearchResult(result);
		map.put("page", page);
		return "sys/user/list";
	}

	/**
	 * <pre>
	 * 页面 - 用户管理新增 
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:12:55, think
	 */
	@RequestMapping(value = "add")
	@RequiresPermissions("sys:user:edit")
	public String add(ModelMap map)
	{

		List<Role> roleList = serviceFactory.getRoleService().findAll(UserUtils.getCompanyId());

		List<State> stateList = new ArrayList<State>();
		stateList.add(State.NORMAL);
		stateList.add(State.CLOSED);
		map.put("roleList", roleList);
		map.put("stateList", stateList);
		return "sys/user/add";
	}

	/**
	 * <pre>
	 * 功能 - 用户管理新增
	 * </pre>
	 * @param userVo
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:13:27, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	public AjaxResponseBody save(UserVo userVo, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(userVo.getUserName(), userVo.getPassword(), userVo.getRoles()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}

		User user = new User();
		user.setUserNo(UserUtils.createUserNo(UserUtils.getCompanyId()));
		user.setCompanyId(UserUtils.getUser().getCompanyId());
		user.setCreateName(UserUtils.getUser().getUserName());
		user.setUserName(userVo.getUserName());
		if (userVo.getEmployeeId() != null && userVo.getEmployeeId() > 0)
		{
			user.setEmployeeId(userVo.getEmployeeId());
			user.setRealName(serviceFactory.getEmployeeService().get(userVo.getEmployeeId()).getName());
		}
		user.setPassword(UserUtils.entryptPassword(userVo.getPassword()));
		user.setMobile(userVo.getMobile());
		user.setEmail(userVo.getEmail());
		user.setState(userVo.getState());
		user.setMemo(userVo.getMemo());
		user.setCreateTime(new Date());
		// 校验数据无误，进入持久化操作
		serviceFactory.getUserService().save(user);
		serviceFactory.getUserService().updateShared(user.getId(), userVo.getEmployeeIds());
		serviceFactory.getRoleService().updateUR(user.getId(), userVo.getRoles());
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 用户管理修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:13:45, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("sys:user:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		User user = serviceFactory.getUserService().getById(id);

		List<Role> allRoleList = serviceFactory.getRoleService().findAll(UserUtils.getCompanyId());
		List<Role> hasRoleList = serviceFactory.getRoleService().findByUserId(id);

		map.put("allRoleList", allRoleList);
		List<Long> allIds = new ArrayList<Long>();
		for (Role role : hasRoleList)
		{
			allIds.add(role.getId());
		}
		map.put("hasRoleIdList", StringUtils.join(allIds.toArray(), ","));
		map.put("user", user);

		List<State> stateList = new ArrayList<State>();
		stateList.add(State.NORMAL);
		stateList.add(State.CLOSED);
		map.put("stateList", stateList);
		return "sys/user/edit";
	}

	/**
	 * <pre>
	 * 功能 - 用户管理修改
	 * </pre>
	 * @param userVo
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:14:01, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	public AjaxResponseBody update(UserVo userVo, ModelMap map)
	{

		if (Validate.validateObjectsNullOrEmpty(userVo, userVo.getRoles()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		User old_user = serviceFactory.getUserService().getById(userVo.getId());
		if (!UserUtils.getCompanyId().equals(old_user.getCompanyId()))
		{
			return returnErrorBody("权限非法！");
		}

		User user = serviceFactory.getDaoFactory().getUserDao().getByMobile(userVo.getMobile());
		if (user != null && !(userVo.getId().equals(user.getId())) && !old_user.getMobile().equals(user.getMobile()))
		{
			return returnErrorBody("手机号码已经存在!");
		}

		if (userVo.getEmployeeId() != null && userVo.getEmployeeId() > 0)
		{
			old_user.setEmployeeId(userVo.getEmployeeId());
			old_user.setRealName(serviceFactory.getEmployeeService().get(userVo.getEmployeeId()).getName());
		}
		else
		{
			old_user.setEmployeeId(null);
			old_user.setRealName(null);
		}
		old_user.setEmail(userVo.getEmail());
		old_user.setMobile(userVo.getMobile());
		old_user.setPhone(userVo.getPhone());
		old_user.setMemo(userVo.getMemo());
		old_user.setUpdateTime(new Date());
		old_user.setUpdateName(UserUtils.getUser().getUserName());
		serviceFactory.getPersistService().update(old_user);
		serviceFactory.getUserService().updateShared(old_user.getId(), userVo.getEmployeeIds());
		serviceFactory.getRoleService().updateUR(userVo.getId(), userVo.getRoles());
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 用户管理查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:14:35, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("sys:user:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		User user = serviceFactory.getUserService().getById(id);
		List<Role> hasRoleList = serviceFactory.getRoleService().findByUserId(id);
		map.put("user", user);
		map.put("hasRoleList", hasRoleList);
		return "sys/user/view";
	}

	/**
	 * <pre>
	 * 功能 - 停用用户
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:14:48, think
	 */
	@RequestMapping(value = "stop/{id}")
	@ResponseBody
	@RequiresPermissions("sys:user:state")
	public boolean stop(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			User user = serviceFactory.getUserService().getById(id);
			user.setState(State.CLOSED);
			user.setUpdateTime(new Date());
			user.setUpdateName(UserUtils.getUser().getUserName());
			serviceFactory.getPersistService().update(user);
			return true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * <pre>
	 * 功能 - 启用用户
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:15:03, think
	 */
	@RequestMapping(value = "start/{id}")
	@ResponseBody
	@RequiresPermissions("sys:user:state")
	public boolean start(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			User user = serviceFactory.getUserService().getById(id);
			user.setState(State.NORMAL);
			user.setUpdateTime(new Date());
			user.setUpdateName(UserUtils.getUser().getUserName());
			serviceFactory.getPersistService().update(user);
			return true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * <pre>
	 * 功能 - 用户名是否存在
	 * </pre>
	 * @param userName
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:15:15, think
	 */
	@RequestMapping(value = "exist/userName")
	@ResponseBody
	@RequiresPermissions("sys:user:edit")
	public boolean existUserName(String userName, ModelMap map)
	{
		return !serviceFactory.getUserService().existUserName(userName, null);
	}

	/**
	 * <pre>
	 * 功能 - 手机号码是否存在
	 * </pre>
	 * @param mobile
	 * @param userId
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:15:29, think
	 */
	@RequestMapping(value = "exist/mobile")
	@ResponseBody
	public boolean existMobile(String mobile, Long userId, ModelMap map)
	{
		return !serviceFactory.getUserService().existMobile(mobile, userId);
	}

	/**
	 * <pre>
	 * 页面 - 修改密码
	 * 返回不能使用精确返回，以防对方恶意操作
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:15:42, think
	 */
	@RequestMapping(value = "toResetPwd/{id}")
	@RequiresPermissions("sys:user:resetpwd")
	public String updatePassword(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		User user = serviceFactory.getDaoFactory().getUserDao().get(id);
		if (user != null)
		{
			if (UserUtils.isSystemAdmin() || UserUtils.getCompanyId().equals(user.getCompanyId()))
			{// 判断系统权限，防止恶意篡改
				map.put("userId", id);
				return "sys/user/resetpwd";
			}
		}
		return returnErrorPage(map, "权限非法");
	}

	/**
	 * <pre>
	 * 功能 - 重置密码
	 * </pre>
	 * @param request
	 * @param user
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午4:16:15, think
	 */
	@RequestMapping(value = "resetPwd")
	@ResponseBody
	@RequiresPermissions("sys:user:resetpwd")
	@SystemControllerLog(SystemLogType = SystemLogType.RESETPWD, Description = "重置密码")
	public AjaxResponseBody reset(HttpServletRequest request, User user, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(user.getId(), user.getPassword()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		ServiceResult<Boolean> result = serviceFactory.getUserService().resetPwd(request, user.getId(), user.getPassword());

		if (result.getIsSuccess())
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody(result.getMessage());
		}
	}
	
	/**
	 * <pre>
	 * 获取当前实时用户信息(从数据库读取，不从缓存中读取)
	 * </pre>
	 * @param request
	 * @return
	 * @since 1.0, 2018年7月23日 下午4:09:57, zhengby
	 */
	@RequestMapping(value = "getUser")
	@ResponseBody
	public AjaxResponseBody getUser(HttpServletRequest request)
	{
		User user = serviceFactory.getUserService().get(UserUtils.getUserId());
		return returnSuccessBody(user);
	}
}
