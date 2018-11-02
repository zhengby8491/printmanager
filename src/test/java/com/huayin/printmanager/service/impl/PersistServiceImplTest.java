package com.huayin.printmanager.service.impl;

import org.junit.Test;

import com.huayin.printmanager.BaseJunit4Test;
import com.huayin.printmanager.persist.entity.sys.Company;

public class PersistServiceImplTest extends BaseJunit4Test {
	
	@Test
	public void testCountByNamedQuery() {
		System.out.println(serviceFactory.getPersistService().countByNamedQuery("company.count", "嗒嗒"));
	}

	@Test
	public void testDeleteAbstractEntity() {
		
	}

	@Test
	public void testDeleteClassOfTObject() {
		
	}

	@Test
	public void testFindByNamedQuery() {
		
	}

	@Test
	public void testFindListAndCountByNamedQuery() {
		
	}

	@Test
	public void testGet() {
		
	}

	@Test
	public void testGetByNamedQuery() {
		System.out.println(serviceFactory.getPersistService().getByNamedQuery(Company.class, "company.findById", 1l));
	}

	@Test
	public void testList() {
	}

	@Test
	public void testListByNamedQuery() {
	}

	@Test
	public void testSaveOrUpdate() {
		
	}

	@Test
	public void testSave() {
		
	}

	@Test
	public void testSaveAllObject() {
//		
	}

	@Test
	public void testUpdate() {
//		
	}

	@Test
	public void testUpdateAllObject() {
		
	}

	@Test
	public void testCountByDynamicQuery() {
		
	}

	@Test
	public void testFindByDynamicQuery() {
		
	}

	@Test
	public void testGetByDynamicQuery() {
		
	}

	@Test
	public void testFindByDynamicQueryPage() {
		
	}

	@Test
	public void testLockObject() {
		
	}

	@Test
	public void testLockByDynamicQuery() {
		
	}

}
