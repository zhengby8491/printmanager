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
import com.huayin.printmanager.persist.entity.basic.MaterialClass;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 材料分类
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月28日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/materialClass")
public class MaterialClassController extends BaseController
{

	/**
	 * <pre>
	 * 页面 - 材料类型列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:29:54, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:materialClass:list")
	public String list()
	{
		return "basic/materialClass/list";
	}

	/**
	 * <pre>
	 * 功能- 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:30:17, zhengby
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<MaterialClass> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getMaterialClassService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 材料分类新增
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:25:40, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:materialClass:create")
	public String create()
	{
		return "basic/materialClass/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增材料类型
	 * </pre>
	 * @param materialClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:25:54, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:materialClass:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "材料分类", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody MaterialClass materialClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(materialClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		MaterialClass obj = serviceFactory.getMaterialClassService().getByName(materialClass.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		MaterialClass _materialClass = new MaterialClass();
		_materialClass.setCompanyId(UserUtils.getCompanyId());
		_materialClass.setName(materialClass.getName());
		_materialClass.setSort(materialClass.getSort());
		_materialClass.setMemo(materialClass.getMemo());
		serviceFactory.getMaterialClassService().save(_materialClass);

		UserUtils.clearCacheBasic(BasicType.MATERIALCLASS);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 材料分类修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:26:17, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "材料分类", Operation = Operation.UPDATE)
	@RequiresPermissions("basic:materialClass:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		MaterialClass materialClass = serviceFactory.getMaterialClassService().get(id);
		map.put("materialClass", materialClass);
		return "basic/materialClass/edit";
	}

	/**
	 * <pre>
	 * 页面 - 修改材料分类
	 * </pre>
	 * @param materialClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:26:58, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:materialClass:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "材料分类", Operation = Operation.DELETE)
	public AjaxResponseBody update(@RequestBody MaterialClass materialClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(materialClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		MaterialClass _materialClass = serviceFactory.getMaterialClassService().get(materialClass.getId());
		MaterialClass obj = serviceFactory.getMaterialClassService().getByName(materialClass.getName());

		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_materialClass.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_materialClass.setName(materialClass.getName());
		_materialClass.setSort(materialClass.getSort());
		_materialClass.setMemo(materialClass.getMemo());
		serviceFactory.getMaterialClassService().update(_materialClass);

		UserUtils.clearCacheBasic(BasicType.MATERIALCLASS);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除材料分类
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:29:38, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:materialClass:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "材料分类", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.MATERIALCLASS, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
