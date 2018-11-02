package com.huayin.printmanager.service.workbench;

import java.util.Date;
import java.util.List;

import com.huayin.printmanager.persist.entity.workbench.WorkbenchMemorandum;

/**
 * <pre>
 * 备忘录，常用功能
 * </pre>
 * @author raintear
 * @version 1.0, 2017年2月16日
 */
public interface WorkbenchMemorandumService
{
	public WorkbenchMemorandum get(Date memoDate);
	
	public Boolean save(WorkbenchMemorandum memorandum);
	
	public List<WorkbenchMemorandum> findMemorandum(String year,String month);
}
