/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年3月19日 上午11:38:16
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.oem;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.oem.OemOrderDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.service.sys.vo.CompanyVo;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 代工平台 - 对外公共业务接口
 * </pre>
 * @author       think
 * @since        1.0, 2018年3月19日
 */
public interface OemZeroService
{

	/**
	 * <pre>
	 * 获取代工平台的发外主表信息（不用公司Id）
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月20日 上午10:06:45, think
	 */
	public OutSourceProcess getOutSourceProcess(Long id);
	
	/**
	 * <pre>
	 * 获取代工平台的发外明细信息（不用公司Id）
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月20日 下午6:24:48, think
	 */
	public OutSourceProcessDetail getOutSourceProcessDetail(Long id);
	
	/**
	 * <pre>
	 * 获取代工平台的发外明细信息（不用公司Id）
	 * </pre>
	 * @param id
	 * @return
	 * @since 1.0, 2018年3月20日 上午10:57:57, think
	 */
	public List<OutSourceProcessDetail> getOutSourceDetailList(Long id);
	
	/**
	 * <pre>
	 * 查询公司信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月16日 上午9:25:02, think
	 */
	public SearchResult<CompanyVo> findCompanyByCondition(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据发外订单的originCompanyId查询发外管理公司信息
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月19日 上午11:26:16, think
	 */
	public SearchResult<CompanyVo> findCompanyByOutSource(QueryParam queryParam);

	/**
	 * <pre>
	 * 代工公里 - 代工未清 - 代工平台
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月19日 上午10:14:31, think
	 */
	public SearchResult<OutSourceProcessDetail> findTransmitOrderByOutSource(QueryParam queryParam);

	/**
	 * <pre>
	 * 根据发外明细查询代工订单明细
	 * </pre>
	 * @param queryParam
	 * @return
	 * @since 1.0, 2018年3月21日 上午11:49:12, think
	 */
	public SearchResult<OemOrderDetail> findOrderDetailByOutSourceDetail(QueryParam queryParam);

}
