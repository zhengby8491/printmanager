/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月8日 下午6:46:57
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.oem;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.oem.OemReconcil;
import com.huayin.printmanager.persist.entity.oem.OemReconcilDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 代工管理  - 代加工对账单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月8日下午6:46:57, zhengby
 */
public interface OemReconcilService
{

	/**
	 * <pre>
	 * 查询对账单数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月16日 上午9:17:41, zhengby
	 */
	public OemReconcil get(Long id);

	/**
	 * <pre>
	 * 查询明细表数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月16日 上午9:17:48, zhengby
	 */
	public List<OemReconcilDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 获取单个明细表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月16日 上午9:17:53, zhengby
	 */
	public OemReconcilDetail getDetail(Long id);

	/**
	 * <pre>
	 * 获取主表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月16日 上午9:17:58, zhengby
	 */
	public OemReconcil getMaster(Long id);

	/**
	 * <pre>
	 * 获取明细表信息（含主表）
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月16日 上午9:18:06, zhengby
	 */
	public OemReconcilDetail getDetailHasMaster(Long id);

	/**
	 * <pre>
	 * 保存
	 * </pre>
	 * @param order
	 * @since 1.0, 2018年3月16日 上午9:18:13, zhengby
	 */
	public void save(OemReconcil order);

	/**
	 * <pre>
	 * 更新
	 * </pre>
	 * @param order
	 * @since 1.0, 2018年3月16日 上午9:18:45, zhengby
	 */
	public void update(OemReconcil order);

	/**
	 * <pre>
	 * 删除
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年3月16日 上午9:20:13, zhengby
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 按条件查询主表列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月16日 上午9:20:25, zhengby
	 */
	public SearchResult<OemReconcil> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 按条件查询明细表列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月16日 上午9:20:34, zhengby
	 */
	public SearchResult<OemReconcilDetail> findDetailBycondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 锁定数据,直到事务结束
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月16日 上午9:20:42, zhengby
	 */
	public OemReconcil lockHasChildren(Long id);

	/**
	 * <pre>
	 * 查询所有代工对账单明细
	 * </pre>
	 * @param isCheck
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:35:09, zhengby
	 */
	public SearchResult<OemReconcilDetail> findAll(BoolValue isCheck);

	/**
	 * <pre>
	 * 查询所有代工对账单明细
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:35:29, zhengby
	 */
	public SearchResult<OemReconcilDetail> findAll();

}
