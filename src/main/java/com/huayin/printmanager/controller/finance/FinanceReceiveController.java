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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.controller.vo.SumBeginMoneyVo;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.begin.CustomerBeginDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveDetail;
import com.huayin.printmanager.persist.entity.oem.OemReconcilDetail;
import com.huayin.printmanager.persist.entity.sale.SaleReconcilDetail;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 财务管理 - 收款单
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/finance/receive")
public class FinanceReceiveController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 收款单新增
	 * </pre>
	 * @param map
	 * @param request
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:05:16, think
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("finance:receive:create")
	public String create(ModelMap map, HttpServletRequest request, Long[] ids, BillType billType)
	{
		if (ids == null)
		{
			return "finance/receive/create";
		}
		QueryParam queryParam = new QueryParam();
		queryParam.setIds(Arrays.asList(ids));
    Long customerId = null;
		if (BillType.SALE_SK == billType) // 销售对账
		{
			SearchResult<SaleReconcilDetail> result = serviceFactory.getReceiveService().findSaleShouldReceive(queryParam);
			for (SaleReconcilDetail detail : result.getResult())
			{
				detail.getMaster().setCustomerName(detail.getMaster().getCustomerName());
				detail.getMaster().setBillTypeText(detail.getMaster().getBillTypeText());
			}
			if (CollectionUtils.isEmpty(result.getResult()))
			{
				return "finance/receive/create";
			}
			
			List<SaleReconcilDetail> list = Lists.newArrayList();
			// 将本次应收账款为负数的排在最前
			for (SaleReconcilDetail detail : result.getResult())
			{
				if (detail.getMoney().compareTo(detail.getReceiveMoney()) == -1)
				{
					list.add(detail);
				}
			}
			// 将本次应收账款为正数的排在最后
			for (SaleReconcilDetail detail_ : result.getResult())
			{
				if (detail_.getMoney().compareTo(detail_.getReceiveMoney()) != -1)
				{
					list.add(detail_);
				}
			}
			map.put("detail", list);
      customerId = result.getResult().get(0).getMaster().getCustomerId();
		}
		else if (BillType.OEM_EC == billType) // 代工对账
		{
			SearchResult<OemReconcilDetail> result = serviceFactory.getReceiveService().findOemShouldReceive(queryParam);
			for (OemReconcilDetail detail : result.getResult())
			{
				detail.getMaster().setCustomerName(detail.getMaster().getCustomerName());
				detail.getMaster().setBillTypeText(detail.getMaster().getBillTypeText());
			}
			if (CollectionUtils.isEmpty(result.getResult()))
			{
				return "finance/receive/create";
			}
			List<OemReconcilDetail> list = Lists.newArrayList();
			// 将本次应收账款为负数的排在最前
			for (OemReconcilDetail detail : result.getResult())
			{
				if (detail.getMoney().compareTo(detail.getReceiveMoney()) == -1)
				{
					list.add(detail);
				}
			}
			// 将本次应收账款为正数的排在最后
			for (OemReconcilDetail detail_ : result.getResult())
			{
				if (detail_.getMoney().compareTo(detail_.getReceiveMoney()) != -1)
				{
					list.add(detail_);
				}
			}
			map.put("detail", list);
      customerId = result.getResult().get(0).getMaster().getCustomerId();
		}
		Customer customer = serviceFactory.getCustomerService().get(customerId);
		
		map.put("customer", customer);

		return "finance/receive/create";
	}

	/**
	 * <pre>
	 * 功能 - 保存收款单
	 * </pre>
	 * @param order
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:05:31, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("finance:receive:create")
	public AjaxResponseBody save(@RequestBody FinanceReceive order, ModelMap map)
	{
		if (Validate.validateObjectsNullOrEmpty(order))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		for (FinanceReceiveDetail detail : order.getDetailList())
		{
			if (null == detail.getSourceDetailId())
			{
				continue;
			}
			if (detail.getSourceBillType() == BillType.SALE_SK)
			{
				SaleReconcilDetail source = serviceFactory.getSaleReconcilService().getDetail(detail.getSourceDetailId());
				if (source.getReceiveMoney().abs().compareTo(source.getMoney().abs()) != -1)
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			} else if (detail.getSourceBillType()  == BillType.BEGIN_CUSTOMER)
			{
				CustomerBeginDetail source = serviceFactory.getCustomerBeginService().getDetail(detail.getSourceId()).get(0);
				if (source.getReceivedMoney().abs().compareTo(source.getReceiveMoney().abs()) != -1)
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			} else if (detail.getSourceBillType() == BillType.OEM_EC)
			{
				OemReconcilDetail source = serviceFactory.getOemReconcilService().getDetail(detail.getSourceDetailId());
				if (source.getReceiveMoney().abs().compareTo(source.getMoney().abs()) != -1)
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
		}
		serviceFactory.getReceiveService().save(order);
		return returnSuccessBody(order);
	}

	/**
	 * <pre>
	 * 页面 - 收款单查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:05:44, think
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("finance:receive:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		FinanceReceive order = serviceFactory.getReceiveService().get(id);
		map.put("order", order);
		return "finance/receive/view";
	}

	/**
	 * <pre>
	 * 功能 - 打印收款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:05:56, think
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("finance:receive:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		Map<String, Object> map = null;
		try
		{
			FinanceReceive order = serviceFactory.getReceiveService().get(id);
			map = ObjectUtils.objectToMap(order);
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());

			BigDecimal totalMoney = new BigDecimal(0);
			for (FinanceReceiveDetail detail : order.getDetailList())
			{
				totalMoney = detail.getMoney().add(totalMoney);
			}
			map.put("totalMoney", totalMoney);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * <pre>
	 * 功能 - 审核收款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:06:13, think
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("finance:receive:audit")
	public AjaxResponseBody audit(@PathVariable Long id)
	{
		if (serviceFactory.getReceiveService().audit(id, BoolValue.YES))
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
	 * 功能 - 反审核收款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:06:24, think
	 */
	@RequestMapping(value = "audit_cancel/{id}")
	@ResponseBody
	@RequiresPermissions("finance:receive:audit_cancel")
	public AjaxResponseBody auditCancel(@PathVariable Long id)
	{
		FinanceReceive order = serviceFactory.getReceiveService().get(id);
		if (order.getIsCancel() == BoolValue.YES)
		{// 作废状态下不能反审核
			return returnErrorBody("反审核失败：单据已作废，不能反审核");
		}
		if (serviceFactory.getReceiveService().audit(id, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("反审核失败：已被下游单据引用");
		}
	}

	/**
	 * <pre>
	 * 功能 - 审核所有收款单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:06:41, think
	 */
	@RequestMapping(value = "checkAll")
	@ResponseBody
	@RequiresPermissions("finance:receive:checkAll")
	public AjaxResponseBody checkAll()
	{
		if (serviceFactory.getReceiveService().checkAll())
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
	 * 功能 - 强制完工收款单
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:06:55, think
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("finance:receive:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? FinanceReceive.class : FinanceReceiveDetail.class;
		if (serviceFactory.getCommonService().forceComplete(cla, ids, BoolValue.YES))
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
	 * 功能 - 取消强制完工收款单
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:07:08, think
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("finance:receive:complete_cancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? FinanceReceive.class : FinanceReceiveDetail.class;
		if (serviceFactory.getCommonService().forceComplete(cla, ids, BoolValue.NO))
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
	 * 功能 - 作废收款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:07:25, think
	 */
	@RequestMapping(value = "cancel/{id}")
	@ResponseBody
	@RequiresPermissions("finance:receive:cancel")
	public AjaxResponseBody cancel(@PathVariable Long id)
	{
		if (serviceFactory.getReceiveService().cancel(id))
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
	 * 功能 - 反作废收款单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:07:36, think
	 */
	@RequestMapping(value = "cancel_back/{id}")
	@ResponseBody
	@RequiresPermissions("finance:receive:cancel_back")
	public AjaxResponseBody cancelBack(@PathVariable Long id)
	{
		FinanceReceive order = serviceFactory.getReceiveService().get(id);
		if (order.getIsCheck() == BoolValue.YES)
		{// 已审核状态下不能反作废
			return returnErrorBody("反作废失败：单据已审核，不能反作废");
		}
		if (serviceFactory.getReceiveService().cancelBack(id))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("反作废失败");
		}
	}

	/**
	 * <pre>
	 * 页面 - 收款单列表
	 * </pre>
	 * @param auditflag
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:07:46, think
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("finance:receive:list")
	public String list(String auditflag, ModelMap map)
	{
		map.put("auditflag", auditflag);
		return "finance/receive/list";
	}

	/**
	 * <pre>
	 * 数据 - 收款单列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:07:57, think
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<FinanceReceive> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<FinanceReceive> result = serviceFactory.getReceiveService().findByCondition(queryParam);
		FinanceReceive receive = new FinanceReceive();

		BigDecimal discount = new BigDecimal(0);
		BigDecimal advance = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);
		for (FinanceReceive _receive : result.getResult())
		{
			discount = discount.add(_receive.getDiscount());
			money = money.add(_receive.getMoney());
			advance = advance.add(_receive.getAdvance());
		}
		receive.setDiscount(discount);
		receive.setMoney(money);
		receive.setAdvance(advance);
		result.getResult().add(receive);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 收款单明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:08:22, think
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("finance:receive:list")
	public String detailList()
	{
		return "finance/receive/detail_list";
	}

	/**
	 * <pre>
	 * 数据 - 收款单明细列表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 下午2:08:34, think
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<FinanceReceiveDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<FinanceReceiveDetail> result = serviceFactory.getReceiveService().findDetailByCondition(queryParam);
		FinanceReceiveDetail detail = new FinanceReceiveDetail();

		BigDecimal sourceMoney = new BigDecimal(0);
		BigDecimal sourceBalanceMoney = new BigDecimal(0);
		BigDecimal money = new BigDecimal(0);
		for (FinanceReceiveDetail receiveDetail : result.getResult())
		{
			sourceMoney = sourceMoney.add(receiveDetail.getSourceMoney());
			money = money.add(receiveDetail.getMoney());
			sourceBalanceMoney = sourceBalanceMoney.add(receiveDetail.getSourceBalanceMoney());
		}
		FinanceReceive receive = new FinanceReceive();
		detail.setMaster(receive);
		detail.setMoney(money);
		detail.setSourceMoney(sourceMoney);
		detail.setSourceBalanceMoney(sourceBalanceMoney);
		result.getResult().add(detail);
		return result;
	}
	
	/**
	 * <pre>
	 * 查询应收款金额
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月22日 下午5:58:43, zhengby
	 */
	@RequestMapping(value = "findShoulRecMoney")
	@ResponseBody
	public SumBeginMoneyVo findShoulRecMoney(@RequestBody QueryParam queryParam)
	{
		SumBeginMoneyVo sumVO = new SumBeginMoneyVo();
		// 查询销售订单的应收款
		SearchResult<SaleReconcilDetail> sale = serviceFactory.getReceiveService().findSaleReconcilDetailList(queryParam);
		for (SaleReconcilDetail s : sale.getResult())
		{
			sumVO.setMoney(sumVO.getMoney().add(s.getMoney()).subtract(s.getReceiveMoney()));
			sumVO.setReceiveMoney(sumVO.getReceiveMoney().add(s.getReceiveMoney()));
		}
		// 查询代工订单的应收款
		SearchResult<OemReconcilDetail> oem = serviceFactory.getReceiveService().findOemReconcilDetailList(queryParam);
		for (OemReconcilDetail s : oem.getResult())
		{
			sumVO.setMoney(sumVO.getMoney().add(s.getMoney()).subtract(s.getReceiveMoney()));
			sumVO.setReceiveMoney(sumVO.getReceiveMoney().add(s.getReceiveMoney()));
		}
		return sumVO;
	}
}
