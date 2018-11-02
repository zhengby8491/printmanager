/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月26日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.finance;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.oem.OemReconcil;
import com.huayin.printmanager.persist.entity.oem.OemReconcilDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcil;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReconcilDetail;
import com.huayin.printmanager.persist.entity.purch.PurchReconcil;
import com.huayin.printmanager.persist.entity.purch.PurchReconcilDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReconcil;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 财务管理 - 财务未清
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/finance/unfinished")
public class FinanceUnfinishedController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 采购应付账款明细列表
	 * </pre>
	 * @param map
	 * @param dateMin
	 * @param dateMax
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:16:14, think
	 */
	@RequestMapping(value = "purchPayment")
	@RequiresPermissions("finance:unfinished:purch_payment")
	public String purchPayment(ModelMap map, Date dateMin, Date dateMax)
	{
		map.put("dateMin", dateMin);
		map.put("dateMax", dateMax);
		return "finance/unfinished/purch_payment";
	}

	/**
	 * <pre>
	 * 数据 - 采购应付账款明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:16:29, think
	 */
	@RequestMapping(value = "purchPaymentData")
	@ResponseBody
	public SearchResult<PurchReconcilDetail> purchPaymentList(@RequestBody QueryParam queryParam)
	{
		SearchResult<PurchReconcilDetail> result = serviceFactory.getPaymentService().findPurchShouldPayment(queryParam);

		PurchReconcilDetail reconcilDetail = new PurchReconcilDetail();

		BigDecimal qty = new BigDecimal(0);
		BigDecimal paymentMoney = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);
		for (PurchReconcilDetail purchReconcilDetail : result.getResult())
		{
			qty = qty.add(purchReconcilDetail.getQty());
			paymentMoney = paymentMoney.add(purchReconcilDetail.getPaymentMoney());
			money = money.add(purchReconcilDetail.getMoney());
		}
		PurchReconcil purchReconcil = new PurchReconcil();
		reconcilDetail.setMaster(purchReconcil);
		reconcilDetail.setMoney(money);
		reconcilDetail.setQty(qty);
		reconcilDetail.setPaymentMoney(paymentMoney);
		result.getResult().add(reconcilDetail);

		return result;
	}

	/**
	 * <pre>
	 * 页面 - 发外应付账款明细列表
	 * </pre>
	 * @param map
	 * @param dateMin
	 * @param dateMax
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:16:50, think
	 */
	@RequestMapping(value = "outSourcePayment")
	@RequiresPermissions("finance:unfinished:outsource_payment")
	public String outSourcePayment(ModelMap map, Date dateMin, Date dateMax)
	{
		map.put("dateMin", dateMin);
		map.put("dateMax", dateMax);
		return "finance/unfinished/outsource_payment";
	}

	/**
	 * <pre>
	 * 数据 - 发外应付账款明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:17:01, think
	 */
	@RequestMapping(value = "outSourcePaymentData")
	@ResponseBody
	public SearchResult<OutSourceReconcilDetail> outSourcePaymentList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OutSourceReconcilDetail> result = serviceFactory.getPaymentService().findOutSourceShouldPayment(queryParam);

		OutSourceReconcilDetail reconcilDetail = new OutSourceReconcilDetail();

		BigDecimal qty = new BigDecimal(0);
		BigDecimal paymentMoney = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);
		for (OutSourceReconcilDetail outSourceReconcilDetail : result.getResult())
		{
			qty = qty.add(outSourceReconcilDetail.getQty());
			paymentMoney = paymentMoney.add(outSourceReconcilDetail.getPaymentMoney());
			money = money.add(outSourceReconcilDetail.getMoney());
		}
		OutSourceReconcil outSourceReconcil = new OutSourceReconcil();
		reconcilDetail.setMaster(outSourceReconcil);
		reconcilDetail.setMoney(money);
		reconcilDetail.setQty(qty);
		reconcilDetail.setPaymentMoney(paymentMoney);
		result.getResult().add(reconcilDetail);
		return result;
	}

	/**
	 * <pre>
	 * 页面  - 应收账款明细列表
	 * </pre>
	 * @param map
	 * @param dateMin
	 * @param dateMax
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:17:14, think
	 */
	@RequestMapping(value = "saleReceive")
	@RequiresPermissions("finance:unfinished:sale_receive")
	public String saleReceive(ModelMap map, Date dateMin, Date dateMax)
	{
		map.put("dateMin", dateMin);
		map.put("dateMax", dateMax);
		return "finance/unfinished/sale_receive";
	}

	/**
	 * <pre>
	 * 数据 - 销售应收账款明细数据列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:17:24, think
	 */
	@RequestMapping(value = "saleReceiveData")
	@ResponseBody
	public SearchResult<SaleReconcilDetail> saleReceiveList(@RequestBody QueryParam queryParam)
	{
		try
		{
			SearchResult<SaleReconcilDetail> result = serviceFactory.getReceiveService().findSaleShouldReceive(queryParam);
			SaleReconcilDetail reconcilDetail = new SaleReconcilDetail();

			Integer qty = new Integer(0);
			BigDecimal receiveMoney = new BigDecimal(0);
			BigDecimal money = new BigDecimal(0);
			for (SaleReconcilDetail saleReconcilDetail : result.getResult())
			{
				qty += saleReconcilDetail.getQty();
				receiveMoney = receiveMoney.add(saleReconcilDetail.getReceiveMoney());
				money = money.add(saleReconcilDetail.getMoney());
			}
			SaleReconcil saleReconcil = new SaleReconcil();
			reconcilDetail.setMaster(saleReconcil);
			reconcilDetail.setMoney(money);
			reconcilDetail.setQty(qty);
			reconcilDetail.setReceiveMoney(receiveMoney);
			result.getResult().add(reconcilDetail);
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * <pre>
	 * 功能 - 强制完工财务未清
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:17:41, think
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("finance:unfinished:purch_complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam("ids[]") List<Long> ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}

		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? PurchReconcil.class : PurchReconcilDetail.class;
		serviceFactory.getCommonService().forceComplete2(cla, ids, BoolValue.YES);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 取消强制完工财务未清
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:18:00, think
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("finance:unfinished:purch_cancel_complete")
	public AjaxResponseBody complete_cancel(TableType tableType, @RequestParam("ids[]") List<Long> ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}

		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? PurchReconcil.class : PurchReconcilDetail.class;
		serviceFactory.getCommonService().forceComplete2(cla, ids, BoolValue.NO);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 销售强制完工
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:18:20, think
	 */
	@RequestMapping(value = "sale_complete")
	@ResponseBody
	@RequiresPermissions("finance:unfinished:purch_complete")
	public AjaxResponseBody saleComplete(TableType tableType, @RequestParam("ids[]") List<Long> ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}

		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? SaleReconcil.class : SaleReconcilDetail.class;
		serviceFactory.getCommonService().forceComplete2(cla, ids, BoolValue.YES);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 取消销售强制完工
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:18:42, think
	 */
	@RequestMapping(value = "sale_complete_cancel")
	@ResponseBody
	@RequiresPermissions("finance:unfinished:purch_cancel_complete")
	public AjaxResponseBody sale_complete_cancel(TableType tableType, @RequestParam("ids[]") List<Long> ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}

		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? SaleReconcil.class : SaleReconcilDetail.class;
		serviceFactory.getCommonService().forceComplete2(cla, ids, BoolValue.NO);
		return returnSuccessBody();
	}

//	/**
//	 * <pre>
//	 * 页面 - 财务未清新增
//	 * </pre>
//	 * @param ids
//	 * @param map
//	 * @return
//	 * @since 1.0, 2018年2月26日 下午2:19:17, think
//	 */
//	@RequestMapping(value = "create")
//	@RequiresPermissions("finance:unfinished:create")
//	public String create(Long[] ids, ModelMap map)
//	{
//		if (ids != null)
//		{
//			QueryParam queryParam = new QueryParam();
//			queryParam.setIds(Arrays.asList(ids));
//			SearchResult<OutSourceReconcilDetail> result = serviceFactory.getPaymentService().findOutSourceShouldPayment(queryParam);
//			if (result.getResult().size() > 0)
//			{
//				for (OutSourceReconcilDetail detail : result.getResult())
//				{
//					detail.getMaster().setBillTypeText(detail.getMaster().getBillTypeText());
//				}
//
//				String billJson = JsonUtils.toJson(result.getResult());
//				if (StringUtils.isNotBlank(billJson))
//				{
//					map.put("billJson", billJson);
//				}
//
//				queryParam.setId(result.getResult().get(0).getMaster().getSupplierId());
//				SearchResult<Supplier> res = serviceFactory.getSupplierService().quickFindByCondition(queryParam);
//				Supplier supplier = res.getResult().get(0);
//				String supplierJson = JsonUtils.toJson(supplier);
//				map.put("supplierJson", supplierJson);
//			}
//		}
//		return "finance/payment/create";
//	}

	/**
	 * <pre>
	 * 功能 - 发外强制完工
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:19:40, think
	 */
	@RequestMapping(value = "outsource_complete")
	@ResponseBody
	@RequiresPermissions("finance:unfinished:complete")
	public AjaxResponseBody outsource_complete(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}

		if (serviceFactory.getCommonService().forceComplete(OutSourceReconcilDetail.class, ids, BoolValue.YES))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 功能  - 取消发外强制完工
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:20:04, think
	 */
	@RequestMapping(value = "outsource_complete_cancel")
	@ResponseBody
	@RequiresPermissions("finance:unfinished:complete_cancel")
	public AjaxResponseBody outsource_completeCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		if (serviceFactory.getCommonService().forceComplete(OutSourceReconcilDetail.class, ids, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}
	
	/**
	 * <pre>
	 * 页面  - 代工应收款明细列表
	 * </pre>
	 * @param map
	 * @param dateMin
	 * @param dateMax
	 * @return
	 * @since 1.0, 2018年3月19日 上午10:18:39, zhengby
	 */
	@RequestMapping(value = "oemReceive")
	@RequiresPermissions("finance:unfinished:oem:receive")
	public String oemReceive(ModelMap map, Date dateMin, Date dateMax)
	{
		map.put("dateMin", dateMin);
		map.put("dateMax", dateMax);
		return "finance/unfinished/oem_receive";
	}
	
	/**
	 * <pre>
	 * 数据  - 代工应收款列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月19日 上午10:54:35, zhengby
	 */
	@RequestMapping(value = "oemReceiveData")
	@ResponseBody
	public SearchResult<OemReconcilDetail> oemReceiveList(@RequestBody QueryParam queryParam)
	{
		try
		{
			SearchResult<OemReconcilDetail> result = serviceFactory.getReceiveService().findOemShouldReceive(queryParam);
			OemReconcilDetail reconcilDetail = new OemReconcilDetail();

			BigDecimal qty = new BigDecimal(0);
			BigDecimal receiveMoney = new BigDecimal(0);
			BigDecimal money = new BigDecimal(0);
			for (OemReconcilDetail oemReconcilDetail : result.getResult())
			{
				qty = qty.add(oemReconcilDetail.getQty());
				receiveMoney = receiveMoney.add(oemReconcilDetail.getReceiveMoney());
				money = money.add(oemReconcilDetail.getMoney());
			}
			OemReconcil oemReconcil = new OemReconcil();
			reconcilDetail.setMaster(oemReconcil);
			reconcilDetail.setMoney(money);
			reconcilDetail.setQty(qty);
			reconcilDetail.setReceiveMoney(receiveMoney);
			result.getResult().add(reconcilDetail);
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * <pre>
	 * 功能 - 强制完工代工对账单
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年3月29日 上午11:50:14, zhengby
	 */
	@RequestMapping(value = "oem_complete")
	@ResponseBody
	@RequiresPermissions("finance:unfinished:oem:complete")
	public AjaxResponseBody oemComplete(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().forceComplete(OemReconcilDetail.class, ids, BoolValue.YES))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody(I18nResource.FAIL);
		}
	}
	
	/**
	 * <pre>
	 * 功能 - 取消强制完工代工对账单
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年3月29日 上午11:57:44, zhengby
	 */
	@RequestMapping(value = "oem_complete_cancel")
	@ResponseBody
	@RequiresPermissions("finance:unfinished:oem:completecancel")
	public AjaxResponseBody oemCompleteCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		if (serviceFactory.getCommonService().forceComplete(OemReconcilDetail.class, ids, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody(I18nResource.FAIL);
		}
	}
}
