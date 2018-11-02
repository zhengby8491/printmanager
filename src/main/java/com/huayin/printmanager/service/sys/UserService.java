/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.huayin.common.exception.ServiceResult;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.service.sys.vo.RegisterVo;

/**
 * <pre>
 * 系统模块 - 用户管理
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface UserService
{
	/**
	 * <pre>
	 * 根据id查询用户
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:04:12, think
	 */
	public User get(Long id);

	/**
	 * <pre>
	 * 获取所有用户
	 * </pre>
	 * @return
	 * @since 1.0, 2018年4月18日 下午2:22:39, think
	 */
	public List<User> getAll();

	/**
	 * <pre>
	 * 根据id查询用户
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:04:34, think
	 */
	public User getById(Long id);

	/**
	 * <pre>
	 * 根据用户名称查询用户
	 * </pre>
	 * @param userName
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:04:24, think
	 */
	public User getByUserName(String userName);

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param employeeId
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:05:20, think
	 */
	public Long[] findSharedEmployeeIds(Long employeeId);

	/**
	 * <pre>
	 * 多条件查询用户
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param userName
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:05:48, think
	 */
	public SearchResult<User> findByCondition(Date dateMin, Date dateMax, String userName, Integer pageIndex, Integer pageSize);

	/**
	 * <pre>
	 * 找回密码
	 * </pre>
	 * @param request
	 * @param userId
	 * @param newPwd
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:06:00, think
	 */
	public ServiceResult<Boolean> findPwd(HttpServletRequest request, Long userId, String newPwd);

	/**
	 * <pre>
	 * 新增用户
	 * </pre>
	 * @param user
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:06:08, think
	 */
	public User save(User user);

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param sharedEmployeeId
	 * @param employeeIds
	 * @since 1.0, 2017年10月25日 下午6:06:24, think
	 */
	public void updateShared(Long sharedEmployeeId, Long[] employeeIds);

	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param sharedUserId
	 * @param employeeId
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:06:30, think
	 */
	public Boolean checkShared(Long sharedUserId, Long employeeId);

	/**
	 * <pre>
	 * 根据用户名检查用户是否存在
	 * </pre>
	 * @param userName
	 * @param unUserId
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:06:32, think
	 */
	public boolean existUserName(String userName, Long unUserId);

	/**
	 * <pre>
	 * 根据手机号码检查用户是否存在
	 * </pre>
	 * @param mobile
	 * @param unUserId
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:06:45, think
	 */
	public boolean existMobile(String mobile, Long unUserId);

	/**
	 * <pre>
	 * 注册
	 * </pre>
	 * @param vo
	 * @param request
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:07:07, think
	 */
	public ServiceResult<User> register(RegisterVo vo, HttpServletRequest request);

	/**
	 * <pre>
	 * 登录成功回调
	 * </pre>
	 * @param request
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:07:13, think
	 */
	public ServiceResult<User> onLoginSuccess(HttpServletRequest request);

	/**
	 * <pre>
	 * 重置密码
	 * </pre>
	 * @param request
	 * @param userId
	 * @param newPwd
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:07:19, think
	 */
	public ServiceResult<Boolean> resetPwd(HttpServletRequest request, Long userId, String newPwd);

}
