package com.huayin.printmanager.persist.dao;

import com.huayin.common.persist.CommonDao;
import com.huayin.printmanager.security.SessionDAO;

/**
 * <pre>
 * DAO接口工厂
 * </pre>
 * @author zhaojitao
 * @version 1.0, 2016-5-5
 */
public interface DaoFactory
{
	public CommonDao getCommonDao();

	public SessionDAO getSessionDao();

	public RoleDao getRoleDao();

	public UserDao getUserDao();
	public UserRepository getUserRepository();
}
