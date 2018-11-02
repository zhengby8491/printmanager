/**
 * <pre>
 * Author:		think
 * Create:	 	2018年1月4日 上午9:30:23
 * Copyright: Copyright (c) 2017
 * Company:		Shenzhen HuaYin
 * <pre>
 */
package com.huayin.printmanager.service.basic.impl;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.exception.ServiceException;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.util.Validate;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.basic.Procedure;
import com.huayin.printmanager.persist.entity.basic.ProcedureClass;
import com.huayin.printmanager.persist.enumerate.BasicType;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.ProcedureType;
import com.huayin.printmanager.persist.enumerate.ProduceType;
import com.huayin.printmanager.persist.enumerate.ScheduleDataSource;
import com.huayin.printmanager.persist.enumerate.YieldReportingType;
import com.huayin.printmanager.service.basic.ProcedureService;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.ExcelUtils;
import com.huayin.printmanager.utils.ObjectUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 基础设置 - 工序信息
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月4日
 * @since        2.0, 2018年1月4日 下午17:07:00, think, 规范和国际化
 */
@Service
public class ProcedureServiceImpl extends BaseServiceImpl implements ProcedureService
{
	@Override
	public Procedure get(Long id)
	{
		DynamicQuery query = new CompanyDynamicQuery(Procedure.class);
		query.eq("id", id);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Procedure.class);
		// return daoFactory.getCommonDao().getEntity(Procedure.class, id);
	}

	@Override
	public Procedure get(String companyId, Long id)
	{
		DynamicQuery query = new DynamicQuery(Procedure.class);
		query.eq("id", id);
		query.eq("companyId", companyId);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Procedure.class);
	}

	@Override
	public Procedure getByName(String name)
	{
		DynamicQuery query = new CompanyDynamicQuery(Procedure.class);
		query.eq("name", name);
		// query.eq("companyId", UserUtils.getCompanyId());
		return daoFactory.getCommonDao().getByDynamicQuery(query, Procedure.class);
	}

	@Override
	@Transactional
	public Procedure save(Procedure procedure)
	{
		procedure.setCreateTime(new Date());
		procedure.setCreateName(UserUtils.getUserName());
		procedure.setCompanyId(UserUtils.getCompanyId());

		ProcedureClass procedureClass = daoFactory.getCommonDao().getEntity(ProcedureClass.class, procedure.getProcedureClassId());
		if (procedureClass != null)
		{
			procedure.setProductType(procedureClass.getProductType());
		}
		return daoFactory.getCommonDao().saveEntity(procedure);
	}

	@Override
	@Transactional
	public Procedure saveQuick(String name, ProcedureType procedureType, Long procedureClassId)
	{
		Procedure procedure = new Procedure();
		procedure.setName(name);
		// 排序
		procedure.setSort(serviceFactory.getCommonService().getNextSort(BasicType.PROCEDURE));
		// 工序类型
		procedure.setProcedureType(procedureType);
		// 工序分类
		procedure.setProcedureClassId(procedureClassId);
		// 编码
		procedure.setCode(serviceFactory.getCommonService().getNextCode(BasicType.PROCEDURE));
		// 生成方式
		procedure.setProduceType(ProduceType.INSIDE);
		// 是否生产
		procedure.setIsProduce(BoolValue.YES);
		// 是否报价
		procedure.setIsQuotation(BoolValue.YES);
		// 上报方式
		// 如果是印前工序默认为按印版张数
		if (ProcedureType.BEFORE == procedureType)
		{
			procedure.setYieldReportingType(YieldReportingType.PLATEPCS);
		}
		// 如果是印刷，印后工序默认为按印张数
		else if (ProcedureType.PRINT == procedureType || ProcedureType.AFTER == procedureType)
		{
			procedure.setYieldReportingType(YieldReportingType.IMPRESSION);
		}
		// 如果是成品工序默认为按产品数
		else if (ProcedureType.FINISHED == procedureType)
		{
			procedure.setYieldReportingType(YieldReportingType.PRODUCE);
		}
		// 是否排程
		procedure.setIsSchedule(BoolValue.YES);
		// 排程数据源
		procedure.setScheduleDataSource(ScheduleDataSource.OUTPUT);
		// 单价
		procedure.setPrice(null);

		Procedure save = this.save(procedure);
		
		// 清除缓存
		UserUtils.clearCacheBasic(BasicType.PROCEDURE);
		
		return save;
	}

	@Override
	@Transactional
	public Procedure update(Procedure procedure)
	{
		ProcedureClass procedureClass = daoFactory.getCommonDao().getEntity(ProcedureClass.class, procedure.getProcedureClassId());
		if (procedureClass != null)
		{
			procedure.setProductType(procedureClass.getProductType());
		}

		return daoFactory.getCommonDao().updateEntity(procedure);
	}

	/*
	 * @Override
	 * @Transactional public void delete(Long id) { daoFactory.getCommonDao().deleteEntity(Procedure.class, id); }
	 */

	@Override
	@Transactional
	public void deleteByIds(Long[] ids)
	{
		DynamicQuery query = new CompanyDynamicQuery(Procedure.class);
		query.in("id", Arrays.asList(ids));
		List<Procedure> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Procedure.class);
		serviceFactory.getDaoFactory().getCommonDao().deleteAllEntity(list);
		// serviceFactory.getDaoFactory().getCommonDao().deleteByIds(Procedure.class, (Object[])ids);
	}

	/*
	 * @Override public List<Procedure> findAll() { return findByCompanyId(UserUtils.getCompanyId()); }
	 */

	@Override
	public SearchResult<Procedure> findByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Procedure.class);
		query.setIsSearchTotalCount(true);
		if (StringUtils.isNotEmpty(queryParam.getProcedureName()))
		{
			query.like("name", "%" + queryParam.getProcedureName() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.asc("sort");
		query.desc("createTime");
		// query.eq("companyId", UserUtils.getCompanyId());
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Procedure.class);
	}

	/*
	 * @Override public List<Procedure> findByCompanyId(String companyId) { DynamicQuery query = new
	 * CompanyDynamicQuery(Procedure.class); query.eq("companyId", companyId); return
	 * daoFactory.getCommonDao().findEntityByDynamicQuery(query, Procedure.class); }
	 */

	@Override
	public SearchResult<Procedure> quickFindByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Procedure.class);
		if (queryParam.getProcedureClassId() != null && queryParam.getProcedureClassId() != 0l)
		{
			query.eq("procedureClassId", queryParam.getProcedureClassId());
		}
		// 工序分类
		if (queryParam.getProcedureTypeArray() != null)
		{
			query.inArray("procedureType", queryParam.getProcedureTypeArray());
		}
		if (StringUtils.isNotEmpty(queryParam.getProcedureName()))
		{
			query.like("name", "%" + queryParam.getProcedureName() + "%");
		}
		query.eq("companyId", UserUtils.getCompanyId());
		query.asc("sort");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);
		return daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, Procedure.class);
	}
	
	@Override
	public Integer importFromExcel(InputStream inputStream) throws Exception
	{
		Integer count = 0;
		try
		{
			Workbook book = ExcelUtils.initBook(inputStream);
			List<ArrayList<String>> sheet = ExcelUtils.readXlsx(book, 0);
			String code = serviceFactory.getCommonService().getNextCode(BasicType.PROCEDURE).replace(BasicType.PROCEDURE.getCodePrefix(), "");
			Integer codeIndex = Integer.parseInt(code);
			for (ArrayList<String> row : sheet)
			{
				Procedure obj = serviceFactory.getProcedureService().getByName(row.get(0));
				if (!Validate.validateObjectsNullOrEmpty(obj))
				{
					continue;
				}
				/*
				 * QueryParam queryParam=new QueryParam (); queryParam.setProcedureName(row.get(0)); SearchResult<Procedure>
				 * result=serviceFactory.getProcedureService().findByCondition(queryParam); if(result.getCount()>0){ continue;
				 * }
				 */
				Procedure procedure = new Procedure();
				procedure.setCode(BasicType.PROCEDURE.getCodePrefix() + new DecimalFormat("000000").format(codeIndex++));
				procedure.setCreateTime(new Date());
				procedure.setCreateName(UserUtils.getUserName());
				procedure.setCompanyId(UserUtils.getCompanyId());
				procedure.setName(row.get(0));
				procedure.setProcedureType(ObjectUtils.getProcedureType(row.get(1)));
				ProcedureClass procedureClass = serviceFactory.getProcedureClassService().getByName(row.get(2));
				if (procedureClass == null)
				{
					throw new ServiceException("工序\"" + procedure.getName() + "\"的分类" + row.get(2) + "不存在！");
				}
				procedure.setProcedureClassId(procedureClass.getId());
				procedure.setProduceType(ObjectUtils.getProduceType(row.get(3)));
				procedure.setIsProduce(ObjectUtils.getBoolValue(row.get(4)));
				procedure.setIsQuotation(ObjectUtils.getBoolValue(row.get(5)));
				procedure.setYieldReportingType(ObjectUtils.getYieldReportingType(row.get(6)));
				procedure.setIsSchedule(ObjectUtils.getBoolValue(row.get(7)));
				procedure.setScheduleDataSource(ObjectUtils.getScheduleDataSource(row.get(8)));
				procedure.setMemo(row.get(9));
				procedure.setSort(Integer.parseInt(row.get(10)));
				procedure.setCreateTime(new Date());
				procedure.setCreateName(UserUtils.getUserName());
				procedure.setCompanyId(UserUtils.getCompanyId());
				procedure.setProductType(1);
				serviceFactory.getProcedureService().save(procedure);
				count++;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			UserUtils.clearCacheBasic(BasicType.PROCEDURE);
		}
		return count;
	}

	@Override
	public List<Procedure> findByType(ProcedureType procedureType)
	{
		DynamicQuery query = new CompanyDynamicQuery(Procedure.class);
		query.eq("procedureType", procedureType);
		List<Procedure> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Procedure.class);
		return list;
	}
	
	@Override
	public Procedure findByPrecise(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(Procedure.class);
		if (queryParam.getProcedureClassId() != null && queryParam.getProcedureClassId() != 0l)
		{
			query.eq("procedureClassId", queryParam.getProcedureClassId());
		}
		// 工序分类
		if (queryParam.getProcedureType() != null)
		{
			query.eq("procedureType", queryParam.getProcedureType());
		}
		if (StringUtils.isNotEmpty(queryParam.getProcedureName()))
		{
			query.eq("name", queryParam.getProcedureName());
		}
		query.eq("companyId", UserUtils.getCompanyId());
		query.asc("sort");
		query.setIsSearchTotalCount(true);
		return daoFactory.getCommonDao().getByDynamicQuery(query, Procedure.class);
	}
}
