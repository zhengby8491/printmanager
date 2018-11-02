/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月22日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.basic;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.controller.vo.EmployeeVo;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.Department;
import com.huayin.printmanager.persist.entity.basic.Employee;
import com.huayin.printmanager.persist.entity.basic.Position;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.EmployeeState;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 员工信息
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月21日 上午9:11:49
 * @since        2.0, 2017年12月22日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/employee")
public class EmployeeController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 员工信息列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:17:25, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:employee:list")
	public String list()
	{
		return "basic/employee/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求 
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:18:16, zhengby
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	@RequiresPermissions("basic:employee:list")
	public SearchResult<Employee> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getEmployeeService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 跳转到新增员工信息 
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:12:58, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:employee:create")
	public String create(ModelMap map)
	{
		map.put("code", serviceFactory.getCommonService().getNextCode(BasicType.EMPLOYEE));
		return "basic/employee/create";
	}

	/**
	 * <pre>
	 * 页面 - 快捷创建跳转到新增员工信息 
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月1日 下午3:20:03, zhengby
	 */
	@RequestMapping(value = "createShotCut")
	@RequiresPermissions("basic:employee:create")
	public String createShotCut(ModelMap map)
	{
		map.put("code", serviceFactory.getCommonService().getNextCode(BasicType.EMPLOYEE));
		return "basic/employee/createShotCut";
	}
	/**
	 * <pre>
	 * 功能 - 新增员工信息
	 * </pre>
	 * @param employee
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:13:24, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:employee:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "员工信息", Operation = Operation.ADD)
	public AjaxResponseBody save(EmployeeVo employee, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(employee.getCode(), employee.getName()))
		{
			return returnErrorBody("I18nResource.VALIDATE_FAIL");
		}
		Employee obj = serviceFactory.getEmployeeService().getByName(employee.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		Employee _employee = new Employee();
		_employee.setCompanyId(UserUtils.getCompanyId());
		_employee.setCode(serviceFactory.getCommonService().getNextCode(BasicType.EMPLOYEE));
		_employee.setName(employee.getName());
		_employee.setMobile(employee.getMobile());
		_employee.setSexType(employee.getSexType());
		_employee.setEmail(employee.getEmail());
		_employee.setPositionId(employee.getPositionId());
		_employee.setDepartmentId(employee.getDepartmentId());
		_employee.setState(employee.getState());
		_employee.setSort(employee.getSort());
		_employee.setEntryTime(employee.getEntryTime());
		_employee.setDepartureTime(employee.getDepartureTime());
		_employee.setCreateName(UserUtils.getUser().getUserName());
		_employee.setCreateTime(new Date());
		_employee.setMemo(employee.getMemo());
		Employee _employeeNew = serviceFactory.getEmployeeService().save(_employee);
		UserUtils.clearCacheBasic(BasicType.EMPLOYEE);
		return returnSuccessBody(_employeeNew);
	}

	/**
	 * <pre>
	 * 页面 - 跳转到修改员工信息
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:13:46, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:employee:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Employee employee = serviceFactory.getEmployeeService().get(id);
		List<Department> departmentList = serviceFactory.getDepartmentService().findAll();
		List<Position> positionList = serviceFactory.getPositionService().findAll();
		map.put("departmentList", departmentList);
		map.put("positionList", positionList);
		map.put("employee", employee);
		return "basic/employee/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改员工信息
	 * </pre>
	 * @param employee
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:14:15, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "员工信息", Operation = Operation.UPDATE)
	@RequiresPermissions("basic:employee:edit")
	public AjaxResponseBody update(EmployeeVo employee, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(employee.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Employee _employee = serviceFactory.getEmployeeService().get(employee.getId());
		Employee obj = serviceFactory.getEmployeeService().getByName(employee.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_employee.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_employee.setCode(employee.getCode());
		_employee.setName(employee.getName());
		_employee.setMobile(employee.getMobile());
		_employee.setSexType(employee.getSexType());
		_employee.setEmail(employee.getEmail());
		_employee.setPositionId(employee.getPositionId());
		_employee.setDepartmentId(employee.getDepartmentId());
		_employee.setState(employee.getState());
		_employee.setSort(employee.getSort());
		_employee.setEntryTime(employee.getEntryTime());
		_employee.setDepartureTime(employee.getDepartureTime());
		_employee.setUpdateName(UserUtils.getUser().getUserName());
		_employee.setUpdateTime(new Date());
		_employee.setMemo(employee.getMemo());
		serviceFactory.getEmployeeService().update(_employee);
		UserUtils.clearCacheBasic(BasicType.EMPLOYEE);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 员工离职
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:15:00, zhengby
	 */
	@RequestMapping(value = "stop/{id}")
	@ResponseBody
	@RequiresPermissions("basic:employee:edit")
	public boolean stop(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			Employee employee = serviceFactory.getPersistService().lockObject(Employee.class, id);
			employee.setState(EmployeeState.LEAVEJOB);
			employee.setUpdateTime(new Date());
			employee.setUpdateName(UserUtils.getUser().getUserName());
			serviceFactory.getPersistService().update(employee);

			UserUtils.clearCacheBasic(BasicType.EMPLOYEE);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <pre>
	 * 功能 - 员工正常
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:15:42, zhengby
	 */
	@RequestMapping(value = "start/{id}")
	@ResponseBody
	@RequiresPermissions("basic:employee:edit")
	public AjaxResponseBody start(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Employee employee = serviceFactory.getPersistService().lockObject(Employee.class, id);
		employee.setState(EmployeeState.NORMAL);
		employee.setUpdateTime(new Date());
		employee.setUpdateName(UserUtils.getUser().getUserName());
		serviceFactory.getPersistService().update(employee);

		UserUtils.clearCacheBasic(BasicType.EMPLOYEE);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 员工休假
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:16:33, zhengby
	 */
	@RequestMapping(value = "holiday/{id}")
	@ResponseBody
	@RequiresPermissions("basic:employee:edit")
	public AjaxResponseBody holiday(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Employee employee = serviceFactory.getPersistService().lockObject(Employee.class, id);
		employee.setState(EmployeeState.HOLIDAY);
		employee.setUpdateTime(new Date());
		employee.setUpdateName(UserUtils.getUser().getUserName());
		serviceFactory.getPersistService().update(employee);

		UserUtils.clearCacheBasic(BasicType.EMPLOYEE);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除员工信息
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:17:04, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:employee:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "员工信息", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.EMPLOYEE, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <pre>
	 * 功能 - 查找同部门的同事
	 * </pre>
	 * @param userId
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:19:12, zhengby
	 */
	@RequestMapping(value = "findSameEmployee")
	@ResponseBody
	@RequiresPermissions("basic:employee:list")
	public AjaxResponseBody findSameEmployee(@RequestParam(required = false) Long userId)
	{
		// 获取客户资料表销售员ID
		List<Long> cemployee = serviceFactory.getCustomerService().findAllSaleEmployeeIds();
		List<Long> semployee = serviceFactory.getSupplierService().findAllSaleEmployeeIds();
		cemployee.removeAll(semployee);
		cemployee.addAll(semployee);
		if (userId != null)
		{
			User user = serviceFactory.getUserService().getById(userId);
			if (!cemployee.contains(user.getEmployeeId()))
			{
				if (user.getEmployeeId() != null)
				{
					cemployee.add(user.getEmployeeId());
				}
			}
		}
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (Long employeeId : cemployee)
		{
			Employee emp = serviceFactory.getEmployeeService().get(employeeId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", emp.getId());
			map.put("name", emp.getName());
			if (userId != null)
			{
				Boolean flag = serviceFactory.getUserService().checkShared(userId, employeeId);
				if (flag)
				{
					map.put("checked", "checked=\"checked\"");
				}
			}
			listMap.add(map);
		}
		return returnSuccessBody(listMap);
	}
}
