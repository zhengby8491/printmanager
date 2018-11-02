package com.huayin.printmanager.service.impl;

import java.util.List;

import org.junit.Test;

import com.huayin.printmanager.BaseJunit4Test;
import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.persist.entity.sys.Role;

public class MenuServiceImplTest extends BaseJunit4Test 
{

//	@Test
	public void testFindByUserId()
	{
		List<Menu> menus=serviceFactory.getMenuService().findByUserId(2l);
		for(Menu m:menus){
		System.out.println(m);
		}
		
		List<Role> roles=serviceFactory.getRoleService().findByUserId(2l);
		System.out.println(roles);
	}

	//@Test
	public void testFindAllPermissionByUserId()
	{
		List<Menu> list=serviceFactory.getMenuService().findAllPermissionByUserId(2l);
		System.out.println(list);
	}

	@Test
	public void testFindNavigationMenuByUserId()
	{
		List<Menu> list=serviceFactory.getMenuService().findNavigationMenuByUserId(2l);
		System.out.println(list);
	}
	
}
