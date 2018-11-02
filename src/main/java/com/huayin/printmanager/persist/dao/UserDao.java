package com.huayin.printmanager.persist.dao;

import com.huayin.common.persist.NestedSimplyDao;
import com.huayin.printmanager.persist.entity.sys.User;

/**
 * 用户DAO接口
 * @author zhaojt
 * @version 2014-05-16
 */
public interface UserDao extends NestedSimplyDao<User>
{
	public User getByUserName(String userName);
	public User getByMobile(String mobile);
}
