package com.huayin.printmanager.utils;

import java.util.List;

import org.junit.Test;

import com.huayin.printmanager.BaseJunit4Test;
import com.huayin.printmanager.persist.entity.sys.Menu;

public class MenuUtilsTest extends BaseJunit4Test
{

	@Test
	public void testBuildtree()
	{
		List<Menu> list=serviceFactory.getDaoFactory().getCommonDao().listAllEntity(Menu.class);
		list=MenuUtils.buildTree(list, 0l);
	}

}
