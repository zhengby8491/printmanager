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

import com.huayin.printmanager.persist.entity.sys.UserShare;
import com.huayin.printmanager.persist.enumerate.UserShareType;

/**
 * <pre>
 * 框架 - 用户快捷登录功能
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
public interface UserShareService
{
	/**
	 * <pre>
	 * 新增用户快捷登录功能
	 * </pre>
	 * @param userShare
	 * @since 1.0, 2017年10月25日 下午6:12:09, think
	 */
	public void save(UserShare userShare);

	/**
	 * <pre>
	 * 检查是否已经绑定 有则返回 否则返回NULL
	 * </pre>
	 * @param openid 第三方唯一身份标识符
	 * @param userShareType 第三方平台类型
	 * @return
	 * @since 1.0, 2017年10月25日 下午6:12:18, think
	 */
	public UserShare isExist(String openid, UserShareType userShareType);

}
