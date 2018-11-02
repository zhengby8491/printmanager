/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月26日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic.impl;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Department;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Position;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.service.basic.EmployeeService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.ExcelUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 员工信息
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月21日 上午9:11:49
 * @since        2.0, 2017年12月22日 下午17:07:00, think, 规范和国际化
 */
@Service
public class EmployeeServiceImpl extends BaseServiceImpl implements EmployeeService
{
	@Override
	public Employee get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Employee.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Employee.class);
	}

	@Override
	public Employee getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(Employee.class);
		query.eq("name", name);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Employee.class);
	}

	@Override
	@Transactional
	public Employee save(Employee employee)
	{
		return daoFactory.getCommonDao().saveEntity(employee);
	}

	@Override
	@Transactional
	public Employee update(Employee employee)
	{
		return daoFactory.getCommonDao().updateEntity(employee);
	}

	@Override
	public List<Employee> findAll()
	{
		DynamicQuery query = new CompanyDynamicQuery(Employee.class);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Employee.class);
	}

	@Override
	public List<Employee> findByDepartmentId(Long departmentId)
	{
		DynamicQuery query = new CompanyDynamicQuery(Employee.class);
		query.eq("departmentId", departmentId);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, Employee.class);
	}

	@Override
	public SearchResult<Employee> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Employee.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getDateMin() != null)
		{
			query.ge("createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("createTime", queryParam.getDateMax());
		}
		if (StringUtils.isNotEmpty(queryParam.getEmployeeName()))
		{
			query.like("name", "%" + queryParam.getEmployeeName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCode()))
		{
			query.like("code", "%" + queryParam.getCode() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		query.desc("createTime");
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Employee.class);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer importFromExcel(InputStream inputStream) throws Exception
	{
		Integer count = 0;
		try
		{
			Workbook book = ExcelUtils.initBook(inputStream);
			List<ArrayList<String>> sheet = ExcelUtils.readXlsx(book, 0);
			String code = serviceFactory.getCommonService().getNextCode(BasicType.EMPLOYEE).replace(BasicType.EMPLOYEE.getCodePrefix(), "");
			Integer codeIndex = Integer.parseInt(code);
			for (ArrayList<String> row : sheet)
			{
				Employee obj = serviceFactory.getEmployeeService().getByName(row.get(0));
				if (!Validate.validateObjectsNullOrEmpty(obj))
				{
					continue;
				}
				/*
				 * QueryParam queryParam=new QueryParam (); queryParam.setEmployeeName(row.get(0)); SearchResult<Employee>
				 * result=serviceFactory.getEmployeeService().findByCondition(queryParam); if(result.getCount()>0){ continue; }
				 */
				Employee _employee = new Employee();
				_employee.setCompanyId(UserUtils.getCompanyId());
				_employee.setCode(BasicType.EMPLOYEE.getCodePrefix() + new DecimalFormat("000000").format(codeIndex++));
				_employee.setName(row.get(0));
				_employee.setMobile(row.get(3));
				_employee.setSexType(ObjectUtils.getSexType(row.get(4)));
				_employee.setEmail(row.get(5));
				Position position = serviceFactory.getPositionService().getByName(row.get(2));
				if (position == null)
				{
					position = new Position();
					position.setName(row.get(2));
					position.setCompanyId(UserUtils.getCompanyId());
					serviceFactory.getPositionService().save(position);
				}
				_employee.setPositionId(position.getId());
				Department department = serviceFactory.getDepartmentService().getByName(row.get(1));
				if (department == null)
				{
					department = new Department();
					department.setName(row.get(1));
					department.setCompanyId(UserUtils.getCompanyId());
					serviceFactory.getDepartmentService().save(department);
				}
				_employee.setDepartmentId(department.getId());
				_employee.setState(ObjectUtils.getEmployeeState(row.get(8)));
				_employee.setSort(Integer.parseInt(row.get(10)));
				_employee.setEntryTime(DateUtils.parseDate(row.get(6)));
				_employee.setDepartureTime(DateUtils.parseDate(row.get(7)));
				_employee.setCreateName(UserUtils.getUserName());
				_employee.setCreateTime(new Date());
				_employee.setMemo(row.get(9));
				daoFactory.getCommonDao().saveEntity(_employee);
				UserUtils.clearCacheBasic(BasicType.EMPLOYEE);
				count++;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			UserUtils.clearCacheBasic(BasicType.EMPLOYEE);
		}
		return count;
	}
}
