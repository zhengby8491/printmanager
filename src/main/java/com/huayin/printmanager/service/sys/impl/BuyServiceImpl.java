/**
 * <pre>
 * Author:		   think
 * Create:	 	   2017年10月25日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.sys.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.exception.OperatorException;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.sys.Buy;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.Product_Menu;
import com.huayin.printmanager.persist.entity.sys.BuyRecord;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OrderState;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.sys.BuyService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 系统模块 - 购买信息
 * </pre>
 * @author       think
 * @since        1.0, 2017年10月25日, 整理规范
 */
@Service
public class BuyServiceImpl extends BaseServiceImpl implements BuyService
{
	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#get(java.lang.Long)
	 */
	@Override
	public Buy get(Long id)
	{
		Buy product = daoFactory.getCommonDao().getEntity(Buy.class, id);
		return product;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#getOrder(java.lang.Long)
	 */
	@Override
	public BuyRecord getOrder(Long id)
	{
		return daoFactory.getCommonDao().getEntity(BuyRecord.class, id);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#findByCondition(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<Buy> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(Buy.class);
		query.setIsSearchTotalCount(true);
		if (queryParam.getBuyType() != null)
		{
			query.eq("type", queryParam.getBuyType());
		}

		query.asc("type,sort");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		SearchResult<Buy> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Buy.class);
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#findProductList()
	 */
	@Override
	public List<Buy> findProductList()
	{
		DynamicQuery query = new DynamicQuery(Buy.class);
		// Company company=UserUtils.getCompany();
		// if(company.getIsFormal()==BoolValue.YES){
		// query.eq("type", 1);
		// }else{
		// query.eq("type", 2);
		// }
		List<Buy> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Buy.class);
		return list;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#findOrderByBillNo(java.lang.String)
	 */
	@Override
	public BuyRecord findOrderByBillNo(String billNo)
	{
		DynamicQuery dynamicQuery = new DynamicQuery(BuyRecord.class);
		dynamicQuery.eq("billNo", billNo);
		return daoFactory.getCommonDao().getByDynamicQuery(dynamicQuery, BuyRecord.class);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.BuyService#findAllOrder(com.huayin.printmanager.service.vo.QueryParam)
	 */
	@Override
	public SearchResult<BuyRecord> findAllOrder(QueryParam queryParam)
	{
		DynamicQuery dynamicQuery = new DynamicQuery(BuyRecord.class);
		dynamicQuery.setIsSearchTotalCount(true);
		if (!UserUtils.isSystemCompany())
		{
			dynamicQuery.eq("companyId", UserUtils.getCompanyId());
		}

		if (queryParam.getDateMax() != null && queryParam.getDateMin() != null)
		{
			dynamicQuery.between("createTime", queryParam.getDateMin(), queryParam.getDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getTel()))
		{
			dynamicQuery.like("telephone", "%" + queryParam.getTel() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getInviter()))
		{
			dynamicQuery.like("inviter", "%" + queryParam.getInviter() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getInviterPhone()))
		{
			dynamicQuery.like("inviterPhone", "%" + queryParam.getInviterPhone() + "%");
		}
		if (queryParam.getIsPay() != null)
		{
			if (queryParam.getIsPay() == 0)
			{
				dynamicQuery.eq("isPay", BoolValue.NO);
			}
			else
			{
				dynamicQuery.eq("isPay", BoolValue.YES);
			}
		}
		if (StringUtils.isNotBlank(queryParam.getInvoiceInfor()))
		{
			dynamicQuery.eq("invoiceInfor", queryParam.getInvoiceInfor());
		}

		if (StringUtils.isNotBlank(queryParam.getProductName()))
		{
			dynamicQuery.like("productName", "%" + queryParam.getProductName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCompanyName()))
		{
			dynamicQuery.like("companyName", "%" + queryParam.getCompanyName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getCompanyLinkName()))
		{
			dynamicQuery.like("linkMan", "%" + queryParam.getCompanyLinkName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			dynamicQuery.like("billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getUserName()))
		{
			dynamicQuery.like("userName", "%" + queryParam.getUserName() + "%");
		}
		if (queryParam.getOrderState() != null)
		{
			dynamicQuery.eq("orderState", queryParam.getOrderState());
		}
		if (queryParam.getOrderType() != null)
		{
			dynamicQuery.eq("orderType", queryParam.getOrderType());
		}

		dynamicQuery.setPageSize(queryParam.getPageSize());
		dynamicQuery.setPageIndex(queryParam.getPageNumber());
		dynamicQuery.desc("id");
		SearchResult<BuyRecord> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(dynamicQuery, BuyRecord.class);
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#findByOrderState()
	 */
	@Override
	public BuyRecord findByOrderState()
	{
		DynamicQuery query = new CompanyDynamicQuery(BuyRecord.class);
		query.eq("orderState", OrderState.COMPLETED.id);
		query.eq("isPay", BoolValue.YES);
		query.setPageIndex(0);
		query.setPageSize(1);
		BuyRecord purchaseRecord = daoFactory.getCommonDao().getByDynamicQuery(query, BuyRecord.class);
		return purchaseRecord;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#save(com.huayin.printmanager.persist.entity.sys.Buy)
	 */
	@Override
	@Transactional
	public Buy save(Buy product)
	{
		return daoFactory.getCommonDao().saveEntity(product);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#savaOrer(com.huayin.printmanager.persist.entity.sys.
	 * PurchaseRecord)
	 */
	@Override
	@Transactional
	public BuyRecord savaOrer(BuyRecord purchaseRecord)
	{
		Buy product = daoFactory.getCommonDao().getEntity(Buy.class, purchaseRecord.getProductId());
		if (product != null)
		{
			purchaseRecord.setPrice(product.getPrice());
			purchaseRecord.setProductName(product.getName());
			purchaseRecord.setCompanyId(UserUtils.getCompanyId());
			purchaseRecord.setCompanyName(UserUtils.getCompany().getName());
			purchaseRecord.setBillNo(String.valueOf(System.currentTimeMillis()) + new Random().nextInt(10));
			purchaseRecord.setOrderState(OrderState.WAT_PAY.id);
			purchaseRecord.setType(product.getType());
			purchaseRecord.setUserName(UserUtils.getUserName());
			purchaseRecord.setBonus(product.getBonus());

			BigDecimal taxRate = new BigDecimal(0);
			if ("1".equals(purchaseRecord.getInvoiceInfor()))
			{
				taxRate = new BigDecimal(0.06);
			}
			if ("2".equals(purchaseRecord.getInvoiceInfor()))
			{
				taxRate = new BigDecimal(0.1);
			}
			BigDecimal tax = product.getPrice().multiply(taxRate);
			purchaseRecord.setTax(tax);
		}
		return daoFactory.getCommonDao().saveEntity(purchaseRecord);
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.sys.BuyService#savaOrerManual(com.huayin.printmanager.persist.entity.sys.
	 * PurchaseRecord)
	 */
	@Override
	@Transactional
	public BuyRecord savaOrerManual(BuyRecord purchaseRecord)
	{
		if (purchaseRecord.getId() == null)
		{
			Buy product = daoFactory.getCommonDao().getEntity(Buy.class, purchaseRecord.getProductId());
			if (product != null)
			{
				purchaseRecord.setPrice(product.getPrice());
				purchaseRecord.setProductName(product.getName());
				purchaseRecord.setBillNo(String.valueOf(System.currentTimeMillis()) + new Random().nextInt(100));
				purchaseRecord.setType(product.getType());
			}
			// 已支付
			// if(purchaseRecord.getIsPay() == BoolValue.YES){
			// purchaseRecord.setOrderState(OrderState.COMPLETED.id);
			// }else{
			// purchaseRecord.setOrderState(OrderState.WAT_PAY.id);
			// }
			purchaseRecord.setOrderType(2);
			return daoFactory.getCommonDao().saveEntity(purchaseRecord);
		}
		else
		{
			// 已支付
			if (purchaseRecord.getIsPay() == BoolValue.YES)
			{
				purchaseRecord.setOrderState(OrderState.COMPLETED.id);
			}
			else
			{
				purchaseRecord.setOrderState(OrderState.WAT_PAY.id);
			}
			return daoFactory.getCommonDao().updateEntity(purchaseRecord);
		}
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#update(com.huayin.printmanager.persist.entity.sys.Buy)
	 */
	@Override
	@Transactional
	public Buy update(Buy product)
	{
		return daoFactory.getCommonDao().updateEntity(product);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#updateProductMemu(java.lang.Long, java.util.List)
	 */
	@Override
	@Transactional
	public void updateProductMemu(Long productId, List<Long> menuIdList)
	{
		List<Product_Menu> addMenuList = new ArrayList<Product_Menu>();// 需要新增的菜单
		List<Product_Menu> delMenuList = new ArrayList<Product_Menu>();// 需要删除的菜单
		DynamicQuery query = new DynamicQuery(Product_Menu.class);
		query.eq("productId", productId);
		List<Product_Menu> old_rmList = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Product_Menu.class);
		Collection<Long> hasMenuIdArray = new ArrayList<Long>();

		// 提取需要删除的菜单ID
		for (Product_Menu pm : old_rmList)
		{
			if (!menuIdList.contains(pm.getMenuId()))
			{
				delMenuList.add(pm);
				continue;
			}
			hasMenuIdArray.add(pm.getMenuId());
		}
		// 提取需要新增的菜单
		for (Long menuId : menuIdList)
		{
			if (!hasMenuIdArray.contains(menuId))
			{
				Product_Menu new_rm = new Product_Menu();
				new_rm.setProductId(productId);
				new_rm.setMenuId(menuId);
				addMenuList.add(new_rm);
			}
		}
		daoFactory.getCommonDao().deleteAllEntity(delMenuList);// 删除权限
		daoFactory.getCommonDao().saveAllEntity(addMenuList);// 新增权限
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#updateOrderInfo(java.lang.String, java.lang.String, int)
	 */
	@Override
	@Transactional
	public void updateOrderInfo(String out_trade_no, String trade_no, int paymentMethod) throws OperatorException
	{
		// 修改订单状态
		BuyRecord purchaseRecord = daoFactory.getCommonDao().getEntity(BuyRecord.class, Long.parseLong(out_trade_no));
		if (purchaseRecord.getIsPay() == BoolValue.YES)
		{
			return;
		}
		purchaseRecord.setIsPay(BoolValue.YES);
		purchaseRecord.setTrade_no(trade_no);
		purchaseRecord.setPayTime(new Date());
		purchaseRecord.setPaymentMethod(paymentMethod);
		purchaseRecord.setOrderState(OrderState.COMPLETED.id);
		purchaseRecord.setPayPrice(purchaseRecord.getPrice().add(purchaseRecord.getTax()));
		daoFactory.getCommonDao().updateEntity(purchaseRecord);

		// 修为为正式用户和到期时间和权限修改
		Company company = serviceFactory.getCompanyService().get(purchaseRecord.getCompanyId());
		company.setIsFormal(BoolValue.YES);
		company.setExpireTime(DateUtils.addYears(company.getExpireTime(), 1));
		serviceFactory.getPersistService().update(company);

		List<Product_Menu> hasMenuList = serviceFactory.getMenuService().findMenuByProductId(purchaseRecord.getProductId());
		List<Long> menuList = new ArrayList<Long>(1000);
		for (Product_Menu menu : hasMenuList)
		{
			menuList.add(menu.getMenuId());
		}

		serviceFactory.getMenuService().updateCompanyAdminMenu(purchaseRecord.getCompanyId(), menuList);

		UserUtils.clearCachePermission(purchaseRecord.getCompanyId());

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#updateOrderInfo(java.lang.String, java.lang.String, int,
	 * java.lang.String)
	 */
	@Override
	@Transactional
	public void updateOrderInfo(String out_trade_no, String trade_no, int paymentMethod, String attach) throws OperatorException
	{
		// 修改订单状态
		BuyRecord purchaseRecord = null;
/*		if ("PC".equals(attach))
		{
			purchaseRecord = serviceFactory.getBuyService().getOrder(Long.parseLong(out_trade_no));
		}
		if ("WX".equals(attach))
		{
			purchaseRecord = serviceFactory.getBuyService().findOrderByBillNo(out_trade_no);
		}*/
		purchaseRecord = serviceFactory.getBuyService().findOrderByBillNo(out_trade_no);
		if (purchaseRecord.getIsPay() == BoolValue.YES)
		{
			return;
		}
		purchaseRecord.setIsPay(BoolValue.YES);
		purchaseRecord.setTrade_no(trade_no);
		purchaseRecord.setPayTime(new Date());
		purchaseRecord.setPaymentMethod(paymentMethod);
		purchaseRecord.setOrderState(OrderState.COMPLETED.id);
		purchaseRecord.setPayPrice(purchaseRecord.getPrice().add(purchaseRecord.getTax()));
		daoFactory.getCommonDao().updateEntity(purchaseRecord);

		// 修为为正式用户和到期时间和权限修改
		Company company = serviceFactory.getCompanyService().get(purchaseRecord.getCompanyId());
		company.setIsFormal(BoolValue.YES);
		company.setExpireTime(DateUtils.addYears(company.getExpireTime(), 1));
		serviceFactory.getPersistService().update(company);

		List<Product_Menu> hasMenuList = serviceFactory.getMenuService().findMenuByProductId(purchaseRecord.getProductId());
		List<Long> menuList = new ArrayList<Long>(1000);
		for (Product_Menu menu : hasMenuList)
		{
			menuList.add(menu.getMenuId());
		}

		serviceFactory.getMenuService().updateCompanyAdminMenu(purchaseRecord.getCompanyId(), menuList);

		UserUtils.clearCachePermission(purchaseRecord.getCompanyId());

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#updateOrder(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional
	public void updateOrder(Long orderId, Long productId) throws OperatorException
	{
		// 1.修改订单信息
		Buy payProduct = daoFactory.getCommonDao().getEntity(Buy.class, productId);
		BuyRecord record = daoFactory.getCommonDao().getEntity(BuyRecord.class, orderId);
		if (payProduct != null && record != null)
		{
			record.setProductId(productId);
			record.setProductName(payProduct.getName());
			record.setPrice(payProduct.getPrice());
			record.setType(payProduct.getType());
			daoFactory.getCommonDao().updateEntity(record);

			// 2.修改权限
			if (payProduct.getType() == 1)
			{
				List<Product_Menu> hasMenuList = serviceFactory.getMenuService().findMenuByProductId(productId);
				List<Long> menuList = new ArrayList<Long>(1000);
				for (Product_Menu menu : hasMenuList)
				{
					menuList.add(menu.getMenuId());
				}

				serviceFactory.getMenuService().updateCompanyMenu(record.getCompanyId(), menuList);

				UserUtils.clearCachePermission(record.getCompanyId());
			}

		}

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public void delete(Long id)
	{
		daoFactory.getCommonDao().deleteByIds(Buy.class, id);

	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#cancelOrder(java.lang.Long)
	 */
	@Override
	@Transactional
	public void cancelOrder(Long id)
	{
		BuyRecord record = daoFactory.getCommonDao().getEntity(BuyRecord.class, id);
		record.setOrderState(OrderState.CANCELED.id);
		daoFactory.getCommonDao().updateEntity(record);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.sys.BuyService#check(int)
	 */
	@Override
	public boolean check(int orderState)
	{
		DynamicQuery query = new CompanyDynamicQuery(BuyRecord.class);
		query.addProjection(Projections.count());
		query.eq("orderState", orderState);

		int count = daoFactory.getCommonDao().countByDynamicQuery(query, BuyRecord.class);
		if (count == 0)
		{
			return true;
		}
		return false;
	}
}
