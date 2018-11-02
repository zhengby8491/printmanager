/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年3月14日 下午6:47:09
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.oem;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.oem.OemReturn;
import com.huayin.printmanager.persist.entity.oem.OemReturnDetail;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.ReturnGoodsType;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 代工管理  - 代工退货单
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年3月14日下午6:47:09, zhengby
 */
public interface OemReturnService
{

	/**
	 * <pre>
	 * 查询退货单数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 下午7:08:01, zhengby
	 */
	public OemReturn get(Long id);

	/**
	 * <pre>
	 * 查询明细表数据
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 下午7:08:16, zhengby
	 */
	public List<OemReturnDetail> getDetailList(Long id);

	/**
	 * <pre>
	 * 获取单个明细表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 下午7:08:24, zhengby
	 */
	public OemReturnDetail getDetail(Long id);

	/**
	 * <pre>
	 * 获取主表信息
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 下午7:08:29, zhengby
	 */
	public OemReturn getMaster(Long id);

	/**
	 * <pre>
	 * 获取明细表信息（含主表）
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 下午7:08:34, zhengby
	 */
	public OemReturnDetail getDetailHasMaster(Long id);

	/**
	 * <pre>
	 * 保存
	 * </pre>
	 * @param order
	 * @since 1.0, 2018年3月14日 下午7:08:39, zhengby
	 */
	public void save(OemReturn order);

	/**
	 * <pre>
	 * 修改
	 * </pre>
	 * @param order
	 * @since 1.0, 2018年3月14日 下午7:08:46, zhengby
	 */
	public void update(OemReturn order);

	/**
	 * <pre>
	 * 删除
	 * </pre>
	 * @param id
	 * @since 1.0, 2018年3月14日 下午7:08:53, zhengby
	 */
	public void delete(Long id);

	/**
	 * <pre>
	 * 按条件查询主表列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月14日 下午7:08:59, zhengby
	 */
	public SearchResult<OemReturn> findByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 按条件查询明细表列表数据
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月14日 下午7:09:06, zhengby
	 */
	public SearchResult<OemReturnDetail> findDetailBycondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 锁定数据,直到事务结束
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月14日 下午7:09:13, zhengby
	 */
	public OemReturn lockHasChildren(Long id);

	/**
	 * <pre>
	 * 查询所有代工退货记录
	 * </pre>
	 * @param exchange
	 * @return
	 * @since 1.0, 2018年3月20日 下午5:00:13, zhengby
	 */
	public SearchResult<OemReturnDetail> findAll(ReturnGoodsType exchange);

	/**
	 * <pre>
	 * 查询所有代工退货记录
	 * </pre>
	 * @param isCheck
	 * @param type
	 * @return
	 * @since 1.0, 2018年3月20日 下午5:01:40, zhengby
	 */
	public SearchResult<OemReturnDetail> findAll(BoolValue isCheck, ReturnGoodsType type);
	
}
