/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年2月24日 下午2:20:07
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.wx.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.printmanager.aspect.SystemLogAspect;
import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.common.SystemConfigUtil;
import com.huayin.printmanager.constants.SysConstants;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.SystemControllerLog;
import com.huayin.printmanager.persist.entity.produce.WorkReport;
import com.huayin.printmanager.persist.entity.produce.WorkReportTask;
import com.huayin.printmanager.persist.enumerate.Operation;
import com.huayin.printmanager.persist.enumerate.OperationResult;
import com.huayin.printmanager.persist.enumerate.SystemLogType;
import com.huayin.printmanager.wx.util.WxJsapiTicketUtil;
import com.huayin.printmanager.wx.util.WxSign;

/**
 * <pre>
 * 微信 - 条码上报（微信扫一扫）
 * </pre>
 * @author       think
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2018年2月26日 下午17:07:00, think, 规范和国际化
 */
@Controller
@RequestMapping(value = "${basePath}/wx/codereport")
public class WXCodeReportController extends BaseController
{
	/**
	 * <pre>
	 * 页面 - 扫一扫
	 * </pre>
	 * @param request
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:16:50, think
	 */
	@RequestMapping(value = "scan/index")
	public String indexView(HttpServletRequest request, ModelMap map)
	{
		String jsapi_ticket = WxJsapiTicketUtil.getJSApiTicket();
		// 注意 URL 一定要动态获取，不能 hardcode
		String billno = request.getParameter("billno");
		String url = SystemConfigUtil.getConfig(SysConstants.WX_URL) + "/print/wx/codereport/scan/index?billno=" + billno;
		Map<String, String> ret = WxSign.sign(jsapi_ticket, url);
		map.putAll(ret);
		map.put("appid", SystemConfigUtil.getConfig(SysConstants.WX_APPID));

		return "wx/codereport/scan";
	}

	/**
	 * <pre>
	 * 功能 - 获取扫一扫配置信息
	 * </pre>
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:17:16, think
	 */
	@RequestMapping(value = "getConfigInfo")
	@ResponseBody
	public AjaxResponseBody getConfigInfo(HttpServletRequest request)
	{
		String jsapi_ticket = WxJsapiTicketUtil.getJSApiTicket();
		// 注意 URL 一定要动态获取，不能 hardcode
		// String url = SystemConfigUtil.getConfig(SysConstants.WX_URL) + "/print/wx/check/view/index";
		String url = (String) request.getSession().getAttribute("WXSCANQR");
		// String url = SystemConfigUtil.getConfig(SysConstants.WX_URL) + "/print/" + uri;
		Map<String, String> ret = WxSign.sign(jsapi_ticket, url);

		Map<String, String> map = new HashMap<String, String>();
		map.putAll(ret);
		map.put("appid", SystemConfigUtil.getConfig(SysConstants.WX_APPID));
		return returnSuccessBody(map);
	}

	/**
	 * <pre>
	 * 功能 - 保存产量上报
	 * </pre>
	 * @param workReport
	 * @param request
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:17:28, think
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	@SystemControllerLog(SystemLogType = SystemLogType.BUSINESS, BillTypeText = "产量上报", Operation = Operation.ADD)
	public AjaxResponseBody save(@RequestBody WorkReport workReport, HttpServletRequest request)
	{
		try
		{
			// if (WxUtil.getUserId(request) != null)
			// {
			// if(!WxUtil.getUserHasMenu("offer:order:list")){
			// return returnErrorBody("Not Permission");
			// }
			// }

			serviceFactory.getWorkService().saveReport(workReport);
			request.setAttribute(SystemLogAspect.BILLNO, workReport.getBillNo());
			return returnSuccessBody(workReport);
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
	 * 功能 - 微信扫码查询
	 * </pre>
	 * @param ids
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月26日 上午9:17:46, think
	 */
	@RequestMapping(value = "scan/search")
	@ResponseBody
	public AjaxResponseBody wxView(Long[] ids, ModelMap map)
	{
		List<WorkReportTask> reportList = serviceFactory.getWorkService().findReportTaskInfo(ids);
		return returnSuccessBody(reportList);
	}
}
