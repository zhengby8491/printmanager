/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年3月19日 上午11:39:22
 * Copyright: 	 Copyright (c) 2018
 * Company:		   ShenZhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.oem.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.expression.SubqueryExpression;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Customer;
import com.huayin.printmanager.persist.entity.basic.Supplier;
import com.huayin.printmanager.persist.entity.oem.OemOrderDetail;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcess;
import com.huayin.printmanager.persist.entity.outsource.OutSourceProcessDetail;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.User;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.oem.OemZeroService;
import com.huayin.printmanager.service.sys.vo.CompanyVo;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 代工平台 - 对外公共业务接口
 * </pre>
 * @author       think
 * @since        1.0, 2018年3月19日
 */
@Service
public class OemZeroServiceImpl extends BaseServiceImpl implements OemZeroService
{
	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.oem.OemZeroService#getOutSourceProcess(java.lang.Long)
	 */
	@Override
	public OutSourceProcess getOutSourceProcess(Long id)
	{
		DynamicQuery query = new DynamicQuery(OutSourceProcess.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, OutSourceProcess.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.oem.OemZeroService#getOutSourceProcessDetail(java.lang.Long)
	 */
	@Override
	public OutSourceProcessDetail getOutSourceProcessDetail(Long id)
	{
		DynamicQuery query = new DynamicQuery(OutSourceProcessDetail.class);
		query.eq("id", id);
		OutSourceProcessDetail detail = daoFactory.getCommonDao().getByDynamicQuery(query, OutSourceProcessDetail.class);
		detail.setMaster(daoFactory.getCommonDao().getEntity(OutSourceProcess.class, detail.getMasterId()));
		return detail;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.oem.OemZeroService#getOutSourceDetailList(java.lang.Long)
	 */
	@Override
	public List<OutSourceProcessDetail> getOutSourceDetailList(Long id)
	{
		DynamicQuery query = new DynamicQuery(OutSourceProcessDetail.class, "od");
		query.createAlias(OutSourceProcess.class, JoinType.LEFTJOIN, "o", "od.masterId=od.id");
		query.eq("id", id);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, OutSourceProcessDetail.class);
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.oem.OemZeroService#findCompanyByCondition(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<CompanyVo> findCompanyByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(Company.class, "c");
		query.addProjection(Projections.property("c, u"));
		query.createAlias(User.class, JoinType.LEFTJOIN, "u", "c.registerUserId=u.id");
		query.setIsSearchTotalCount(true);
		query.eq("c.isOem", BoolValue.YES);

		// 区域 - 省
		if (StringUtils.isNotBlank(queryParam.getProvince()))
		{
			query.eq("province", queryParam.getProvince());
		}
		// 区域 - 市
		if (StringUtils.isNotBlank(queryParam.getCity()))
		{
			query.eq("city", queryParam.getCity());
		}
		// 区域 - 区
		if (StringUtils.isNotBlank(queryParam.getCounty()))
		{
			query.eq("county", queryParam.getCounty());
		}

		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("c.createTime");
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<CompanyVo> result = new SearchResult<CompanyVo>();
		result.setResult(new ArrayList<CompanyVo>());
		for (Object[] objs : temp_result.getResult())
		{
			Company c = (Company) objs[0];

			User u = (User) objs[1];
			CompanyVo vo = new CompanyVo();
			vo.setCompany(c);
			vo.setRegisterUser(u);
			result.getResult().add(vo);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see com.huayin.printmanager.service.oem.OemZeroService#findCompanyByOutSource(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<CompanyVo> findCompanyByOutSource(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(Company.class, "c");
		query.addProjection(Projections.property("c, u"));
		query.createAlias(User.class, JoinType.LEFTJOIN, "u", "c.registerUserId=u.id");
		query.createAlias(OutSourceProcess.class, JoinType.LEFTJOIN, "ot", "c.id=ot.companyId");
		query.setIsSearchTotalCount(true);
		query.isNotNull("ot.originCompanyId");
		query.eq("ot.originCompanyId", UserUtils.getCompanyId());

		// 区域 - 省
		if (StringUtils.isNotBlank(queryParam.getProvince()))
		{
			query.eq("c.province", queryParam.getProvince());
		}
		// 区域 - 市
		if (StringUtils.isNotBlank(queryParam.getCity()))
		{
			query.eq("c.city", queryParam.getCity());
		}
		// 区域 - 区
		if (StringUtils.isNotBlank(queryParam.getCounty()))
		{
			query.eq("c.county", queryParam.getCounty());
		}

		//query.setPageIndex(queryParam.getPageNumber());
		//query.setPageSize(queryParam.getPageSize());
		query.desc("c.createTime");
		SearchResult<Object[]> temp_result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);

		SearchResult<CompanyVo> result = new SearchResult<CompanyVo>();
		result.setResult(new ArrayList<CompanyVo>());
		for (Object[] objs : temp_result.getResult())
		{
			Company c = (Company) objs[0];

			User u = (User) objs[1];
			CompanyVo vo = new CompanyVo();
			vo.setCompany(c);
			vo.setRegisterUser(u);
			result.getResult().add(vo);
		}
		result.setCount(temp_result.getCount());
		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.oem.OemZeroService#findTransmitOrderByOutSource(com.huayin.printmanager.service.vo.
	 * QueryParam)
	 */
	@Override
	public SearchResult<OutSourceProcessDetail> findTransmitOrderByOutSource(QueryParam queryParam)
	{
		// 发外明细
		DynamicQuery query = new DynamicQuery(OutSourceProcessDetail.class, "o");
		// 发外主表
		query.createAlias(OutSourceProcess.class, JoinType.LEFTJOIN, "m", "o.masterId=m.id");
		// 代工平台公司信息
		query.createAlias(Company.class, JoinType.LEFTJOIN, "c", "o.companyId=c.id");
		// 代工平台客户信息
		query.createAlias(Customer.class, JoinType.LEFTJOIN, "ct", "o.companyId=ct.originCompanyId and o.originCompanyId=ct.companyId");
		// 代工订单（用子查询代替）
		//query.createAlias(OemOrderDetail.class, JoinType.LEFTJOIN, "od", "o.companyId = od.originCompanyId and od.sourceDetailId = o.id");
		// 公司开启了代工平台
		query.createAlias(Company.class, JoinType.LEFTJOIN, "cc", "o.originCompanyId=cc.id and cc.isOem='YES'");
		query.addProjection(Projections.property("o, m, c, ct, cc"));
		query.setIsSearchTotalCount(true);
		
		// 代工订单没有引用
		DynamicQuery query2 = new CompanyDynamicQuery(OemOrderDetail.class, "od");
		query2.addProjection(Projections.property("od.sourceDetailId"));
		query2.eqProperty("od.originCompanyId", "o.companyId");
		query.add(new SubqueryExpression("o.id", "not in", query2));
		
		// 区域时间
		if (queryParam.getDateMin() != null)
		{
			query.ge("m.createTime", queryParam.getDateMin());
		}
		if (queryParam.getDateMax() != null)
		{
			query.le("m.createTime", queryParam.getDateMax());
		}
		if (queryParam.getDeliverDateMin() != null)
		{
			query.ge("m.deliveryTime", queryParam.getDeliverDateMin());
		}
		if (queryParam.getDeliverDateMax() != null)
		{
			query.le("m.deliveryTime", queryParam.getDeliverDateMax());
		}
		if (StringUtils.isNotBlank(queryParam.getBillNo()))
		{
			query.like("m.billNo", "%" + queryParam.getBillNo() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getProcedureName()))
		{
			query.like("o.procedureName", "%" + queryParam.getProcedureName() + "%");
		}
		if (StringUtils.isNotBlank(queryParam.getWorkBillNo()))
		{
			query.like("o.workBillNo", "%" + queryParam.getWorkBillNo() + "%");
		}
		// 客户名称
		if (StringUtils.isNotBlank(queryParam.getCustomerName()))
		{
			query.like("c.name", "%" + queryParam.getCustomerName() + "%");
		}
		// 查询当前公司的发外数据
		query.eq("o.originCompanyId", UserUtils.getCompanyId());
		// 查询发外公司的发外数据
		if (queryParam.getOriginCompanyId() != null)
		{
			query.eq("o.companyId", queryParam.getOriginCompanyId());
		}
		// 主表未强制完工
		query.eq("m.isForceComplete", BoolValue.NO);
		// 明细非强制完工
		if (null == queryParam.getCompleteFlag() || queryParam.getCompleteFlag() == BoolValue.NO)
		{
			query.eq("o.isForceComplete", BoolValue.NO);// 工序是否强制完工
		}
		// 明细已强制完工
		else
		{
			query.eq("o.isForceComplete", BoolValue.YES);
		}

		// 已审核
		query.eq("m.isCheck", BoolValue.YES);
		// 业务权限
		Long[] employes = UserUtils.getBusinessDataAuthorizationUser();
		if (employes.length > 0)
		{
			query.createAlias(Supplier.class, "s");
			query.eqProperty("s.id", "o.supplierId");
			query.inArray("s.employeeId", employes);
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.desc("m.createTime");

		// 计算结果
		SearchResult<OutSourceProcessDetail> result = new SearchResult<>();
		List<OutSourceProcessDetail> list = Lists.newArrayList();
		result.setResult(list);
		SearchResult<Object[]> tempResult = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, Object[].class);
		for (Object[] o : tempResult.getResult())
		{
			// 过滤已经创建代工订单的发外工序
//			if (o[4] != null)
//			{
//				continue;
//			}

			// 当前公司必须开启代工平台
			if (o[4] == null)
			{
				continue;
			}

			OutSourceProcessDetail outSourceDetail = (OutSourceProcessDetail) o[0];
			// 主表
			OutSourceProcess master = (OutSourceProcess) o[1];
			outSourceDetail.setMaster(master);
			// 代工平台公司信息
			Company company = (Company) o[2];
			Company _company = new Company();
			_company.setId(company.getId());
			_company.setName(company.getName());
			_company.setAddress(company.getAddress());
			_company.setLinkName(company.getLinkName());
			_company.setTel(company.getTel());
			outSourceDetail.setOemCompany(company);
			// 代工平台客户信息
			if (null != o[3])
			{
				Customer customer = (Customer) o[3];
				outSourceDetail.setOemCustomer(customer);
			}

			list.add(outSourceDetail);
		}
		result.setCount(tempResult.getCount());

		return result;
	}

	/*
	 * （非 Javadoc）
	 * @see
	 * com.huayin.printmanager.service.oem.OemZeroService#findOrderDetailByOutSourceDetail(com.huayin.printmanager.service
	 * .vo.QueryParam)
	 */
	@Override
	public SearchResult<OemOrderDetail> findOrderDetailByOutSourceDetail(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(OemOrderDetail.class, "oem");
		query.createAlias(OutSourceProcessDetail.class, JoinType.INNERJOIN, "opd", "oem.originCompanyId = opd.companyId and (oem.sourceDetailId IS NOT NULL and oem.sourceDetailId = opd.id)");
		query.eq("oem.originCompanyId", UserUtils.getCompanyId());
		if (null != queryParam.getIds() && queryParam.getIds().size() > 0)
		{
			query.inArray("opd.id", queryParam.getIds().toArray());
		}
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, OemOrderDetail.class);
	}
}
