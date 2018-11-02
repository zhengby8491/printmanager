/**
 * <pre>
 * Author:		   zhengby
 * Create:	 	   2018年6月21日 上午9:12:35
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.finance;

import java.util.List;

import com.huayin.common.persist.query.SearchResult;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjust;
import com.huayin.printmanager.persist.entity.finance.FinanceAdjustDetail;
import com.huayin.printmanager.service.vo.QueryParam;

/**
 * <pre>
 * 财务管理 - 财务调整
 * </pre>
 * @author       zhengby
 * @version 	   1.0, 2018年6月21日上午9:12:35, zhengby
 */
public interface FinanceAdjustService
{
	public FinanceAdjust getMaster(Long id);

	public List<FinanceAdjustDetail> getDetailList(Long id);
	
	public FinanceAdjust get(Long id);
	
	public FinanceAdjustDetail getDetail(Long id);

	public FinanceAdjustDetail getDetailHasMaster(Long id);
	
	public void save(FinanceAdjust order);

	public void update(FinanceAdjust order);

	public void delete(Long id);

	public SearchResult<FinanceAdjust> findByCondition(QueryParam queryParam);

	public FinanceAdjust lockHasChildren(Long id);

	public SearchResult<FinanceAdjustDetail> findDetailBycondition(QueryParam queryParam);

	public boolean audit(FinanceAdjust order);
}
