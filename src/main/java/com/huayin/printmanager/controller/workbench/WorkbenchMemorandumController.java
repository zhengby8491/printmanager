/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.controller.workbench;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.printmanager.common.AjaxResponseBody;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.persist.entity.workbench.WorkbenchMemorandum;

/**
 * <pre>
 * 系统  - 备忘录
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2017年2月16日, raintear
 * @version 	   2.0, 2018年2月27日下午2:06:00, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/workbench/memorandum")
public class WorkbenchMemorandumController extends BaseController
{
	/**
	 * <pre>
	 * 功能  - 查当天的备忘录
	 * </pre>
	 * @param memoDate
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:06:59, zhengby
	 */
	@RequestMapping(value = "get")
	public WorkbenchMemorandum get(Date memoDate)
	{
		return serviceFactory.getWorkbenchMemorandumService().get(memoDate);
	}

	/**
	 * <pre>
	 * 功能  - 查备忘录
	 * </pre>
	 * @param year
	 * @param month
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:07:10, zhengby
	 */
	@RequestMapping(value = "findAll")
	@ResponseBody
	public AjaxResponseBody findAll(String year, String month)
	{
		return returnSuccessBody(serviceFactory.getWorkbenchMemorandumService().findMemorandum(year, month));
	}

	/**
	 * <pre>
	 * 功能  - 保存
	 * </pre>
	 * @param date
	 * @param content
	 * @return
	 * @since 1.0, 2018年2月27日 下午2:07:22, zhengby
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public AjaxResponseBody save(Date date, String content)
	{
		WorkbenchMemorandum memorandum = new WorkbenchMemorandum();
		memorandum.setDate(date);
		memorandum.setContent(content);
		if (serviceFactory.getWorkbenchMemorandumService().save(memorandum))
		{
			return returnSuccessBody();
		}
		else
		{
			return returnErrorBody("保存错误");
		}

	}

}
