package com.huayin.printmanager.service.basic;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import com.huayin.printmanager.BaseJunit4Test;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.CustomerAddress;

public class CustomerServiceTest extends BaseJunit4Test
{

	@Test
	public void testGetCustomerByCompanyId()
	{
		List<Customer> list=serviceFactory.getCustomerService().findAll();
		for(Customer bean:list)
		{
			List<CustomerAddress> addressList=serviceFactory.getCustomerService().getAddressList(bean.getId());
			bean.setAddressList(addressList);
		}
		//serviceFactory.getCustomerService().delete(102l);
		
	}

	@Test
	public void testGetCustomerById()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetCustomerList()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testFindByCondition()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testSaveCustomer()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateCustomer()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteCustomer()
	{
		fail("Not yet implemented");
	}

}
