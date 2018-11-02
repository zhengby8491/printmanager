package com.huayin.printmanager.service.stock;

import java.util.List;

import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.service.stock.vo.NotCheckStockVo;

public interface StockService
{
	/**
	 * <pre>
	 * 是否存在未审核订单
	 * </pre>
	 * @param billTypeList 盘点时关系到库存的单据
	 * @return
	 */
	public Boolean findIsHasNotCheck(List<BillType> billTypeList);

	/**
	 * <pre>
	 * 查盘点时未审核的库存操作单据列表
	 * </pre>
	 * @param billTypeList 需查询的单据类型集合
	 * @return
	 */
	public List<NotCheckStockVo> findNotCheckStock(List<BillType> billTypeList);
}
