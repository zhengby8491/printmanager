/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月8日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.begin;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.begin.MaterialBegin;
import com.huayin.printmanager.persist.entity.begin.MaterialBeginDetail;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 基础设置 - 材料期初
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月8日
 * @since        2.0, 2018年1月8日 下午17:07:00, think, 规范和国际化
 */
public interface MaterialBeginService
{

	/**
	 * <pre>
	 * 根据id获取材料期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:46:42, think
	 */
	public MaterialBegin get(Long id);

	/**
	 * <pre>
	 * 根据id获取材料期初明细
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:46:54, think
	 */
	public List<MaterialBeginDetail> getDetail(Long id);

	/**
	 * <pre>
	 * 添加材料期初
	 * </pre>
	 * @param productBegin
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:47:09, think
	 */
	public Long save(MaterialBegin productBegin);

	/**
	 * <pre>
	 * 修改材料期初
	 * </pre>
	 * @param productBegin
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:47:20, think
	 */
	public Long update(MaterialBegin productBegin);

	/**
	 * <pre>
	 * 删除材料期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:47:27, think
	 */
	public Integer delete(Long id);

	/**
	 * <pre>
	 * 审核材料期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:47:34, think
	 */
	public Integer check(Long id);
	
	/**
	 * <pre>
	 * 反审核材料期初
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年4月28日 下午1:54:59, zhengby
	 */
	public Integer checkBack(Long id);
	
	/**
	 * <pre>
	 * TODO
	 * </pre>
	 * @param materialBegin
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:47:42, think
	 */
	public List<MaterialBeginDetail> isBeginList(MaterialBegin materialBegin);

	/**
	 * <pre>
	 * 后台多条件查询
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:49:15, think
	 */
	public SearchResult<MaterialBegin> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据id锁定订单实体，直到事务提交
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年1月8日 上午10:49:22, think
	 */
	public MaterialBegin lockHasChildren(Long id);
}
