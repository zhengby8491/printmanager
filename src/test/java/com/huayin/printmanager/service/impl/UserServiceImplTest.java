package com.huayin.printmanager.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.huayin.common.util.DateTimeUtil;
import com.huayin.printmanager.BaseJunit4Test;
import com.huayin.printmanager.persist.dao.UserRepository;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.State;
import com.huayin.printmanager.utils.UserUtils;

public class UserServiceImplTest extends BaseJunit4Test
{

	@Test
	public void testGetByUserName()
	{
		User u = serviceFactory.getUserService().getByUserName("admin");
		System.out.println(u);
		assertNotNull(u);
	}

	// @Test
	public void testSave()
	{
		User test = new User();
		test.setUserName("test");
		test.setPassword(UserUtils.entryptPassword("test"));
		test.setState(State.CLOSED);
		test.setCreateTime(new Date());
		test = serviceFactory.getPersistService().save(test);
		System.out.println(test);
		assertNotNull(test);
		User admin = new User();
		admin.setUserName("admin");
		admin.setPassword(UserUtils.entryptPassword("admin"));
		admin.setState(State.NORMAL);
		admin.setCreateTime(new Date());
		admin = serviceFactory.getPersistService().save(admin);
		System.out.println(admin);
		assertNotNull(admin);
	}

	@Test
	public void testUpdate()
	{
		User u = serviceFactory.getUserService().getByUserName("admin");
		u = serviceFactory.getPersistService().lockObject(User.class, u.getId());
		System.out.println(u);
		System.out.println(DateTimeUtil.formatLongStr(u.getLastLoginTime()));
		u.setLastLoginTime(new Date());
		u = serviceFactory.getDaoFactory().getCommonDao().updateEntity(u);
		// u=serviceFactory.getUserService().update(u);
		System.out.println(DateTimeUtil.formatLongStr(u.getLastLoginTime()));
		assertNotNull(u);
	}

	// @Test
	public void testUpdateUserName()
	{
		UserRepository ur = serviceFactory.getDaoFactory().getUserRepository();
		//serviceFactory.getUserService().updateRealName("超级管理员", 2l);
		System.out.println(ur.getByUserName("admin"));
		System.out.println(ur.findByCompanyId("1"));

		Sort sort = new Sort(Direction.DESC, "id");
		for (int i = 0; i < 2; i++)
		{
			System.out.println("第" + (i + 1) + "页数据");
			System.out.println(ur.findByPage(new PageRequest(i, 1, sort)));
		}

		System.out.println(ur.getByUserNameForNative("admin"));
	}
}
