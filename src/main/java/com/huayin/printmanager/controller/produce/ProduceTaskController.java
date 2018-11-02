/**
 * <pre>
 * Author:		zhengby
 * Create:	 	2018年2月23日上午10:44:17
 * Copyright: Copyright (c) 2018
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.controller.produce;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.controller.BaseController;
import com.huayin.printmanager.domain.annotation.RequiresPermissions;
import com.huayin.printmanager.persist.entity.produce.WorkReportTask;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 生产管理 - 生产任务
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2017年5月27日, minxl
 * @version 	   2.0, 2018年2月23日上午10:44:27, zhengby, 代码规范
 */
@Controller
@RequestMapping(value = "${basePath}/produce/task")
public class ProduceTaskController extends BaseController
{

	/**
	 * <pre>
	 * 页面 - 跳转到生产任务列表
	 * </pre>
	 * @param auditflag
	 * @param map
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:45:19, zhengby
	 */
	@RequestMapping(value = "list")
	@RequiresPermissions("report:work:list")
	public String list(String auditflag, ModelMap map)
	{
		map.put("auditflag", auditflag);
		return "produce/task/task_list";
	}

	/**
	 * <pre>
	 * 功能 - 返回生产任务列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年2月23日 上午10:45:52, zhengby
	 */
	@RequestMapping(value = "ajaxList")
	@ResponseBody
	@RequiresPermissions("report:work:list")
	public SearchResult<WorkReportTask> ajaxList(@RequestBody QueryParam queryParam)
	{
		SearchResult<WorkReportTask> result = serviceFactory.getWorkService().findProductTaskList(queryParam);
		return result;
	}
}
