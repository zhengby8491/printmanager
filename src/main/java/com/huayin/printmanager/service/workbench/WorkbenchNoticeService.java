/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年2月22日 下午2:02:02
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.workbench;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.workbench.WorkbenchNotice;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 系统  - 公告
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年2月16日, raintear
 * @version 	   2.0, 2018年2月27日下午2:18:01, zhengby, 代码规范
 */
public interface WorkbenchNoticeService
{
	public WorkbenchNotice get(Long id);
	
	public String getNotice();
	
	public WorkbenchNotice save(String content);
	
	public WorkbenchNotice update(WorkbenchNotice notice);
	
	public Boolean del(Long id);
	
	public SearchResult<WorkbenchNotice> findNotice(QueryParam queryParam);
}
