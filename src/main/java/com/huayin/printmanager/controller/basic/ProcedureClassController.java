/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月28日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.basic;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.ProcedureClass;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 工序分类
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月28日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/procedureClass")
public class ProcedureClassController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 工序分类列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:58:22, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:procedureClass:list")
	public String list()
	{
		return "basic/procedureClass/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:59:03, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<ProcedureClass> ajaxList(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getProcedureClassService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 工序新增
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:59:16, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:procedureClass:create")
	public String create()
	{
		return "basic/procedureClass/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增工序
	 * </pre>
	 * @param procedureClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:59:34, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:procedureClass:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "工序分类", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody ProcedureClass procedureClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(procedureClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		ProcedureClass obj = serviceFactory.getProcedureClassService().getByName(procedureClass.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		serviceFactory.getProcedureClassService().save(procedureClass);
		UserUtils.clearCacheBasic(BasicType.PROCEDURECLASS);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 工序修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:59:44, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:procedureClass:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		ProcedureClass procedureClass = serviceFactory.getProcedureClassService().get(id);
		map.put("procedureClass", procedureClass);
		return "basic/procedureClass/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改工序
	 * </pre>
	 * @param procedureClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:59:58, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:procedureClass:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "工序分类", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody ProcedureClass procedureClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(procedureClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		ProcedureClass _procedureClass = serviceFactory.getProcedureClassService().get(procedureClass.getId());
		ProcedureClass obj = serviceFactory.getProcedureClassService().getByName(procedureClass.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_procedureClass.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_procedureClass.setName(procedureClass.getName());
		_procedureClass.setProcedureType(procedureClass.getProcedureType());
		_procedureClass.setSort(procedureClass.getSort());
		_procedureClass.setMemo(procedureClass.getMemo());
		_procedureClass.setProductType(procedureClass.getProductType());
		serviceFactory.getProcedureClassService().update(_procedureClass);

		UserUtils.clearCacheBasic(BasicType.PROCEDURECLASS);
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
	 * @since 1.0, 2017年12月28日 下午3:00:16, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:procedureClass:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "工序分类", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.PROCEDURECLASS, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
