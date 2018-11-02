package com.huayin.printmanager.persist.dao.impl;

import org.springframework.stereotype.Repository;

import com.huayin.common.persist.AbstractDao;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.printmanager.persist.dao.UserDao;
import com.huayin.printmanager.persist.entity.sys.User;
@Repository
public class UserDaoImpl extends AbstractDao<User> implements UserDao
{
	@Override
	public User getByUserName(String userName)
	{
		DynamicQuery query=new DynamicQuery(User.class);
		query.eq("userName", userName);
		return getSingleByDynamicQuery(query);
	}

	@Override
	public User getByMobile(String mobile)
	{
		DynamicQuery query=new DynamicQuery(User.class);
		query.eq("mobile", mobile);
		return getSingleByDynamicQuery(query);
	}
	
}
