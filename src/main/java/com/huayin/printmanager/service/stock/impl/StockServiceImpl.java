package com.huayin.printmanager.service.stock.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.common.util.PropertyClone;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.BaseBillMasterTableEntity;
import com.huayin.printmanager.persist.enumerate.BillType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.stock.StockService;
import com.huayin.printmanager.service.stock.vo.NotCheckStockVo;
import com.huayin.printmanager.utils.UserUtils;

@Service
public class StockServiceImpl extends BaseServiceImpl implements StockService
{

	@Override
	public Boolean findIsHasNotCheck(List<BillType> billTypeList)
	{
		for (BillType type : billTypeList)
		{
			DynamicQuery query = new CompanyDynamicQuery(type.getCla());
			query.eq("isCheck", BoolValue.NO);
			query.eq("companyId", UserUtils.getCompanyId());
			List<? extends BaseBillMasterTableEntity> claList = daoFactory.getCommonDao()
					.findEntityByDynamicQuery(query, type.getCla());
			if (claList.size() > 0)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public List<NotCheckStockVo> findNotCheckStock(List<BillType> billTypeList)
	{
		List<NotCheckStockVo> result = new ArrayList<NotCheckStockVo>();
		for (BillType type : billTypeList)
		{
			DynamicQuery query = new CompanyDynamicQuery(type.getCla(),"a");
			if(type==BillType.OUTSOURCE_OA||type==BillType.OUTSOURCE_OR){
				query.createAlias(type.getDetailCla(), JoinType.LEFTJOIN, "b", "a.id = b.masterId and b.type='PRODUCT'");
			}
			query.eq("a.isCheck", BoolValue.NO);
			query.eq("a.companyId", UserUtils.getCompanyId());
			query.setQueryType(QueryType.JDBC);
			List<? extends BaseBillMasterTableEntity> claList = daoFactory.getCommonDao()
					.findEntityByDynamicQuery(query, type.getCla());
			for (BaseBillMasterTableEntity bill : claList)
			{
				NotCheckStockVo vo = new NotCheckStockVo();
				PropertyClone.copyProperties(vo, bill, false);
				result.add(vo);
			}

		}
		return result;
	}
}
