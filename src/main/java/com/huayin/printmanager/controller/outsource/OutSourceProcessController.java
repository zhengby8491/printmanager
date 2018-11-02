/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月23日 下午2:34:06
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.outsource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.JsonUtils;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.basic.SupplierAddress;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.produce.WorkProcedure;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.OutSourceType;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.WorkProcedureType;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.service.vo.SumVo;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 发外管理 - 发外加工
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年7月1日, zhaojt
 * @version 	   2.0, 2018年2月23日下午4:14:40, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/outsource/process")
public class OutSourceProcessController extends BaseController
{

	/**
	 * <pre>
	 * 页面 - 发外加工单创建
	 * </pre>
	 * @param type
	 * @param ids
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:16:27, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("outsource:process:create")
	public String create(OutSourceType type, Long[] ids, ModelMap map)
	{
		List<OutSourceProcessDetail> list = new ArrayList<OutSourceProcessDetail>();
		if (Validate.validateObjectsNullOrEmpty(type, ids))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		if (type == OutSourceType.PROCESS)
		{// 工序发外
			// 查询所有工单WorkPart2Product，并设置到发外加工明细中
			Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
			for (Long id : ids)
			{
				WorkProcedure procedure = serviceFactory.getWorkService().getProcedure(id);
				OutSourceProcessDetail detail = new OutSourceProcessDetail();
				if (procedure.getOutOfQty().compareTo(procedure.getInputQty()) != -1)
				{
					continue;
				}
				detail.setType(type);
				detail.setSourceBillType(procedure.getWork().getBillType());
				detail.setSourceQty(procedure.getInputQty());// 源单数=投入数
				detail.setSourceBillNo(procedure.getWorkBillNo());
				detail.setSourceId(procedure.getWorkId());
				detail.setSourceDetailId(procedure.getId());
				detail.setWorkBillNo(procedure.getWorkBillNo());
				detail.setWorkId(procedure.getWorkId());
				detail.setProductName(null);
				detail.setProductId(null);
				detail.setProcedureId(procedure.getProcedureId());
				detail.setProcedureCode(procedure.getProcedureCode());
				detail.setProcedureClassId(procedure.getProcedureClassId());
				detail.setProcedureName(procedure.getProcedureName());
				detail.setProcedureType(procedure.getProcedureType());
				detail.setWorkProcedureType(procedure.getWorkProcedureType());

				// 优先取部件上机规格
				if (procedure.getWorkProcedureType() == WorkProcedureType.PART)
				{
					detail.setStyle(procedure.getWorkPart().getStyle());
					detail.setPartName(procedure.getWorkPart().getPartName());
					detail.setProduceNum(procedure.getWorkPart().getQty());
				}
				else
				{
					detail.setStyle(serviceFactory.getWorkService().getWorkProduct(procedure.getWorkPack().getMasterId()).get(0).getStyle());
					detail.setProduceNum(procedure.getInputQty().intValue());
				}
				detail.setQty(procedure.getInputQty().subtract(procedure.getOutOfQty()));
				// 数量=部件数量
				detail.setPrice(new BigDecimal(0));
				detail.setMoney(new BigDecimal(0));
				detail.setNoTaxPrice(new BigDecimal(0));
				detail.setNoTaxMoney(new BigDecimal(0));
				detail.setMemo(procedure.getMemo());
				detail.setProductType(procedure.getWork().getProductType()); // 针对轮转工单
				// 源单类型，发外类型，源单号，客户名称，成品名称，部件名称
				// 工序名称，加工规格（可改），工序加工要求（可改），加工数量（可改）
				// 加工单价（可改），加工金额，生产数量，税率，税额，备注（可改）
				// 获取工单所有信息（主要是工序和材料）
				//_appendProceduresMaterials(detail);
				long workId = procedure.getWorkId();
				List<WorkProduct> list2 = productMap.get(workId);
				if (null != list)
				{
					detail.setProductList(list2);
				}

				list.add(detail);
			}

		}
		else if (type == OutSourceType.PRODUCT)
		{// 整单发外
			for (Long id : ids)
			{
				WorkProduct product = serviceFactory.getWorkService().getProduct(id);
				OutSourceProcessDetail detail = new OutSourceProcessDetail();
				if (product.getOutOfQty() >= product.getProduceQty())
				{
					continue;
				}
				detail.setType(type);
				detail.setSourceBillType(product.getMaster().getBillType());
				detail.setSourceQty(new BigDecimal(product.getProduceQty()));// 源单数=生产数量
				detail.setSourceBillNo(product.getMaster().getBillNo());
				detail.setSourceId(product.getMaster().getId());
				detail.setSourceDetailId(product.getId());
				detail.setWorkBillNo(product.getMaster().getBillNo());
				detail.setWorkId(product.getMaster().getId());
				detail.setProductId(product.getProductId());
				detail.setProductName(product.getProductName());
				detail.setStyle(product.getStyle());

				// 工序加工要求
				detail.setProcessRequire(product.getCustomerRequire());
				detail.setQty(new BigDecimal(product.getProduceQty() - product.getOutOfQty()));
				// 生产数量
				detail.setProduceNum(product.getProduceQty());
				detail.setPrice(new BigDecimal(0));
				detail.setMoney(new BigDecimal(0));
				detail.setNoTaxPrice(new BigDecimal(0));
				detail.setNoTaxMoney(new BigDecimal(0));
				// 获取工单所有信息（主要是工序和材料）
				//_appendProceduresMaterials(detail);
				detail.setMemo(product.getMemo());
				// 源单类型，发外类型，源单号，客户名称，成品名称，部件名称
				// 工序名称，加工规格（可改），工序加工要求（可改），加工数量（可改）
				// 加工单价（可改），加工金额，生产数量，税率，税额，备注（可改）

				list.add(detail);
			}
		}

		map.put("type", type);
		map.put("list", list);
		return "outsource/process/create";
	}

	/**
	 * <pre>
	 * 功能 - 保存发外加工单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:17:00, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("outsource:process:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "发外加工", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody OutSourceProcess order, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(order))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		// 保存之前验证是否已发外完，前端的已校验过，后台再校验一次，防止保存重复发外单
		for (OutSourceProcessDetail detail : order.getDetailList())
		{
			if (null == detail.getSourceDetailId())
			{
				continue;
			}
			if (detail.getType() == OutSourceType.PROCESS)
			{
				WorkProcedure source = serviceFactory.getWorkService().getProcedure(detail.getSourceDetailId());
				if (source.getOutOfQty().compareTo(source.getInputQty()) != -1)
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
			if (detail.getType() == OutSourceType.PRODUCT)
			{
				WorkProduct source = serviceFactory.getWorkService().getProduct(detail.getSourceDetailId());
				if (source.getOutOfQty() >= source.getProduceQty())
				{
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
		}
		try
		{
			serviceFactory.getOutSourceProcessService().save(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(order);
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			logger.error(ex.getMessage(), "OutSourceProcess:" + JsonUtils.toJson(order));
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 页面 - 发外加工单查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:17:18, zhengby
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("outsource:process:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		OutSourceProcess order = serviceFactory.getOutSourceProcessService().get(id);
		// 查询所有工单WorkPart2Product，并设置到发外加工明细中
		if (order.getType() == OutSourceType.PROCESS)
		{
			Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
			for (OutSourceProcessDetail detail : order.getDetailList())
			{
				if (null != detail.getWorkId())
				{
					long workId = detail.getWorkId();
					List<WorkProduct> list = productMap.get(workId);
					if (null != list)
					{
						detail.setProductList(list);
						// 因为页面实用json格式，所以必须给字段设置数据，否则json数据是不存在的
						detail.setProductNames(detail.getProductNames());
					}
				}
			}
		}

		// 获取工单所有信息（主要是工序和材料）
		// for (OutSourceProcessDetail detail : order.getDetailList())
		// {
		// _appendProceduresMaterials(detail);
		// }
		map.put("order", order);
		return "outsource/process/view";
	}

	/**
	 * <pre>
	 * 功能 - 根据发外加工单号查询加工单
	 * </pre>
	 * @param request
	 * @param map
	 * @param billNo
	 * @return
	 * @since 1.0, 2017年12月29日 下午3:00:14, zhengby
	 */
	@RequestMapping(value = "toView/{billNo}")
	@RequiresPermissions("outsource:process:list")
	public String toView(HttpServletRequest request, ModelMap map, @PathVariable String billNo)
	{
		OutSourceProcess order = serviceFactory.getOutSourceProcessService().get(billNo);
		map.put("id", order.getId());
		return view(request, map, order.getId());
	}

	/**
	 * <pre>
	 * 功能 - 追加工单工序和材料信息
	 * 注意：只能在创建的时候调用
	 * </pre>
	 * @param detail
	 * @since 1.0, 2018年2月23日 下午4:18:16, zhengby
	 */
	/*private void _appendProceduresMaterials(OutSourceProcessDetail detail)
	{
		// 获取工单所有信息（主要是工序和材料）
		Work work = serviceFactory.getWorkService().get(detail.getWorkId());
		if (null != work)
		{
			StringBuilder workProcedures = new StringBuilder();// 追加工序
			StringBuilder workMaterials = new StringBuilder();// 追加材料
			for (WorkPart workPart : work.getPartList())
			{
				if (workPart.getProcedureList().size() > 0)
				{
					workProcedures.append(workPart.getPartName()).append("：");
					int i = 1, len = workPart.getProcedureList().size();
					for (WorkProcedure workProcedure : workPart.getProcedureList())
					{
						workProcedures.append(workProcedure.getProcedureName());
						if (i != len)
						{
							workProcedures.append("，");
						}
						i++;
					}

					workProcedures.append("；");
				}

				if (workPart.getMaterialList().size() > 0)
				{
					workMaterials.append(workPart.getPartName()).append("：");
					int i = 1, len = workPart.getMaterialList().size();
					for (WorkMaterial workMaterial : workPart.getMaterialList())
					{
						workMaterials.append(workMaterial.getMaterialName());
						if (i != len)
						{
							workMaterials.append("，");
						}
						i++;
					}

					workMaterials.append("；");
				}
			}
			if (null != work.getPack() && (work.getPack().getProcedureList().size() > 0 || work.getPack().getMaterialList().size() > 0))
			{
				if (work.getPack().getProcedureList().size() > 0)
				{
					workProcedures.append("成品").append("：");
					int i = 1, len = work.getPack().getProcedureList().size();
					for (WorkProcedure workProcedure : work.getPack().getProcedureList())
					{
						workProcedures.append(workProcedure.getProcedureName());
						if (i != len)
						{
							workProcedures.append("，");
						}
						i++;
					}

					workProcedures.append("；");
				}

				if (work.getPack().getMaterialList().size() > 0)
				{
					workMaterials.append("成品").append("：");
					int i = 1, len = work.getPack().getMaterialList().size();
					for (WorkMaterial workMaterial : work.getPack().getMaterialList())
					{
						workMaterials.append(workMaterial.getMaterialName());
						if (i != len)
						{
							workMaterials.append("，");
						}
						i++;
					}

					workMaterials.append("；");
				}
			}

			detail.setWorkProcedures(StringUtils.removeEnd(workProcedures.toString(), "；"));
			detail.setWorkMaterials(StringUtils.removeEnd(workMaterials.toString(), "；"));
		}
	}*/
	
	/**
	 * <pre>
	 * 功能 - 追加工单工序和材料信息
	 * </pre>
	 * @param detail
	 * @since 1.0, 2018年10月8日 上午11:07:16, zhengxchn@163.com
	 */
	@RequestMapping(value = "appendProceduresMaterials")
	@ResponseBody
	public List<OutSourceProcessDetail> appendProceduresMaterials(@RequestBody List<OutSourceProcessDetail> detailList)
	{
		for (OutSourceProcessDetail detail : detailList)
		{
			// 设置工艺信息和材料信息
			serviceFactory.getWorkService().setProceduresMaterials(detail);
		}	
		return detailList;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到发外加工单打印页面
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:18:58, zhengby
	 */
	@RequestMapping(value = "print/{id}")
	@RequiresPermissions("outsource:process:print")
	public String print(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		OutSourceProcess order = serviceFactory.getOutSourceProcessService().get(id);
		map.put("order", order);
		map.put("printDate", new Date());
		return "outsource/process/print";
	}

	/**
	 * <pre>
	 * 功能 - 返回发外加工单打印数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:19:36, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:process:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{

		Map<String, Object> map = null;
		try
		{
			OutSourceProcess order = serviceFactory.getOutSourceProcessService().get(id);
			if (order.getType() == OutSourceType.PROCESS)
			{
				Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
				for (OutSourceProcessDetail detail : order.getDetailList())
				{
					if (null != detail.getWorkId())
					{
						long workId = detail.getWorkId();
						List<WorkProduct> list = productMap.get(workId);
						if (null != list)
						{
							detail.setProductList(list);
							// 因为页面实用json格式，所以必须给字段设置数据，否则json数据是不存在的
							detail.setProductName(detail.getProductNames());
						}
					}
				}
			}
			// for (OutSourceProcessDetail detail : order.getDetailList())
			// {
			// // 获取工单所有信息（主要是工序和材料）
			// _appendProceduresMaterials(detail);
			// }
			map = ObjectUtils.objectToMap(order);
			map.put("printDate", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
			map.put("companyName", UserUtils.getCompany().getName());
			map.put("companyAddress", UserUtils.getCompany().getAddress());
			map.put("companyFax", UserUtils.getCompany().getFax());
			map.put("companyLinkName", UserUtils.getCompany().getLinkName());
			map.put("companyTel", UserUtils.getCompany().getTel());
			map.put("companyEmail", UserUtils.getCompany().getEmail());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * <pre>
	 * 页面 - 发外加工单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:19:58, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("outsource:process:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		OutSourceProcess order = serviceFactory.getOutSourceProcessService().get(id);

		// 查询所有工单WorkPart2Product，并设置到发外加工明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (OutSourceProcessDetail detail : order.getDetailList())
		{
			if (null != detail.getWorkId())
			{
				long workId = detail.getWorkId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					detail.setProductList(list);
				}
			}
		}

		// 获取工单所有信息（主要是工序和材料）
		// for (OutSourceProcessDetail detail : order.getDetailList())
		// {
		// // 获取工单所有信息（主要是工序和材料）
		// _appendProceduresMaterials(detail);
		// }
		// 供应商地址
		List<SupplierAddress> supplierAddressList = serviceFactory.getSupplierService().getAddressList(order.getSupplierId());
		map.put("order", order);
		map.put("supplierAddressList", supplierAddressList);
		return "outsource/process/edit";
	}

	/**
	 * <pre>
	 * 功能 - 更新发外加工单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:20:33, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("outsource:process:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "发外加工", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody OutSourceProcess order, HttpServletRequest request)
	{
		try
		{
			serviceFactory.getOutSourceProcessService().update(order);
			// 更新旧数据
			OutSourceProcess new_order = serviceFactory.getOutSourceProcessService().get(order.getId());
			request.setAttribute(SystemLogAspect.BILLNO, new_order.getBillNo());
			return returnSuccessBody(new_order);
		}
		catch (BusinessException ex)
		{
			throw ex;
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 功能 - 审核发外加工单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:20:56, zhengby
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:process:audit")
	public AjaxResponseBody audit(@PathVariable Long id)
	{
		if (serviceFactory.getCommonService().audit(BillType.OUTSOURCE_OP, id, BoolValue.YES))
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
	 * 功能 - 反审核发外加工单
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:21:20, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "audit_cancel/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:process:audit_cancel")
	public AjaxResponseBody auditCancel(@PathVariable Long id)
	{
		// 是否代工平台引用
		List<OutSourceProcessDetail> detailList = serviceFactory.getOutSourceProcessService().getDetailList(id);
		List<Long> ids = Lists.newArrayList();
		for (OutSourceProcessDetail detail : detailList)
		{
			ids.add(detail.getId());
		}
		QueryParam queryParam = new QueryParam();
		queryParam.setIds(ids);
		if (serviceFactory.getZeroService().isOemOrderUse(queryParam))
		{
			Map<String, List<String>> map = Maps.newHashMap();
			List<String> ss = Lists.newArrayList();
			ss.add("oem");
			map.put("oem", ss);
			return returnErrorBody(map);
		}

		// 内部引用
		if (serviceFactory.getCommonService().audit(BillType.OUTSOURCE_OP, id, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(BillType.OUTSOURCE_OP, id);
			return returnErrorBody(map);
		}
	}

	/**
	 * <pre>
	 * 功能 - 审核所有发外加工单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:21:41, zhengby
	 */
	@RequestMapping(value = "checkAll")
	@ResponseBody
	@RequiresPermissions("outsource:process:checkAll")
	public AjaxResponseBody checkAll()
	{
		if (serviceFactory.getOutSourceProcessService().checkAll())
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
	 * 功能 - 强制完工发外加工单
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:22:07, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("outsource:process:complete")
	public AjaxResponseBody complete(@RequestParam("ids[]") Long[] ids)
	{
		if (serviceFactory.getCommonService().forceComplete(OutSourceProcess.class, ids, BoolValue.YES))
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
	 * 功能 - 取消强制完工发外加工单
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:22:36, zhengby
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("outsource:process:complete_cancel")
	public AjaxResponseBody completeCancel(@RequestParam("ids[]") Long[] ids)
	{
		if (serviceFactory.getCommonService().forceComplete(OutSourceProcess.class, ids, BoolValue.NO))
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
	 * 功能 - 删除发外加工单
	 * </pre>
	 * @param id
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:23:08, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("outsource:process:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "发外加工", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		try
		{

			OutSourceProcess order = serviceFactory.getOutSourceProcessService().get(id);
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getOutSourceProcessService().delete(id);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody();
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 页面 - 跳转到发外加工单列表查看
	 * </pre>
	 * @param auditflag
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:23:30, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("outsource:process:list")
	public String list(String auditflag, ModelMap map)
	{
		map.put("auditflag", auditflag);
		return "outsource/process/list";
	}

	/**
	 * <pre>
	 * 功能 - 返回发外加工单列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:24:20, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<OutSourceProcess> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OutSourceProcess> result = serviceFactory.getOutSourceProcessService().findByCondition(queryParam);
		OutSourceProcess process = new OutSourceProcess();
		BigDecimal totalMoney = new BigDecimal(0);
		BigDecimal totalTax = new BigDecimal(0);
		BigDecimal noTaxTotalMoney = new BigDecimal(0);
		for (OutSourceProcess outSourceProcess : result.getResult())
		{
			totalMoney = totalMoney.add(outSourceProcess.getTotalMoney());
			totalTax = totalTax.add(outSourceProcess.getTotalTax());
			noTaxTotalMoney = noTaxTotalMoney.add(outSourceProcess.getNoTaxTotalMoney());
		}
		process.setTotalMoney(totalMoney);
		process.setTotalTax(totalTax);
		process.setNoTaxTotalMoney(noTaxTotalMoney);
		process.setIsCheck(null);
		result.getResult().add(process);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到发外加工明细列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:24:52, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("outsource:process_detail:list")
	public String detailList()
	{
		return "outsource/process/detail_list";
	}

	/**
	 * <pre>
	 * 页面 - 跳转发外加工单进度表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:26:11, zhengby
	 */
	@RequestMapping(value = "detailFlowList")
	@RequiresPermissions("outsource:process_detail:list")
	public String detailFlowList()
	{
		return "outsource/process/detail_flow_list";
	}

	/**
	 * <pre>
	 * 功能 - 返回发外加工明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:26:36, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<OutSourceProcessDetail> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<OutSourceProcessDetail> result = serviceFactory.getOutSourceProcessService().findDetailByCondition(queryParam);

		// 查询所有工单WorkPart2Product，并设置到发外加工明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (OutSourceProcessDetail detail : result.getResult())
		{
			if (null != detail.getWorkId())
			{
				long workId = detail.getWorkId();
				List<WorkProduct> list = productMap.get(workId);
				if (null != list)
				{
					detail.setProductList(list);
					// 因为页面实用json格式，所以必须给字段设置数据，否则json数据是不存在的
					detail.setProductNames(detail.getProductNames());
				}
			}
		}

		OutSourceProcessDetail detail = new OutSourceProcessDetail();
		BigDecimal money = new BigDecimal(0);
		BigDecimal tax = new BigDecimal(0);
		BigDecimal noTaxMoney = new BigDecimal(0);
		BigDecimal arriveQty = new BigDecimal(0);
		BigDecimal price = new BigDecimal(0);
		BigDecimal totalQty = new BigDecimal(0);
		for (OutSourceProcessDetail outSourceProcessDetail : result.getResult())
		{
			money = money.add(outSourceProcessDetail.getMoney());
			tax = tax.add(outSourceProcessDetail.getTax());
			noTaxMoney = noTaxMoney.add(outSourceProcessDetail.getNoTaxMoney());
			totalQty = totalQty.add(outSourceProcessDetail.getQty());
			arriveQty = arriveQty.add(outSourceProcessDetail.getArriveQty());
			price = price.add(outSourceProcessDetail.getPrice());

			outSourceProcessDetail.setUnarriveQty(outSourceProcessDetail.getArriveQty());
			outSourceProcessDetail.setUnpayment(outSourceProcessDetail.getPayment());
			outSourceProcessDetail.setUnreconcilQty(outSourceProcessDetail.getReconcilQty());
		}
		OutSourceProcess process = new OutSourceProcess();
		detail.setMaster(process);
		detail.setMoney(money);
		detail.setArriveQty(arriveQty);
		detail.setPrice(price);
		detail.setTax(tax);
		detail.setNoTaxMoney(noTaxMoney);
		detail.setQty(totalQty);

		result.getResult().add(detail);

		return result;
	}

	/**
	 * <pre>
	 * 功能 - 获取发外加工单创建日期列表
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:27:08, zhengby
	 */
	@RequestMapping(value = "getYearsFromOutSourceProcess")
	@ResponseBody
	public List<OutSourceProcess> getYearsFromOutSourceProcess()
	{
		List<OutSourceProcess> list = serviceFactory.getOutSourceProcessService().getYearsFromOutSourceProcess();
		return list;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到发外汇总表(按供应商)页面
	 * </pre>
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:27:30, zhengby
	 */
	@RequestMapping(value = "collect_supplier_list/{type}")
	@RequiresPermissions("outsource:sum:collect_supplier")
	public String collectSupplierList(@PathVariable String type)
	{
		return "outsource/process/collect/supplier_" + type + "_list";
	}

	/**
	 * <pre>
	 * 页面 - 跳转到发外汇总表(按工序)页面
	 * </pre>
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:27:59, zhengby
	 */
	@RequestMapping(value = "collect_procedure_list/{type}")
	@RequiresPermissions("outsource:sum:collect_procedure")
	public String collectProcedureList(@PathVariable String type)
	{
		return "outsource/process/collect/procedure_" + type + "_list";
	}

	/**
	 * <pre>
	 * 页面 - 跳转到发外汇总表(按发外类型)页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:28:23, zhengby
	 */
	@RequestMapping(value = "collect_outsource_type_list")
	@RequiresPermissions("outsource:sum:collect_outsource")
	public String collectOutsourceTypeList()
	{
		return "outsource/process/collect/outsource_type_list";
	}

	/**
	 * <pre>
	 * 功能 - 返回发外汇总表(按供应商)列表数据
	 * </pre>
	 * @param queryParam
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:28:42, zhengby
	 */
	@RequestMapping(value = "sumOutsourceBySupplier/{type}")
	@ResponseBody
	public SearchResult<SumVo> sumOutsourceBySupplier(@RequestBody QueryParam queryParam, @PathVariable String type)
	{
		SearchResult<SumVo> result = serviceFactory.getOutSourceProcessService().sumOutsourceBySupplier(queryParam, type);
		SumVo vo = new SumVo();
		BigDecimal sumMoney = new BigDecimal(0);
		BigDecimal january = new BigDecimal(0);
		BigDecimal february = new BigDecimal(0);
		BigDecimal march = new BigDecimal(0);
		BigDecimal april = new BigDecimal(0);
		BigDecimal may = new BigDecimal(0);
		BigDecimal june = new BigDecimal(0);
		BigDecimal july = new BigDecimal(0);
		BigDecimal august = new BigDecimal(0);
		BigDecimal september = new BigDecimal(0);
		BigDecimal october = new BigDecimal(0);
		BigDecimal november = new BigDecimal(0);
		BigDecimal december = new BigDecimal(0);
		for (SumVo sumVo : result.getResult())
		{
			sumMoney = sumMoney.add(sumVo.getSumMoney());
			january = january.add(sumVo.getJanuary());
			february = february.add(sumVo.getFebruary());
			march = march.add(sumVo.getMarch());
			april = april.add(sumVo.getApril());
			may = may.add(sumVo.getMay());
			june = june.add(sumVo.getJune());
			july = july.add(sumVo.getJuly());
			august = august.add(sumVo.getAugust());
			september = september.add(sumVo.getSeptember());
			october = october.add(sumVo.getOctober());
			november = november.add(sumVo.getNovember());
			december = december.add(sumVo.getDecember());
		}
		vo.setSumMoney(sumMoney);
		vo.setJanuary(january);
		vo.setFebruary(february);
		vo.setMarch(march);
		vo.setApril(april);
		vo.setMay(may);
		vo.setJune(june);
		vo.setJuly(july);
		vo.setAugust(august);
		vo.setSeptember(september);
		vo.setOctober(october);
		vo.setNovember(november);
		vo.setDecember(december);
		result.getResult().add(vo);
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 返回发外汇总表(按工序)列表数据
	 * </pre>
	 * @param queryParam
	 * @param type
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:29:17, zhengby
	 */
	@RequestMapping(value = "sumOutsourceByProcedure/{type}")
	@ResponseBody
	public SearchResult<SumVo> sumOutsourceByProcedure(@RequestBody QueryParam queryParam, @PathVariable String type)
	{
		SearchResult<SumVo> result = serviceFactory.getOutSourceProcessService().sumOutsourceByProcedure(queryParam, type);
		SumVo vo = new SumVo();
		BigDecimal sumMoney = new BigDecimal(0);
		BigDecimal january = new BigDecimal(0);
		BigDecimal february = new BigDecimal(0);
		BigDecimal march = new BigDecimal(0);
		BigDecimal april = new BigDecimal(0);
		BigDecimal may = new BigDecimal(0);
		BigDecimal june = new BigDecimal(0);
		BigDecimal july = new BigDecimal(0);
		BigDecimal august = new BigDecimal(0);
		BigDecimal september = new BigDecimal(0);
		BigDecimal october = new BigDecimal(0);
		BigDecimal november = new BigDecimal(0);
		BigDecimal december = new BigDecimal(0);
		for (SumVo sumVo : result.getResult())
		{
			sumMoney = sumMoney.add(sumVo.getSumMoney());
			january = january.add(sumVo.getJanuary());
			february = february.add(sumVo.getFebruary());
			march = march.add(sumVo.getMarch());
			april = april.add(sumVo.getApril());
			may = may.add(sumVo.getMay());
			june = june.add(sumVo.getJune());
			july = july.add(sumVo.getJuly());
			august = august.add(sumVo.getAugust());
			september = september.add(sumVo.getSeptember());
			october = october.add(sumVo.getOctober());
			november = november.add(sumVo.getNovember());
			december = december.add(sumVo.getDecember());
		}
		vo.setSumMoney(sumMoney);
		vo.setJanuary(january);
		vo.setFebruary(february);
		vo.setMarch(march);
		vo.setApril(april);
		vo.setMay(may);
		vo.setJune(june);
		vo.setJuly(july);
		vo.setAugust(august);
		vo.setSeptember(september);
		vo.setOctober(october);
		vo.setNovember(november);
		vo.setDecember(december);
		result.getResult().add(vo);
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 返回发外汇总表(按发外类型)列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午4:29:45, zhengby
	 */
	@RequestMapping(value = "sumOutsourceByType")
	@ResponseBody
	public SearchResult<SumVo> sumOutsourceByType(@RequestBody QueryParam queryParam)
	{
		SearchResult<SumVo> result = serviceFactory.getOutSourceProcessService().sumOutsourceByType(queryParam);
		SumVo vo = new SumVo();
		BigDecimal sumMoney = new BigDecimal(0);
		BigDecimal january = new BigDecimal(0);
		BigDecimal february = new BigDecimal(0);
		BigDecimal march = new BigDecimal(0);
		BigDecimal april = new BigDecimal(0);
		BigDecimal may = new BigDecimal(0);
		BigDecimal june = new BigDecimal(0);
		BigDecimal july = new BigDecimal(0);
		BigDecimal august = new BigDecimal(0);
		BigDecimal september = new BigDecimal(0);
		BigDecimal october = new BigDecimal(0);
		BigDecimal november = new BigDecimal(0);
		BigDecimal december = new BigDecimal(0);
		for (SumVo sumVo : result.getResult())
		{
			sumMoney = sumMoney.add(sumVo.getSumMoney());
			january = january.add(sumVo.getJanuary());
			february = february.add(sumVo.getFebruary());
			march = march.add(sumVo.getMarch());
			april = april.add(sumVo.getApril());
			may = may.add(sumVo.getMay());
			june = june.add(sumVo.getJune());
			july = july.add(sumVo.getJuly());
			august = august.add(sumVo.getAugust());
			september = september.add(sumVo.getSeptember());
			october = october.add(sumVo.getOctober());
			november = november.add(sumVo.getNovember());
			december = december.add(sumVo.getDecember());
		}
		vo.setSumMoney(sumMoney);
		vo.setJanuary(january);
		vo.setFebruary(february);
		vo.setMarch(march);
		vo.setApril(april);
		vo.setMay(may);
		vo.setJune(june);
		vo.setJuly(july);
		vo.setAugust(august);
		vo.setSeptember(september);
		vo.setOctober(october);
		vo.setNovember(november);
		vo.setDecember(december);
		result.getResult().add(vo);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 单价变更
	 * </pre>
	 * @param id
	 * @param map
	 * @return
	 * @since 1.0, 2018年4月17日 下午6:00:52, think
	 */
	@RequestMapping(value = "editPrice/{id}")
	public String editPrice(@PathVariable Long id, ModelMap map)
	{
		OutSourceProcessDetail outSourceProcessDetail = serviceFactory.getOutSourceProcessService().getDetail(id);
		// 查询所有工单WorkPart2Product，并设置到发外加工明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		if (null != outSourceProcessDetail.getWorkId())
		{
			long workId = outSourceProcessDetail.getWorkId();
			List<WorkProduct> list = productMap.get(workId);
			if (null != list)
			{
				outSourceProcessDetail.setProductList(list);
				// 因为页面实用json格式，所以必须给字段设置数据，否则json数据是不存在的
				outSourceProcessDetail.setProductNames(outSourceProcessDetail.getProductNames());
			}
		}
		map.put("outSourceProcessDetail", outSourceProcessDetail);
		return "outsource/process/editPrice";
	}

	/**
	 * <pre>
	 * 功能 - 单价变更
	 * </pre>
	 * @param outSourceProcessDetail
	 * @return
	 * @since 1.0, 2018年4月17日 下午6:02:29, think
	 */
	@RequestMapping(value = "changePrice")
	@RequiresPermissions("outsource:process:changePrice")
	@ResponseBody
	public AjaxResponseBody changePrice(@RequestBody OutSourceProcessDetail outSourceProcessDetail)
	{
		try
		{
			serviceFactory.getOutSourceProcessService().changePrice(outSourceProcessDetail);
			return returnSuccessBody();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return returnErrorBody(I18nResource.FAIL);
		}
	}
}
