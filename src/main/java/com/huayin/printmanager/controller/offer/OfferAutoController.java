/**
 * <pre>
 * Author:		THINK
 * Create:	 	2017年11月3日 上午9:30:23
 * Copyright: 	Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.offer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.persist.entity.offer.OfferBflute;
import com.huayin.printmanager.persist.entity.offer.OfferOrder;
import com.huayin.printmanager.persist.entity.offer.OfferPaper;
import com.huayin.printmanager.persist.entity.offer.OfferProcedure;
import com.huayin.printmanager.persist.enumerate.BoxFormula;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.PaperType;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 报价系统 - 自动报价
 * </pre>
 * @author zhengby
 * @version 1.0, 2017年11月3日 上午9:30:23
 */
@Controller
@RequestMapping(value = "${basePath}/offer/auto")
public class OfferAutoController extends BaseController
{
	// ==================== 自动报价 - 单张报价 ====================

	/**
	 * <pre>
	 * 页面 - 单张类
	 * </pre>
	 * @param map
	 * @param type
	 * @return
	 * @since 1.0, 2017年11月3日 上午9:48:00, think
	 */
	@RequestMapping(value = "single")
	@RequiresPermissions("offer:auto:single")
	public String single(ModelMap map)
	{
		_common(map, OfferType.SINGLE);
		return "offer/auto/single";
	}
	// ==================== 自动报价 - 书刊报价 ====================

	/**
	 * 
	 * <pre>
	 * 页面 - 书刊
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月3日 下午5:36:31, zhengby
	 */
	@RequestMapping(value = "book")
	@RequiresPermissions("offer:auto:book")
	public String book(ModelMap map)
	{
		_common(map, OfferType.ALBUMBOOK);
		return "offer/auto/book";
	}

	// ==================== 自动报价 - 彩盒报价 ====================

	/**
	 * <pre>
	 * 页面 - 彩盒纸箱
	 * </pre>
	 * @param map
	 * @param type
	 * @return
	 * @since 1.0, 2017年11月3日 上午9:33:03, zhengby
	 */
	@RequestMapping(value = "carton")
	@RequiresPermissions("offer:auto:carton")
	public String carton(ModelMap map)
	{
		_common(map, OfferType.CARTONBOX);
		// 默认正度纸
		map.put("paperType", PaperType.ARE_DEGREES_PAPER);
		return "offer/auto/carton";
	}

	// ==================== 自动报价- 便签信纸 ====================

	/**
	 * 
	 * <pre>
	 * 页面 - 便签信纸
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月3日 下午5:39:04, zhengby
	 */
	@RequestMapping(value = "note")
	@RequiresPermissions("offer:auto:note")
	public String note(ModelMap map)
	{
		_common(map, OfferType.NOTESLETTERFORM);
		return "offer/auto/note";
	}

	// ==================== 自动报价 - 吊牌卡片 ======================
	/**
	 * 
	 * <pre>
	 * 頁面 - 吊牌卡片
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月3日 下午5:40:26, zhengby
	 */
	@RequestMapping(value = "card")
	@RequiresPermissions("offer:auto:card")
	public String card(ModelMap map)
	{
		_common(map, OfferType.TAGCARD);
		return "offer/auto/card";
	}

	// ==================== 自动报价 - 封套类 ======================
	/**
	 * 
	 * <pre>
	 * 頁面 - 封套类
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月3日 下午5:40:26, zhengby
	 */
	@RequestMapping(value = "sheath")
	@RequiresPermissions("offer:auto:sheath")
	public String sheath(ModelMap map)
	{
		_common(map, OfferType.ENVELOPETYPE);
		return "offer/auto/sheath";
	}

	// ==================== 自动报价 - 不干胶 ======================
	/**
	 * 
	 * <pre>
	 * 頁面 - 不干胶
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月3日 下午5:42:21, zhengby
	 */
	@RequestMapping(value = "glue")
	@RequiresPermissions("offer:auto:glue")
	public String glue(ModelMap map)
	{
		_common(map, OfferType.PRESSURESENSITIVEADHSIVE);
		return "offer/auto/glue";
	}

	// ==================== 自动报价 - 联单类 ======================

	/**
	 * <pre>
	 * 页面 - 联单类
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月3日 下午5:45:43, zhengby
	 */
	@RequestMapping(value = "bill")
	@RequiresPermissions("offer:auto:bill")
	public String bill(ModelMap map)
	{
		_common(map, OfferType.ASSOCIATEDSINGLECLASS);
		return "offer/auto/bill";
	}

	// ==================== 自动报价 - 信封类 ======================

	/**
	 * <pre>
	 * 页面 - 信封类
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月3日 下午5:45:43, zhengby
	 */
	@RequestMapping(value = "envelope")
	@RequiresPermissions("offer:auto:envelope")
	public String envelope(ModelMap map)
	{
		_common(map, OfferType.MAILERTYPE);
		return "offer/auto/envelope";
	}

	// ==================== 自动报价 - 纸杯类 ======================

	/**
	 * <pre>
	 * 页面 - 信封类
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年11月3日 下午5:45:43, zhengby
	 */
	@RequestMapping(value = "paperCup")
	@RequiresPermissions("offer:auto:papercup")
	public String paperCup(ModelMap map)
	{
		_common(map, OfferType.CUP);
		return "offer/auto/paperCup";
	}

	// ==================== 自动报价 - 公共 ======================

	/**
	 * 
	 * <pre>
	 * 功能 - 保存
	 * </pre>
	 * @param offerorder
	 * @return
	 * @since 1.0, 2017年11月7日 下午4:43:03, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "创建报价单", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody OfferOrder offerorder)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */

		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(offerorder))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2.保存数据
		OfferOrder offerorderNew = serviceFactory.getOfferAutoService().save(offerorder);
		return returnSuccessBody(offerorderNew);
	}
	
	/**
	 * <pre>
	 * 计算报价
	 * </pre>
	 * @param offerOrder
	 * @return
	 * @since 1.0, 2017年11月6日 上午10:55:28, think
	 */
	@RequestMapping(value = "quote")
	@ResponseBody
	public AjaxResponseBody quote(@RequestBody OfferOrder offerOrder)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */

		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(offerOrder))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2. 保存数据
		OfferOrder quote = serviceFactory.getOfferAutoService().quote(offerOrder);
		return returnSuccessBody(quote);
	}

	/**
	 * 
	 * <pre>
	 * 功能 - 获取纸张材料列表（自动报价下拉框）
	 * </pre>
	 * @param type
	 * @param name
	 * @return
	 * @since 1.0, 2017年11月9日 下午1:49:45, zhengby
	 */
	@RequestMapping(value = "getOfferPaperList")
	@ResponseBody
	public List<OfferPaper> getOfferPaperList(String offerType, String name)
	{
		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(offerType))
		{
			return null;
		}

		return serviceFactory.getOfferSettingService().getPaperList(OfferType.valueOf(offerType), name);
	}

	/**
	 * 
	 * <pre>
	 * 功能 - 获取坑纸列表（自动报价下拉框）
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年11月9日 上午11:28:43, zhengby
	 */
	@RequestMapping(value = "getOfferBfluteList")
	@ResponseBody
	public List<OfferBflute> getOfferBfluteList(String pit)
	{
		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(pit))
		{
			return null;
		}

		return serviceFactory.getOfferSettingService().getBfluteList(pit);
	}

	/**
	 * <pre>
	 * 盒型计算
	 * </pre>
	 * @param boxFormula
	 * @param length
	 * @param width
	 * @param high
	 * @return
	 * @since 1.0, 2017年11月6日 上午10:39:52, think
	 */
	@RequestMapping(value = "countBoxFormula")
	@ResponseBody
	public AjaxResponseBody countBoxFormula(BoxFormula boxFormula, String length, String width, String high)
	{
		/**
		 * 1. 验证数据 
		 * 2. 保存数据
		 */

		// 1. 验证数据
		if (Validate.validateObjectsNullOrEmpty(boxFormula, length, width, high))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试");
		}
		// 2. 保存数据
		try
		{
			ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
			String lenFormula = boxFormula.getLenFormula();
			String widFormula = boxFormula.getWidFormula();
			if (StringUtils.isNotBlank(length))
			{
				lenFormula = lenFormula.replaceAll("length", length);
				widFormula = widFormula.replaceAll("length", length);
			}
			if (StringUtils.isNotBlank(width))
			{
				lenFormula = lenFormula.replaceAll("width", width);
				widFormula = widFormula.replaceAll("width", width);
			}
			if (StringUtils.isNotBlank(high))
			{
				lenFormula = lenFormula.replaceAll("high", high);
				widFormula = widFormula.replaceAll("high", high);
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("length", new BigDecimal(jse.eval(lenFormula).toString()).setScale(0, BigDecimal.ROUND_HALF_UP));
			result.put("width", new BigDecimal(jse.eval(widFormula).toString()).setScale(0, BigDecimal.ROUND_HALF_UP));
			return returnSuccessBody(result);
		}
		catch (ScriptException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return returnErrorBody(e);
		}
	}

	/**
	 * <pre>
	 * 自动报价 - 公共代码
	 * </pre>
	 * @param map
	 * @param type
	 * @since 1.0, 2017年10月19日 上午9:16:24, think
	 */
	private void _common(ModelMap map, OfferType type)
	{
		map.put("offerType", type);
		List<OfferProcedure> procedureList = serviceFactory.getOfferSettingService().getProcedure(type);
		map.put("procedureList", procedureList);
	}
}
