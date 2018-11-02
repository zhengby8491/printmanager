/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年2月23日上午9:01:12
 * Copyright: Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.produce;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.JsonUtils;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.controller.vo.WorkCreateVo;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.pay.util.JsonMapper;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkMaterial;
import com.huayin.printmanager.persist.entity.produce.WorkPack;
import com.huayin.printmanager.persist.entity.produce.WorkPart;
import com.huayin.printmanager.persist.entity.produce.WorkPart2Product;
import com.huayin.printmanager.persist.entity.produce.WorkProcedure;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.entity.produce.WorkReportDetail;
import com.huayin.printmanager.persist.entity.produce.WorkReportTask;
import com.huayin.printmanager.persist.entity.sale.SaleOrderDetail;
import com.huayin.printmanager.persist.entity.sale.SaleOrderMaterial;
import com.huayin.printmanager.persist.entity.sale.SaleOrderPack;
import com.huayin.printmanager.persist.entity.sale.SaleOrderPart;
import com.huayin.printmanager.persist.entity.sale.SaleOrderPart2Product;
import com.huayin.printmanager.persist.entity.sale.SaleOrderProcedure;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.PrintType;
import com.huayin.printmanager.persist.enumerate.ProductType;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.service.produce.vo.WorkMaterialVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 生产管理 - 生产工单
 * </pre>
 * @author       zhengby
 * @version 		 1.0, 2016年5月18日
 * @version 	   2.0, 2018年2月23日上午9:02:14, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/produce/work")
public class ProduceController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 生产工单创建
	 * </pre>
	 * @param map
	 * @param productType 产品类型
	 * @param billType 工单类型
	 * @param sourceOrderId 源工单ID（如果是翻单或补单）
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:04:07, zhengby
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("produce:work:create")
	public String create(ModelMap map, Long[] ids, ProductType productType, BillType billType, Long sourceWorkId)
	{
		try
		{
			List<WorkCreateVo> orderList = Lists.newArrayList();
			if (null != ids)
			{
				QueryParam queryParam = new QueryParam();
				queryParam.setIds(Lists.newArrayList(ids));
				// 通过ids去查询销售单详情
				SearchResult<SaleOrderDetail> result = serviceFactory.getSaleOrderService().quickFindForWorkByCondition(queryParam);

				for (SaleOrderDetail d : result.getResult())
				{
					WorkCreateVo vo = new WorkCreateVo();
					vo.setId(d.getProductId());
					vo.setName(d.getProductName());
					vo.setSpecifications(d.getStyle());
					vo.setUnitId(d.getUnitId());
					vo.setSourceQty(d.getQty());
					vo.setSaleProduceQty(d.getQty() - d.getProduceedQty());
					vo.setSpareProduceQty(d.getSpareQty() - d.getProduceSpareedQty());
					vo.setSourceId(d.getMaster().getId());
					vo.setSourceDetailId(d.getId());
					vo.setSourceBillType(d.getMaster().getBillType());
					vo.setSourceBillNo(d.getMaster().getBillNo());
					vo.setCustomerBillNo(d.getMaster().getCustomerBillNo());
					vo.setCustomerId(d.getMaster().getCustomerId());
					vo.setCustomerCode(d.getMaster().getCustomerCode());
					vo.setCustomerName(d.getMaster().getCustomerName());
					vo.setCustomerMaterialCode(d.getCustomerMaterialCode());
					vo.setDeliveryTime(d.getDeliveryTime());
					vo.setCustomerRequire(d.getCustRequire());
					vo.setMemo(d.getMemo());
					vo.setSalePrice(d.getPrice());
					vo.setImgUrl(d.getImgUrl());
					if (d.getProduceedQty() == 0)
					{
						vo.setMoney(d.getMoney());
					}
					else
					{
						vo.setMoney(new BigDecimal(d.getQty() - d.getProduceedQty()).multiply(d.getPrice()).setScale(2, RoundingMode.HALF_UP));
					}
					vo.setCode(d.getProductCode());
					orderList.add(vo);
				}
			}

			List<BillType> billTypeList = new ArrayList<BillType>();
			if (billType != null && sourceWorkId != null && (billType == BillType.PRODUCE_SUPPLEMENT || billType == BillType.PRODUCE_TURNING))
			{// 翻单或补单
				billTypeList.add(billType);
				map.put("billType", billType);
				map.put("sourceWorkId", sourceWorkId);
				Work order = serviceFactory.getWorkService().get(sourceWorkId);
				map.put("order", order);
				productType = order.getProductType();
			}
			else
			{
				billTypeList.add(BillType.PRODUCE_MO);
				billTypeList.add(BillType.PRODUCE_PROOFING);
			}

			if (productType == null)
			{
				productType = ProductType.PACKE;
			}
			map.put("productType", productType);
			map.put("billTypeList", billTypeList);
			map.put("printTypeList", PrintType.values());
			map.put("hasCompanyPermission", UserUtils.hasCompanyPermission("sale:order:list"));

			if (CollectionUtils.isNotEmpty(orderList))
			{
				map.put("productArray", JsonMapper.toJsonString(orderList));

				// 【需求V6.5】根据销售明细ID，查询销售明细得工序材料，并放到order中（一条记录时）
				// List<Map<String, Object>> list = (List<Map<String, Object>>) JsonUtils.jsonToList(productArray);
				if (orderList.size() == 1)
				{
					Long saleDetaiId = orderList.get(0).getSourceDetailId();
					Integer saleProduceQty = orderList.get(0).getSaleProduceQty();
					Integer spareProduceQty = orderList.get(0).getSpareProduceQty();
					Integer allQty = saleProduceQty + spareProduceQty;

					SaleOrderDetail detail = serviceFactory.getSaleOrderService().getDetail(saleDetaiId);
					Work order = new Work();
					order.setProductType(productType);
					// 部件
					List<WorkPart> workPartList = Lists.newArrayList();
					order.setPartList(workPartList);
					for (SaleOrderPart part : detail.getPartList())
					{
						WorkPart workPart = new WorkPart();
						workPart.setVersion(part.getVersion());
						workPart.setPartName(part.getPartName());
						workPart.setMemo(part.getMemo());
						workPart.setQty(allQty);// 部件数量
						workPart.setMultiple(1);// 倍率
						workPart.setPieceNum(1);// 拼版数
						workPart.setGeneralColor("1+0");// 正反普色
						workPart.setSpotColor("0+0");// 正反专色
						workPart.setImpressionNum(allQty);// 印张正数
						workPart.setPlateSuitNum(1);// 印版付数
						workPart.setPlateSheetNum(1);// 印版张数
						workPart.setLossRate(new BigDecimal(0));// 放损%
						workPart.setLossQty(new BigDecimal(0));// 放损数
						workPart.setTotalImpressionNum(allQty);// 总印张
						workPart.setPrintType(PrintType.SINGLE);
						if (productType == ProductType.BOOK)
						{
							workPart.setPageNum(4);// P数量
							workPart.setImpressionNum(allQty * 4);// 印张正数
							workPart.setStickersNum(2);// 贴数
							workPart.setStickersPostedNum(allQty * 2);// 每贴正数
							workPart.setPlateSuitNum(2);// 印版付数
							workPart.setPlateSheetNum(2);// 印版张数
							workPart.setStickerlossQty(0);// 单贴放损
							workPart.setTotalImpressionNum(allQty * 4);// 总印张
						}
						// 材料MaterialList
						List<WorkMaterial> materialList = Lists.newArrayList();
						workPart.setMaterialList(materialList);
						for (SaleOrderMaterial material : part.getMaterialList())
						{
							WorkMaterial workMaterial = new WorkMaterial();
							// materialId
							workMaterial.setMaterialId(material.getMaterialId());
							workMaterial.setMaterialCode(material.getMaterialCode());
							workMaterial.setMaterialName(material.getMaterialName());
							workMaterial.setStyle(material.getStyle());
							workMaterial.setWeight(material.getWeight());
							workMaterial.setStockUnitId(material.getStockUnitId());
							workMaterial.setValuationUnitId(material.getValuationUnitId());
							workMaterial.setQty(new BigDecimal(allQty));
							workMaterial.setSplitQty(1);
							workMaterial.setIsCustPaper(material.getIsCustPaper());
							workPart.getMaterialList().add(workMaterial);
						}
						// 工序ProcedureList
						List<WorkProcedure> procedureList = Lists.newArrayList();
						workPart.setProcedureList(procedureList);
						for (SaleOrderProcedure procedure : part.getProcedureList())
						{
							WorkProcedure workProcedure = new WorkProcedure();
							workProcedure.setProcedureId(procedure.getProcedureId());
							workProcedure.setProcedureCode(procedure.getProcedureCode());
							workProcedure.setProcedureName(procedure.getProcedureName());
							workProcedure.setProcedureType(procedure.getProcedureType());
							workProcedure.setProcedureClassId(procedure.getProcedureClassId());
							workProcedure.setIsOutSource(procedure.getIsOutSource());
							workProcedure.setOutputQty(procedure.getOutputQty());
							workProcedure.setInputQty(procedure.getInputQty());
							workProcedure.setSort(procedure.getSort());
							workProcedure.setMemo(procedure.getMemo());
							workPart.getProcedureList().add(workProcedure);
						}
						// 产品ProductList
						List<WorkPart2Product> productList = Lists.newArrayList();
						workPart.setProductList(productList);
						for (SaleOrderPart2Product product : part.getProductList())
						{
							WorkPart2Product workPart2Product = new WorkPart2Product();
							workPart2Product.setProductId(product.getProductId());
							workPart2Product.setProductName(product.getProductName());
							workPart2Product.setProduceQty(allQty);
							workPart2Product.setPieceNum(1);
							workPart2Product.setPageNum(1);
							if (productType == ProductType.BOOK)
							{
								workPart2Product.setPageNum(4);// P数量
								// partList.stickersNum
								// workPart.setStickersNum(stickersNum);
								// partList.stickersPostedNum
								// partList.plateSuitNum
								// partList.plateSheetNum
								// partList.lossRate
								// partList.stickerlossQty
								// partList.lossQty
							}
							workPart.getProductList().add(workPart2Product);
						}
						workPartList.add(workPart);
					}
					// 成品
					SaleOrderPack pack = detail.getPack();
					if (null != pack)
					{
						WorkPack workPack = new WorkPack();
						workPack.setMemo(pack.getMemo());
						order.setPack(workPack);
						// 材料MaterialList
						List<WorkMaterial> materialList = Lists.newArrayList();
						workPack.setMaterialList(materialList);
						for (SaleOrderMaterial material : pack.getMaterialList())
						{
							WorkMaterial workMaterial = new WorkMaterial();
							// materialId
							workMaterial.setMaterialId(material.getMaterialId());
							workMaterial.setMaterialCode(material.getMaterialCode());
							workMaterial.setMaterialName(material.getMaterialName());
							workMaterial.setStyle(material.getStyle());
							workMaterial.setWeight(material.getWeight());
							workMaterial.setStockUnitId(material.getStockUnitId());
							workMaterial.setValuationUnitId(material.getValuationUnitId());
							workMaterial.setQty(new BigDecimal(1));
							workMaterial.setSplitQty(1);
							workMaterial.setIsCustPaper(material.getIsCustPaper());
							workPack.getMaterialList().add(workMaterial);
						}
						// 工序ProcedureList
						List<WorkProcedure> procedureList = Lists.newArrayList();
						workPack.setProcedureList(procedureList);
						for (SaleOrderProcedure procedure : pack.getProcedureList())
						{
							WorkProcedure workProcedure = new WorkProcedure();
							workProcedure.setProcedureId(procedure.getProcedureId());
							workProcedure.setProcedureCode(procedure.getProcedureCode());
							workProcedure.setProcedureName(procedure.getProcedureName());
							workProcedure.setProcedureType(procedure.getProcedureType());
							workProcedure.setProcedureClassId(procedure.getProcedureClassId());
							workProcedure.setIsOutSource(procedure.getIsOutSource());
							workProcedure.setOutputQty(procedure.getOutputQty());
							workProcedure.setInputQty(procedure.getInputQty());
							workProcedure.setSort(procedure.getSort());
							workProcedure.setMemo(procedure.getMemo());
							workPack.getProcedureList().add(workProcedure);
						}
					}
					map.put("saleTransmit", "yes");
					map.put("order", order);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return returnErrorPage(map, "生成生产工单错误");
		}
		return "produce/work/work_create";
	}

	/**
	 * <pre>
	 * 功能 - 保存生产工单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:05:37, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("produce:work:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "生产工单", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody Work order, HttpServletRequest request)
	{
		// 保存之前验证是否已生产完，前端的已校验过，后台再校验一次，防止保存重复工单
		for (WorkProduct wp : order.getProductList())
		{
			if (null != wp.getSourceDetailId())
			{
				SaleOrderDetail source = serviceFactory.getSaleOrderService().getDetail(wp.getSourceDetailId());
				if (source.getProduceedQty() >= source.getQty())
				{
					// 已生产数量 >= 生产数量，则报错
					return returnErrorBody("单据已生成，无需重复操作");
				}
			}
		}
		try
		{
			serviceFactory.getWorkService().save(order);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody(order);
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			logger.error(ex.getMessage(), "Work:" + JsonUtils.toJson(order));
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 页面 - 生产工单查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:05:54, zhengby
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("produce:work:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		Work order = serviceFactory.getWorkService().get(id);
		map.put("order", order);
		return "produce/work/work_view";
	}

	/**
	 * <pre>
	 * 页面 - 点击源单单号查看生产工单
	 * </pre>
	 * @param request
	 * @param map
	 * @param billNo
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:07:00, zhengby
	 */
	@RequestMapping(value = "toView/{billNo}")
	@RequiresPermissions("produce:work:list")
	public String toView(HttpServletRequest request, ModelMap map, @PathVariable String billNo)
	{
		if (Validate.validateObjectsNullOrEmpty(billNo))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		Work _order = serviceFactory.getWorkService().get(billNo);
		Work order = serviceFactory.getWorkService().get(_order.getId());
		map.put("order", order);
		return "produce/work/work_view";
	}
	
	/**
	 * <pre>
	 * 页面  - 自动分贴
	 * </pre>
	 * @return
	 * @since 1.0, 2018年4月17日 下午4:06:01, zhengby
	 */
	@RequestMapping(value = "autoStricks")
	public String autoStricks()
	{
		return "produce/auto_stricks";
	}
	
	/**
	 * <pre>
	 * 页面 - 生产工单打印
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "print/{id}")
	@RequiresPermissions("produce:work:print")
	public String print(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		Work order = serviceFactory.getWorkService().get(id);
		map.put("order", order);
		List<WorkProcedure> before_procedureList = new ArrayList<WorkProcedure>();
		List<WorkProcedure> print_procedureList = new ArrayList<WorkProcedure>();
		List<WorkProcedure> after_procedureList = new ArrayList<WorkProcedure>();
		for (WorkPart part : order.getPartList())
		{
			if (part.getProcedureList() != null)
			{
				for (WorkProcedure p : part.getProcedureList())
				{
					WorkPart wpart = new WorkPart();
					wpart.setPartName(part.getPartName());
					wpart.setPrintType(part.getPrintType());
					wpart.setLossQty(part.getLossQty());
					wpart.setGeneralColor(part.getGeneralColor());
					wpart.setSpotColor(part.getSpotColor());
					wpart.setPieceNum(part.getPieceNum());
					wpart.setPageNum(part.getPageNum());
					wpart.setStickersNum(part.getStickersNum());
					wpart.setStickersPostedNum(part.getStickersPostedNum());
					wpart.setImpressionNum(part.getImpressionNum());
					p.setWorkPart(wpart);
					switch (p.getProcedureType())
					{
						case BEFORE:
							before_procedureList.add(p);
							break;
						case PRINT:
							print_procedureList.add(p);
							break;
						case AFTER:
							after_procedureList.add(p);
							break;
						default:
							break;
					}
					;
				}
			}
		}

		Integer allMaterialNum = 0;
		for (WorkPart part : order.getPartList())
		{
			if (part.getMaterialList() != null)
			{
				allMaterialNum = allMaterialNum + part.getMaterialList().size();
			}
		}

		if (order.getPack() != null)
		{
			if (order.getPack().getMaterialList() != null)
			{
				allMaterialNum = allMaterialNum + order.getPack().getMaterialList().size();
			}
		}
		map.put("before_procedureList", before_procedureList);
		map.put("print_procedureList", print_procedureList);
		map.put("after_procedureList", after_procedureList);
		map.put("allMaterialNum", allMaterialNum);
		return "produce/work_print";
	}

	/**
	 * <pre>
	 * 功能 - 返回生产工单需打印的数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:08:53, zhengby
	 */
	@RequestMapping(value = "printAjax/{id}")
	@ResponseBody
	@RequiresPermissions("produce:work:print")
	public Map<String, Object> printAjax(@PathVariable Long id)
	{
		return serviceFactory.getWorkService().printDataMap(id);
	}

	/**
	 * <pre>
	 * 页面 - 生产工单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:10:16, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("produce:work:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		Work order = serviceFactory.getWorkService().get(id);
		List<BillType> billTypeList = new ArrayList<BillType>();
		if (order.getBillType() == BillType.PRODUCE_MO || order.getBillType() == BillType.PRODUCE_PROOFING)
		{// 翻单或补单
			billTypeList.add(BillType.PRODUCE_MO);
			billTypeList.add(BillType.PRODUCE_PROOFING);
		}
		else
		{
			billTypeList.add(order.getBillType());
		}

		map.put("order", order);
		map.put("billTypeList", billTypeList);
		map.put("printTypeList", PrintType.values());
		return "produce/work/work_edit";
	}

	/**
	 * <pre>
	 * 功能 - 更新生产工单
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:10:51, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("produce:work:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "生产工单", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody Work order, HttpServletRequest request)
	{
		try
		{
			serviceFactory.getWorkService().update(order);
			// 更新旧数据
			Work new_order = serviceFactory.getWorkService().get(order.getId());
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
	 * 功能 - 审核生产工单
	 * </pre>
	 * @param billType
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:11:33, zhengby
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("produce:work:audit")
	public AjaxResponseBody audit(BillType billType, @PathVariable Long id)
	{
		if (serviceFactory.getCommonService().audit(billType, id, BoolValue.YES))
		{
			serviceFactory.getWorkService().updateReportTask(id, BoolValue.YES, null, null);
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 反审核生产工单
	 * </pre>
	 * @param billType
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:11:56, zhengby
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "audit_cancel/{id}")
	@ResponseBody
	@RequiresPermissions("produce:work:audit_cancel")
	public AjaxResponseBody auditCancel(BillType billType, @PathVariable Long id)
	{
		// boolean flag = serviceFactory.getWorkService().findReportDetailByTask(id);
		/*if(flag){
			return returnErrorBody("反审核失败：已被下游单据引用");
		}*/
		Map<String, List> map2 = serviceFactory.getWorkService().findProductTask(id);
		if (!map2.isEmpty())
		{
			Map<String, List> m = serviceFactory.getCommonService().findRefBillNo(billType, id);
			if (!m.isEmpty())
			{
				map2.putAll(m);
			}
			return returnErrorBody(map2);
		}
		else
		{
			// audit不能判断是否有产量上报引用了工单
			if (serviceFactory.getCommonService().audit(billType, id, BoolValue.NO))
			{
				serviceFactory.getWorkService().updateReportTaskState(id);
				return returnSuccessBody();
			}
			else
			{
				Map<String, List> map = serviceFactory.getCommonService().findRefBillNo(billType, id);
				return returnErrorBody(map);
			}
		}

	}

	/**
	 * <pre>
	 * 功能 - 审核所有生产工单
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:12:45, zhengby
	 */
	@RequestMapping(value = "checkAll")
	@ResponseBody
	@RequiresPermissions("produce:work:checkAll")
	public AjaxResponseBody checkAll()
	{
		if (serviceFactory.getWorkService().checkAll())
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
	 * 功能 - 强制完工生产工单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param id 表ID
	 * @param flag 完工标记
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:13:14, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("produce:work:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? Work.class : WorkProduct.class;
		if (serviceFactory.getCommonService().forceComplete(cla, ids, BoolValue.YES))
		{
			// 生产任务也要强制完工
			serviceFactory.getWorkService().completeTaskByWorkId(ids, BoolValue.YES);

			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 取消强制完工生产工单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param id 表ID
	 * @param flag 完工标记
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:13:41, zhengby
	 */
	@RequestMapping(value = "complete_cancel")
	@ResponseBody
	@RequiresPermissions("produce:work:complete_cancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? Work.class : WorkProduct.class;
		if (serviceFactory.getCommonService().forceComplete(cla, ids, BoolValue.NO))
		{
			// 生产任务也要强制完工
			serviceFactory.getWorkService().completeTaskByWorkId(ids, BoolValue.NO);
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 删除生产工单
	 * </pre>
	 * @param id
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:14:12, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("produce:work:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "生产工单", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		try
		{

			Work order = serviceFactory.getWorkService().get(id);
			if (order.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getWorkService().delete(id);
			request.setAttribute(SystemLogAspect.BILLNO, order.getBillNo());
			return returnSuccessBody();
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			return returnErrorBody(ex);
		}
	}

	/**
	 * <pre>
	 * 页面 - 跳转到生产工单列表查看页面
	 * </pre>
	 * @param auditflag
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:15:42, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("produce:work:list")
	public String list(String auditflag, ModelMap map)
	{
		map.put("auditflag", auditflag);
		return "produce/work/work_list";
	}

	/**
	 * <pre>
	 * 功能 - 返回所有生产工单数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:16:53, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	public SearchResult<Work> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<Work> result = serviceFactory.getWorkService().findByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到生产工单明细列表查看页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:17:48, zhengby
	 */
	@RequestMapping(value = "detailList")
	@RequiresPermissions("produce:work_detail:list")
	public String detailList()
	{
		return "produce/work/work_detailList";
	}

	/**
	 * <pre>
	 * 功能 - 返回生产工单明细数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:19:06, zhengby
	 */
	@RequestMapping(value = "ajaxDetailList")
	@ResponseBody
	public SearchResult<WorkProduct> ajaxDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<WorkProduct> result = serviceFactory.getWorkService().findProductByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 返回销售订单下的生产工单明细列表 
	 * </pre>
	 * @param saleBillId
	 * @param productCode
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:19:34, zhengby
	 */
	@RequestMapping(value = "ajaxSaleDetailList")
	@ResponseBody
	public SearchResult<WorkProduct> ajaxSaleDetailList(@RequestParam(value = "saleBillId") Long saleBillId, @RequestParam(value = "productCode") String productCode)
	{
		SearchResult<WorkProduct> result = serviceFactory.getWorkService().findSaleProductByCondition(saleBillId, productCode);
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 生产进度表：返回生产工单明细
	 * </pre>
	 * @param billNo
	 * @param companyId
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:23:31, zhengby
	 */
	@RequestMapping(value = "ajaxDetailItemPc", method = RequestMethod.GET)
	@ResponseBody
	public SearchResult<Map<String, Object>> ajaxDetailItemPc(@RequestParam(value = "billNo") String billNo, @RequestParam(value = "companyId", required = false) String companyId, HttpServletRequest request)
	{
		return serviceFactory.getWorkService().findReportTaskDetail(billNo, companyId);
	}

	/**
	 * <pre>
	 * 功能 - 生产进度表：返回生产工单材料明细
	 * </pre>
	 * @param workId
	 * @param companyId
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:25:17, zhengby
	 */
	@RequestMapping(value = "ajaxDetailMaterialItemPc", method = RequestMethod.GET)
	@ResponseBody
	public SearchResult<Map<String, Object>> ajaxDetailMaterialItemPc(@RequestParam(value = "workId") Long workId, @RequestParam(value = "companyId", required = false) String companyId, HttpServletRequest request)
	{
		return serviceFactory.getWorkService().findWorkMaterials(workId, companyId);
	}

	/**
	 * <pre>
	 * 页面 - 跳转到生产工单工序明细列表查看页面
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:26:04, zhengby
	 */
	@RequestMapping(value = "work_detail_procedure_list") // workProduceDetailList
	@RequiresPermissions("produce:work:procedureList")
	public String procedureDetailList(ModelMap map)
	{
		List<BillType> billTypeList = new ArrayList<BillType>();
		billTypeList.add(BillType.PRODUCE_MO);
		billTypeList.add(BillType.PRODUCE_PROOFING);
		billTypeList.add(BillType.PRODUCE_SUPPLEMENT);
		billTypeList.add(BillType.PRODUCE_TURNING);
		map.put("billTypeList", billTypeList);
		return "produce/work_detail_procedure_list";
	}

	/**
	 * <pre>
	 * 功能 - 返回生产工单工序明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:28:11, zhengby
	 */
	@RequestMapping(value = "ajaxWorkProcedureDetailList")
	@ResponseBody
	public SearchResult<WorkProcedure> ajaxProcedureDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<WorkProcedure> result = serviceFactory.getWorkService().findProcedureByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到生产工单用料明细列表查看页面
	 * </pre>
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:29:06, zhengby
	 */
	@RequestMapping(value = "work_detail_material_list") // workMaterialDetailList
	@RequiresPermissions("produce:work:workMaterialList")
	public String workMaterialDetailList(ModelMap map)
	{
		List<BillType> billTypeList = new ArrayList<BillType>();
		billTypeList.add(BillType.PRODUCE_MO);
		billTypeList.add(BillType.PRODUCE_PROOFING);
		billTypeList.add(BillType.PRODUCE_SUPPLEMENT);
		billTypeList.add(BillType.PRODUCE_TURNING);
		map.put("billTypeList", billTypeList);
		return "produce/work_detail_material_list";
	}

	/**
	 * <pre>
	 * 功能 - 返回生产工单用料明细列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:31:17, zhengby
	 */
	@RequestMapping(value = "ajaxWorkMaterialDetailList")
	@ResponseBody
	public SearchResult<WorkMaterialVo> ajaxMaterialDetailList(@RequestBody QueryParam queryParam)
	{
		SearchResult<WorkMaterialVo> result = serviceFactory.getWorkService().findMaterialByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到生产工单进度表页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:31:45, zhengby
	 */
	@RequestMapping(value = "work_schedule")
	@RequiresPermissions("produce:work:schedule")
	public String work_schedule()
	{
		return "produce/work/work_schedule";
	}

	/**
	 * <pre>
	 * 页面 - 跳转到快速选择生产工单页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:32:35, zhengby
	 */
	@RequestMapping(value = "quick_select_work")
	public String quick_select_work()
	{
		return "produce/quick_select_work";
	}

	/**
	 * <pre>
	 * 页面 - 跳转到生产日报表页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:33:10, zhengby
	 */
	@RequestMapping(value = "work_daily_report")
	@RequiresPermissions("daily:work:list")
	public String to_product_daily_report()
	{
		return "produce/work_daily_report";
	}

	/**
	 * <pre>
	 * 功能 - 返回生产工单日报明细数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:33:42, zhengby
	 */
	@RequestMapping(value = "ajaxDailyReport")
	@RequiresPermissions("daily:work:list")
	@ResponseBody
	public SearchResult<WorkReportDetail> ajaxDailyReport(@RequestBody QueryParam queryParam)
	{
		SearchResult<WorkReportDetail> result = serviceFactory.getWorkService().findWorkReportDeailsByCondition(queryParam);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 跳转到条码上报页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:34:29, zhengby
	 */
	@RequestMapping(value = "work_barcode_report") // to_barcode_report
	public String toBarcodeReport()
	{
		return "produce/work_barcode_report";
	}

	/**
	 * <pre>
	 * 功能 - 条码上报请求路径
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:35:25, zhengby
	 */
	@RequestMapping(value = "ajaxBarcodeReport")
	@ResponseBody
	public SearchResult<WorkReportTask> ajaxBarcodeReport(@RequestParam(value = "id") String id)
	{
		SearchResult<WorkReportTask> result = new SearchResult<WorkReportTask>();
		List<WorkReportTask> reportTaskInfos = null;
		if (id.startsWith("MO"))
		{
			reportTaskInfos = serviceFactory.getWorkService().findReportTaskUnreportQtyInfo(null, id, null);
		}
		else
		{
			try
			{
				Long[] idarr = { Long.parseLong(id) };
				reportTaskInfos = serviceFactory.getWorkService().findReportTaskUnreportQtyInfo(idarr, null, null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		result.setResult(reportTaskInfos);
		if (reportTaskInfos != null)
		{
			result.setCount(reportTaskInfos.size());
		}
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 查询材料是否被下级订单引用 
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:36:48, zhengby
	 */
	@RequestMapping(value = "findMateriaState")
	@ResponseBody
	public AjaxResponseBody findMateriaState(@RequestParam(value = "id") Long id)
	{
		try
		{
			Map<String, Object> map = serviceFactory.getWorkService().findMateriaState(id);
			return returnSuccessBody(map);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
			return returnErrorBody("查询失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 更新材料信息
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:39:13, zhengby
	 */
	@RequestMapping(value = "updateMateria")
	@ResponseBody
	public AjaxResponseBody updateMateria(@RequestBody Work order, HttpServletRequest request)
	{
		try
		{
			serviceFactory.getWorkService().updateMateria(order);
			// 更新旧数据
			Work new_order = serviceFactory.getWorkService().get(order.getId());
			request.setAttribute(SystemLogAspect.BILLNO, new_order.getBillNo());

			return returnSuccessBody(new_order);
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
	 * 功能 - 更新工序
	 * </pre>
	 * @param order
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:41:47, zhengby
	 */
	@RequestMapping(value = "updateProcedure")
	@ResponseBody
	public AjaxResponseBody updateProcedure(@RequestBody Work order, HttpServletRequest request)
	{
		try
		{
			serviceFactory.getWorkService().updateProcedure(order);
			// 更新旧数据
			Work new_order = serviceFactory.getWorkService().get(order.getId());
			request.setAttribute(SystemLogAspect.BILLNO, new_order.getBillNo());

			return returnSuccessBody(new_order);
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
	 * 功能 - 查询工序是否被下级引用
	 * </pre>
	 * @param procedureId
	 * @param billNo
	 * @param procedureRefId
	 * @param partName
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:42:26, zhengby
	 */
	@RequestMapping(value = "findProcedureState")
	@ResponseBody
	public AjaxResponseBody findProcedureState(@RequestParam(value = "procedureId") Long procedureId, @RequestParam(value = "billNo") String billNo, Long procedureRefId, String partName)
	{
		try
		{
			Map<String, Object> map = serviceFactory.getWorkService().findProcedureState(procedureId, billNo, procedureRefId, partName);
			return returnSuccessBody(map);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error(e.getMessage());
			return returnErrorBody("查询失败");
		}
	}

	/**
	 * <pre>
	 * 功能 - 查询订单采购入库记录
	 * </pre>
	 * @param workBillNo
	 * @return
	 * @since 1.0, 2018年2月23日 上午9:42:48, zhengby
	 */
	@RequestMapping(value = "findPurchStockLog")
	@ResponseBody
	public AjaxResponseBody findPurchStockLog(String workBillNo)
	{
		Map<String, Object> map = serviceFactory.getWorkService().findPurchStockLog(workBillNo);
		return returnSuccessBody(map);
	}
}
