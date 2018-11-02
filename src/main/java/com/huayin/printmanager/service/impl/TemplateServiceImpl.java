/**
 * <pre>
 * Author:		   think
 * Create:	 	   2018年1月11日 上午11:17:46
 * Copyright: 	 Copyright (c) 2017
 * Company:		   Shenzhen HuaYin
 * @since:       1.0
 * <pre>
 */
package com.huayin.printmanager.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.huayin.common.exception.ServiceException;
import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.SearchResult;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.sys.Company;
import com.huayin.printmanager.persist.entity.sys.TemplateDataModel;
import com.huayin.printmanager.persist.entity.sys.TemplateModel;
import com.huayin.printmanager.persist.enumerate.BoolValue;
import com.huayin.printmanager.persist.enumerate.PrintModleName;
import com.huayin.printmanager.service.TemplateService;
import com.huayin.printmanager.service.vo.QueryParam;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 框架 - 自定义模板
 * </pre>
 * @author       think
 * @since        1.0, 2018年1月11日
 */
@Service
public class TemplateServiceImpl extends BaseServiceImpl implements TemplateService
{
	@Override
	public SearchResult<TemplateDataModel> findDataByCondition(QueryParam queryParam)
	{
		DynamicQuery query = new CompanyDynamicQuery(TemplateDataModel.class);
		query.setIsSearchTotalCount(true);
		query.in("billType", queryParam.getBillType().toString(), "PUBLIC_FN");
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		SearchResult<TemplateDataModel> result = daoFactory.getCommonDao().findCustomEntityByDynamicQueryPage(query, TemplateDataModel.class);
		return result;
	}

	@Transactional(rollbackFor = Exception.class)
	public void addTemplate(TemplateModel templateModel) throws Exception
	{
		try
		{
			if (templateModel.getId() != null)
			{
				TemplateModel oldTemplateModel = daoFactory.getCommonDao().getEntity(TemplateModel.class, templateModel.getId());
				if (!oldTemplateModel.getCreateUserId().equals(UserUtils.getUserId()))
				{
					throw new ServiceException("非法操作(账号：" + UserUtils.getUserName() + ")，信息已保留待管理员稽查!");
				}
				templateModel.setCompanyId(oldTemplateModel.getCompanyId());
				templateModel.setCreateUserId(oldTemplateModel.getCreateUserId());
				templateModel.setCreateTime(oldTemplateModel.getCreateTime());
				templateModel.setUpdateUserId(UserUtils.getUserId());
				templateModel.setUpdateTime(new Date());
				daoFactory.getCommonDao().updateEntity(templateModel);
			}
			else
			{
				templateModel.setCompanyId(UserUtils.getCompanyId());
				templateModel.setUpdateUserId(UserUtils.getUserId());
				templateModel.setUpdateTime(new Date());
				templateModel.setCreateUserId(UserUtils.getUserId());
				templateModel.setCreateTime(new Date());
				daoFactory.getCommonDao().saveEntity(templateModel);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<TemplateModel> listTemplate(PrintModleName billType)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(TemplateModel.class);
			query.eq("billType", billType);
			query.add(Restrictions.or(Restrictions.eq("isPublic", BoolValue.YES), Restrictions.and(Restrictions.eq("isPublic", BoolValue.NO), Restrictions.eq("companyId", UserUtils.getCompanyId()))));
			return daoFactory.getCommonDao().findEntityByDynamicQuery(query, TemplateModel.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<TemplateModel> listTemplateByAdmin(PrintModleName billType)
	{
		try
		{
			DynamicQuery query = new DynamicQuery(TemplateModel.class);
			query.eq("billType", billType);
			return daoFactory.getCommonDao().findEntityByDynamicQuery(query, TemplateModel.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public TemplateModel getTemplateByAdmin(Long id)
	{
		/*
		 * if(!UserUtils.isSystemCompany()){ throw new ServiceException("你暂时无足够权限进此操作！"); }
		 */
		DynamicQuery query = new DynamicQuery(TemplateModel.class);
		query.eq("id", id);
		TemplateModel templateModel = daoFactory.getCommonDao().getByDynamicQuery(query, TemplateModel.class);
		Company c = serviceFactory.getCompanyService().get(templateModel.getCompanyId());
		if (c != null)
		{
			templateModel.setCompanyName(c.getName());
		}

		return templateModel;
	}

	@Override
	public TemplateModel getTemplate(Long id)
	{
		if (id == null)
		{
			return null;
		}
		DynamicQuery query = new DynamicQuery(TemplateModel.class);
		query.eq("id", id);
		query.add(Restrictions.or(Restrictions.eq("isPublic", BoolValue.YES), Restrictions.and(Restrictions.eq("isPublic", BoolValue.NO), Restrictions.eq("companyId", UserUtils.getCompanyId()))));
		TemplateModel templateModel = daoFactory.getCommonDao().getByDynamicQuery(query, TemplateModel.class);
		return templateModel;
	}

	@Override
	public SearchResult<TemplateModel> queryTemplate(QueryParam queryParam)
	{
		DynamicQuery query = new DynamicQuery(TemplateModel.class, "t");
		query.addProjection(Projections.property("t"));
		query.createAlias(Company.class, JoinType.LEFTJOIN, "c", "t.companyId=c.id");
		if (!StringUtils.isEmpty(queryParam.getCompanyName()))
		{
			query.like("c.name", "%" + queryParam.getCompanyName() + "%");
		}
		if (!StringUtils.isEmpty(queryParam.getTel()))
		{
			query.like("c.tel", "%" + queryParam.getTel() + "%");
		}
		if (queryParam.getPrintModleName() != null && queryParam.getPrintModleName() != PrintModleName.DEFAULT)
		{
			query.eq("t.billType", queryParam.getPrintModleName());
		}
		if (queryParam.getPublish() != null && queryParam.getPublish() == BoolValue.YES)
		{
			query.eq("t.isPublic", queryParam.getPublish());
		}
		if (!StringUtils.isEmpty(queryParam.getTitle()))
		{
			query.like("t.title", "%" + queryParam.getTitle() + "%");
		}
		query.setPageIndex(queryParam.getPageNumber());
		query.setPageSize(queryParam.getPageSize());
		query.setIsSearchTotalCount(true);
		query.desc("t.updateTime");
		SearchResult<TemplateModel> result = daoFactory.getCommonDao().findEntityByDynamicQueryPage(query, TemplateModel.class);
		List<TemplateModel> list = result.getResult();
		for (TemplateModel t : list)
		{
			Company c = serviceFactory.getCompanyService().get(t.getCompanyId());
			if (c != null)
			{
				t.setCompanyName(c.getName());
				t.setIsFormal(c.getIsFormal().getText());
				t.setTel(c.getTel());
			}
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addTemplateByAdmin(TemplateModel templateModel) throws Exception
	{
		try
		{
			templateModel.setUpdateUserId(UserUtils.getUserId());
			templateModel.setUpdateTime(new Date());
			templateModel.setCreateUserId(UserUtils.getUserId());
			templateModel.setCreateTime(new Date());
			daoFactory.getCommonDao().saveEntity(templateModel);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	@Transactional
	public void delTemplate(Long id)
	{
		DynamicQuery query = new DynamicQuery(TemplateModel.class);
		query.eq("id", id);
		// query.eq("createUserId", UserUtils.getUserId());
		TemplateModel templateModel = daoFactory.getCommonDao().getByDynamicQuery(query, TemplateModel.class);
		if (templateModel == null)
		{
			throw new ServiceException("删除发生逻辑错误,请联系管理处理!");
		}
		daoFactory.getCommonDao().deleteEntity(templateModel);
	}

	@Override
	@Transactional
	public void editTemplateByAdmin(TemplateModel templateModel)
	{
		TemplateModel oldTemplateModel = daoFactory.getCommonDao().getEntity(TemplateModel.class, templateModel.getId());
		templateModel.setCreateUserId(oldTemplateModel.getCreateUserId());
		templateModel.setCreateTime(oldTemplateModel.getCreateTime());
		templateModel.setUpdateUserId(UserUtils.getUserId());
		templateModel.setUpdateTime(new Date());
		daoFactory.getCommonDao().updateEntity(templateModel);
	}
}