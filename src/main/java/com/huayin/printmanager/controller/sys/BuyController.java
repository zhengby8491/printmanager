/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.JsonUtils;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.AdminAuth;
import com.huayin.printmanager.exception.OperatorException;
import com.huayin.printmanager.persist.entity.sys.Buy;
import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.persist.entity.sys.Product_Menu;
import com.huayin.printmanager.persist.entity.sys.BuyRecord;
import com.huayin.printmanager.persist.enumerate.OrderState;
import com.huayin.printmanager.service.sys.vo.CompanyVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.MenuUtils;
import com.huayin.printmanager.utils.StringUtils;

/**
 * <pre>
 * 系统模块 - 购买信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Controller
@RequestMapping(value = "${basePath}/sys/buy")
public class BuyController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 购买新增
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:40:28, think
	 */
	@RequestMapping(value = "create")
	@AdminAuth
	public String create(ModelMap map)
	{
		return "sys/buy/create";
	}

	/**
	 * <pre>
	 * 功能 - 购买新增
	 * </pre>
	 * @param product
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:40:41, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@AdminAuth
	public AjaxResponseBody save(@RequestBody Buy product, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(product.getName()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		serviceFactory.getBuyService().save(product);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 购买修改
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:41:11, think
	 */
	@RequestMapping(value = "edit/{id}")
	@AdminAuth
	public String edit(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		Buy product = serviceFactory.getBuyService().get(id);
		map.put("product", product);
		return "sys/buy/edit";
	}

	/**
	 * <pre>
	 * 功能 - 购买修改
	 * </pre>
	 * @param product
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:41:31, think
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@AdminAuth
	public AjaxResponseBody update(@RequestBody Buy product, ModelMap map)
	{

		if (Validate.validateObjectsNullOrEmpty(product.getName()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		Buy result = serviceFactory.getBuyService().get(product.getId());
		if (Validate.validateObjectsNullOrEmpty(result))
		{
			return returnErrorBody("销售产品已不存在");
		}

		serviceFactory.getBuyService().update(product);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 购买删除
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:42:14, think
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@AdminAuth
	public AjaxResponseBody delete(@PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		serviceFactory.getBuyService().delete(id);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 购买列表
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:39:08, think
	 */
	@RequestMapping(value = "list")
	@AdminAuth
	public String list()
	{
		return "sys/buy/list";

	}

	/**
	 * <pre>
	 * Ajax列表 - 购买信息列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:39:31, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<Buy> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<Buy> result = serviceFactory.getBuyService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 查询购买模块对应的权限
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:42:35, think
	 */
	@RequestMapping(value = "editMenu/{id}")
	@AdminAuth
	public String editMenu(@PathVariable Long id, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}

		List<Menu> allMenuList = serviceFactory.getMenuService().findAll();
		allMenuList = MenuUtils.buildTree(allMenuList, MenuUtils.TREE_ROOT_ID);

		Map<Long, Long> hasMenuIdMap = new HashMap<Long, Long>();
		List<Product_Menu> hasMenuList = serviceFactory.getMenuService().findMenuByProductId(id);
		for (Product_Menu m : hasMenuList)
		{
			hasMenuIdMap.put(m.getMenuId(), m.getMenuId());
		}

		map.put("allMenuList", allMenuList);
		map.put("hasMenuIdMap", hasMenuIdMap);
		map.put("id", id);
		return "sys/buy/editMenu";
	}

	/**
	 * <pre>
	 * 功能 - 更新用户对应的权限菜单
	 * </pre>
	 * @param product
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:43:04, think
	 */
	@RequestMapping(value = "updateMenu")
	@ResponseBody
	@AdminAuth
	public AjaxResponseBody updateMenu(@RequestBody Buy product, ModelMap map)
	{
		serviceFactory.getBuyService().updateProductMemu(product.getId(), product.getMenuIdList());
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 查询模块
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:43:28, think
	 */
	@RequestMapping(value = "queryModule")
	@ResponseBody
	public AjaxResponseBody queryModule(ModelMap map)
	{
		List<Buy> list = serviceFactory.getBuyService().findProductList();

		return returnSuccessBody(list);
	}

	/**
	 * <pre>
	 * 功能 - 查询所有栏目
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:43:46, think
	 */
	@RequestMapping(value = "findAllMenu")
	@ResponseBody
	public AjaxResponseBody findAllMenu()
	{
		List<Menu> allMenuList = serviceFactory.getMenuService().findAll();
		allMenuList = MenuUtils.buildTree(allMenuList, MenuUtils.TREE_ROOT_ID);
		return returnSuccessBody(allMenuList);
	}

	/**
	 * <pre>
	 * 功能 - 根据购买版本查询对应菜单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:44:01, think
	 */
	@RequestMapping(value = "findMenuByProductId/{id}")
	@ResponseBody
	public AjaxResponseBody findMenuByProductId(@PathVariable Long id)
	{
		Map<Long, Long> hasMenuIdMap = new HashMap<Long, Long>();
		List<Product_Menu> hasMenuList = serviceFactory.getMenuService().findMenuByProductId(id);
		for (Product_Menu m : hasMenuList)
		{
			hasMenuIdMap.put(m.getMenuId(), m.getMenuId());
		}
		return returnSuccessBody(hasMenuIdMap);
	}

	/**
	 * <pre>
	 * 页面  - 公司购买订单列表
	 * </pre>
	 * @return
	 * @throws Exception
	 * @since 1.0, 2017年10月25日 下午2:44:13, think
	 */
	@RequestMapping(value = "orderList")
	public String orderList() throws Exception
	{
		return "sys/buy/orderList";
	}

	/**
	 * <pre>
	 * Ajax列表 - 查询公司购买订单
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:44:26, think
	 */
	@RequestMapping(value = "ajaxOrderList")
	@ResponseBody
	public SearchResult<BuyRecord> orderList(@RequestBody QueryParam queryParam)
	{
		SearchResult<BuyRecord> result = serviceFactory.getBuyService().findAllOrder(queryParam);
		return result;
	}

	/**
	 * 
	 * <pre>
	 * 公共 - 公司购买订单
	 * </pre>
	 * @param map
	 * @since 1.0, 2017年10月25日 下午2:44:54, think
	 */
	private void _orderPublic(ModelMap map)
	{
		// 查询所有公司信息
		SearchResult<CompanyVo> company = serviceFactory.getCompanyService().findByCondition(new QueryParam());
		List<CompanyVo> listCompany = Lists.newArrayList();
		for (CompanyVo vo : company.getResult())
		{
			// 这里要过滤公司名称为空的数据
			if (StringUtils.isEmpty(vo.getCompany().getName()))
			{
				continue;
			}
			listCompany.add(vo);
		}
		map.put("company", listCompany);
		map.put("jsonCompany", JsonUtils.toJson(listCompany));
		// 查询所有购买版本
		SearchResult<Buy> buy = serviceFactory.getBuyService().findByCondition(new QueryParam());
		List<Buy> listBuy = Lists.newArrayList();
		for (Buy vo : buy.getResult())
		{
			listBuy.add(vo);
		}
		map.put("buy", listBuy);
		map.put("jsonBuy", JsonUtils.toJson(listBuy));
	}

	/**
	 * <pre>
	 * 页面 - 公司购买订单新增
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:45:33, think
	 */
	@RequestMapping(value = "orderCreate")
	@AdminAuth
	public String orderCreate(ModelMap map)
	{
		_orderPublic(map);

		return "sys/buy/orderCreate";
	}

	/**
	 * <pre>
	 * 页面 - 公司购买订单修改
	 * </pre>
	 * @param map
	 * @param orderId
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:45:53, think
	 */
	@RequestMapping(value = "orderEdit/{orderId}")
	@AdminAuth
	public String orderEdit(ModelMap map, @PathVariable Long orderId)
	{
		_orderPublic(map);
		BuyRecord purchaseRecord = serviceFactory.getBuyService().getOrder(orderId);
		map.put("purchaseRecord", purchaseRecord);
		return "sys/buy/orderEdit";
	}

	/**
	 * <pre>
	 * 功能 - 公司购买订单新增
	 * </pre>
	 * @param purchaseRecord
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:46:26, think
	 */
	@RequestMapping(value = "orderSave")
	@ResponseBody
	@AdminAuth
	public AjaxResponseBody orderSave(@RequestBody BuyRecord purchaseRecord, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(purchaseRecord.getUserName()))
		{
			return returnErrorBody("数据没有通过验证，请刷新页面后重试！");
		}
		// 默认为1
		purchaseRecord.setPaymentMethod(1);
		serviceFactory.getBuyService().savaOrerManual(purchaseRecord);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 订单取消
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:46:40, think
	 */
	@RequestMapping(value = "cancelOrder/{id}")
	public String cancelOrder(@PathVariable Long id)
	{
		serviceFactory.getBuyService().cancelOrder(id);
		return "sys/buy/orderList";
	}

	/**
	 * <pre>
	 * 功能 - 订单修改
	 * </pre>
	 * @param orderId
	 * @param productId
	 * @param map
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:49:38, think
	 */
	@RequestMapping(value = "updateOrder/{orderId}/{productId}")
	@AdminAuth
	public String updateOrder(@PathVariable Long orderId, @PathVariable Long productId, ModelMap map)
	{
		try
		{
			serviceFactory.getBuyService().updateOrder(orderId, productId);
			return "sys/buy/orderList";
		}
		catch (OperatorException e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
			return returnErrorPage(map, "修改失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 检查用户是否可以继续购买
	 * </pre>
	 * @return
	 * @since 1.0, 2017年10月25日 下午2:49:52, think
	 */
	@RequestMapping(value = "isPay")
	@ResponseBody
	public AjaxResponseBody isPay()
	{
		boolean flag = serviceFactory.getBuyService().check(OrderState.WAT_PAY.id);
		return returnSuccessBody(flag);
	}

}
