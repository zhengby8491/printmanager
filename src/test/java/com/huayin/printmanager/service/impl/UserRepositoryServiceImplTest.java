package com.huayin.printmanager.service.impl;

import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.huayin.printmanager.BaseJunit4Test;
import com.huayin.printmanager.persist.dao.UserRepository;
import com.huayin.printmanager.service.sys.UserService;

public class UserRepositoryServiceImplTest extends BaseJunit4Test
{

	private UserRepository ur = serviceFactory.getDaoFactory().getUserRepository();

	public UserService userService = serviceFactory.getUserService();

	@Test
	public void testUpdateUserName()
	{
		//userService.updateRealName("超级管理员", 2l);
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
