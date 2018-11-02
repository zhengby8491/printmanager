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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.persist.entity.finance.FinancePaymentDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceReceiveDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPayment;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffPaymentDetail;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceive;
import com.huayin.printmanager.persist.entity.finance.FinanceWriteoffReceiveDetail;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.WXSumQueryType;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.wx.util.WxUtil;
import com.huayin.printmanager.wx.vo.OutSourceCheckVo;
import com.huayin.printmanager.wx.vo.OutSourceDetailCheckVo;
import com.huayin.printmanager.wx.vo.PaymentCheckVo;
import com.huayin.printmanager.wx.vo.PaymentDetailCheckVo;
import com.huayin.printmanager.wx.vo.PurchCheckDetailVo;
import com.huayin.printmanager.wx.vo.PurchCheckVo;
import com.huayin.printmanager.wx.vo.QueryParam;
import com.huayin.printmanager.wx.vo.ReceiveCheckDetailVo;
import com.huayin.printmanager.wx.vo.ReceiveCheckVo;
import com.huayin.printmanager.wx.vo.SaleCheckDetailVo;
import com.huayin.printmanager.wx.vo.SaleCheckVo;
import com.huayin.printmanager.wx.vo.WriteoffPaymentCheckVo;
import com.huayin.printmanager.wx.vo.WriteoffPaymentDetailCheckVo;
import com.huayin.printmanager.wx.vo.WriteoffReceiveCheckDetailVo;
import com.huayin.printmanager.wx.vo.WriteoffReceiveCheckVo;

/**
 * <pre>
 * 微信 - 订单审核
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/wx/check")
public class WXCheckController extends BaseController
{
	/**
	 * <pre>
	 * 数据 - 销售订单审核列表
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月24日 下午7:12:20, think
	 */
	@RequestMapping(value = "saleCheckList")
	@ResponseBody
	public SearchResult<SaleCheckVo> saleCheckList(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));// 17302 1601003
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<SaleCheckVo> result = serviceFactory.getWXCheckService().findSaleByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 销售订单明细
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午8:57:23, think
	 */
	@RequestMapping(value = "saleCheckDetail")
	@ResponseBody
	public SearchResult<SaleCheckDetailVo> saleCheckDetail(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		return serviceFactory.getWXCheckService().findSaleDetailById(queryParam);
	}

	/**
	 * <pre>
	 * 功能 - 审核销售订单
	 * </pre>
	 * @param request
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午8:57:34, think
	 */
	@RequestMapping(value = "saleCheck")
	@ResponseBody
	public AjaxResponseBody saleCheck(HttpServletRequest request, Long id)
	{
		QueryParam queryParam = new QueryParam();
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		queryParam.setId(id);
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserId());
		if (user.getUserName().equals(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER)))
		{
			return returnSuccessBody();
		}
		if (serviceFactory.getWXCheckService().checkSale(queryParam))
		{
			return returnSuccessBody();
		}
		return returnErrorBody("审核失败");

	}

	/**
	 * <pre>
	 * 功能 - 批量审核销售订单
	 * </pre>
	 * @param request
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午8:57:54, think
	 */
	@RequestMapping(value = "saleCheckAll")
	@ResponseBody
	public AjaxResponseBody saleCheckAll(HttpServletRequest request, @RequestBody QueryParam queryParam)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserId());
		if (user.getUserName().equals(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER)))
		{
			return returnSuccessBody();
		}
		if (serviceFactory.getWXCheckService().checkSaleAll(queryParam))
		{
			return returnSuccessBody();
		}
		return returnErrorBody("审核失败");

	}

	/**
	 * <pre>
	 * 数据 - 采购订单审核
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午8:58:05, think
	 */
	@RequestMapping(value = "purchCheckList")
	@ResponseBody
	public SearchResult<PurchCheckVo> purchCheckList(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<PurchCheckVo> result = serviceFactory.getWXCheckService().findPurchByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 采购订单明细
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午8:58:52, think
	 */
	@RequestMapping(value = "purchCheckDetail")
	@ResponseBody
	public SearchResult<PurchCheckDetailVo> purchCheckDetail(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		return serviceFactory.getWXCheckService().findPurchDetailById(queryParam);
	}

	/**
	 * <pre>
	 * 功能 - 审核采购订单
	 * </pre>
	 * @param request
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午8:59:03, think
	 */
	@RequestMapping(value = "purchCheck")
	@ResponseBody
	public AjaxResponseBody purchCheck(HttpServletRequest request, Long id)
	{
		QueryParam queryParam = new QueryParam();
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		queryParam.setId(id);
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserId());
		if (user.getUserName().equals(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER)))
		{
			return returnSuccessBody();
		}
		if (serviceFactory.getWXCheckService().checkPurch(queryParam))
		{
			return returnSuccessBody();
		}
		return returnErrorBody("审核失败");

	}

	/**
	 * <pre>
	 * 功能 - 批量审核采购订单
	 * </pre>
	 * @param request
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午8:59:18, think
	 */
	@RequestMapping(value = "purchCheckAll")
	@ResponseBody
	public AjaxResponseBody PurchCheckAll(HttpServletRequest request, @RequestBody QueryParam queryParam)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserId());
		if (user.getUserName().equals(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER)))
		{
			return returnSuccessBody();
		}
		if (serviceFactory.getWXCheckService().checkPurchAll(queryParam))
		{
			return returnSuccessBody();
		}
		return returnErrorBody("审核失败");

	}

	/**
	 * <pre>
	 * 功能 - 发外加工单审核
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午8:59:28, think
	 */
	@RequestMapping(value = "outSourceCheckList")
	@ResponseBody
	public SearchResult<OutSourceCheckVo> outSourceCheckList(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<OutSourceCheckVo> result = serviceFactory.getWXCheckService().findOutSourceByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 发外加工单明细
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午8:59:40, think
	 */
	@RequestMapping(value = "outSourceCheckDetail")
	@ResponseBody
	public SearchResult<OutSourceDetailCheckVo> outSourceCheckDetail(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		return serviceFactory.getWXCheckService().findOutSourceDetailById(queryParam);
	}

	/**
	 * <pre>
	 * 功能 - 审核发外加工单
	 * </pre>
	 * @param request
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午8:59:52, think
	 */
	@RequestMapping(value = "outSourceCheck")
	@ResponseBody
	public AjaxResponseBody outSourceCheck(HttpServletRequest request, Long id)
	{
		QueryParam queryParam = new QueryParam();
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		queryParam.setId(id);
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserId());
		if (user.getUserName().equals(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER)))
		{
			return returnSuccessBody();
		}
		if (serviceFactory.getWXCheckService().checkOutSource(queryParam))
		{
			return returnSuccessBody();
		}
		return returnErrorBody("审核失败");

	}

	/**
	 * <pre>
	 * 功能 - 批量审核发外加工单
	 * </pre>
	 * @param request
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:00:03, think
	 */
	@RequestMapping(value = "outSourceCheckAll")
	@ResponseBody
	public AjaxResponseBody outSourceCheckAll(HttpServletRequest request, @RequestBody QueryParam queryParam)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserId());
		if (user.getUserName().equals(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER)))
		{
			return returnSuccessBody();
		}
		if (serviceFactory.getWXCheckService().checkOutSourceAll(queryParam))
		{
			return returnSuccessBody();
		}
		return returnErrorBody("审核失败");

	}

	/**
	 * <pre>
	 * 数据 - 收款单审核
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:00:21, think
	 */
	@RequestMapping(value = "receiveCheckList")
	@ResponseBody
	public SearchResult<ReceiveCheckVo> receiveCheckList(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<ReceiveCheckVo> result = serviceFactory.getWXCheckService().findReceiveByCondition(queryParam);
		for (ReceiveCheckVo receiveCheckVo : result.getResult())
		{
			Double days = DateUtils.getDistanceOfTwoDate(new Date(), receiveCheckVo.getBillTime());
			receiveCheckVo.setBillDay(days.intValue());
		}
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 收款单明细
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:00:32, think
	 */
	@RequestMapping(value = "receiveCheckDetail")
	@ResponseBody
	public SearchResult<ReceiveCheckDetailVo> receiveCheckDetail(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<FinanceReceiveDetail> resultReceive = serviceFactory.getWXCheckService().findReceiveDetailById(queryParam);
		List<ReceiveCheckDetailVo> resultList = new ArrayList<ReceiveCheckDetailVo>();
		for (FinanceReceiveDetail detail : resultReceive.getResult())
		{
			ReceiveCheckDetailVo vo = new ReceiveCheckDetailVo(detail.getId(), detail.getProductName(), detail.getSourceMoney(), detail.getSourceBalanceMoney(), detail.getMoney());
			resultList.add(vo);
		}
		SearchResult<ReceiveCheckDetailVo> result = new SearchResult<ReceiveCheckDetailVo>();
		result.setResult(resultList);
		result.setCount(resultReceive.getCount());
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 审核收款单
	 * </pre>
	 * @param request
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:00:44, think
	 */
	@RequestMapping(value = "receiveCheck")
	@ResponseBody
	public AjaxResponseBody receiveCheck(HttpServletRequest request, Long id)
	{
		QueryParam queryParam = new QueryParam();
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		queryParam.setId(id);
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserId());
		if (user.getUserName().equals(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER)))
		{
			return returnSuccessBody();
		}
		if (serviceFactory.getWXCheckService().checkReceive(queryParam))
		{
			return returnSuccessBody();
		}
		return returnErrorBody("审核失败");

	}

	/**
	 * <pre>
	 * 功能 - 批量审核收款单
	 * </pre>
	 * @param request
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:00:57, think
	 */
	@RequestMapping(value = "receiveCheckAll")
	@ResponseBody
	public AjaxResponseBody receiveCheckAll(HttpServletRequest request, @RequestBody QueryParam queryParam)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserId());
		if (user.getUserName().equals(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER)))
		{
			return returnSuccessBody();
		}
		if (serviceFactory.getWXCheckService().checkReceiveAll(queryParam))
		{
			return returnSuccessBody();
		}
		return returnErrorBody("审核失败");

	}

	/**
	 * <pre>
	 * 数据 - 付款单审核
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:01:08, think
	 */
	@RequestMapping(value = "paymentCheckList")
	@ResponseBody
	public SearchResult<PaymentCheckVo> paymentCheckList(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<PaymentCheckVo> result = serviceFactory.getWXCheckService().findPaymentByCondition(queryParam);
		for (PaymentCheckVo paymentCheckVo : result.getResult())
		{
			Double days = DateUtils.getDistanceOfTwoDate(new Date(), paymentCheckVo.getBillTime());
			paymentCheckVo.setBillDay(days.intValue());
		}
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 付款单明细
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:01:21, think
	 */
	@RequestMapping(value = "paymentCheckDetail")
	@ResponseBody
	public SearchResult<PaymentDetailCheckVo> paymentCheckDetail(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<FinancePaymentDetail> resultReceive = serviceFactory.getWXCheckService().findPaymentDetailByCondition(queryParam);
		List<PaymentDetailCheckVo> resultList = new ArrayList<PaymentDetailCheckVo>();
		for (FinancePaymentDetail detail : resultReceive.getResult())
		{
			PaymentDetailCheckVo vo = new PaymentDetailCheckVo(detail.getId(), null, detail.getStyle(), detail.getSourceMoney(), detail.getSourceBalanceMoney(), detail.getMoney());
			if (detail.getMaterialName() != null)
			{
				vo.setName(detail.getMaterialName());
			}
			else if (detail.getProcedureName() != null)
			{
				vo.setName(detail.getProcedureName());
			}
			else if (detail.getProductName() != null)
			{
				vo.setName(detail.getProductName());
			}
			resultList.add(vo);
		}
		SearchResult<PaymentDetailCheckVo> result = new SearchResult<PaymentDetailCheckVo>();
		result.setResult(resultList);
		result.setCount(resultReceive.getCount());
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 审核付款单
	 * </pre>
	 * @param request
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:01:34, think
	 */
	@RequestMapping(value = "paymentCheck")
	@ResponseBody
	public AjaxResponseBody paymentCheck(HttpServletRequest request, Long id)
	{
		QueryParam queryParam = new QueryParam();
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		queryParam.setId(id);
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserId());
		if (user.getUserName().equals(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER)))
		{
			return returnSuccessBody();
		}
		if (serviceFactory.getWXCheckService().checkPayment(queryParam))
		{
			return returnSuccessBody();
		}
		return returnErrorBody("审核失败");

	}

	/**
	 * <pre>
	 * 功能 - 批量审核付款单
	 * </pre>
	 * @param request
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:01:47, think
	 */
	@RequestMapping(value = "paymentCheckAll")
	@ResponseBody
	public AjaxResponseBody paymentCheckAll(HttpServletRequest request, @RequestBody QueryParam queryParam)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserId());
		if (user.getUserName().equals(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER)))
		{
			return returnSuccessBody();
		}
		if (serviceFactory.getWXCheckService().checkPaymentAll(queryParam))
		{
			return returnSuccessBody();
		}
		return returnErrorBody("审核失败");

	}

	/**
	 * <pre>
	 * 数据 - 收款核销单审核
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:02:02, think
	 */
	@RequestMapping(value = "writeoffReceiveCheckList")
	@ResponseBody
	public SearchResult<WriteoffReceiveCheckVo> writeoffReceiveCheckList(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<FinanceWriteoffReceive> resultReceive = serviceFactory.getWXCheckService().findWriteoffReceiveByCondition(queryParam);
		List<WriteoffReceiveCheckVo> resultList = new ArrayList<WriteoffReceiveCheckVo>();
		for (FinanceWriteoffReceive detail : resultReceive.getResult())
		{
			WriteoffReceiveCheckVo vo = new WriteoffReceiveCheckVo(detail.getId(), detail.getBillNo(), detail.getCustomerName(), detail.getMoney(), detail.getDiscount());
			resultList.add(vo);
		}
		SearchResult<WriteoffReceiveCheckVo> result = new SearchResult<WriteoffReceiveCheckVo>();
		result.setResult(resultList);
		result.setCount(resultReceive.getCount());
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 收款核销单明细
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:02:16, think
	 */
	@RequestMapping(value = "writeoffReceiveCheckDetail")
	@ResponseBody
	public SearchResult<WriteoffReceiveCheckDetailVo> writeoffReceiveCheckDetail(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<FinanceWriteoffReceiveDetail> resultReceive = serviceFactory.getWXCheckService().findWriteoffReceiveDetailByCondition(queryParam);
		List<WriteoffReceiveCheckDetailVo> resultList = new ArrayList<WriteoffReceiveCheckDetailVo>();
		for (FinanceWriteoffReceiveDetail detail : resultReceive.getResult())
		{
			WriteoffReceiveCheckDetailVo vo = new WriteoffReceiveCheckDetailVo(detail.getId(), detail.getProductName(), detail.getSourceMoney(), detail.getSourceBalanceMoney(), detail.getMoney());
			resultList.add(vo);
		}
		SearchResult<WriteoffReceiveCheckDetailVo> result = new SearchResult<WriteoffReceiveCheckDetailVo>();
		result.setResult(resultList);
		result.setCount(resultReceive.getCount());
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 审核收款核销单
	 * </pre>
	 * @param request
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:02:30, think
	 */
	@RequestMapping(value = "writeoffReceiveCheck")
	@ResponseBody
	public AjaxResponseBody writeoffReceiveCheck(HttpServletRequest request, Long id)
	{
		QueryParam queryParam = new QueryParam();
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		queryParam.setId(id);
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserId());
		if (user.getUserName().equals(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER)))
		{
			return returnSuccessBody();
		}
		if (serviceFactory.getWXCheckService().checkWriteoffReceive(queryParam))
		{
			return returnSuccessBody();
		}
		return returnErrorBody("审核失败");

	}

	/**
	 * <pre>
	 * 功能  - 批量审核收款核销单
	 * </pre>
	 * @param request
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:02:40, think
	 */
	@RequestMapping(value = "writeoffReceiveCheckAll")
	@ResponseBody
	public AjaxResponseBody writeoffReceiveCheckAll(HttpServletRequest request, @RequestBody QueryParam queryParam)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserId());
		if (user.getUserName().equals(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER)))
		{
			return returnSuccessBody();
		}
		if (serviceFactory.getWXCheckService().checkWriteoffReceiveAll(queryParam))
		{
			return returnSuccessBody();
		}
		return returnErrorBody("审核失败");

	}

	/**
	 * <pre>
	 * 数据 - 付款核销单审核
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:02:54, think
	 */
	@RequestMapping(value = "writeoffPaymentCheckList")
	@ResponseBody
	public SearchResult<WriteoffPaymentCheckVo> writeoffPaymentCheckList(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<FinanceWriteoffPayment> resultReceive = serviceFactory.getWXCheckService().findWriteoffPaymentByCondition(queryParam);
		List<WriteoffPaymentCheckVo> resultList = new ArrayList<WriteoffPaymentCheckVo>();
		for (FinanceWriteoffPayment detail : resultReceive.getResult())
		{
			WriteoffPaymentCheckVo vo = new WriteoffPaymentCheckVo(detail.getId(), detail.getBillNo(), detail.getSupplierName(), detail.getMoney(), detail.getDiscount());
			resultList.add(vo);
		}
		SearchResult<WriteoffPaymentCheckVo> result = new SearchResult<WriteoffPaymentCheckVo>();
		result.setResult(resultList);
		result.setCount(resultReceive.getCount());
		return result;
	}

	/**
	 * <pre>
	 * 数据 - 付款核销单明细
	 * </pre>
	 * @param queryParam
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:03:07, think
	 */
	@RequestMapping(value = "writeoffPaymentCheckDetail")
	@ResponseBody
	public SearchResult<WriteoffPaymentDetailCheckVo> writeoffPaymentCheckDetail(@RequestBody QueryParam queryParam, HttpServletRequest request)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		SearchResult<FinanceWriteoffPaymentDetail> resultReceive = serviceFactory.getWXCheckService().findWriteoffPaymentDetailByCondition(queryParam);
		List<WriteoffPaymentDetailCheckVo> resultList = new ArrayList<WriteoffPaymentDetailCheckVo>();
		for (FinanceWriteoffPaymentDetail detail : resultReceive.getResult())
		{
			WriteoffPaymentDetailCheckVo vo = new WriteoffPaymentDetailCheckVo(detail.getId(), null, detail.getStyle(), detail.getSourceMoney(), detail.getSourceBalanceMoney(), detail.getMoney());
			if (detail.getMaterialName() != null)
			{
				vo.setName(detail.getMaterialName());
			}
			else if (detail.getProcedureName() != null)
			{
				vo.setName(detail.getProcedureName());
			}
			else if (detail.getProductName() != null)
			{
				vo.setName(detail.getProductName());
			}
			resultList.add(vo);
		}
		SearchResult<WriteoffPaymentDetailCheckVo> result = new SearchResult<WriteoffPaymentDetailCheckVo>();
		result.setResult(resultList);
		result.setCount(resultReceive.getCount());
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 审核付款核销单
	 * </pre>
	 * @param request
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:03:20, think
	 */
	@RequestMapping(value = "writeoffPaymentCheck")
	@ResponseBody
	public AjaxResponseBody writeoffPaymentCheck(HttpServletRequest request, Long id)
	{
		QueryParam queryParam = new QueryParam();
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		queryParam.setId(id);
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserId());
		if (user.getUserName().equals(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER)))
		{
			return returnSuccessBody();
		}
		if (serviceFactory.getWXCheckService().checkWriteoffPayment(queryParam))
		{
			return returnSuccessBody();
		}
		return returnErrorBody("审核失败");

	}

	/**
	 * <pre>
	 * 功能 - 批量审核付款核销单
	 * </pre>
	 * @param request
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:03:33, think
	 */
	@RequestMapping(value = "writeoffPaymentCheckAll")
	@ResponseBody
	public AjaxResponseBody writeoffPaymentCheckAll(HttpServletRequest request, @RequestBody QueryParam queryParam)
	{
		queryParam.setUserId(WxUtil.getUserId(request));
		queryParam.setCompanyId(WxUtil.getCompanyId(request));
		User user = serviceFactory.getWXBasicService().getUser(queryParam.getUserId());
		if (user.getUserName().equals(SystemConfigUtil.getConfig(SysConstants.WX_WXPERIENCE_USER)))
		{
			return returnSuccessBody();
		}
		if (serviceFactory.getWXCheckService().checkWriteoffPaymentAll(queryParam))
		{
			return returnSuccessBody();
		}
		return returnErrorBody("审核失败");

	}

	/**
	 * <pre>
	 * 页面 - 销售订单审核
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:03:46, think
	 */
	@RequestMapping(value = "view/saleCheckList")
	public String saleCheckListView(HttpServletRequest request, ModelMap map)
	{
		return "wx/check/sale_order_check";
	}

	/**
	 * <pre>
	 * 页面 - 采购订单审核
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:03:57, think
	 */
	@RequestMapping(value = "view/purchCheckList")
	public String purchCheckListView(HttpServletRequest request, ModelMap map)
	{
		return "wx/check/purch_order_check";
	}

	/**
	 * <pre>
	 * 页面 - 发外加工审核
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:04:08, think
	 */
	@RequestMapping(value = "view/outSourceCheckList")
	public String outSourceCheckListView(HttpServletRequest request, ModelMap map)
	{
		return "wx/check/outsource_process_check";
	}

	/**
	 * <pre>
	 * 跳转付款单审核
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "view/paymentCheckList")
	public String paymentCheckListView(HttpServletRequest request, ModelMap map)
	{
		return "wx/check/payment_check";
	}

	/**
	 * <pre>
	 * 页面 - 收款单审核
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:04:29, think
	 */
	@RequestMapping(value = "view/receiveCheckList")
	public String receiveCheckListView(HttpServletRequest request, ModelMap map)
	{
		return "wx/check/receive_check";
	}

	/**
	 * <pre>
	 * 页面 - 核销付款单
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:04:48, think
	 */
	@RequestMapping(value = "view/writeoffPaymentCheckList")
	public String writeoffPaymentCheckListView(HttpServletRequest request, ModelMap map)
	{
		return "wx/check/writeoff_payment_check";
	}

	/**
	 * <pre>
	 * 页面 - 核销收款单
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:04:59, think
	 */
	@RequestMapping(value = "view/writeoffReceiveCheckList")
	public String writeoffReceiveCheckListView(HttpServletRequest request, ModelMap map)
	{
		return "wx/check/writeoff_receive_check";
	}

	/**
	 * <pre>
	 * 页面 - 审核首页
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:05:09, think
	 */
	@RequestMapping(value = "view/index")
	public String indexView(HttpServletRequest request, ModelMap map, Long id, WXSumQueryType type)
	{
		return "wx/check/index";
	}
}
