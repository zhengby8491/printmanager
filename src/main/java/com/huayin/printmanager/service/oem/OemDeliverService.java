/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月8日 下午6:46:33
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.oem;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.oem.OemDeliver;
import com.huayin.printmanager.persist.entity.oem.OemDeliverDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 代工管理  - 代工送货单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月8日下午6:46:33, zhengby
 */
public interface OemDeliverService
{
	/**
	 * <pre>
	 * 查询送货单数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 上午11:18:13, zhengby
	 */
	OemDeliver get(Long id);

	/**
	 * <pre>
	 * 查询明细表数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 上午11:18:20, zhengby
	 */
	List<OemDeliverDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 获取单个明细表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 上午11:18:44, zhengby
	 */
	OemDeliverDetail getDetail(Long id);

	/**
	 * <pre>
	 * 获取主表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 上午11:19:56, zhengby
	 */
	OemDeliver getMaster(Long id);

	/**
	 * <pre>
	 * 获取明细表信息（含主表）
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 上午11:20:49, zhengby
	 */
	OemDeliverDetail getDetailHasMaster(Long id);

	/**
	 * <pre>
	 * 保存
	 * </pre>
	 * @param order
	 * @since 1.0, 2018年3月14日 上午11:21:01, zhengby
	 */
	void save(OemDeliver order);

	/**
	 * <pre>
	 * 修改
	 * </pre>
	 * @param order
	 * @since 1.0, 2018年3月14日 上午11:21:11, zhengby
	 */
	void update(OemDeliver order);

	/**
	 * <pre>
	 * 删除
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年3月14日 上午11:21:54, zhengby
	 */
	void delete(Long id);

	/**
	 * <pre>
	 * 按条件查询列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月14日 上午11:21:59, zhengby
	 */
	SearchResult<OemDeliver> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 按条件查询明细表
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月14日 上午11:22:07, zhengby
	 */
	SearchResult<OemDeliverDetail> findDetailBycondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 锁定数据,直到事务结束
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 上午11:22:17, zhengby
	 */
	OemDeliver lockHasChildren(Long id);

	/**
	 * <pre>
	 * 查询送货单记录
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月15日 下午3:03:42, zhengby
	 */
	SearchResult<OemDeliverDetail> findDeliverRecords(QueryParam queryParam);

	/**
	 * <pre>
	 * 查询所有送货单明细
	 * </pre>
	 * @param isCheck
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:24:51, zhengby
	 */
	SearchResult<OemDeliverDetail> findAll(BoolValue isCheck);

	/**
	 * <pre>
	 * 查询所有送货单明细
	 * </pre>
	 * @return
	 * @since 1.0, 2018年3月19日 下午5:25:14, zhengby
	 */
	SearchResult<OemDeliverDetail> findAll();

}
