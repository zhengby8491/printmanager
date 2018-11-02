/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.JsonUtils;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.persist.entity.sys.Buy;
import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.persist.entity.sys.Product_Menu;
import com.huayin.printmanager.persist.entity.sys.BuyRecord;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.utils.MenuUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;
import com.huayin.printmanager.wx.service.WXPayService;
import com.huayin.printmanager.wx.vo.QueryParam;

/**
 * <pre>
 * 微信 - 支付
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Service
public class WXPayServiceImpl extends BaseServiceImpl implements WXPayService
{
	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXPayService#findProductAndMenu()
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findProductAndMenu()
	{
		// Step1 获取所有产品模块列表
		List<Buy> productList = serviceFactory.getBuyService().findProductList();
		// Step2 获取所有产品模块菜单
		List<Product_Menu> productMenuList = serviceFactory.getMenuService().findMenuByProduct();
		// Step3 获取所有菜单列表
		List<Menu> menuList = serviceFactory.getMenuService().findAll();

		String sysMemuJson = SystemConfigUtil.getConfig(SysConstants.SYS_MENU);
		sysMemuJson = sysMemuJson.replaceAll("&quot;", "\"");
		Set<String> sysMemu = (Set<String>) JsonUtils.jsonToSet(sysMemuJson);
		List<Menu> removeMenu = new ArrayList<>();
		for (Menu menu : menuList)
		{
			if (sysMemu.contains(String.valueOf(menu.getId())))
			{
				removeMenu.add(menu);
			}
		}
		menuList.removeAll(removeMenu);
		menuList = MenuUtils.buildTree(menuList, MenuUtils.TREE_ROOT_ID);
		// Step4 过滤产品、产品菜单、菜单数据
		Map<Long, List<Long>> filterProductMenu = Maps.newHashMap();
		for (Product_Menu pmenu : productMenuList)
		{
			List<Long> child = filterProductMenu.get(pmenu.getProductId());
			if (null == child)
			{
				child = Lists.newArrayList();
				filterProductMenu.put(pmenu.getProductId(), child);
			}
			child.add(pmenu.getMenuId());
		}

		Map<String, Object> ret = Maps.newHashMap();

		ret.put("productList", productList);
		ret.put("productMenuMap", JsonUtils.toJson(filterProductMenu));
		ret.put("menuList", menuList);
		return ret;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXPayService#getByProductId(java.lang.Long)
	 */
	public Buy getByProductId(Long id)
	{
		return serviceFactory.getBuyService().get(id);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.wx.service.WXPayService#findAllOrder(com.huayin.printmanager.wx.vo.QueryParam)
	 */
	public SearchResult<BuyRecord> findAllOrder(QueryParam queryParam)
	{
		DynamicQuery dynamicQuery = new DynamicQuery(BuyRecord.class);
		dynamicQuery.setIsSearchTotalCount(true);
		if (!UserUtils.isSystemCompany())
		{
			dynamicQuery.eq("companyId", UserUtils.getCompanyId());
		}
		if (StringUtils.isNotBlank(queryParam.getSearchContent()))
		{
			dynamicQuery.add(Restrictions.or(Restrictions.like("productName", "%" + queryParam.getSearchContent() + "%"), Restrictions.like("billNo", "%" + queryParam.getSearchContent() + "%")));
		}
		dynamicQuery.setPageSize(queryParam.getPageSize());
		dynamicQuery.setPageIndex(queryParam.getPageNumber());
		dynamicQuery.desc("id");
		SearchResult<BuyRecord> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(dynamicQuery, BuyRecord.class);
		return result;
	}
}
