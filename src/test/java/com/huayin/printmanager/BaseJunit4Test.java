package com.huayin.printmanager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.huayin.printmanager.service.ServiceFactory;
//使用junit4进行测试  
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations={"classpath:system-component-definition.xml"})
public class BaseJunit4Test
{
	@Autowired
	public ServiceFactory serviceFactory;
	@Test
	public void init()
	{
		System.out.println("sprint init ok!");
	}

}
