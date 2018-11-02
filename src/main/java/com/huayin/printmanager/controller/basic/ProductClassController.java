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
import com.huayin.printmanager.persist.entity.basic.ProductClass;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 产品分类
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月28日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/productClass")
public class ProductClassController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 产品类型列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月28日 下午3:01:12, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:productClass:list")
	public String list()
	{
		return "basic/productClass/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月28日 下午3:01:29, think
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<ProductClass> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getProductClassService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 产品分类新增
	 * </pre>
	 * @return
	 * @since 1.0, 2017年12月28日 下午3:01:35, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:productClass:create")
	public String create()
	{
		return "basic/productClass/create";
	}

	/**
	 * <pre>
	 * 功能 - 新增产品分类
	 * </pre>
	 * @param productClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月28日 下午3:02:12, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:productClass:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "产品分类", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody ProductClass productClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(productClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		ProductClass obj = serviceFactory.getProductClassService().getByName(productClass.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		ProductClass _productClass = new ProductClass();
		_productClass.setCompanyId(UserUtils.getCompanyId());
		_productClass.setProductType(productClass.getProductType());
		_productClass.setName(productClass.getName());
		_productClass.setSort(productClass.getSort());
		_productClass.setMemo(productClass.getMemo());
		serviceFactory.getProductClassService().save(_productClass);

		UserUtils.clearCacheBasic(BasicType.PRODUCTCLASS);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 产品分类修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月28日 下午3:02:26, think
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:productClass:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		ProductClass productClass = serviceFactory.getProductClassService().get(id);
		map.put("productClass", productClass);
		return "basic/productClass/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改产品分类
	 * </pre>
	 * @param productClass
	 * @param map
	 * @return
	 * @since 1.0, 2017年12月28日 下午3:02:39, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:productClass:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "产品分类", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody ProductClass productClass, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(productClass.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		ProductClass _productClass = serviceFactory.getProductClassService().get(productClass.getId());
		ProductClass obj = serviceFactory.getProductClassService().getByName(productClass.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_productClass.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_productClass.setName(productClass.getName());
		_productClass.setProductType(productClass.getProductType());
		_productClass.setSort(productClass.getSort());
		_productClass.setMemo(productClass.getMemo());
		serviceFactory.getProductClassService().update(_productClass);
		UserUtils.clearCacheBasic(BasicType.PRODUCTCLASS);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除产品分类
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月28日 下午3:02:50, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:productClass:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "产品分类", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.PRODUCTCLASS, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
