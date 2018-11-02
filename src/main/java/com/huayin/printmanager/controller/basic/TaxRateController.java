/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月27日 上午9:30:23
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
import com.huayin.printmanager.i18n.service.BasicI18nResource;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.TaxRate;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 税率信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年12月27日
 * @since        2.0, 2017年12月27日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/taxRate")
public class TaxRateController extends BaseController
{

	/**
	 * 税率信息列表
	 * @return
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:taxRate:list")
	public String list()
	{
		return "basic/taxRate/list";
	}

	/**
	 * 列表AJAX请求
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<TaxRate> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<TaxRate> result = serviceFactory.getTaxRateService().findByCondition(queryParam);

		return result;
	}

	/**
	 * 跳转到新增税率信息
	 * @return
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:taxRate:create")
	public String create()
	{
		return "basic/taxRate/create";
	}

	/**
	 * 新增税率信息
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:taxRate:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "税收信息", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody TaxRate taxRate, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(taxRate.getName(), taxRate.getPercent()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		TaxRate obj = serviceFactory.getTaxRateService().getByName(taxRate.getName());
		TaxRate _obj = serviceFactory.getTaxRateService().getByPercent(taxRate.getPercent());
		if (!Validate.validateObjectsNullOrEmpty(_obj))
		{
			return returnErrorBody(BasicI18nResource.TAXRATE_VALIDATE_NAME_EXIST);
		}
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		TaxRate _taxRate = new TaxRate();
		_taxRate.setCompanyId(UserUtils.getCompanyId());
		_taxRate.setName(taxRate.getName());
		_taxRate.setSort(taxRate.getSort());
		_taxRate.setPercent(taxRate.getPercent());
		_taxRate.setMemo(taxRate.getMemo());

		TaxRate _taxRateNew = serviceFactory.getTaxRateService().save(_taxRate);
		UserUtils.clearCacheBasic(BasicType.TAXRATE);
		return returnSuccessBody(_taxRateNew);
	}

	/**
	 * 跳转到修改税率信息
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:taxRate:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		TaxRate taxRate = serviceFactory.getTaxRateService().get(id);
		map.put("taxRate", taxRate);
		return "basic/taxRate/edit";
	}

	/**
	 * 修改税率信息
	 * @param request
	 * @param response
	 * @param model
	 * @param position
	 * @return
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:taxRate:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "税收信息", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody TaxRate taxRate, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(taxRate.getName(), taxRate.getPercent()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		TaxRate _taxRate = serviceFactory.getTaxRateService().get(taxRate.getId());
		TaxRate obj = serviceFactory.getTaxRateService().getByName(taxRate.getName());
		TaxRate _obj = serviceFactory.getTaxRateService().getByPercent(taxRate.getPercent());
		if (!Validate.validateObjectsNullOrEmpty(_obj))
		{
			if (_taxRate.getId().longValue() != _obj.getId().longValue())
			{
				return returnErrorBody(BasicI18nResource.TAXRATE_VALIDATE_NAME_EXIST);
			}
		}
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_taxRate.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_taxRate.setName(taxRate.getName());
		_taxRate.setSort(taxRate.getSort());
		_taxRate.setPercent(taxRate.getPercent());
		_taxRate.setMemo(taxRate.getMemo());

		serviceFactory.getTaxRateService().update(_taxRate);
		UserUtils.clearCacheBasic(BasicType.TAXRATE);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 删除税率信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月27日 上午9:30:33, think
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:taxRate:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "税收信息", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.TAXRATE, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
