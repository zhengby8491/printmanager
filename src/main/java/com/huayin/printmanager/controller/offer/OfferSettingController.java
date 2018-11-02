/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月17日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.offer;

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
import com.huayin.printmanager.domain.annotation.Logical;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.persist.entity.offer.OfferBflute;
import com.huayin.printmanager.persist.entity.offer.OfferMachine;
import com.huayin.printmanager.persist.entity.offer.OfferPaper;
import com.huayin.printmanager.persist.entity.offer.OfferPrePrint;
import com.huayin.printmanager.persist.entity.offer.OfferProcedure;
import com.huayin.printmanager.persist.entity.offer.OfferProfit;
import com.huayin.printmanager.persist.entity.offer.OfferWaste;
import com.huayin.printmanager.persist.enumerate.OfferProfitType;
import com.huayin.printmanager.persist.enumerate.OfferSettingType;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 报价系统 - 报价设置
 * </pre>
 * @author think
 * @since 1.0, 2017年10月17日
 */
@Controller
@RequestMapping(value = "${basePath}/offer/setting")
public class OfferSettingController extends BaseController
{
	// ==================== 报价设置 - 损耗设置 ====================
	/**
	 * <pre>
	 * 页面 - 损耗设置
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月16日 上午10:14:48, think
	 */
	@RequestMapping(value = "waste")
	@RequiresPermissions("offer:setting:waste")
	public String waste(ModelMap map)
	{
		List<OfferWaste> settingWasteList = serviceFactory.getOfferSettingService().getWaste();
		map.put("settingWasteList", settingWasteList);// 注意：wasteSettingList一定会有值，否则是系统出现问题
		return "offer/setting/waste";
	}

	/**
	 * <pre>
	 * 功能 - 保存损耗设置
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月16日 下午5:30:06, think
	 */
	@RequestMapping(value = "saveWaste")
	@ResponseBody
	@RequiresPermissions("offer:waste:save")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "损耗设置", Operation = Operation.UPDATE)
	public AjaxResponseBody saveWaste(@RequestBody List<OfferWaste> wasteSettingList)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */

		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(wasteSettingList))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		for (OfferWaste wasteSetting : wasteSettingList)
		{
			if (wasteSetting.getId() == null)
			{
				logger.error("更新损耗设置失败! Id为null[" + wasteSetting.getOfferType());
				return returnErrorBody("数据未通过验证");
			}
			wasteSetting.setCompanyId(UserUtils.getCompanyId());
		}
		// 2. 保存数据
		serviceFactory.getOfferSettingService().saveWaste(wasteSettingList);
		return returnSuccessBody();
	}

	// ==================== 报价设置 - 机台 ====================
	
	/**
	 * <pre>
	 * 菜单 - 统一跳转入口
	 * </pre>
	 * @param map
	 * @param type
	 * @return
	 * @since 1.0, 2017年12月8日 下午6:13:11, think
	 */
	@RequestMapping(value = "forword")
	public String forword(ModelMap map, @RequestParam("type") OfferType type)
	{
		/**
		 * 机台设置 offer:book:machine
		 * 印前费用 offer:book:preprint
		 * 利润设置 offer:book:profit
		 * 纸张设置 offer:book:paper
		 * 坑纸设置 offer:carton:bflute
		 * 工序设置 offer:book:procedure
		 */
		for(OfferSettingType settingType : OfferSettingType.values())
		{
			// 动态权限，如offer:single:machine
			String permission = "offer:" + type.getMapping().toLowerCase() + ":" + settingType.getMapping().toLowerCase();
			if(UserUtils.hasPermission(permission))
			{
				return "redirect:" + basePath + "/offer/setting/"+settingType.getMapping()+"?type=" + type;
			}
		}
		
		return null;
	}

	/**
	 * <pre>
	 * 页面 - 机台设置
	 * </pre>
	 * @param map
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月17日 下午2:30:06, think
	 */
	@RequestMapping(value = "machine")
	@RequiresPermissions(value = { "offer:single:machine", "offer:book:machine", "offer:carton:machine", "offer:note:machine", "offer:card:machine", "offer:sheath:machine", "offer:glue:machine", "offer:bill:machine", "offer:envelope:machine", "offer:papercup:machine" }, logical = Logical.OR)
	public String machine(ModelMap map, @RequestParam("type") OfferType type)
	{
		_common(map, type, OfferSettingType.MACHINE);
		return "offer/setting/machine";
	}

	/**
	 * <pre>
	 * 列表 - 机台设置
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月20日 下午5:14:16, think
	 */
	@RequestMapping(value = "ajaxMachineList")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:machine", "offer:book:machine", "offer:carton:machine", "offer:note:machine", "offer:card:machine", "offer:sheath:machine", "offer:glue:machine", "offer:bill:machine", "offer:envelope:machine", "offer:papercup:machine" }, logical = Logical.OR)
	public SearchResult<OfferMachine> ajaxMachineList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OfferMachine> result = serviceFactory.getOfferSettingService().findMachineByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 机台新增
	 * </pre>
	 * @param map
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月17日 下午5:39:17, think
	 */
	@RequestMapping(value = "machineCreate")
	@RequiresPermissions(value = { "offer:single:machine:create", "offer:book:machine:create", "offer:carton:machine:create", "offer:note:machine:create", "offer:card:machine:create", "offer:sheath:machine:create", "offer:glue:machine:create", "offer:bill:machine:create", "offer:envelope:machine:create", "offer:papercup:machine:create" }, logical = Logical.OR)
	public String machineCreate(ModelMap map, @RequestParam("type") OfferType type)
	{
		_common(map, type, OfferSettingType.MACHINE);
		return "offer/setting/machineCreate";
	}

	/**
	 * <pre>
	 * 功能 - 机台新增
	 * </pre>
	 * @param map
	 * @param offerMachine
	 * @return
	 * @since 1.0, 2017年10月20日 下午3:52:11, think
	 */
	@RequestMapping(value = "saveMachine")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:machine:create", "offer:book:machine:create", "offer:carton:machine:create", "offer:note:machine:create", "offer:card:machine:create", "offer:sheath:machine:create", "offer:glue:machine:create", "offer:bill:machine:create", "offer:envelope:machine:create", "offer:papercup:machine:create" }, logical = Logical.OR)
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "创建机台", Operation = Operation.ADD)
	public AjaxResponseBody saveMachine(ModelMap map, @RequestBody OfferMachine offerMachine)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */

		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(offerMachine, offerMachine.getOfferType(), offerMachine.getName()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 机台名称不能重复
		OfferMachine oldOfferMachine = serviceFactory.getOfferSettingService().findMachineByName(offerMachine.getOfferType(), offerMachine.getName());
		if (!Validate.validateObjectsNullOrEmpty(oldOfferMachine))
		{
			return returnErrorBody("名称已经存在");
		}
		// 2. 保存数据
		OfferMachine ret = serviceFactory.getOfferSettingService().saveMachine(offerMachine);
		return returnSuccessBody(ret);
	}

	/***
	 * <pre>
	 * 页面 - 机台修改
	 * </pre>
	 * @param map
	 * @param type
	 * @return
	 * @since 1.0,2017年10月17日 下午2:30:06, think
	 */
	@RequestMapping(value = "machineEdit/{id}")
	@RequiresPermissions(value = { "offer:single:machine:edit", "offer:book:machine:edit", "offer:carton:machine:edit", "offer:note:machine:edit", "offer:card:machine:edit", "offer:sheath:machine:edit", "offer:glue:machine:edit", "offer:bill:machine:edit", "offer:envelope:machine:edit", "offer:papercup:machine:edit" }, logical = Logical.OR)
	public String machineEdit(ModelMap map, @RequestParam("type") OfferType type, @PathVariable Long id)
	{
		_common(map, type, OfferSettingType.MACHINE);
		OfferMachine machine = serviceFactory.getOfferSettingService().findMachine(id);
		map.put("machine", machine);
		return "offer/setting/machineEdit";
	}

	/**
	 * <pre>
	 * 功能 - 机台修改
	 * </pre>
	 * @param map
	 * @param offerMachine
	 * @return
	 * @since 1.0, 2017年10月23日 下午3:00:42, think
	 */
	@RequestMapping(value = "updateMachine")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:machine:edit", "offer:book:machine:edit", "offer:carton:machine:edit", "offer:note:machine:edit", "offer:card:machine:edit", "offer:sheath:machine:edit", "offer:glue:machine:edit", "offer:bill:machine:edit", "offer:envelope:machine:edit", "offer:papercup:machine:edit" }, logical = Logical.OR)
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "修改机台", Operation = Operation.UPDATE)
	public AjaxResponseBody updateMachine(ModelMap map, @RequestBody OfferMachine offerMachine)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */

		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(offerMachine, offerMachine.getId(), offerMachine.getOfferType(), offerMachine.getName()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 机台名称不能重复
		OfferMachine oldOfferMachine1 = serviceFactory.getOfferSettingService().getMachine(offerMachine.getId());
		OfferMachine oldOfferMachine2 = serviceFactory.getOfferSettingService().findMachineByName(offerMachine.getOfferType(), offerMachine.getName());
		if (!Validate.validateObjectsNullOrEmpty(oldOfferMachine1, oldOfferMachine2))
		{
			if (oldOfferMachine1.getId().longValue() != oldOfferMachine2.getId().longValue())
			{
				return returnErrorBody("名称已经存在");
			}
		}
		// 2. 保存数据
		OfferMachine ret = serviceFactory.getOfferSettingService().updateMachine(offerMachine);
		return returnSuccessBody(ret);
	}

	/**
	 * <pre>
	 * 功能 - 删除机台
	 * </pre>
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月23日 下午5:05:08, think
	 */
	@RequestMapping(value = "deleteMachine/{id}")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:machine:del", "offer:book:machine:del", "offer:carton:machine:del", "offer:note:machine:del", "offer:card:machine:del", "offer:sheath:machine:del", "offer:glue:machine:del", "offer:bill:machine:del", "offer:envelope:machine:del", "offer:papercup:machine:del" }, logical = Logical.OR)
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "删除机台", Operation = Operation.DELETE)
	public AjaxResponseBody deleteMachine(ModelMap map, @PathVariable Long id)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */

		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2. 保存数据
		serviceFactory.getOfferSettingService().deleteMachine(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 机台查看
	 * </pre>
	 * @param map
	 * @param type
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月23日 上午11:23:50, think
	 */
	@RequestMapping(value = "machineView/{id}")
	@RequiresPermissions(value = { "offer:single:machine", "offer:book:machine", "offer:carton:machine", "offer:note:machine", "offer:card:machine", "offer:sheath:machine", "offer:glue:machine", "offer:bill:machine", "offer:envelope:machine", "offer:papercup:machine" }, logical = Logical.OR)
	public String machineView(ModelMap map, @RequestParam("type") OfferType type, @PathVariable Long id)
	{
		_common(map, type, OfferSettingType.MACHINE);
		OfferMachine machine = serviceFactory.getOfferSettingService().findMachine(id);
		map.put("machine", machine);
		return "offer/setting/machineView";
	}

	// ==================== 报价设置 - 印前费用 ====================

	/***
	 * <pre>
	 * 页面 - 印前费用
	 * </pre>
	 * @param map
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月17日 下午2:30:06, zhengby
	 */
	@RequestMapping(value = "prePrint")
	@RequiresPermissions(value = { "offer:single:preprint", "offer:book:preprint", "offer:carton:preprint", "offer:note:preprint", "offer:card:preprint", "offer:sheath:preprint", "offer:glue:preprint", "offer:bill:preprint", "offer:envelope:preprint", "offer:papercup:preprint" }, logical = Logical.OR)
	public String prePrint(ModelMap map, @RequestParam("type") OfferType type)
	{
		OfferPrePrint offerPrePrint = serviceFactory.getOfferSettingService().getPrePrint(type);
		map.put("offerPrePrint", offerPrePrint);
		_common(map, type, OfferSettingType.PRE_PRINT);
		return "offer/setting/prePrint";
	}

	/**
	 * <pre>
	 * 功能-保存更新印前费用
	 * </pre>
	 * @param offerPrePrint
	 * @return
	 * @since 1.0, 2017年10月19日 下午2:05:39, zhengby
	 */
	@RequestMapping(value = "savePrePrint")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:preprint:save", "offer:book:preprint:save", "offer:carton:preprint:save", "offer:note:preprint:save", "offer:card:preprint:save", "offer:sheath:preprint:save", "offer:glue:preprint:save", "offer:bill:preprint:save", "offer:envelope:preprint:save", "offer:papercup:preprint:save" }, logical = Logical.OR)
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "印前费用", Operation = Operation.ADD)
	public AjaxResponseBody savePrePrint(@RequestBody OfferPrePrint offerPrePrint)
	{
		/**
		 * 1. 验证数据
		 * 2. 保存数据
		 */
		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(offerPrePrint))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2.保存数据
		OfferPrePrint reObj = serviceFactory.getOfferSettingService().savePrePrint(offerPrePrint);
		return returnSuccessBody(reObj);

	}

	// ==================== 报价设置 - 利润设置 ====================

	/**
	 * <pre>
	 * 页面 - 利润设置
	 * </pre>
	 * @param map
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月17日 下午2:30:06, think
	 */
	@RequestMapping(value = "profit")
	@RequiresPermissions(value = { "offer:single:profit", "offer:book:profit", "offer:carton:profit", "offer:note:profit", "offer:card:profit", "offer:sheath:profit", "offer:glue:profit", "offer:bill:profit", "offer:envelope:profit", "offer:papercup:profit" }, logical = Logical.OR)
	public String profit(ModelMap map, @RequestParam("type") OfferType type)
	{
		_common(map, type, OfferSettingType.PROFIT);
		List<OfferProfit> settingProfitList = serviceFactory.getOfferSettingService().getProfit(type);
		map.put("settingProfitList", settingProfitList);

		// offerProfitType
		if (settingProfitList.size() > 0)
		{
			OfferProfitType offerProfitType = settingProfitList.get(0).getOfferProfitType();
			map.put("offerProfitType", offerProfitType);
		}

		return "offer/setting/profit";
	}

	/**
	 * <pre>
	 * 功能 - 保存利润设置
	 * </pre>
	 * @param profitSettingList
	 * @return
	 * @since 1.0, 2017年10月26日 下午2:46:41, think
	 */
	@RequestMapping(value = "saveProfit")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:profit:save", "offer:book:profit:save", "offer:carton:profit:save", "offer:note:profit:save", "offer:card:profit:save", "offer:sheath:profit:save", "offer:glue:profit:save", "offer:bill:profit:save", "offer:envelope:profit:save", "offer:papercup:profit:save" }, logical = Logical.OR)
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "利润设置", Operation = Operation.UPDATE)
	public AjaxResponseBody saveProfit(@RequestBody List<OfferProfit> profitSettingList)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */

		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(profitSettingList))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}

		// 2. 保存数据
		serviceFactory.getOfferSettingService().saveProfit(profitSettingList);
		return returnSuccessBody();
	}

	// ==================== 报价设置 - 纸张设置 ====================

	/**
	 * <pre>
	 * 页面 - 纸张设置
	 * </pre>
	 * @param map
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月17日 下午2:30:06, zhengby
	 */
	@RequestMapping(value = "paper")
	@RequiresPermissions(value = { "offer:single:paper", "offer:book:paper", "offer:carton:paper", "offer:note:paper", "offer:card:paper", "offer:sheath:paper", "offer:glue:paper", "offer:bill:paper", "offer:envelope:paper", "offer:papercup:paper" }, logical = Logical.OR)
	public String paper(ModelMap map, @RequestParam("type") OfferType type)
	{
		_common(map, type, OfferSettingType.PAPER);
		return "offer/setting/paper";
	}

	/**
	 * 
	 * <pre>
	 * 功能 - 获取纸张设置的列表信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月31日 下午4:47:54, zhengby
	 */
	@RequestMapping(value = "ajaxPaperList")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:paper", "offer:book:paper", "offer:carton:paper", "offer:note:paper", "offer:card:paper", "offer:sheath:paper", "offer:glue:paper", "offer:bill:paper", "offer:envelope:paper", "offer:papercup:paper" }, logical = Logical.OR)
	public SearchResult<OfferPaper> ajaxPaperList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OfferPaper> offerPaperList = serviceFactory.getOfferSettingService().getPaperList(queryParam);
		return offerPaperList;
	}

	/**
	 * <pre>
	 * 页面 - 纸张新增
	 * </pre>
	 * @param map
	 * @param isBatch
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月17日 下午2:30:06, zhengby
	 */
	@RequestMapping(value = "paperCreate/{isBatch}")
	@RequiresPermissions(value = { "offer:single:paper:create", "offer:book:paper:create", "offer:carton:paper:create", "offer:note:paper:create", "offer:card:paper:create", "offer:sheath:paper:create", "offer:glue:paper:create", "offer:bill:paper:create", "offer:envelope:paper:create", "offer:papercup:paper:create" }, logical = Logical.OR)
	public String paperCreate(ModelMap map, @PathVariable Boolean isBatch, @RequestParam("type") OfferType type)
	{
		_common(map, type, OfferSettingType.PAPER);
		map.put("isBatch", isBatch);
		return "offer/setting/paperCreate";
	}

	/**
	 * <pre>
	 * 页面 - 纸张编辑
	 * </pre>
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月17日 下午2:30:06, zhengby
	 */
	@RequestMapping(value = "paperEdit/{id}")
	@RequiresPermissions(value = { "offer:single:paper:edit", "offer:book:paper:edit", "offer:carton:paper:edit", "offer:note:paper:edit", "offer:card:paper:edit", "offer:sheath:paper:edit", "offer:glue:paper:edit", "offer:bill:paper:edit", "offer:envelope:paper:edit", "offer:papercup:paper:edit" }, logical = Logical.OR)
	public String paperEdit(ModelMap map, @PathVariable Long id)
	{
		OfferPaper offerPaperMaterial = serviceFactory.getOfferSettingService().getPaper(id);
		map.put("paperMaterial", offerPaperMaterial);
		return "offer/setting/paperEdit";
	}

	/**
	 * 
	 * <pre>
	 * 功能 - 保存手动添加纸张材料
	 * </pre>
	 * @param offerPaper
	 * @return
	 * @since 1.0, 2017年10月31日 下午4:46:06, zhengby
	 */
	@RequestMapping(value = "savePaper")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:paper:create", "offer:book:paper:create", "offer:carton:paper:create", "offer:note:paper:create", "offer:card:paper:create", "offer:sheath:paper:create", "offer:glue:paper:create", "offer:bill:paper:create", "offer:envelope:paper:create", "offer:papercup:paper:create" }, logical = Logical.OR)
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "纸张设置单个保存", Operation = Operation.ADD)
	public AjaxResponseBody savePaper(@RequestBody OfferPaper offerPaper)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */
		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(offerPaper))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2.保存数据
		serviceFactory.getOfferSettingService().savePaper(offerPaper);
		return returnSuccessBody();
	}

	/**
	 * 
	 * <pre>
	 * 功能 - 保存手动批量添加纸张材料
	 * </pre>
	 * @param offerPaper
	 * @return
	 * @since 1.0, 2017年10月31日 下午4:46:40, zhengby
	 */
	@RequestMapping(value = "savePaperByBatch")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:paper:create", "offer:book:paper:create", "offer:carton:paper:create", "offer:note:paper:create", "offer:card:paper:create", "offer:sheath:paper:create", "offer:glue:paper:create", "offer:bill:paper:create", "offer:envelope:paper:create", "offer:papercup:paper:create" }, logical = Logical.OR)
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "纸张设置批量保存", Operation = Operation.ADD)
	public AjaxResponseBody savePaperByBatch(@RequestBody List<OfferPaper> offerPaperList)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */
		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(offerPaperList))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2.保存数据
		serviceFactory.getOfferSettingService().savePaperByBatch(offerPaperList);
		return returnSuccessBody();
	}

	/**
	 * 
	 * <pre>
	 * 功能 - 保存从材料选择列表添加纸张材料
	 * </pre>
	 * @param list
	 * @return
	 * @since 1.0, 2017年10月31日 下午4:47:05, zhengby
	 */
	@RequestMapping(value = "savePaperByMaterial")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:paper:create", "offer:book:paper:create", "offer:carton:paper:create", "offer:note:paper:create", "offer:card:paper:create", "offer:sheath:paper:create", "offer:glue:paper:create", "offer:bill:paper:create", "offer:envelope:paper:create", "offer:papercup:paper:create" }, logical = Logical.OR)
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "纸张设置列表选择材料保存", Operation = Operation.ADD)
	public AjaxResponseBody savePaperByMaterial(@RequestBody List<OfferPaper> list)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */
		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(list))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2.保存数据
		serviceFactory.getOfferSettingService().savePaperByMaterial(list);
		return returnSuccessBody();
	}

	/**
	 * 
	 * <pre>
	 * 功能 - 更新纸张设置信息
	 * </pre>
	 * @param offerPaperMaterial
	 * @return
	 * @since 1.0, 2017年10月31日 下午4:52:50, zhengby
	 */
	@RequestMapping(value = "updatePaper")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:paper:edit", "offer:book:paper:edit", "offer:carton:paper:edit", "offer:note:paper:edit", "offer:card:paper:edit", "offer:sheath:paper:edit", "offer:glue:paper:edit", "offer:bill:paper:edit", "offer:envelope:paper:edit", "offer:papercup:paper:edit" }, logical = Logical.OR)
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "纸张设置更新", Operation = Operation.UPDATE)
	public AjaxResponseBody updatePaper(@RequestBody OfferPaper offerPaper)
	{
		/**
		 * 1. 验证数据
		 * 2. 保存数据
		 */
		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(offerPaper))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2.保存数据
		serviceFactory.getOfferSettingService().updatePaper(offerPaper);
		return returnSuccessBody();
	}

	/**
	 * 
	 * <pre>
	 * 功能 - 删除纸张设置
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月31日 下午5:24:02, zhengby
	 */
	@RequestMapping(value = "deletePaper/{id}")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:paper:del", "offer:book:paper:del", "offer:carton:paper:del", "offer:note:paper:del", "offer:card:paper:del", "offer:sheath:paper:del", "offer:glue:paper:del", "offer:bill:paper:del", "offer:envelope:paper:del", "offer:papercup:paper:del" }, logical = Logical.OR)
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "纸张设置删除", Operation = Operation.DELETE)
	public AjaxResponseBody deletePaper(@PathVariable Long id)
	{
		serviceFactory.getOfferSettingService().deletePaper(id);
		return returnSuccessBody();

	}

	/**
	 * 
	 * <pre>
	 * 功能 - 批量删除纸张设置
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2017年10月31日 下午5:47:41, zhengby
	 */
	@RequestMapping(value = "deletePaperByBatch")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:paper:delbatch", "offer:book:paper:delbatch", "offer:carton:paper:delbatch", "offer:note:paper:delbatch", "offer:card:paper:delbatch", "offer:sheath:paper:delbatch", "offer:glue:paper:delbatch", "offer:bill:paper:delbatch", "offer:envelope:paper:delbatch", "offer:papercup:paper:delbatch" }, logical = Logical.OR)
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "纸张设置批量删除", Operation = Operation.DELETE)
	public AjaxResponseBody deletePaperByBatch(@RequestParam(required = false, name = "ids[]") Long[] ids)
	{
		serviceFactory.getOfferSettingService().deletePaperByBatch(ids);
		return returnSuccessBody();
	}

	// ==================== 报价设置 - 坑纸设置 ====================

	/**
	 * <pre>
	 * 页面 - 坑纸设置
	 * </pre>
	 * @param map
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月17日 下午2:30:06, zhengby
	 */
	@RequestMapping(value = "bflute")
	@RequiresPermissions("offer:carton:bflute")
	public String bflute(ModelMap map, @RequestParam("type") OfferType type)
	{
		_common(map, type, OfferSettingType.BFLUTE);
		return "offer/setting/bflute";
	}

	/**
	 * 
	 * <pre>
	 * 功能 - 获取坑纸设置列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年11月1日 上午11:22:13, zhengby
	 */
	@RequestMapping(value = "ajaxBfluteList")
	@ResponseBody
	@RequiresPermissions("offer:carton:bflute")
	public SearchResult<OfferBflute> ajaxBfluteList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OfferBflute> bfluteList = serviceFactory.getOfferSettingService().getBfluteList(queryParam);
		return bfluteList;
	}

	/**
	 * <pre>
	 * 页面 - 坑纸新增
	 * </pre>
	 * @param map
	 * @param isBatch
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月17日 下午2:30:06, zhengby
	 */
	@RequestMapping(value = "bfluteCreate/{isBatch}")
	@RequiresPermissions("offer:carton:bflute")
	public String bfluteCreate(ModelMap map, @PathVariable Boolean isBatch, @RequestParam("type") OfferType type)
	{
		_common(map, type, OfferSettingType.BFLUTE);
		map.put("isBatch", isBatch);
		return "offer/setting/bfluteCreate";
	}

	/**
	 * 
	 * <pre>
	 * 功能 - 单条保存手动添加坑纸设置数据
	 * </pre>
	 * @param offerBflute
	 * @return
	 * @since 1.0, 2017年11月1日 上午11:32:53, zhengby
	 */
	@RequestMapping(value = "saveBflute")
	@ResponseBody
	@RequiresPermissions("offer:carton:bflute:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "坑纸设置批量删除", Operation = Operation.DELETE)
	public AjaxResponseBody saveBflute(@RequestBody OfferBflute offerBflute)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */

		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(offerBflute))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2.保存数据
		serviceFactory.getOfferSettingService().saveBflute(offerBflute);
		return returnSuccessBody();
	}

	/**
	 * 
	 * <pre>
	 * 功能 - 批量保存手动添加的坑纸设置
	 * </pre>
	 * @param offerBflute
	 * @return
	 * @since 1.0, 2017年11月1日 上午11:32:07, zhengby
	 */
	@RequestMapping(value = "saveBfluteByBatch")
	@ResponseBody
	@RequiresPermissions("offer:carton:bflute:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "坑纸设置批量保存", Operation = Operation.ADD)
	public AjaxResponseBody saveBfluteByBatch(@RequestBody List<OfferBflute> offerBfluteList)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */
		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(offerBfluteList))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2.保存数据
		serviceFactory.getOfferSettingService().saveBfluteByBatch(offerBfluteList);
		return returnSuccessBody();
	}

	/**
	 * 
	 * <pre>
	 * 功能 - 保存从材料选择列表选择的坑纸设置
	 * </pre>
	 * @param list
	 * @return
	 * @since 1.0, 2017年11月1日 上午11:31:23, zhengby
	 */
	@RequestMapping(value = "saveBfluteByMaterial")
	@ResponseBody
	@RequiresPermissions("offer:carton:bflute:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "坑纸设置批量保存", Operation = Operation.ADD)
	public AjaxResponseBody saveBfluteByMaterial(@RequestBody List<OfferBflute> list)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */

		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(list))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2.保存数据
		serviceFactory.getOfferSettingService().saveBfluteByMaterial(list);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 坑纸编辑
	 * </pre>
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0,2017年10月17日 下午2:30:06, zhengby
	 */
	@RequestMapping(value = "bfluteEdit/{id}")
	@RequiresPermissions("offer:carton:bflute:edit")
	public String bfluteEdit(ModelMap map, @PathVariable Long id)
	{
		OfferBflute bflute = serviceFactory.getOfferSettingService().getBflute(id);
		map.put("bflute", bflute);
		return "offer/setting/bfluteEdit";
	}

	/**
	 * 
	 * <pre>
	 * 功能 - 更新坑纸设置
	 * </pre>
	 * @param offerBflute
	 * @return
	 * @since 1.0, 2017年11月1日 下午6:26:39, zhengby
	 */
	@RequestMapping(value = "updateBflute")
	@ResponseBody
	@RequiresPermissions("offer:carton:bflute:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "坑纸设置更新", Operation = Operation.UPDATE)
	public AjaxResponseBody updateBflute(@RequestBody OfferBflute offerBflute)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */

		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(offerBflute))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2.保存数据
		serviceFactory.getOfferSettingService().updateBflute(offerBflute);
		return returnSuccessBody();

	}

	/**
	 * <pre>
	 * 页面 - 单条删除坑纸设置
	 * </pre>
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月17日 下午2:30:06, zhengby
	 */
	@RequestMapping(value = "deleteBflute/{id}")
	@ResponseBody
	@RequiresPermissions("offer:carton:bflute:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "坑纸设置删除", Operation = Operation.DELETE)
	public AjaxResponseBody deleteBflute(@PathVariable Long id)
	{
		/**
		 * 1. 验证数据 
		 * 2. 删除数据
		 */

		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2. 删除数据
		serviceFactory.getOfferSettingService().deleteBflute(id);
		return returnSuccessBody();

	}

	/**
	 * 
	 * <pre>
	 * 功能 - 批量删除坑纸设置
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2017年11月1日 上午11:38:02, zhengby
	 */
	@RequestMapping(value = "deleteBfluteByBatch")
	@ResponseBody
	@RequiresPermissions("offer:carton:bflute:delbatch")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "批量删除坑纸设置", Operation = Operation.DELETE)
	public AjaxResponseBody deleteBfluteByBatch(@RequestParam(required = false, name = "ids[]") Long[] ids)
	{
		/**
		 * 1. 验证数据 
		 * 2. 删除数据
		 */

		// 1. 验证数据
		if (null == ids)
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2. 删除数据
		serviceFactory.getOfferSettingService().deleteBfluteByBatch(ids);
		return returnSuccessBody();
	}

	// ==================== 报价设置 - 工序设置 ====================

	/**
	 * <pre>
	 * 页面 - 工序设置
	 * </pre>
	 * @param map
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月17日 下午2:30:06, think
	 */
	@RequestMapping(value = "procedure")
	@RequiresPermissions(value = { "offer:single:procedure", "offer:book:procedure", "offer:carton:procedure", "offer:note:procedure", "offer:card:procedure", "offer:sheath:procedure", "offer:glue:procedure", "offer:bill:procedure", "offer:envelope:procedure", "offer:papercup:procedure" }, logical = Logical.OR)
	public String procedure(ModelMap map, @RequestParam("type") OfferType type)
	{
		_common(map, type, OfferSettingType.PROCEDURE);
		List<OfferProcedure> list = serviceFactory.getOfferSettingService().getProcedure(type);
		map.put("list", list);
		return "offer/setting/procedure";
	}

	/**
	 * <pre>
	 * 列表 - 工序设置
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月30日 上午10:23:47, think
	 */
	@RequestMapping(value = "ajaxProcedureList")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:procedure", "offer:book:procedure", "offer:carton:procedure", "offer:note:procedure", "offer:card:procedure", "offer:sheath:procedure", "offer:glue:procedure", "offer:bill:procedure", "offer:envelope:procedure", "offer:papercup:procedure" }, logical = Logical.OR)
	public SearchResult<OfferProcedure> ajaxProcedureList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OfferProcedure> result = serviceFactory.getOfferSettingService().findProcedureByCondition(queryParam);

		return result;
	}

	/**
	 * <pre>
	 * 功能 - 工序保存
	 * </pre>
	 * @param map
	 * @param procedureList
	 * @return
	 * @since 1.0, 2017年10月30日 下午2:21:14, think
	 */
	@RequestMapping(value = "saveProcedure")
	@ResponseBody
	@RequiresPermissions(value = { "offer:single:procedure:save", "offer:book:procedure:save", "offer:carton:procedure:save", "offer:note:procedure:save", "offer:card:procedure:save", "offer:sheath:procedure:save", "offer:glue:procedure:save", "offer:bill:procedure:save", "offer:envelope:procedure:save", "offer:papercup:procedure:save" }, logical = Logical.OR)
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "保存机台", Operation = Operation.UPDATE)
	public AjaxResponseBody saveProcedure(@RequestBody List<OfferProcedure> procedureList)
	{
		/**
		 * 1. 验证数据
		 * 2. 保存数据
		 */

		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(procedureList))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2. 保存数据
		serviceFactory.getOfferSettingService().saveProcedure(procedureList);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 工序自定义公式	
	 * </pre>
	 * @param map
	 * @param type
	 * @return
	 * @since 1.0, 2017年10月17日 下午2:30:06, zhengby
	 */
	@RequestMapping(value = "procedureFormula")
	@RequiresPermissions(value = { "offer:single:procedure:save", "offer:book:procedure:save", "offer:carton:procedure:save", "offer:note:procedure:save", "offer:card:procedure:save", "offer:sheath:procedure:save", "offer:glue:procedure:save", "offer:bill:procedure:save", "offer:envelope:procedure:save", "offer:papercup:procedure:save" }, logical = Logical.OR)
	public String procedureFormula(ModelMap map, @RequestParam("type") OfferType type)
	{
		return "offer/setting/procedureFormula";
	}

	// ==================== 报价设置 - 公共 ====================

	/**
	 * <pre>
	 * 报价设置 - 公共代码
	 * </pre>
	 * @param map
	 * @param type
	 * @param settingType
	 * @since 1.0, 2017年10月19日 上午9:16:24, think
	 */
	private void _common(ModelMap map, OfferType type, OfferSettingType settingType)
	{
		map.put("offerType", type);
		map.put("offerSettingType", settingType);
	}
}
