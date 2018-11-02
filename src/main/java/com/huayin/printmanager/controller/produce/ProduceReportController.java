/**
 * <pre>
 * Author:		  zhengby
 * Create:	 	  2018年2月23日上午10:34:45
 * Copyright:   Copyright (c) 2018
 * Company:		  Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.produce;

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

import com.huayin.common.util.JsonUtils;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.exception.BusinessException;
import com.huayin.printmanager.i18n.service.I18nResource;
import com.huayin.printmanager.persist.entity.BaseBillTableEntity;
import com.huayin.printmanager.persist.entity.produce.WorkReport;
import com.huayin.printmanager.persist.entity.produce.WorkReportDetail;
import com.huayin.printmanager.persist.entity.produce.WorkReportTask;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.persist.enumerate.TableType;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 生产管理 - 产量上报
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年2月23日上午10:34:55, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/produce/report")
public class ProduceReportController extends BaseController
{
	/**
	 * <pre>
	 * 功能 - 保存产量上报单
	 * </pre>
	 * @param workReport
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:35:39, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@RequiresPermissions("report:work:create")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "产量上报", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody WorkReport workReport, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(workReport))
		{
			return returnErrorBody(I18nResource.VALIDATE_FAIL);
		}
		// 保存之前验证是否已上报完，前端的已校验过，后台再校验一次，防止保存重复任务单
		for (WorkReportDetail detail : workReport.getReportList())
		{
			if (null == detail.getTaskId())
			{
				continue;
			}
			WorkReportTask task = serviceFactory.getWorkService().getWorkReportTaskById(detail.getTaskId());
			if (task.getReportQty().compareTo(task.getYieldQty()) >= 0)
			{
				return returnErrorBody("单据已生成，无需重复操作");
			}
		}
		try
		{
			serviceFactory.getWorkService().saveReport(workReport);
			request.setAttribute(SystemLogAspect.BILLNO, workReport.getBillNo());
			return returnSuccessBody(workReport);
		}
		catch (Exception ex)
		{
			request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
			ex.printStackTrace();
			logger.error(ex.getMessage(), "WorkReport:" + JsonUtils.toJson(workReport));
			return returnErrorBody("");
		}
	}

	/**
	 * <pre>
	 * 页面 - 产量上报单查看
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:36:23, zhengby
	 */
	@RequestMapping(value = "view/{id}")
	@RequiresPermissions("report:work:list")
	public String view(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}
		WorkReport workReport = serviceFactory.getWorkService().findWorkReportById(id);
		map.put("workReport", workReport);
		return "produce/report/view";
	}

	/**
	 * <pre>
	 * 页面 - 产量上报创建
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "create")
	@RequiresPermissions("report:work:create")
	public String create(Long[] ids, ModelMap map)
	{
		List<WorkReportTask> reportList = serviceFactory.getWorkService().findReportTaskInfo(ids);
		map.put("reportList", reportList);
		return "produce/report/report_creat";
	}

	/**
	 * <pre>
	 * 功能 - 查看产量上报
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:37:25, zhengby
	 */
	@RequestMapping(value = "viewAjax/{id}")
	@ResponseBody
	public Map<String, Object> viewAjax(@PathVariable Long id)
	{
		Map<String, Object> mapData = null;
		try
		{
			WorkReport workReport = serviceFactory.getWorkService().findWorkReportById(id);
			mapData = ObjectUtils.objectToMap(workReport);
			mapData.put("companyName", UserUtils.getCompany().getName());
			mapData.put("companyAddress", UserUtils.getCompany().getAddress());
			mapData.put("companyFax", UserUtils.getCompany().getFax());
			mapData.put("companyLinkName", UserUtils.getCompany().getLinkName());
			mapData.put("companyTel", UserUtils.getCompany().getTel());
			mapData.put("companyEmail", UserUtils.getCompany().getEmail());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return mapData;

	}

	/**
	 * <pre>
	 * 页面 - 产量上报单编辑
	 * </pre>
	 * @param request
	 * @param map
	 * @param id
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:37:50, zhengby
	 */
	@RequestMapping(value = "edit/{id}")
	@RequiresPermissions("report:work:edit")
	public String edit(HttpServletRequest request, ModelMap map, @PathVariable Long id)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorPage(map, "数据没有通过验证，请刷新页面后重试！");
		}

		WorkReport workReport = serviceFactory.getWorkService().findWorkReportById(id);
		map.put("workReport", workReport);
		return "produce/report/report_edit";

	}

	/**
	 * <pre>
	 * 功能 - 更新产量上报单
	 * </pre>
	 * @param workReport
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:38:22, zhengby
	 */
	@RequestMapping(value = "update")
	@ResponseBody
	@RequiresPermissions("report:work:edit")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "产量上报单", Operation = Operation.UPDATE)
	public AjaxResponseBody update(@RequestBody WorkReport workReport, HttpServletRequest request)
	{
		try
		{
			serviceFactory.getWorkService().updateReport(workReport);
			return returnSuccessBody(workReport);
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
	 * 功能 - 审核产量上报单
	 * </pre>
	 * @param billType
	 * @param id
	 * @param boolValue
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:38:58, zhengby
	 */
	@RequestMapping(value = "audit/{id}")
	@ResponseBody
	@RequiresPermissions("report:work:audit")
	public AjaxResponseBody audit(BillType billType, @PathVariable Long id, BoolValue boolValue)
	{
		serviceFactory.getWorkService().auditReport(billType, id, boolValue);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 取消审核产量上报单
	 * </pre>
	 * @param billType
	 * @param id
	 * @param boolValue
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:39:25, zhengby
	 */
	@RequestMapping(value = "auditCancel/{id}")
	@ResponseBody
	@RequiresPermissions("report:work:auditCancel")
	public AjaxResponseBody auditCancel(BillType billType, @PathVariable Long id, BoolValue boolValue)
	{
		serviceFactory.getWorkService().auditReport(billType, id, boolValue);
		return returnSuccessBody();
	}

	/**
	 * <pre>
	 * 功能 - 删除产量上报单
	 * </pre>
	 * @param id
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:39:49, zhengby
	 */
	@RequestMapping(value = "del/{id}")
	@ResponseBody
	@RequiresPermissions("report:work:del")
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "产量上报单", Operation = Operation.DELETE)
	public AjaxResponseBody delete(@PathVariable Long id, HttpServletRequest request)
	{
		if (Validate.validateObjectsNullOrEmpty(id))
		{
			return returnErrorBody("ID不能为空");
		}
		try
		{
			WorkReport workReport = serviceFactory.getWorkService().findWorkReportById(id);
			if (workReport.getIsCheck() == BoolValue.YES)
			{// 已审核的不允许删除
				request.setAttribute(SystemLogAspect.PRODUCERESULT, OperationResult.FAILE);
				return returnErrorBody("已审核的不允许删除");
			}
			serviceFactory.getWorkService().deleteReport(id);
			request.setAttribute(SystemLogAspect.BILLNO, workReport.getBillNo());
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
	 * 功能 - 强制完工产量上报单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids
	 * @param boolValue
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:40:15, zhengby
	 */
	@RequestMapping(value = "complete")
	@ResponseBody
	@RequiresPermissions("report:work:complete")
	public AjaxResponseBody complete(TableType tableType, @RequestParam("ids[]") Long[] ids, BoolValue boolValue)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? WorkReport.class : WorkReportDetail.class;
		if (serviceFactory.getCommonService().forceComplete(cla, ids, boolValue))
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
	 * 功能 - 取消强制完工产量上报单
	 * </pre>
	 * @param tableType 表类型（MASTER:主表;DETAIL:明细表）
	 * @param ids
	 * @param boolValue
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:41:00, zhengby
	 */
	@RequestMapping(value = "completeCancel")
	@ResponseBody
	@RequiresPermissions("report:work:completeCancel")
	public AjaxResponseBody completeCancel(TableType tableType, @RequestParam("ids[]") Long[] ids, BoolValue boolValue)
	{
		if (Validate.validateObjectsNullOrEmpty(tableType, ids))
		{
			return returnErrorBody("提交数据不完整");
		}
		Class<? extends BaseBillTableEntity> cla = (tableType == TableType.MASTER) ? WorkReport.class : WorkReportDetail.class;
		if (serviceFactory.getCommonService().forceComplete(cla, ids, boolValue))
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
	 * 功能 - 强制完工生产任务:
	 * </pre>
	 * @param ids
	 * @param boolValue
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:41:35, zhengby
	 */
	@RequestMapping(value = "completeTask")
	@ResponseBody
	@RequiresPermissions("reportTask:work:complete")
	public AjaxResponseBody completeTask(@RequestParam("ids[]") Long[] ids, BoolValue boolValue)
	{

		if (serviceFactory.getCommonService().forceComplete(WorkReportTask.class, ids, boolValue))
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
	 * 功能 - 取消强制完工生产任务:
	 * </pre>
	 * @param ids
	 * @param boolValue
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:42:28, zhengby
	 */
	@RequestMapping(value = "completeTaskCancel")
	@ResponseBody
	@RequiresPermissions("reportTask:work:completeCancel")
	public AjaxResponseBody completeTaskCancel(@RequestParam("ids[]") Long[] ids, BoolValue boolValue)
	{
		if (serviceFactory.getCommonService().forceComplete(WorkReportTask.class, ids, boolValue))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("操作失败");
		}
	}

}
