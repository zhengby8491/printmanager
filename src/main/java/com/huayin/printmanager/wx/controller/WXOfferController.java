/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

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
import com.huayin.printmanager.persist.entity.offer.OfferBflute;
import com.huayin.printmanager.persist.entity.offer.OfferOrder;
import com.huayin.printmanager.persist.entity.offer.OfferPaper;
import com.huayin.printmanager.persist.entity.offer.OfferProcedure;
import com.huayin.printmanager.persist.enumerate.BoxFormula;
import com.huayin.printmanager.persist.enumerate.OfferType;
import com.huayin.printmanager.persist.enumerate.PaperType;
import com.huayin.printmanager.persist.enumerate.WXSumQueryType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.wx.util.WxUtil;

/**
 * <pre>
 * 微信 - 报价系统
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/wx/offer")
public class WXOfferController extends BaseController
{
	/**
	 * <pre>
	 * 功能 - 盒型计算
	 * </pre>
	 * @param boxFormula
	 * @param length
	 * @param width
	 * @param high
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:25:14, think
	 */
	@RequestMapping(value = "countBoxFormula")
	@ResponseBody
	public AjaxResponseBody countBoxFormula(BoxFormula boxFormula, String length, String width, String high)
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
		try
		{
			result.put("length", new BigDecimal(jse.eval(lenFormula).toString()).setScale(0, BigDecimal.ROUND_HALF_UP));
			result.put("width", new BigDecimal(jse.eval(widFormula).toString()).setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		catch (ScriptException e)
		{
			e.printStackTrace();
		}
		return returnSuccessBody(result);
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
	 * 页面 - 报价单据列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:25:34, think
	 */
	@RequestMapping(value = "view/list")
	public String listView()
	{
		if (!WxUtil.getUserHasMenu("offer:order:list"))
		{
			return "wx/notPermission";
		}
		return "wx/offer/list";
	}

	/**
	 * <pre>
	 * 数据 - 报价单据列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:25:43, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<OfferOrder> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OfferOrder> result = null;
		try
		{
			result = serviceFactory.getOfferOrderService().findByCondition(queryParam);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 报价单详情
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:26:00, think
	 */
	@RequestMapping(value = "view/detail")
	public String saleSumDetailView(HttpServletRequest request, ModelMap map, Long id)
	{
		map.put("id", id);
		return "wx/offer/detail";
	}

	/**
	 * <pre>
	 * 页面 - 报价查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:26:11, think
	 */
	@RequestMapping(value = "view/{id}")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		map.put("id", id);
		return "wx/offer/view_pack";
	}

	/**
	 * <pre>
	 * 功能 - 报价单据列表
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:26:30, think
	 */
	@RequestMapping(value = "getOfferOrder")
	@ResponseBody
	public AjaxResponseBody getOfferOrder(Long id)
	{
		OfferOrder offerBean = null;
		try
		{
			offerBean = serviceFactory.getOfferOrderService().get(id);
		}
		catch (Exception e)
		{
			logger.error("查询明细出错了", e);
			return returnErrorBody(e.getMessage());
		}
		return returnSuccessBody(offerBean);
	}

	/**
	 * <pre>
	 * 页面 - 报价首页
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:26:46, think
	 */
	@RequestMapping(value = "view/index")
	public String indexView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		if (!WxUtil.getUserHasMenu("offer:order:list"))
		{
			return "wx/notPermission";
		}
		return "wx/offer/index";
	}

	/**
	 * <pre>
	 * 页面 - 单张类
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:26:58, think
	 */
	@RequestMapping(value = "view/single")
	public String singleView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		_common(map, OfferType.SINGLE);
		return "wx/offer/auto/single";
	}

	/**
	 * <pre>
	 * 页面 - 画册书刊
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:27:17, think
	 */
	@RequestMapping(value = "view/book")
	public String bookView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		_common(map, OfferType.ALBUMBOOK);
		return "wx/offer/auto/book";
	}

	/**
	 * <pre>
	 * 页面 - 彩盒
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:27:28, think
	 */
	@RequestMapping(value = "view/carton")
	public String cartonView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		_common(map, OfferType.CARTONBOX);
		// 默认正度纸
		map.put("paperType", PaperType.ARE_DEGREES_PAPER);
		return "wx/offer/auto/carton";
	}

	/**
	 * <pre>
	 * 页面 - 便签信纸
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:27:40, think
	 */
	@RequestMapping(value = "view/note")
	public String noteView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		_common(map, OfferType.NOTESLETTERFORM);
		return "wx/offer/auto/note";
	}

	/**
	 * <pre>
	 * 页面 - 吊牌卡片
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:27:51, think
	 */
	@RequestMapping(value = "view/card")
	public String cardView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		_common(map, OfferType.TAGCARD);
		return "wx/offer/auto/card";
	}

	/**
	 * <pre>
	 * 页面 - 封套类
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:28:06, think
	 */
	@RequestMapping(value = "view/sheath")
	public String sheathView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		_common(map, OfferType.ENVELOPETYPE);
		return "wx/offer/auto/sheath";
	}

	/**
	 * <pre>
	 * 页面 - 不干胶
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:28:24, think
	 */
	@RequestMapping(value = "view/glue")
	public String glueView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		_common(map, OfferType.PRESSURESENSITIVEADHSIVE);
		return "wx/offer/auto/glue";
	}

	/**
	 * <pre>
	 * 页面 - 联单
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:29:45, think
	 */
	@RequestMapping(value = "view/bill")
	public String billView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		_common(map, OfferType.ASSOCIATEDSINGLECLASS);
		return "wx/offer/auto/bill";
	}

	/**
	 * <pre>
	 * 页面 - 信封类
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:29:56, think
	 */
	@RequestMapping(value = "view/envelope")
	public String envelopeView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		_common(map, OfferType.MAILERTYPE);
		return "wx/offer/auto/envelope";
	}

	/**
	 * <pre>
	 * 页面 - 纸杯类
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:30:08, think
	 */
	@RequestMapping(value = "view/paperCup")
	public String paperCupView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		_common(map, OfferType.CUP);
		return "wx/offer/auto/paperCup";
	}

	/**
	 * <pre>
	 * 自动报价 - 公共代码
	 * </pre>
	 * @param map
	 * @param type
	 * @since 1.0, 2018年2月26日 上午9:30:18, think
	 */
	private void _common(ModelMap map, OfferType type)
	{
		map.put("offerType", type);
		List<OfferProcedure> procedureList = serviceFactory.getOfferSettingService().getProcedure(type);
		map.put("procedureList", procedureList);
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
		try
		{
			OfferOrder quote = serviceFactory.getOfferAutoService().quote(offerOrder);
			return returnSuccessBody(quote);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return returnErrorBody(e);
		}
	}

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
		try
		{
			OfferOrder offerorderNew = serviceFactory.getOfferAutoService().save(offerorder);
			return returnSuccessBody(offerorderNew);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return returnErrorBody(e);
		}
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
}
