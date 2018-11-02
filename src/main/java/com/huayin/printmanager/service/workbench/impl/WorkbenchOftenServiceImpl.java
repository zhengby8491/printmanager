package com.huayin.printmanager.service.workbench.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huayin.common.persist.query.DynamicQuery;
import com.huayin.common.persist.query.Projections;
import com.huayin.common.persist.query.Restrictions;
import com.huayin.common.persist.query.constants.QueryConstants.JoinType;
import com.huayin.common.persist.query.constants.QueryConstants.QueryType;
import com.huayin.printmanager.persist.CompanyDynamicQuery;
import com.huayin.printmanager.persist.entity.sys.Menu;
import com.huayin.printmanager.persist.entity.sys.Role_Menu;
import com.huayin.printmanager.persist.entity.sys.User_Menu;
import com.huayin.printmanager.persist.entity.sys.User_Role;
import com.huayin.printmanager.persist.entity.workbench.WorkbenchOften;
import com.huayin.printmanager.persist.enumerate.PermissionType;
import com.huayin.printmanager.service.impl.BaseServiceImpl;
import com.huayin.printmanager.service.workbench.WorkbenchOftenService;
import com.huayin.printmanager.utils.UserUtils;

/**
 * <pre>
 * 常用功能
 * </pre>
 * @author raintear
 * @version 1.0, 2017年2月17日
 */
@Service
public class WorkbenchOftenServiceImpl extends BaseServiceImpl implements WorkbenchOftenService
{

	@Override
	public List<WorkbenchOften> get()
	{
		DynamicQuery query = new CompanyDynamicQuery(WorkbenchOften.class);
		query.eq("companyId", UserUtils.getCompanyId());
		query.eq("userId", UserUtils.getUserId());
		return daoFactory.getCommonDao().findEntityByDynamicQuery(query, WorkbenchOften.class);
	}

	@Override
	@Transactional
	public Boolean save(List<WorkbenchOften> oftenList)
	{
		try
		{
			List<WorkbenchOften> oftenList_old=get();
			List<Long> old_Ids = new ArrayList<Long>();
			List<Long> new_Ids = new ArrayList<Long>();
			for (WorkbenchOften often : oftenList_old)
			{
				old_Ids.add(often.getId());
			}
			if(oftenList==null){
				oftenList=new ArrayList<WorkbenchOften>();
			}
			for (WorkbenchOften often : oftenList)
			{
				new_Ids.add(often.getId());
			}
			for (Long id : old_Ids)
			{
				if (!new_Ids.contains(id))
				{
					daoFactory.getCommonDao().deleteEntity(WorkbenchOften.class, id);
				}
			}
			for (WorkbenchOften often : oftenList)
			{
				if(often.getId()!=null){
					WorkbenchOften old_often=daoFactory.getCommonDao().getEntity(WorkbenchOften.class,often.getId());
					old_often.setName(often.getName());
					old_often.setUrl(often.getUrl());
				}else{
					often.setCompanyId(UserUtils.getCompanyId());
					often.setUserId(UserUtils.getUserId());
					daoFactory.getCommonDao().saveEntity(often);
				}
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}

	@Override
	public List<Menu> getMenu()
	{
		// 查找用户关联菜单
		DynamicQuery queryUM = new DynamicQuery(User_Menu.class, "um");
		queryUM.eq("um.userId", UserUtils.getUserId());
		queryUM.addProjection(Projections.property("um.menuId", "mId"));
		// 查找角色关联菜单
		DynamicQuery queryUR = new DynamicQuery(User_Role.class, "ur");
		queryUR.createAlias(Role_Menu.class, "rm");
		queryUR.add(Restrictions.eqProperty("ur.roleId", "rm.roleId"));
		queryUR.eq("ur.userId", UserUtils.getUserId());
		queryUR.addProjection(Projections.property("rm.menuId", "mId"));
		// 菜单ID合并
		queryUM.union(queryUR);

		DynamicQuery queryUM2RM = new DynamicQuery(queryUM, "um2rm");
		queryUM2RM.addProjection(Projections.property("distinct mId"));

		DynamicQuery query = new DynamicQuery(Menu.class, "m");
		query.createAlias(queryUM2RM, JoinType.INNERJOIN, "um2rm", "m.id=um2rm.mId");
		
		query.eq("m.type", PermissionType.MENU);
		query.ne("url", "");
		// 过滤菜单ID
	
		query.asc("m.sort");

		query.addProjection(Projections.property("*"));
		query.setQueryType(QueryType.JDBC);
		List<Menu> list = daoFactory.getCommonDao().findEntityByDynamicQuery(query, Menu.class);
		return list;
	}

	
}
