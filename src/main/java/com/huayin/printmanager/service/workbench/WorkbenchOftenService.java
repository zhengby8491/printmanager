package com.huayin.printmanager.service.workbench;

import java.util.List;

import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.persist.entity.workbench.WorkbenchOften;

/**
 * <pre>
 * 常用功能
 * </pre>
 * @author raintear
 * @version 1.0, 2017年2月16日
 */
public interface WorkbenchOftenService
{
	public List<WorkbenchOften> get();

	public Boolean save(List<WorkbenchOften> oftenList);
	
	/**
	 * <pre>
	 * 查用户所有菜单链接
	 * </pre>
	 * @return
	 */
	public List<Menu> getMenu();
}
