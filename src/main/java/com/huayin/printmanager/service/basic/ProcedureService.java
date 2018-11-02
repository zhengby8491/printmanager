/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月4日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.io.InputStream;
import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 工序信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
public interface ProcedureService
{
	/**
	 * <pre>
	 * 根据id获取工序信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:10:30, think
	 */
	public Procedure get(Long id);

	/**
	 * <pre>
	 * 根据id获取工序信息
	 * </pre>
	 * @param companyId
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:10:37, think
	 */
	public Procedure get(String companyId, Long id);

	/**
	 * <pre>
	 * 根据name获取工序信息
	 * </pre>
	 * @param name
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:10:44, think
	 */
	public Procedure getByName(String name);

	/**
	 * <pre>
	 * 保存工序信息
	 * </pre>
	 * @param procedure
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:10:52, think
	 */
	public Procedure save(Procedure procedure);
	

	/**
	 * <pre>
	 * 保存工序信息（快速添加）
	 * </pre>
	 * @param name
	 * @param procedureType
	 * @param procedureClassId
	 * @return
	 * @since 1.0, 2018年2月9日 上午10:13:54, think
	 */
	public Procedure saveQuick(String name, ProcedureType procedureType, Long procedureClassId);

	/**
	 * <pre>
	 * 修改工序信息
	 * </pre>
	 * @param procedure
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:10:59, think
	 */
	public Procedure update(Procedure procedure);

	/**
	 * <pre>
	 * 批量删除工序信息
	 * </pre>
	 * @param ids
	 * @since 1.0, 2018年1月4日 上午9:11:08, think
	 */
	public void deleteByIds(Long[] ids);

	/**
	 * <pre>
	 * 快捷查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:11:15, think
	 */
	public SearchResult<Procedure> quickFindByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:11:28, think
	 */
	public SearchResult<Procedure> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * excel文件导入
	 * </pre>
	 * @param inputStream
	 * @return
	 * @throws Exception
	 * @since 1.0, 2018年1月4日 上午9:11:49, think
	 */
	public Integer importFromExcel(InputStream inputStream) throws Exception;

	/**
	 * <pre>
	 * 根据工序类型查工序
	 * </pre>
	 * @param procedureType
	 * @return
	 * @since 1.0, 2018年1月4日 上午9:12:02, think
	 */
	public List<Procedure> findByType(ProcedureType procedureType);

	/**
	 * <pre>
	 * 根据工序名称精确查询工序
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年4月2日 下午2:25:50, zhengby
	 */
	public Procedure findByPrecise(QueryParam queryParam);
}
