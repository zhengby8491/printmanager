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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.outsource.OutSourceArriveDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceDetailBaseEntity;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceReturnDetail;
import com.huayin.printmanager.persist.entity.produce.Work;
import com.huayin.printmanager.persist.entity.produce.WorkProcedure;
import com.huayin.printmanager.persist.entity.produce.WorkProduct;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.OutSourceType;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.persist.enumerate.WorkProcedureType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 发外管理 - 发外未清
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2016年7月1日, zhaojt
 * @version 	   2.0, 2018年2月23日下午5:47:06, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/outsource/transmit")
public class OutSourceTransmitController extends BaseController
{
	/**
	 * <pre>
	 * 页面 -工单工序转发外加工单创建页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:48:02, zhengby
	 */
	@RequestMapping(value = "to_process_procedure")
	@RequiresPermissions("outsource:transmit:to_process_procedure")
	public String transmit_process_procedure()
	{
		return "outsource/transmit/to_process_procedure";
	}

	/**
	 * <pre>
	 * 功能 - 返回工单工序转发外的数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:48:46, zhengby
	 */
	@RequestMapping(value = "to_process_procedure_list")
	@ResponseBody
	public SearchResult<WorkProcedure> transmit_process_procedure_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<WorkProcedure> result = serviceFactory.getWorkService().findForTransmitProcedureOut(queryParam);

		// 查询所有工单WorkPart2Product，并设置到部件工序中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (WorkProcedure workProcedure : result.getResult())
		{
			long workId = workProcedure.getWorkId();
			List<WorkProduct> list = productMap.get(workId);
			if (null != list)
			{
				workProcedure.setProductList(list);
			}
		}

		WorkProcedure vo = new WorkProcedure();
		BigDecimal qty = new BigDecimal(0); // 生产数
		BigDecimal inputQty = new BigDecimal(0); // 应外发数量
		BigDecimal outOfQty = new BigDecimal(0); // 已外发数量
		for (WorkProcedure w : result.getResult())
		{
			if (w.getWorkProcedureType() == WorkProcedureType.PART)
			{
				qty = qty.add(new BigDecimal(w.getWorkPart().getQty()));
			}
			else
			{
				qty = qty.add(w.getInputQty());
			}
			inputQty = inputQty.add(w.getInputQty());
			outOfQty = outOfQty.add(w.getOutOfQty());
		}
		vo.setSumQty(qty);
		vo.setInputQty(inputQty);
		vo.setOutOfQty(outOfQty);
		result.getResult().add(vo);
		return result;
	}

	/**
	 * <pre>
	 * 页面 - 工单整单转发外加工单创建页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:50:43, zhengby
	 */
	@RequestMapping(value = "to_process_product")
	@RequiresPermissions("outsource:transmit:to_process_product")
	public String transmit_process_product()
	{
		return "outsource/transmit/to_process_product";
	}

	/**
	 * <pre>
	 * 功能 - 返回工单整单转发外数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:51:40, zhengby
	 */
	@RequestMapping(value = "to_process_product_list")
	@ResponseBody
	public SearchResult<WorkProduct> transmit_process_product_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<WorkProduct> result = serviceFactory.getWorkService().findForTransmitProductOut(queryParam);
		WorkProduct workProduct = new WorkProduct();
		Integer produceQty = new Integer(0); // 应外发数
		Integer outOfQty = new Integer(0); // 已外发数
		for (WorkProduct w : result.getResult())
		{
			produceQty += w.getProduceQty();
			outOfQty += w.getOutOfQty();
		}
		workProduct.setProduceQty(produceQty);
		workProduct.setOutOfQty(outOfQty);
		result.getResult().add(workProduct);

		return result;
	}

	/**
	 * <pre>
	 * 功能 - 工单工序取消发外
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:52:18, zhengby
	 */
	@RequestMapping(value = "cancelProcedureOut")
	@ResponseBody
	@RequiresPermissions("outsource:transmit:cancelProcedureOut")
	public AjaxResponseBody cancelProcedureOut(@RequestParam("ids[]") Long[] ids)
	{
		List<Long> list = new LinkedList<Long>();
		for (int i = 0; i < ids.length; i++)
		{
			if (!list.contains(ids[i]))
			{
				list.add(ids[i]);
			}
		}
		serviceFactory.getWorkService().cancelOutSourceProcess(OutSourceType.PROCESS, (Long[]) list.toArray(new Long[list.size()]));

		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 取消整单发外
	 * </pre>
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:52:41, zhengby
	 */
	@RequestMapping(value = "cancelProductOut")
	@ResponseBody
	@RequiresPermissions("outsource:transmit:cancelProductOut")
	public AjaxResponseBody cancelProductOut(@RequestParam("ids[]") Long[] ids)
	{
		List<Long> list = new LinkedList<Long>();
		for (int i = 0; i < ids.length; i++)
		{
			if (!list.contains(ids[i]))
			{
				list.add(ids[i]);
			}
		}
		serviceFactory.getWorkService().cancelOutSourceProcess(OutSourceType.PRODUCT, (Long[]) list.toArray(new Long[list.size()]));

		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 页面 - 发外加工转发外到货页面
	 * </pre>
	 * @param dateMin
	 * @param dateMax
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:53:12, zhengby
	 */
	@RequestMapping(value = "to_arrive")
	@RequiresPermissions("outsource:transmit:to_arrive")
	public String to_arrive(Date dateMin, Date dateMax, ModelMap map)
	{
		map.put("dateMin", dateMin);
		map.put("dateMax", dateMax);
		return "outsource/transmit/to_arrive";
	}

	/**
	 * <pre>
	 * 功能 - 返回发外加工转发外到货数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:54:17, zhengby
	 */
	@RequestMapping(value = "to_arrive_list")
	@ResponseBody
	public SearchResult<OutSourceProcessDetail> to_arrive_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<OutSourceProcessDetail> result = serviceFactory.getOutSourceProcessService().findForTransmitArrive(queryParam);

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
				}
			}
		}

		OutSourceProcessDetail detail = new OutSourceProcessDetail();
		BigDecimal qty = new BigDecimal(0);
		BigDecimal arriveQty = new BigDecimal(0);
		for (OutSourceProcessDetail o : result.getResult())
		{
			qty = qty.add(o.getQty());
			arriveQty = arriveQty.add(o.getArriveQty());
		}
		detail.setQty(arriveQty.setScale(2, RoundingMode.HALF_UP));
		detail.setArriveQty(arriveQty.setScale(2, RoundingMode.HALF_UP));
		result.getResult().add(detail);

		return result;
	}

	/**
	 * <pre>
	 * 页面 - 发外到货/发外退货转发外对账单创建页面
	 * </pre>
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:54:52, zhengby
	 */
	@RequestMapping(value = "to_reconcil")
	@RequiresPermissions("outsource:transmit:to_reconcil")
	public String to_reconcil()
	{
		return "outsource/transmit/to_reconcil";
	}

	/**
	 * <pre>
	 * 功能 - 返回发外加工转发外到货数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:55:40, zhengby
	 */
	@RequestMapping(value = "to_reconcil_list")
	@ResponseBody
	public SearchResult<OutSourceDetailBaseEntity> to_reconcil_list(@RequestBody QueryParam queryParam)
	{
		SearchResult<OutSourceDetailBaseEntity> result = new SearchResult<OutSourceDetailBaseEntity>();
		List<OutSourceDetailBaseEntity> list = new ArrayList<OutSourceDetailBaseEntity>();
		result.setResult(list);
		SearchResult<OutSourceArriveDetail> resultArrive = serviceFactory.getOutSourceArriveService().findForTransmitReconcil(queryParam);
		SearchResult<OutSourceReturnDetail> resultReturn = serviceFactory.getOutSourceReturnService().findForTransmitReconcil(queryParam);
		
		// 查询所有工单WorkPart2Product，并设置到发外退货明细中
		Map<Long, List<WorkProduct>> productMap = serviceFactory.getWorkService().findAllProductForMap();
		for (OutSourceArriveDetail detail : resultArrive.getResult())
		{
			if (null != detail.getWorkId())
			{
				long workId = detail.getWorkId();
				List<WorkProduct> _list = productMap.get(workId);
				if (null != list)
				{
					detail.setProductList(_list);
				}
			}
		}
		for (OutSourceReturnDetail detail : resultReturn.getResult())
		{
			if (null != detail.getWorkId())
			{
				long workId = detail.getWorkId();
				List<WorkProduct> _list = productMap.get(workId);
				if (null != list)
				{
					detail.setProductList(_list);
				}
			}
		}
		result.getResult().addAll(resultArrive.getResult());
		result.getResult().addAll(resultReturn.getResult());
		BigDecimal qty = new BigDecimal(0); // 应对账数量
		BigDecimal reconcilQty = new BigDecimal(0); // 已对账数量
		Integer produceNum = new Integer(0);
		for (OutSourceArriveDetail a : resultArrive.getResult())
		{
			produceNum += a.getProduceNum();
			qty = qty.add(a.getQty());
			reconcilQty = reconcilQty.add(a.getReconcilQty());
		}

		for (OutSourceReturnDetail r : resultReturn.getResult())
		{
			produceNum += r.getProduceNum();
			qty = qty.subtract(r.getQty());
			reconcilQty = reconcilQty.subtract(r.getReconcilQty());
		}
		OutSourceArriveDetail sumVo = new OutSourceArriveDetail();
		sumVo.setProduceNum(produceNum);
		sumVo.setQty(qty.setScale(2, RoundingMode.HALF_UP));
		sumVo.setReconcilQty(reconcilQty.setScale(2, RoundingMode.HALF_UP));
		result.getResult().add(sumVo);

		result.setCount(result.getResult().size());
		return result;
	}

	/**
	 * <pre>
	 * 功能 - 强制完工工单工序转发外加工单
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:56:04, zhengby
	 */
	@RequestMapping(value = "procedure_complete")
	@ResponseBody
	@RequiresPermissions("produce:work:complete")
	public AjaxResponseBody procedureComplete(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? Work.class : WorkProcedure.class;
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
	 * 功能 - 工单工序转发外 取消强制完工
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:58:53, zhengby
	 */
	@RequestMapping(value = "procedure_complete_cancel")
	@ResponseBody
	@RequiresPermissions("produce:work:complete_cancel")
	public AjaxResponseBody procedureCompleteCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? Work.class : WorkProcedure.class;
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
	 * 功能 - 强制完工 整单转发外
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:59:14, zhengby
	 */
	@RequestMapping(value = "product_complete")
	@ResponseBody
	@RequiresPermissions("produce:work:complete")
	public AjaxResponseBody productComplete(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? Work.class : WorkProduct.class;
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
	 * 取消强制完工 整单转发外 
	 * </pre>
	 * @param tableType
	 * @param ids
	 * @return
	 * @since 1.0, 2018年2月23日 下午5:59:32, zhengby
	 */
	@RequestMapping(value = "product_complete_cancel")
	@ResponseBody
	@RequiresPermissions("produce:work:complete_cancel")
	public AjaxResponseBody productCompleteCancel(TableType tableType, @RequestParam("ids[]") Long[] ids)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? Work.class : WorkProduct.class;
		if (serviceFactory.getCommonService().forceComplete(cla, ids, BoolValue.NO))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

}
