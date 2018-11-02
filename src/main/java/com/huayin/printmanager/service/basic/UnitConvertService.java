/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月3日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.basic.UnitConvert;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 单位换算
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月3日
 * @since        2.0, 2018年1月3日 下午17:07:00, think, 规范和国际化
 */
public interface UnitConvertService
{
	/**
	 * <pre>
	 * 根据id获取单位换算
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:32:04, think
	 */
	public UnitConvert get(Long id);

	/**
	 * <pre>
	 * 添加单位换算
	 * </pre>
	 * @param unitConvert
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:32:10, think
	 */
	public UnitConvert save(UnitConvert unitConvert);

	/**
	 * <pre>
	 * 修改单位换算
	 * </pre>
	 * @param unitConvert
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:32:17, think
	 */
	public UnitConvert update(UnitConvert unitConvert);

	/**
	 * <pre>
	 * 得到全部单位换算
	 * </pre>
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:32:25, think
	 */
	public List<UnitConvert> findAll();

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:32:30, think
	 */
	public SearchResult<UnitConvert> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据源单ID和换算单位ID 查换算公式
	 * </pre>
	 * @param sourceUnitId
	 * @param conversionUnitId
	 * @return
	 * @since 1.0, 2018年1月3日 上午9:32:37, think
	 */
	public UnitConvert getByUnit(Long sourceUnitId, Long conversionUnitId);
}
