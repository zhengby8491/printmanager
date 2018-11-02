package com.huayin.printmanager.service.workbench.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.workbench.WorkbenchMemorandum;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.workbench.WorkbenchMemorandumService;
import com.huayin.printmanager.utils.DateUtils;
import com.huayin.printmanager.utils.StringUtils;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 备忘录
 * </pre>
 * @author raintear
 * @version 1.0, 2017年2月17日
 */
@Service
public class WorkbenchMemorandumServiceImpl extends BaseServiceImpl implements WorkbenchMemorandumService
{

	@Override
	public WorkbenchMemorandum get(Date memoDate)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkbenchMemorandum.class);
		query.eq("memoDate", DateUtils.formatDate(memoDate, ""));
		query.eq("userId", UserUtils.getUserId());
		return daoFactory.getCommonDao().getByDynamicQuery(query, WorkbenchMemorandum.class);
	}

	@Override
	@Transactional
	public Boolean save(WorkbenchMemorandum memorandum)
	{
		try
		{
			DynamicQuery query = new CompanyDynamicQuery(WorkbenchMemorandum.class);
			query.eq("date", memorandum.getDate());
			query.eq("userId", UserUtils.getUserId());
			WorkbenchMemorandum memorandum_= daoFactory.getCommonDao().getByDynamicQuery(query, WorkbenchMemorandum.class);
			if(memorandum_!=null){//修改
				if(StringUtils.isBlank(memorandum.getContent())){//传入空字符串则删除数据
					daoFactory.getCommonDao().deleteEntity(memorandum_);
					return true;
				}
				memorandum_.setContent(memorandum.getContent());
				daoFactory.getCommonDao().updateEntity(memorandum_);
				return true;
			}else{//新增
				if(StringUtils.isBlank(memorandum.getContent())){
					return true;
				}
				memorandum.setUserId(UserUtils.getUserId());
				memorandum.setCompanyId(UserUtils.getCompanyId());
				memorandum.setBadge(true);
				daoFactory.getCommonDao().saveEntity(memorandum);
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public List<WorkbenchMemorandum> findMemorandum(String year,String month)
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkbenchMemorandum.class);
		query.eq("userId", UserUtils.getUserId());
		query.eq("DATE_FORMAT(date,'%Y-%m')", year+"-"+month);
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkbenchMemorandum.class);
	}
}
