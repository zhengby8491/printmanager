/**
 * <pre>
 * Author:		think
 * Create:	 	2017年12月28日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.ProcedureClass;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 工序分类
 * </pre>
 * @author       zhengby
 * @version      1.0, 2017年11月26日 下午4:23:07
 * @since        2.0, 2017年12月28日 下午17:07:00, think, 规范和国际化
 */
public interface ProcedureClassService
{
	/**
	 * <pre>
	 * 根据id获取工序分类
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:50:20, think
	 */
	public ProcedureClass get(Long id);

	/**
	 * <pre>
	 * 根据id获取工序信息
	 * </pre>
	 * @param companyId
	 * @param id
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:50:26, think
	 */
	public ProcedureClass get(String companyId, Long id);

	/**
	 * <pre>
	 * 根据工序分类名称查询工序分类
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:50:32, think
	 */
	public ProcedureClass getByName(String name);

	/**
	 * <pre>
	 * 保存工序分类
	 * </pre>
	 * @param procedureClass
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:50:40, think
	 */
	public ProcedureClass save(ProcedureClass procedureClass);
	
	/**
	 * <pre>
	 * 保存工序分类（快速添加）
	 * </pre>
	 * @param name
	 * @param procedureType
	 * @return
	 * @since 1.0, 2018年2月28日 下午6:22:24, think
	 */
	public ProcedureClass saveQuick(String name, ProcedureType procedureType);

	/**
	 * <pre>
	 * 修改工序分类
	 * </pre>
	 * @param procedureClass
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:50:45, think
	 */
	public ProcedureClass update(ProcedureClass procedureClass);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2017年12月28日 下午2:50:55, think
	 */
	public SearchResult<ProcedureClass> findByCondition(QueryParam queryParam);

}
