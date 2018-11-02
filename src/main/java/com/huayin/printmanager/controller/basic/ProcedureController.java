/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月4日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.basic;

import java.util.Date;
import java.util.List;

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
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.i18n.ResourceBundleMessageSource;
import com.huayin.printmanager.i18n.service.BasicI18nResource;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.entity.basic.ProcedureClass;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.UpdateNameType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 工序信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/procedure")
public class ProcedureController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 工序信息列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:12:31, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:procedure:list")
	public String list()
	{
		return "basic/procedure/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:12:52, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<Procedure> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getProcedureService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 工序新增
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:13:04, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:procedure:create")
	public String create(ModelMap map)
	{
		map.put("code", serviceFactory.getCommonService().getNextCode(BasicType.PROCEDURE));
		return "basic/procedure/create";
	}

	/**
	 * <pre>
	 * 页面 - 工序复制
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:13:19, think
	 */
	@RequestMapping(value = "copyCreate/{id}")
	@RequiresPermissions("basic:procedure:copyCreate")
	public String copyCreate(@PathVariable Long id, ModelMap map)
	{
		Procedure procedure = serviceFactory.getProcedureService().get(id);
		map.put("procedure", procedure);
		map.put("code", serviceFactory.getCommonService().getNextCode(BasicType.PROCEDURE));
		return "basic/procedure/copyCreate";
	}

	/**
	 * <pre>
	 * 功能 - 新增工序
	 * </pre>
	 * @param procedure
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:13:29, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:procedure:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "工序信息", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody Procedure procedure, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(procedure.getCode(), procedure.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Procedure obj = serviceFactory.getProcedureService().getByName(procedure.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		serviceFactory.getProcedureService().save(procedure);
		UserUtils.clearCacheBasic(BasicType.PROCEDURE);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 快速新增工序信息
	 * </pre>
	 * @param procedure
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月12日 下午2:10:46, think
	 */
	@RequestMapping(value = "saveQuick")
	@ResponseBody
	@RequiresPermissions("basic:procedure:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "工序信息", Operation = Operation.ADD)
	public AjaxResponseBody saveQuick(@RequestBody List<Procedure> procedureList, ModelMap map)
	{
		for (Procedure procedure : procedureList)
		{
			if (Validate.validateObjectsNullOrEmpty(procedure.getName(), procedure.getProcedureType()))
			{
				return returnErrorBody(I18nResource.VALIDATE_FAIL);
			}
			Procedure obj = serviceFactory.getProcedureService().getByName(procedure.getName());
			if (!Validate.validateObjectsNullOrEmpty(obj))
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		for (Procedure procedure : procedureList)
		{
			// 工序分类
			ProcedureClass procedureClass = serviceFactory.getProcedureClassService().getByName(procedure.getClassName());
			if (null == procedureClass)
			{
				procedureClass = serviceFactory.getProcedureClassService().saveQuick(procedure.getClassName(), procedure.getProcedureType());
			}

			// 工序信息
			serviceFactory.getProcedureService().saveQuick(procedure.getName(), procedure.getProcedureType(), procedureClass.getId());
		}
		UserUtils.clearCacheBasic(BasicType.PROCEDURE);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 工序修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:13:44, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:procedure:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		Procedure procedure = serviceFactory.getProcedureService().get(id);
		map.put("procedure", procedure);
		return "basic/procedure/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改工序
	 * </pre>
	 * @param procedure
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:14:56, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:procedure:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "工序信息", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody Procedure procedure, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(procedure.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Procedure _procedure = serviceFactory.getProcedureService().get(procedure.getId());
		Procedure obj = serviceFactory.getProcedureService().getByName(procedure.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_procedure.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_procedure.setName(procedure.getName());
		_procedure.setProcedureClassId(procedure.getProcedureClassId());
		_procedure.setProcedureType(procedure.getProcedureType());
		_procedure.setProduceType(procedure.getProduceType());
		_procedure.setYieldReportingType(procedure.getYieldReportingType());
		_procedure.setScheduleDataSource(procedure.getScheduleDataSource());
		_procedure.setIsProduce(procedure.getIsProduce());
		_procedure.setIsQuotation(procedure.getIsQuotation());
		_procedure.setIsSchedule(procedure.getIsSchedule());
		_procedure.setSort(procedure.getSort());
		_procedure.setUpdateName(UserUtils.getUser().getUserName());
		_procedure.setUpdateTime(new Date());
		_procedure.setMemo(procedure.getMemo());
		_procedure.setPrice(procedure.getPrice());
		if (procedure.getParamsType() != null)
		{
			_procedure.setParamsType(procedure.getParamsType());
			_procedure.setFormulaId(null);
		}
		else
		{
			_procedure.setParamsType(null);
			_procedure.setFormulaId(procedure.getFormulaId());
		}
		serviceFactory.getProcedureService().update(_procedure);
		UserUtils.clearCacheBasic(BasicType.PROCEDURE);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除工序
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:15:07, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:procedure:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "工序信息", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.PROCEDURE, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <pre>
	 * 功能 - 批量删除工序
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:15:17, think
	 */
	@RequestMapping(value = "batchDelete")
	@ResponseBody
	@RequiresPermissions("basic:procedure:batchDelete")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "工序信息", Operation = Operation.DELETE)
	public AjaxResponseBody batchDelete(@RequestParam("ids[]") Long[] ids)
	{
		if (ids.length == 0)
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Procedure procedure = new Procedure();
		for (Long id : ids)
		{
			if (serviceFactory.getCommonService().isUsed(BasicType.PROCEDURE, id))
			{
				procedure = serviceFactory.getProcedureService().get(id);
				return returnErrorBody(ResourceBundleMessageSource.i18nFormatter(BasicI18nResource.PROCEDURE_VALIDATE_MSG1, procedure.getName()));
			}
		}
		serviceFactory.getProcedureService().deleteByIds(ids);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 跳转到变更名称页面
	 * </pre>
	 * @param id
	 * @param name
	 * @param code
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月7日 下午2:11:40, zhengby
	 */
	@RequestMapping(value = "updateName/{id}")
	@RequiresPermissions("basic:procedure:updateName")
	public String updateName(@PathVariable Long id, ModelMap map)
	{
		Procedure procedure = serviceFactory.getProcedureService().get(id);
		map.put("id", id);
		map.put("name", procedure.getName());
		map.put("code", procedure.getCode());
		return "basic/procedure/updateName";
	}

	/**
	 * <pre>
	 * 功能 - 全局变更工序名称
	 * </pre>
	 * @param procedure
	 * @return
	 * @since 1.0, 2018年2月7日 下午2:11:54, zhengby
	 */
	@RequestMapping(value = "updateName")
	@ResponseBody
	@RequiresPermissions("basic:procedure:updateName")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "工序信息", Operation = Operation.UPDATE)
	public AjaxResponseBody updateName(@RequestBody Procedure procedure)
	{
		if (serviceFactory.getCommonService().isExist(procedure.getName(), UpdateNameType.PROCEDURE))
		{
			return returnErrorBody("已存在该名称");
		}
		else
		{
			serviceFactory.getCommonService().updateBasicName(procedure.getId(), procedure.getName(), UpdateNameType.PROCEDURE);
		}
		return returnSuccessBody();
	}
	
	/**
	 * <pre>
	 * 功能 - 精确查询工序
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年4月2日 下午2:39:52, zhengby
	 */
	@RequestMapping(value = "findProcedureByPrecise")
	@ResponseBody
	public Procedure findProcedureByPrecise(@RequestBody QueryParam queryParam)
	{
		Procedure procedure = serviceFactory.getProcedureService().findByPrecise(queryParam);
		return procedure;
	}
}
