/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.service;

import java.util.List;

import com.huayin.printmanager.persist.entity.BaseBasicTableEntity;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.entity.sys.UserShare;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.wx.vo.UserVo;

/**
 * <pre>
 * 微信 - 基础信息
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
public interface WXBasicService
{
	/**
	 * <pre>
	 * 获取当前用户业务数据授权的用户
	 * </pre>
	 * @param userId
	 * @param companyId
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:38:48, think
	 */
	public Long[] findSharedEmployeeIds(Long userId,String companyId);
	
	/**
	 * <pre>
	 * 根据OpenId获取用户
	 * </pre>
	 * @param openid
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:38:56, think
	 */
	public UserShare getUserShareByOpenId(String openid);
	
	/**
	 * <pre>
	 * 获取user
	 * </pre>
	 * @param userName
	 * @param passWord
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:39:02, think
	 */
	public User getUser(String userName,String passWord);
	
	/**
	 * <pre>
	 * 根据用户id 查用户，验证公司ID
	 * </pre>
	 * @param userId
	 * @param companyId
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:39:09, think
	 */
	public UserVo getUser(Long userId,String companyId);
	
	/**
	 * <pre>
	 * 根据用户ID查UserShare对象（是否有绑定）
	 * </pre>
	 * @param userId
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:39:15, think
	 */
	public UserShare getUserShare(Long userId);
	
	/**
	 * <pre>
	 * 绑定用户
	 * </pre>
	 * @param user
	 * @param openid
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:39:23, think
	 */
	public Boolean bind(User user,String openid);
	
	/**
	 * <pre>
	 * 解除绑定用户
	 * </pre>
	 * @param userShare
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:39:29, think
	 */
	public Boolean delBind(UserShare userShare);
	
	/**
	 * <pre>
	 * 获取用户
	 * </pre>
	 * @param userId
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:39:39, think
	 */
	public User getUser(Long userId);
	
	/**
	 * <pre>
	 * 得到基础资料信息列表
	 * </pre>
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午10:39:46, think
	 */
	public List<BaseBasicTableEntity> getBasicInfoList(BasicType type);
}
