/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月2日 上午9:30:23
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
import com.huayin.printmanager.persist.entity.basic.Material;
import com.huayin.printmanager.persist.entity.basic.MaterialClass;
import com.huayin.printmanager.persist.entity.basic.Unit;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.OfferMaterialType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.UpdateNameType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 材料信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月2日
 * @since        2.0, 2018年1月2日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/basic/material")
public class MaterialController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 材料信息列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:36:15, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("basic:material:list")
	public String list()
	{
		return "basic/material/list";
	}

	/**
	 * <pre>
	 * 功能 - 列表AJAX请求
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:36:31, zhengby
	 */
	@RequestMapping(value = "listAjax")
	@ResponseBody
	public SearchResult<Material> listAjax(@RequestBody QueryParam queryParam)
	{
		return serviceFactory.getMaterialService().findByCondition(queryParam);
	}

	/**
	 * <pre>
	 * 页面 - 跳转到新增材料信息
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:32:23, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("basic:material:create")
	public String create(ModelMap map)
	{
		map.put("code", serviceFactory.getCommonService().getNextCode(BasicType.MATERIAL));
		return "basic/material/create";
	}

	/**
	 * <pre>
	 * 页面 - 跳转到复制新增材料信息
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:32:48, zhengby
	 */
	@RequestMapping(value = "copyCreate/{id}")
	@RequiresPermissions("basic:material:copyCreate")
	public String copyCreate(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Material material = serviceFactory.getMaterialService().get(id);
		map.put("material", material);
		map.put("code", serviceFactory.getCommonService().getNextCode(BasicType.MATERIAL));
		return "basic/material/copyCreate";
	}

	/**
	 * <pre>
	 * 功能 - 新增材料信息
	 * </pre>
	 * @param material
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:33:12, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("basic:material:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "材料信息", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody Material material, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(material.getName()))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Material obj = serviceFactory.getMaterialService().getByName(material.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
		}
		serviceFactory.getMaterialService().save(material);

		UserUtils.clearCacheBasic(BasicType.MATERIAL);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 快捷新增材料
	 * </pre>
	 * @param material
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月12日 下午1:51:35, think
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "saveQuick")
	@ResponseBody
	@RequiresPermissions("basic:material:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "材料信息", Operation = Operation.ADD)
	public AjaxResponseBody saveQuick(@RequestBody List<Material> materialList, ModelMap map)
	{
		for (Material material : materialList)
		{
			if (Validate.validateObjectsNullOrEmpty(material.getName(), material.getWeight()))
			{
				return returnErrorBody(I18nResource.VALIDATE_FAIL);
			}
			// 这里可以考虑一次查询所有名称
			Material obj = serviceFactory.getMaterialService().getByName(material.getName());
			if (!Validate.validateObjectsNullOrEmpty(obj))
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		/**
		 * TODO 单位换算可能需要支持国际化
		 * 1. 材料信息时，自动新增的材料计价单位默认为【吨】，库存单位默认为【张】
		 * 2. 坑纸材料信息时， 自动新增的材料计价单位默认为【千平方英寸】，库存单位默认为【张】
		 */
		List<Unit> unitList = (List<Unit>) UserUtils.getBasicList("UNIT");
		// 单位 - 吨（正常情况不可能为null）
		Unit d = null;
		// 单位 - 张（正常情况不可能为null）
		Unit z = null;
		// 单位 - 千平方英寸（正常情况不可能为null）
		Unit thousand = null;
		for (Unit unit : unitList)
		{
			if ("吨".equals(unit.getName()))
			{
				d = unit;
			}
			else if ("张".equals(unit.getName()))
			{
				z = unit;
			}
			else if ("千平方英寸".equals(unit.getName()))
			{
				thousand = unit;
			}
		}

		for (Material material : materialList)
		{
			// 材料名称
			String name = material.getName();
			// 材料分类名称
			String className = material.getOfferClassName();
			// 添加材料分类
			MaterialClass materialClass = serviceFactory.getMaterialClassService().getByName(className);
			if (null == materialClass)
			{
				materialClass = serviceFactory.getMaterialClassService().saveQuick(className);
			}

			// 创建材料
			if (null != material.getOfferMaterialType() && material.getOfferMaterialType() == OfferMaterialType.MATERIAL)
			{
				serviceFactory.getMaterialService().saveQuick(materialClass.getId(), name, material.getWeight(), d.getId(), z.getId(), material.getLastPurchPrice());
			}
			// 创建坑纸
			else if (null != material.getOfferMaterialType() && material.getOfferMaterialType() == OfferMaterialType.BFLUTE)
			{
				serviceFactory.getMaterialService().saveQuick(materialClass.getId(), name, material.getWeight(), thousand.getId(), z.getId(), material.getLastPurchPrice());
			}
		}
		UserUtils.clearCacheBasic(BasicType.MATERIAL);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 跳转到修改材料信息
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:33:34, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("basic:material:edit")
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, I18nResource.VALIDATE_FAIL);
		}
		Material material = serviceFactory.getMaterialService().get(id);
		List<MaterialClass> materialClassList = serviceFactory.getMaterialClassService().findAll();
		List<Unit> unitList = serviceFactory.getUnitService().findAll();
		map.put("materialClassList", materialClassList);
		map.put("unitList", unitList);
		map.put("material", material);
		return "basic/material/edit";
	}

	/**
	 * <pre>
	 * 功能 - 修改材料信息
	 * </pre>
	 * @param material
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:34:57, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("basic:material:edit")
	public AjaxResponseBody update(@RequestBody Material material, ModelMap map)
	{
		Material _material = serviceFactory.getMaterialService().get(material.getId());
		// 更新的时候如果计价单位和库存单位有修改，则需判断是否被单据引用过，引用了则不能修改。
//		if (material.getStockUnitId().longValue() != _material.getStockUnitId().longValue() || material.getValuationUnitId().longValue() != _material.getValuationUnitId().longValue())
//		{
//			if (serviceFactory.getCommonService().isUsed(BasicType.MATERIAL, material.getId()))
//			{
//				return returnErrorBody("<font style='color:red'>计价单位</font>或<font style='color:red'>库存单位</font>不允许修改，原因：已做业务单据");
//			};
//		}
		Material obj = serviceFactory.getMaterialService().getByName(material.getName());
		if (!Validate.validateObjectsNullOrEmpty(obj))
		{
			if (_material.getId().longValue() != obj.getId().longValue())
			{
				return returnErrorBody(I18nResource.VALIDATE_NAME_EXIST);
			}
		}
		_material.setCode(material.getCode());
		_material.setName(material.getName());
		_material.setBrand(material.getBrand());
		_material.setShortName(material.getShortName());
		_material.setMaterialClassId(material.getMaterialClassId());
		_material.setMaterialType(material.getMaterialType());
		_material.setStockUnitId(material.getStockUnitId());
		_material.setValuationUnitId(material.getValuationUnitId());
		_material.setProduceUnitId(material.getProduceUnitId());
		_material.setSaleUnitId(material.getSaleUnitId());
		_material.setPurchPrice(material.getPurchPrice());
		_material.setLastPurchPrice(material.getLastPurchPrice());
		_material.setSalePrice(material.getSalePrice());
		_material.setWeight(material.getWeight());
		_material.setShelfLife(material.getShelfLife());
		_material.setIsPurch(material.getIsPurch());
		_material.setIsSale(material.getIsSale());
		_material.setIsProduce(material.getIsProduce());
		_material.setCalculFormula(material.getCalculFormula());
		_material.setIsValid(material.getIsValid());
		_material.setMinStockNum(material.getMinStockNum());
		_material.setSort(material.getSort());
		_material.setMemo(material.getMemo());
		_material.setUpdateName(UserUtils.getUser().getUserName());
		_material.setUpdateTime(new Date());
		serviceFactory.getMaterialService().update(_material);

		UserUtils.clearCacheBasic(BasicType.MATERIAL);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除材料信息
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:35:35, zhengby
	 */
	@RequestMapping(value = "delete/{id}")
	@ResponseBody
	@RequiresPermissions("basic:material:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "材料信息", Operation = Operation.DELETE)
	public boolean delete(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return false;
		}
		try
		{
			return serviceFactory.getCommonService().delete(BasicType.MATERIAL, id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <pre>
	 * 功能 - 批量删除材料信息
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2017年11月21日 上午9:35:55, zhengby
	 */
	@RequestMapping(value = "batchDelete")
	@ResponseBody
	@RequiresPermissions("basic:material:batchDelete")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "材料信息", Operation = Operation.DELETE)
	public AjaxResponseBody batchDelete(@RequestParam("ids[]") Long[] ids)
	{
		if (ids.length == 0)
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		Material material = new Material();
		for (Long id : ids)
		{
			if (serviceFactory.getCommonService().isUsed(BasicType.MATERIAL, id))
			{
				material = serviceFactory.getMaterialService().get(id);
				return returnErrorBody(ResourceBundleMessageSource.i18nFormatter(BasicI18nResource.MATERIAL_VALIDATE_MSG1, material.getName()));
			}
		}
		serviceFactory.getMaterialService().deleteByIds(ids);
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
	 * @since 1.0, 2018年2月7日 上午11:28:21, zhengby
	 */
	@RequestMapping(value = "updateName/{id}")
	@RequiresPermissions("basic:material:updateName")
	public String updateName(@PathVariable Long id, ModelMap map)
	{
		Material material = serviceFactory.getMaterialService().get(id);
		map.put("id", id);
		map.put("name", material.getName());
		map.put("code", material.getCode());
		return "basic/material/updateName";
	}

	/**
	 * <pre>
	 * 功能 - 全局变更供应商名称
	 * </pre>
	 * @param material
	 * @return
	 * @since 1.0, 2018年2月7日 上午11:29:13, zhengby
	 */
	@RequestMapping(value = "updateName")
	@ResponseBody
	@RequiresPermissions("basic:material:updateName")
	@SystemControllerLog(SystemLogType = SystemLogType.BASIC, BillTypeText = "材料信息", Operation = Operation.UPDATE)
	public AjaxResponseBody updateName(@RequestBody Material material)
	{
		if (serviceFactory.getCommonService().isExist(material.getName(), UpdateNameType.MATERIAL))
		{
			return returnErrorBody("已存在该名称");
		}
		else
		{
			serviceFactory.getCommonService().updateBasicName(material.getId(), material.getName(), UpdateNameType.MATERIAL);
		}
		return returnSuccessBody();
	}
}
