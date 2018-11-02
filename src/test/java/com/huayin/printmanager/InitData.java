package com.huayin.printmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.persist.entity.sys.Role;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.CompanyState;
import com.huayin.printmanager.persist.enumerate.CompanyType;
import com.huayin.printmanager.persist.enumerate.State;
import com.huayin.printmanager.utils.MenuUtils;
import com.huayin.printmanager.utils.UserUtils;
import com.thoughtworks.xstream.XStream;

public class InitData extends BaseJunit4Test
{
	@Test
	public void initCompany()
	{

		if (serviceFactory.getDaoFactory().getCommonDao().getEntity(Company.class, "1") == null)
		{
			Company c = new Company();
			c.setId("1");
			c.setName("深圳华印信息技术股份有限公司");
			c.setCity("深圳");
			c.setProvince("广东");
			c.setState(CompanyState.ONSALING);
			c.setType(CompanyType.NORMAL);
			c.setCreateTime(new Date());
			serviceFactory.getPersistService().save(c);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void initMenu()
	{
		List<Menu> existMenus = serviceFactory.getPersistService().list(Menu.class);

		XStream _xstream = new XStream();
		_xstream.alias("menus", List.class);
		_xstream.alias("menu", Menu.class);
		String oldXml=_xstream.toXML(MenuUtils.buildTree(existMenus, MenuUtils.TREE_ROOT_ID));
		System.out.println(oldXml);

		XStream xstream = new XStream();
		xstream.alias("menus", List.class);
		xstream.alias("childrens", List.class);
		xstream.alias("menu", Menu.class);

		List<Menu> menuTree = (List<Menu>) xstream
				.fromXML(new File(ClassLoader.getSystemResource("").getPath() + "menu.xml"));
		List<Menu> allMenus = new ArrayList<Menu>();
		//树型转非树型数组
		converTreeToList(allMenus, menuTree, 0l);
		
		List<Menu> noExistMenus = new ArrayList<Menu>();
		//过滤已存在菜单
		for (Menu m : allMenus)
		{
			if (!existMenus.contains(m))
			{
				noExistMenus.add(m);
			}
		}
		if (noExistMenus.size() > 0)
		{
			serviceFactory.getPersistService().saveAllObject(noExistMenus);
		}

	
	}
	@Test
	public void initRole()
	{
		List<Role> data = serviceFactory.getRoleService().findAll("1");
		if (data == null || data.size() == 0)
		{
			Role r = new Role();
			r.setName("超级管理员");
			r.setCompanyId("1");
			serviceFactory.getPersistService().save(r);
		}
	}

	@Test
	public void initUser()
	{
		if (serviceFactory.getUserService().getByUserName("admin") == null)
		{
			User admin = new User();
			admin.setUserName("admin");
			admin.setPassword(UserUtils.entryptPassword("admin"));
			admin.setState(State.NORMAL);
			admin.setCreateTime(new Date());
			admin.setMobile("13111111111");
			admin.setEmail("abc@163.com");
			admin.setCompanyId("1");
			admin = serviceFactory.getPersistService().save(admin);

		}
	}

	


	private void converTreeToList(List<Menu> list, List<Menu> tree, Long parentId)
	{
		for (Menu menu : tree)
		{
			if (menu.getChildrens() != null && menu.getChildrens().size() > 0)
			{
				converTreeToList(list, menu.getChildrens(), menu.getId());
			}
			menu.setChildrens(null);
			menu.setVersion(0l);
			menu.setParentId(parentId);
			list.add(menu);
		}
	}
	
}
